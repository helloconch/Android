package com.android.network;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.testing.App;
import com.android.testing.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/7/26.
 */
public class MarsActivity extends AppCompatActivity {
    final static String RECENT_API_ENDPOINT = "http://marsweather.ingenology.com/v1/latest/";
    final static String FLICKR_API_KEY = "b5efcc7f330be32bed06c9c8a174f8fd";
    final static String IMAGES_API_ENDPOINT = "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&sort=random&method=flickr.photos.search&" +
            "tags=mars,planet,rover&tag_mode=all&api_key=";
    App app = App.getInstance();
    @BindView(R.id.main_bg)
    ImageView mImageView;
    @BindView(R.id.error)
    TextView mTxtError;
    @BindView(R.id.degrees)
    TextView mTxtDegrees;
    @BindView(R.id.weather)
    TextView mTxtWeather;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mars);
        ButterKnife.bind(this);
        loadData();
        try {
            searchRandomImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, RECENT_API_ENDPOINT,
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
                    mTxtDegrees.setText(avgTemp + "°");
                    mTxtWeather.setText(atmo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtError(error);
            }
        });
        customJsonRequest.setmPriority(Request.Priority.HIGH);
        app.add(customJsonRequest);
    }

    private void txtError(Exception e) {
        mTxtError.setVisibility(View.VISIBLE);
        e.printStackTrace();
    }

    private void searchRandomImage() throws Exception {
        if (FLICKR_API_KEY.equals(""))
            throw new Exception("You didn't provide a working Flickr API!");

        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, IMAGES_API_ENDPOINT + FLICKR_API_KEY, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray images = response.getJSONObject("photos").getJSONArray("photo");
                            int index = new Random().nextInt(images.length());

                            JSONObject imageItem = images.getJSONObject(index);

                            String imageUrl = "http://farm" + imageItem.getString("farm") +
                                    ".static.flickr.com/" + imageItem.getString("server") + "/" +
                                    imageItem.getString("id") + "_" + imageItem.getString("secret") + "_" + "c.jpg";

                            // TODO: do something with *imageUrl*
                            loadImg(imageUrl);
                        } catch (Exception e) {
                            imageError(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageError(error);
                    }
                });
        request.setmPriority(Request.Priority.LOW);
        app.add(request);
    }

    private void loadImg(String imageUrl) {
        // Retrieves an image specified by the URL, and displays it in the UI
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageError(error);
                    }
                });

        // we don't need to set the priority here;
        // ImageRequest already comes in with
        // priority set to LOW, that is exactly what we need.
        app.add(request);
    }

    private void imageError(Exception e) {
        int mainColor = Color.parseColor("#FF5722");
        mImageView.setBackgroundColor(mainColor);
        e.printStackTrace();
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
