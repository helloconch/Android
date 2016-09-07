package com.android.rxretrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rxretrofit.api.HttpMethods;
import com.android.rxretrofit.api.MovieService;
import com.android.rxretrofit.api.ProgressSubscriber;
import com.android.rxretrofit.api.SubscriberOnNextListener;
import com.android.rxretrofit.entity.HttpResult;
import com.android.rxretrofit.entity.Subject;
import com.android.testing.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/7.
 */
public class RxRetrofitActivity extends AppCompatActivity {
    @BindView(R.id.result)
    TextView result;

    SubscriberOnNextListener<List<Subject>> subscriberOnNextListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.click)
    public void onClick() {
//        goMovieByCall();
//        goMovieByObservable();
//        goMovieByObservalble2();
//        goMovieByObserver3();
        goMovieByObserver4();
    }

    /**
     * retrofit
     */
    private void goMovieByCall() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(MovieService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        Call<HttpResult<List<Subject>>> call = movieService.getTopMovieByCall(0, 10);

        call.enqueue(new Callback<HttpResult<List<Subject>>>() {
            @Override
            public void onResponse(Call<HttpResult<List<Subject>>> call, Response<HttpResult<List<Subject>>> response) {
                result.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<HttpResult<List<Subject>>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });

    }

    /**
     * rxjava+retrofit
     */
    private void goMovieByObservable() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MovieService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Observable<HttpResult<List<Subject>>> observable = movieService.getTopMovieByObservable(0, 10);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<List<Subject>>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RxRetrofitActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        result.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<List<Subject>> listHttpResult) {
                        result.setText(listHttpResult.toString());
                    }
                });
    }

    /**
     * 采用单例模式访问对请求形式进行封装
     */
    private void goMovieByObservalble2() {

        Subscriber<HttpResult<List<Subject>>> subscriber = new Subscriber<HttpResult<List<Subject>>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxRetrofitActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                result.setText(e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Subject>> listHttpResult) {
                result.setText(listHttpResult.toString());
            }
        };


        HttpMethods.getInstance().getTopMovie(subscriber, 0, 10);

    }


    private void goMovieByObserver3() {
        Subscriber<List<Subject>> subscriber = new Subscriber<List<Subject>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxRetrofitActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                result.setText(e.getMessage());
            }

            @Override
            public void onNext(List<Subject> subjects) {
                result.setText(subjects.toString());
            }
        };


        HttpMethods.getInstance().getTopMovies(subscriber, 0, 10);


        //解除请求
//        subscriber.unsubscribe();
    }

    /**
     * 添加加载进度开始 结束或者取消
     */
    private void goMovieByObserver4() {

        subscriberOnNextListener = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                //处理真正返回的数据
            }
        };

        ProgressSubscriber<List<Subject>> subscriber = new ProgressSubscriber<>(subscriberOnNextListener, this);


        HttpMethods.getInstance().getTopMovies(subscriber, 0, 10);
    }
}
