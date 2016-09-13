package com.android.tv;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/12.
 */
public class FocusAFragment extends Fragment implements View.OnKeyListener, View.OnFocusChangeListener {
    @BindView(R.id.myFocusImage1)
    MyFocusImage myFocusImage1;
    @BindView(R.id.myFocusImage2)
    MyFocusImage myFocusImage2;
    @BindView(R.id.myFocusImage3)
    MyFocusImage myFocusImage3;
    @BindView(R.id.myFocusImage4)
    MyFocusImage myFocusImage4;
    @BindView(R.id.myFocusImage5)
    MyFocusImage myFocusImage5;
    @BindView(R.id.myFocusImage6)
    MyFocusImage myFocusImage6;
    @BindView(R.id.myFocusImage7)
    MyFocusImage myFocusImage7;
    @BindView(R.id.myFocusImag8)
    MyFocusImage myFocusImag8;
    @BindView(R.id.myFocusImag9)
    MyFocusImage myFocusImag9;
    IChageTabs tabsImpl;

    SparseArray<FocusItem> sparseArray = new SparseArray<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChageTabs)
            tabsImpl = (IChageTabs) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_focus_a, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        focusView(myFocusImage1);
        focusView(myFocusImage2);
        focusView(myFocusImage3);
        focusView(myFocusImage4);
        focusView(myFocusImage5);
        focusView(myFocusImage6);
        focusView(myFocusImage7);
        focusView(myFocusImag8);
        focusView(myFocusImag9);

    }

    private void focusView(View view) {
        view.setFocusable(true);
        //默认进入该页面进行选中
//        view.requestFocus();
        view.setFocusableInTouchMode(true);
        //监听方向键事件
        view.setOnKeyListener(this);
        //监听临界点焦点事件
        view.setOnFocusChangeListener(this);
    }

    @OnClick({R.id.myFocusImage1, R.id.myFocusImage2, R.id.myFocusImage3, R.id.myFocusImage4, R.id.myFocusImage5, R.id.myFocusImage6, R.id.myFocusImage7, R.id.myFocusImag8})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myFocusImage1:
                System.out.print("");
                break;
            case R.id.myFocusImage2:
                System.out.print("");
                break;
            case R.id.myFocusImage3:
                System.out.print("");
                break;
            case R.id.myFocusImage4:
                System.out.print("");
                break;
            case R.id.myFocusImage5:
                System.out.print("");
                break;
            case R.id.myFocusImage6:
                System.out.print("");
                break;
            case R.id.myFocusImage7:
                System.out.print("");
                break;
            case R.id.myFocusImag8:
                System.out.print("");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        handleFocusItem(v, hasFocus);
        if (v instanceof MyFocusImage) {
            if (hasFocus)
                ((MyFocusImage) v).zoom();
            else
                ((MyFocusImage) v).reset();
        }
    }


    private void handleFocusItem(View v, boolean hasFocus) {
        sparseArray.put(v.getId(), new FocusItem(hasFocus));
    }

    private void handleOnKey(View v, int keyCode) {
        int vId = v.getId();
        FocusItem focusItem = sparseArray.get(vId, null);
        if (focusItem == null) {
            return;
        }
        focusItem.setKeyCount(focusItem.getKeyCount() + 1);

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //判断是否到达右侧临界点
                if (v.getId() == R.id.myFocusImage6 || v.getId() == R.id.myFocusImage7
                        || v.getId() == R.id.myFocusImag8 || v.getId() == R.id.myFocusImag9) {
                    if (focusItem.isFocus() && focusItem.getKeyCount() == 2) {
                        nextFocusRight();
                    }
                }

                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                //判断是否达到顶侧临界点
                if (v.getId() == R.id.myFocusImage1 || v.getId() == R.id.myFocusImage2
                        || v.getId() == R.id.myFocusImage4 || v.getId() == R.id.myFocusImage6) {
                    if (focusItem.isFocus() && focusItem.getKeyCount() == 2) {
                        nextFocusUp();
                    }
                }

                break;

        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        handleOnKey(v, keyCode);
        return false;
    }

    private void nextFocusRight() {
        if (tabsImpl != null) {
            tabsImpl.changeTabIndex(R.id.two);
        }
    }

    private void nextFocusUp() {
        if (tabsImpl != null) {
            tabsImpl.changeTabIndex(R.id.one);
        }
    }

    public interface IChageTabs {
        void changeTabIndex(int id);
    }


}
