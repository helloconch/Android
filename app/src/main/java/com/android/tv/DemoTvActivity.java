package com.android.tv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/12.
 */
public class DemoTvActivity extends AppCompatActivity implements View.OnFocusChangeListener, FocusAFragment.IChageTabs {
    private static final String TAG_A = "tag_a";
    private static final String TAG_B = "tag_b";
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.one)
    Button one;
    @BindView(R.id.two)
    Button two;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_tv);
        ButterKnife.bind(this);
        onClick(one);
        one.setOnFocusChangeListener(this);
        two.setOnFocusChangeListener(this);
    }

    @OnClick({R.id.one, R.id.two})
    public void onClick(View view) {
        show(view.getId());
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus)
            show(view.getId());
    }

    private void show(int id) {

        if (isFinishing()) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragmentA = fragmentManager.findFragmentByTag(TAG_A);
        Fragment fragmentB = fragmentManager.findFragmentByTag(TAG_B);
        if (id == R.id.one) {
            if (fragmentB != null) {
                fragmentTransaction.hide(fragmentB);
            }
            if (fragmentA != null) {
                fragmentTransaction.show(fragmentA);
            } else {
                fragmentA = new FocusAFragment();
                fragmentTransaction.add(R.id.container, fragmentA, TAG_A);
            }
        } else {
            if (fragmentA != null) {
                fragmentTransaction.hide(fragmentA);
            }
            if (fragmentB != null) {
                fragmentTransaction.show(fragmentB);
            } else {
                fragmentB = new FocusBFragment();
                fragmentTransaction.add(R.id.container, fragmentB, TAG_B);
            }

        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void changeTabIndex(int id) {
        if (id == R.id.one) {
            one.requestFocus();
        }

        if (id == R.id.two)
            one.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
