package com.android.rxretrofit.api;

import com.android.rxretrofit.entity.HttpResult;
import com.android.rxretrofit.entity.Subject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/7.
 */
public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;
    Retrofit retrofit;
    MovieService movieService;

    private HttpMethods() {
        //创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder().client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(MovieService.baseUrl)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods instance = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 获取电影TOP
     *
     * @param subscriber 调用者传过来的观察者对象
     * @param start
     * @param count
     */
    public void getTopMovie(Subscriber<HttpResult<List<Subject>>> subscriber, int start, int count) {
        movieService.getTopMovieByObservable(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取电影TOP
     *
     * @param subscriber 调用者传过来的观察者对象--只关心Activity/Fragment需要的类别信息
     * @param start
     * @param count
     */

    public void getTopMovies(Subscriber<List<Subject>> subscriber, int start, int count) {
        movieService.getTopMovieByObservable(start, count)
                .map(new HttpResultFunc<List<Subject>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {

            if (httpResult.getResultCode() != 0) {
                throw new ApiException(httpResult.getResultCode());
            }

            return httpResult.getData();
        }
    }
}
