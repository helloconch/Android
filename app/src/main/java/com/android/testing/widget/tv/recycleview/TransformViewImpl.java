package com.android.testing.widget.tv.recycleview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class TransformViewImpl<V extends View> implements ViewTreeObserver.OnGlobalFocusChangeListener,
        ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener,
        ViewTreeObserver.OnTouchModeChangeListener {

    private static final String TAG = TransformViewImpl.class.getSimpleName();

    private ViewGroup mViewGroup;

    private ITransformView transformView;

    private V mView;

    private View mLastView;


    public TransformViewImpl(V mView) {
        this.transformView = new TransformViewHandler();
        this.mView = mView;
    }

    public TransformViewImpl(V mView, ITransformView mTransformView) {
        this.transformView = mTransformView;
        this.mView = mView;
    }

    public TransformViewImpl(Context context, int resId) {
        this((V) LayoutInflater.from(context).inflate(resId, null, false));
    }

    public TransformViewImpl(Context context) {
        this(context, null, 0);
    }

    public TransformViewImpl(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformViewImpl(Context context, @Nullable AttributeSet attrs, int defStyle) {
        init(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        this.transformView = new TransformViewHandler();
        this.mView = (V) new View(context, attrs, defStyle);
    }


    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {

        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (oldFocus == null && this.mLastView != null) {
                    oldFocus = mLastView;
                }

            }
            this.transformView.onFoucsChanged(this.mView, oldFocus, newFocus);
            this.mLastView = newFocus;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGlobalLayout() {
        this.transformView.onLayout(this.mView, this.mViewGroup);
    }

    @Override
    public void onScrollChanged() {
        this.transformView.onScroolChanged(this.mView, this.mViewGroup);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        this.transformView.onTouchModeChanged(this.mView, this.mViewGroup, isInTouchMode);
    }

    public void attachTo(ViewGroup viewGroup) {

        try {
            if (viewGroup == null) {
                if (this.mView.getContext() instanceof Activity) {
                    Activity activity = (Activity) this.mView.getContext();
                    viewGroup = (ViewGroup) activity.getWindow().getDecorView().getRootView();
                }
            }

            if (this.mViewGroup != viewGroup) {
                ViewTreeObserver viewTreeObserver = viewGroup.getViewTreeObserver();
                if (viewTreeObserver.isAlive() && this.mViewGroup == null) {
                    viewTreeObserver.addOnGlobalFocusChangeListener(this);
                    viewTreeObserver.addOnScrollChangedListener(this);
                    viewTreeObserver.addOnGlobalLayoutListener(this);
                    viewTreeObserver.addOnTouchModeChangeListener(this);
                }
                this.mViewGroup = viewGroup;
            }

            this.transformView.onAttach(mView, viewGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void detach() {

        if (this.mViewGroup != null) {
            ViewTreeObserver viewTreeObserver = this.mViewGroup.getViewTreeObserver();
            viewTreeObserver.removeOnGlobalFocusChangeListener(this);
            viewTreeObserver.removeOnScrollChangedListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                viewTreeObserver.removeOnGlobalLayoutListener(this);
            viewTreeObserver.removeOnTouchModeChangeListener(this);
            this.transformView.onDetach(this.mView, this.mViewGroup);

        }

    }

    public V getView() {
        return mView;
    }

    public void setBackground(int resId) {
        if (this.mView != null)
            this.mView.setBackgroundResource(resId);
    }
}
