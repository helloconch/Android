package com.android.app.lib.net;


import com.android.app.lib.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class RequestManager {

    ArrayList<HttpRequest> requests = null;

    public RequestManager(final BaseActivity acitvity) {
        //异步请求列表
        this.requests = new ArrayList<HttpRequest>();
    }

    /**
     * 添加Request到列表
     *
     * @param request
     */
    public void addRequest(final HttpRequest request) {
        requests.add(request);
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        if ((requests != null) && (requests.size() > 0)) {

            Iterator<HttpRequest> datas = requests.iterator();
            while (datas.hasNext()) {
                HttpRequest httpRequest = datas.next();
                httpRequest.getRequest().abort();
                datas.remove();
            }

        }

    }

    /**
     * 无参数调用
     *
     * @param urlData
     * @param requestCallback
     * @return
     */
    public HttpRequest createRequest(final URLData urlData, final RequestCallback requestCallback) {

        return createRequest(urlData, requestCallback, null);
    }

    /**
     * 有参数调用
     *
     * @param urlData
     * @param requestCallback
     * @param parameters
     * @return
     */
    public HttpRequest createRequest(final URLData urlData, final RequestCallback requestCallback,
                                     final ArrayList<RequestParameter> parameters) {

        HttpRequest request = new HttpRequest(urlData, parameters, requestCallback);
        addRequest(request);
        return request;

    }

}
