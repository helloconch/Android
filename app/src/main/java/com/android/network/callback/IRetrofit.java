package com.android.network.callback;

import com.android.network.model.MarsJson;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRetrofit {

    @GET("/")
    Call<MarsJson> marsData();


//    //请求类似接口https://api.github.com/repos/{owner}/{repo}/contributors
//    @GET("repos/{owner}/{repo}/contributors")
//    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
}
