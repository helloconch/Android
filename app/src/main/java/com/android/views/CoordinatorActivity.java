package com.android.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/8/27.
 */
public class CoordinatorActivity extends AppCompatActivity {

    @BindView(R.id.dependent)
    ImageView dependent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dependent)
    public void onClick() {

        ViewCompat.offsetTopAndBottom(dependent, 5);
    }
}
