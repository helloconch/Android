package com.android.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.android.testing.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class MovieActivity extends AppCompatActivity {
    @BindView(R.id.movieWebView)
    WebView movieWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_webview);
        ButterKnife.bind(this);

        String template = getFromAssets("data1_template.html");
        StringBuilder sbContent = new StringBuilder();

        ArrayList<MovieInfo> movieList = organizeMovieList();
        for (MovieInfo movie : movieList) {
            String rowData;
            rowData = template.replace("<name>", movie.getName());
            rowData = rowData.replace("<price>", movie.getPrice());
            sbContent.append(rowData);
        }

        String realData = getFromAssets("102.html");
        realData = realData.replace("<data1DefinedByBaobao>",
                sbContent.toString());

        movieWebView.loadData(realData, "text/html", "utf-8");
    }

    public ArrayList<MovieInfo> organizeMovieList() {
        ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
        movieList.add(new MovieInfo("Movie 1", "120"));
        movieList.add(new MovieInfo("Movie B", "80"));
        movieList.add(new MovieInfo("Movie III", "60"));

        return movieList;
    }

    public class MovieInfo {
        public MovieInfo(String name, String price) {
            this.name = name;
            this.price = price;
        }

        private String name;
        private String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
