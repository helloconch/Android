package com.android.testing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cheyanxu on 16/8/27.
 */
public abstract class BaseAppFragment extends Fragment {

    private static final String TAG = BaseAppFragment.class.getSimpleName();

    /**
     * 控件是否初始化完毕
     */
    private boolean isViewCreated;
    /**
     * 数据是否初始化完毕
     */
    public boolean isLoadDataCompleted;

    public String value;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        value = getArguments().getString("item");
        Log.i(TAG, "onAttach=" + value);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate=" + value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView=" + value);
        return createView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated=" + value);
        isViewCreated = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated=" + value);

        if (getUserVisibleHint()) {
            loadData();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart=" + value);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause=" + value);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop=" + value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView=" + value);
        isLoadDataCompleted = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy=" + value);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach=" + value);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint=" + value);


        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            loadData();
        }

    }

    public abstract void loadData();

    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


}
