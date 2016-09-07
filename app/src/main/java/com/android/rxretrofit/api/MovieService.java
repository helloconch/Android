package com.android.rxretrofit.api;

import com.android.rxretrofit.entity.HttpResult;
import com.android.rxretrofit.entity.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 豆瓣电影https://api.douban.com/v2/movie/top250?start=0&count=10
 */
public interface MovieService {

    String baseUrl = "https://api.douban.com/v2/movie/";

    @GET("top250")
    Call<HttpResult<List<Subject>>> getTopMovieByCall(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovieByObservable(@Query("start") int start, @Query("count") int count);

//添加头部信息，进行POST请求
//    FormBody.Builder builder = new FormBody.Builder();
//    builder.add("userCode","00000");
//    FormBody body = builder.build();
//    Observable<BaseResult<User>> user = userService.getUser(body);
//    @POST("getuser")
//    Observable<BaseResult<User>> getUser(@Body RequestBody request);

}
