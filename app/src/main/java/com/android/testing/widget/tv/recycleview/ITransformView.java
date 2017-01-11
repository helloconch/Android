package com.android.testing.widget.tv.recycleview;


import android.view.View;

public interface ITransformView {

    void onFoucsChanged(View target, View oldFocusView, View newFocusView);

    void onScroolChanged(View target, View attachView);

    void onLayout(View target, View attachView);

    void onTouchModeChanged(View target, View attachView, boolean isInTouchMode);

    void onAttach(View target, View attachView);

    void onDetach(View target, View attachView);
}
