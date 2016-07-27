package com.android.app.lib.net;


import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.android.app.lib.cache.CacheManager;
import com.android.app.lib.utils.BaseUtils;
import com.android.app.lib.utils.FrameConstants;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

public class HttpRequest implements Runnable {
    private final static String cookiePath = "/data/data/com.conch.basicandroidapp/cache/cookie";
    // 区分get还是post
    public static final String REQUEST_GET = "get";
    public static final String REQUEST_POST = "post";
    private HttpUriRequest request = null;
    private URLData urlData = null;
    private RequestCallback requestCallback = null;
    private List<RequestParameter> parameters = null;
    private String newUrl = null; // 拼接key-value后的url
    private String url = null;
    private HttpResponse response = null;
    private DefaultHttpClient httpClient;
    //切换回UI线程
    protected Handler handler;
    //头部信息
    HashMap<String, String> heads;
    static long deltaBetweenServerAndClientTime; // 服务器时间和客户端时间的差值

    public HttpRequest(final URLData data, final List<RequestParameter> params,
                       final RequestCallback callback) {
        this.urlData = data;
        this.url = this.urlData.getUrl();
        this.parameters = params;
        this.requestCallback = callback;
        if (this.httpClient == null) {
            this.httpClient = new DefaultHttpClient();
        }
        handler = new Handler();
        heads = new HashMap<String, String>();
    }

    /**
     * 获取HttpUriRequest请求
     *
     * @return
     */
    public HttpUriRequest getRequest() {
        return request;
    }

    @Override
    public void run() {

        try {

            if (urlData.getNetType().equals(REQUEST_GET)) {
                //添加参数
                final StringBuffer paramBuffer = new StringBuffer();
                if ((parameters != null) && (parameters.size() > 0)) {
                    // 这里要对key进行排序
                    sortKeys();
                    for (final RequestParameter p : parameters) {
                        if (paramBuffer.length() == 0) {
                            paramBuffer.append(p.getName() + "=" + BaseUtils.UrlEncodeUnicode(p.getValue()));
                        } else {
                            paramBuffer.append("&" + p.getName() + "=" + BaseUtils.UrlEncodeUnicode(p.getValue()));
                        }
                    }
                    newUrl = url + "?" + paramBuffer.toString();
                } else {
                    newUrl = url;
                }
                //如果这个get的API有缓存时间(大于0)
                if (urlData.getExpires() > 0) {

                    final String content = CacheManager.getInstance().getFileCache(newUrl);
                    if (content != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onSuccess(content);
                            }
                        });
                        return;
                    }
                }

                request = new HttpGet(newUrl);

            } else if (urlData.getNetType().equals(REQUEST_POST)) {
                request = new HttpPost(url);
                //添加参数
                if ((parameters != null) && (parameters.size() > 0)) {
                    final List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
                    for (final RequestParameter p : parameters) {
                        list.add(new BasicNameValuePair(p.getName(), p.getValue()));
                    }
                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
                }


            } else {
                return;
            }
            request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            /**
             * 添加必要的头信息
             */
            setHttpHeaders(request);
            //添加Cookie到请求头
            addCookie();
            //发送请求
            response = httpClient.execute(request);
            //获取状态
            final int statusCode = response.getStatusLine().getStatusCode();
            // 设置回调函数，但如果requestCallback，说明不需要回调，不需要知道返回结果
            if (requestCallback != null) {
                if (statusCode == HttpStatus.SC_OK) {
                    // 更新服务器时间和本地时间的差值
                    updateDeltaBetweenServerAndClientTime();
                    final ByteArrayOutputStream content = new ByteArrayOutputStream();
                    //根据是否支持gzip来使用不同解析方式
                    String strResponse = "";
                    if ((response.getEntity().getContentEncoding() != null) &&
                            (response.getEntity().getContentEncoding().getValue() != null)) {

                        if (response.getEntity().getContentEncoding().getValue().contains("gzip")) {
                            InputStream in = response.getEntity().getContent();
                            InputStream is = new GZIPInputStream(in);
                            strResponse = HttpRequest.inputStreamToString(is);
                            is.close();
                        } else {
                            response.getEntity().writeTo(content);
                            strResponse = new String(content.toByteArray()).trim();
                        }

                    }

                    final Response responseInJson = JSON.parseObject(strResponse, Response.class);
                    if (responseInJson.isError()) {
                        if (responseInJson.getErrorType() == 1) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onCookieExpired();
                                }
                            });
                        } else {
                            handleNetworkError(responseInJson.getErrorMessage());
                        }
                    } else {

                        //把成功获取到的数据记录存到缓存中
                        if (urlData.getNetType().equals(REQUEST_GET) && urlData.getExpires() > 0) {
                            CacheManager.getInstance().putFileCache(newUrl, responseInJson.getResult(), urlData.getExpires());
                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HttpRequest.this.requestCallback.onSuccess(responseInJson.getResult());
                            }
                        });
                        //保存Cookie
                        saveCookie();
                    }

                } else {
                    handleNetworkError("网络异常");
                }
            }

        } catch (Exception e) {
            handleNetworkError("网络异常");
        }

    }

    public void handleNetworkError(final String errorMsg) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                HttpRequest.this.requestCallback.onFail(errorMsg);
            }
        });

    }

    public static String inputStreamToString(final InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 对key进行排序
     * 确保缓存数据时key-value顺序一致
     */
    void sortKeys() {
        for (int i = 1; i < parameters.size(); i++) {
            for (int j = i; j > 0; j--) {
                RequestParameter p1 = parameters.get(j - 1);
                RequestParameter p2 = parameters.get(j);
                if (compare(p1.getName(), p2.getName())) {
                    // 交互p1和p2这两个对象
                    String name = p1.getName();
                    String value = p1.getValue();

                    p1.setName(p2.getName());
                    p1.setValue(p2.getValue());

                    p2.setName(name);
                    p2.setValue(value);
                }
            }
        }
    }

    // 返回true说明str1大，返回false说明str2大
    boolean compare(String str1, String str2) {
        String uppStr1 = str1.toUpperCase();
        String uppStr2 = str2.toUpperCase();

        boolean str1IsLonger = true;
        int minLen = 0;

        if (str1.length() < str2.length()) {
            minLen = str1.length();
            str1IsLonger = false;
        } else {
            minLen = str2.length();
            str1IsLonger = true;
        }

        for (int index = 0; index < minLen; index++) {
            char ch1 = uppStr1.charAt(index);
            char ch2 = uppStr2.charAt(index);
            if (ch1 != ch2) {
                if (ch1 > ch2) {
                    return true; // str1大
                } else {
                    return false; // str2大
                }
            }
        }

        return str1IsLonger;
    }

    /**
     * cookie列表保存到本地
     */
    public synchronized void saveCookie() {
        //获取本次访问的cookie
        final List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        //将普通的cookie转化为可序列化的cookie
        List<SerializableCookie> serializableCookies = null;
        if ((cookies != null) && (cookies.size() > 0)) {
            serializableCookies = new ArrayList<SerializableCookie>();
            for (final Cookie c : cookies) {
                serializableCookies.add(new SerializableCookie(c));
            }
        }
        BaseUtils.saveObject(cookiePath, serializableCookies);
    }

    /**
     * 从本地获取cookie列表
     */
    public void addCookie() {

        List<SerializableCookie> cookieList = null;
        Object cookieObj = BaseUtils.restoreObject(cookiePath);
        if (cookieObj != null) {
            cookieList = (ArrayList<SerializableCookie>) cookieObj;
        }
        if ((cookieList != null) && (cookieList.size() > 0)) {
            BasicCookieStore cs = new BasicCookieStore();
            cs.addCookies(cookieList.toArray(new Cookie[]{}));
            httpClient.setCookieStore(cs);
        } else {
            httpClient.setCookieStore(null);
        }

    }

    /**
     * 添加头部信息
     *
     * @param httpUriRequest
     */
    void setHttpHeaders(HttpUriRequest httpUriRequest) {
        heads.clear();
        heads.put(FrameConstants.ACCEPT_CHARSET, "UTF-8,*");
        heads.put(FrameConstants.USER_AGENT, "my basic app");
        //gzip压缩
        heads.put(FrameConstants.ACCEPT_ENCODING, "gzip");
        if (httpUriRequest != null && heads != null) {
            for (final Map.Entry<String, String> entry : heads.entrySet()) {
                if (entry.getKey() != null) {
                    httpUriRequest.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * 更新服务器时间和本地时间的差值
     */
    void updateDeltaBetweenServerAndClientTime() {
        if (response != null) {
            final Header header = response.getLastHeader("Date");
            if (header != null) {
                final String strServerDate = header.getValue();
                try {
                    if ((strServerDate != null) && !strServerDate.equals("")) {
                        final SimpleDateFormat sdf = new SimpleDateFormat(
                                "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

                        Date serverDateUAT = sdf.parse(strServerDate);

                        deltaBetweenServerAndClientTime = serverDateUAT
                                .getTime()
                                + 8 * 60 * 60 * 1000
                                - System.currentTimeMillis();
                    }
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Date getServerTime() {
        return new Date(System.currentTimeMillis()
                + deltaBetweenServerAndClientTime);
    }

}
