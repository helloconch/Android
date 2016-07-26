package com.android.network.callback;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.network.model.MarsBean;
import com.android.testing.App;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求
 */
public class BaseSend implements ISend {

    @Override
    public void marsData(SendCallback callback) {

        if (netRequestType == NetRequestType.VOLLEY) {
            loadMarsDataByVolley(callback);
        }

    }

    @Override
    public void marsImage(SendCallback callback) {
        if (netRequestType == NetRequestType.VOLLEY) {
            loadImgByVolley(callback);
        }
    }

    /**
     * 使用Volley进行数据获取
     *
     * @param callback
     */
    private void loadMarsDataByVolley(final SendCallback callback) {
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, BaseSendUrls.RECENT_API_ENDPOINT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String minTemp, maxTemp, atmo;
                    int avgTemp;
                    response = response.getJSONObject("report");
                    minTemp = response.getString("min_temp");
                    minTemp = minTemp.substring(0, minTemp.indexOf("."));
                    maxTemp = response.getString("max_temp");
                    maxTemp = maxTemp.substring(0, maxTemp.indexOf("."));
                    avgTemp = (Integer.parseInt(minTemp) + Integer.parseInt(maxTemp)) / 2;
                    atmo = response.getString("atmo_opacity");
                    MarsBean marsBean = new MarsBean();
                    marsBean.setDegrees(avgTemp + "");
                    marsBean.setWeather(atmo);
                    callback.onSuccessed(marsBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });
        customJsonRequest.setmPriority(Request.Priority.HIGH);
        App.getInstance().add(customJsonRequest);
    }

    private void loadImgByVolley(final SendCallback callback) {
        // Retrieves an image specified by the URL, and displays it in the UI
        ImageRequest request = new ImageRequest(BaseSendUrls.MOCK_MARS_IMAGE_URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        callback.onSuccessed(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                });

        // we don't need to set the priority here;
        // ImageRequest already comes in with
        // priority set to LOW, that is exactly what we need.
        App.getInstance().add(request);
    }

    public class CustomJsonRequest extends JsonObjectRequest {

        public CustomJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        private Priority mPriority;

        public void setmPriority(Priority mPriority) {
            this.mPriority = mPriority;
        }

        @Override
        public Priority getPriority() {
            return mPriority == null ? Priority.NORMAL : mPriority;
        }
    }
}
