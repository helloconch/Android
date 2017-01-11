package com.android.testing.widget.tv.recycleview;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformViewHandler implements ITransformView {

    private boolean mScalable = true;
    private float mScale = 1.1f;
    private long mFocusTranslateDuration = 200;
    private int mMargin;
    private View lastFocus;
    private View oldLastFocus;
    private View mTarget;
    private AnimatorSet mAnimatorSet;
    private List<Animator> mAnimatorList = new ArrayList<>();
    private List<FocusListener> focusListenerList = new ArrayList<>();
    private List<Animator.AnimatorListener> animatorListenerList = new ArrayList<>();
    protected List<View> attacheViews = new ArrayList<>();
    protected Map<View, AdapterView.OnItemSelectedListener> onItemSelectedListenerList = new HashMap<>();
    private boolean mEnableTouch = false;
    protected boolean isScrolling = false;
    protected boolean mFirstFocus = true;

    public TransformViewHandler() {
        this.focusListenerList.add(mFocusMoveListener);
        this.focusListenerList.add(mFocusScaleListener);
        this.focusListenerList.add(mFocusPlayListener);
        this.focusListenerList.add(mAbsListViewFocusListener);

    }


    @Override
    public void onFoucsChanged(View target, View oldFocus, View newFocus) {
        try {
            if (newFocus == null && attacheViews.indexOf(newFocus) >= 0) {
                return;
            }
            if (oldFocus == newFocus)
                return;
            //如果动画正在运行时
            if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
                mAnimatorSet.end();
            }
            lastFocus = newFocus;
            oldLastFocus = oldFocus;
            mTarget = target;

            VisibleScope scope = checkVisibleScope(oldFocus, newFocus);
            if (!scope.isVisible) {
                return;
            } else {
                oldFocus = scope.oldFocus;
                newFocus = scope.newFocus;
                oldLastFocus = scope.oldFocus;
            }

            if (isScrolling || newFocus == null || newFocus.getWidth() <= 0 || newFocus.getHeight() <= 0)
                return;

            //清除动画
            mAnimatorList.clear();

            for (FocusListener f : this.focusListenerList) {
                f.onFocusChanged(oldFocus, newFocus);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onScroolChanged(View target, View attachView) {

    }

    @Override
    public void onLayout(View target, View attachView) {
        try {
            ViewGroup viewGroup = (ViewGroup) attachView.getRootView();
            if (target.getParent() != null && target.getParent() != viewGroup) {
                target.setVisibility(View.GONE);
                //如果是首次获取焦点,强制变成焦点态
                if (mFirstFocus)
                    viewGroup.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTouchModeChanged(View target, View attachView, boolean isInTouchMode) {
        try {
            if (mEnableTouch && isInTouchMode) {
                target.setVisibility(View.INVISIBLE);
                if (lastFocus != null) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(getScaleAnimator(lastFocus, false));
                    animatorSet.setDuration(0).start();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAttach(View target, View attachView) {
        try {
            mTarget = target;

            if (target.getParent() != null && (target.getParent() instanceof ViewGroup)) {
                ViewGroup vg = (ViewGroup) target.getParent();
                vg.removeView(target);
            }

            ViewGroup vg = (ViewGroup) attachView.getRootView();
            vg.addView(target);

            target.setVisibility(View.GONE);
            if (attachView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) attachView;
                RecyclerView.OnScrollListener recyclerViewOnScrollListener = null;
                recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        try {
                            super.onScrollStateChanged(recyclerView, newState);

                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                isScrolling = false;
                                View oldFocus = oldLastFocus;
                                View newFocus = lastFocus;
                                VisibleScope scope = checkVisibleScope(oldFocus, newFocus);
                                if (!scope.isVisible) {
                                    return;
                                } else {
                                    oldFocus = scope.oldFocus;
                                    newFocus = scope.newFocus;
                                }
                                AnimatorSet animatorSet = new AnimatorSet();
                                List<Animator> list = new ArrayList<>();
                                list.addAll(getScaleAnimator(newFocus, true));
                                list.addAll(getMoveAnimator(newFocus, 0, 0));
                                animatorSet.setDuration(mFocusTranslateDuration);
                                animatorSet.playTogether(list);
                                animatorSet.start();

                            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                                isScrolling = true;
                                if (lastFocus != null) {
                                    List<Animator> list = getScaleAnimator(lastFocus, false);
                                    AnimatorSet animatorSet = new AnimatorSet();
                                    animatorSet.setDuration(150);
                                    animatorSet.playTogether(list);
                                    animatorSet.start();
                                }
                            }
                        } catch (Exception ex) {

                        }
                    }
                };
                recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
            } else if (attachView instanceof AbsListView) {

                final AbsListView absListView = (AbsListView) attachView;
                final AdapterView.OnItemSelectedListener onItemSelectedListener = absListView.getOnItemSelectedListener();

                View temp = null;
                if (absListView.getChildCount() > 0) {
                    temp = absListView.getChildAt(0);
                }
                final View tempFocus = temp;
                MyOnItemSelectedListener myOnItemSelectedListener = new MyOnItemSelectedListener();
                myOnItemSelectedListener.onItemSelectedListener = onItemSelectedListener;
                myOnItemSelectedListener.oldFocus = temp;
                absListView.setOnItemSelectedListener(myOnItemSelectedListener);
                onItemSelectedListenerList.put(attachView, myOnItemSelectedListener);

            }

            attacheViews.add(attachView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDetach(View target, View attachView) {
        if (target.getParent() == attachView) {
            ((ViewGroup) attachView).removeView(target);
        }
        attacheViews.remove(attachView);
    }

    FocusListener mFocusMoveListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            try {
                if (newFocus == null)
                    return;
                mAnimatorList.addAll(getMoveAnimator(newFocus, 0, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    FocusListener mFocusScaleListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            try {
                //新焦点放大
                mAnimatorList.addAll(getScaleAnimator(newFocus, true));
                if (oldFocus != null) {
                    //上一个焦点复位
                    mAnimatorList.addAll(getScaleAnimator(oldFocus, false));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    FocusListener mFocusPlayListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            try {
                if (newFocus instanceof AbsListView)
                    return;

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setInterpolator(new DecelerateInterpolator(1));
                animatorSet.setDuration(mFocusTranslateDuration);
                animatorSet.playTogether(mAnimatorList);
                for (Animator.AnimatorListener listener : animatorListenerList) {
                    animatorSet.addListener(listener);
                }
                mAnimatorSet = animatorSet;
                if (oldFocus == null) {
                    animatorSet.setDuration(0);
                    mTarget.setVisibility(View.VISIBLE);
                }
                animatorSet.start();//开启动画
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    FocusListener mAbsListViewFocusListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            try {
                if (oldFocus == null) {
                    for (int i = 0; i < attacheViews.size(); i++) {
                        View view = attacheViews.get(i);
                        if (view instanceof AbsListView) {
                            final AbsListView absListView = (AbsListView) view;
                            mTarget.setVisibility(View.INVISIBLE);
                            if (mFirstFocus) {
                                absListView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                        try {
                                            absListView.removeOnLayoutChangeListener(this);
                                            int factorX = 0, factorY = 0;
                                            Rect rect = new Rect();
                                            View firstView = (View) absListView.getSelectedView();
                                            firstView.getLocalVisibleRect(rect);
                                            if (Math.abs(rect.left - rect.right) > firstView.getMeasuredWidth()) {
                                                factorX = (Math.abs(rect.left - rect.right) - firstView.getMeasuredWidth()) / 2 - 1;
                                                factorY = (Math.abs(rect.top - rect.bottom) - firstView.getMeasuredHeight()) / 2;
                                            }
                                            List<Animator> animatorList = new ArrayList<Animator>(3);
                                            animatorList.addAll(getScaleAnimator(firstView, true));
                                            animatorList.addAll(getMoveAnimator(firstView, factorX, factorY));
                                            mTarget.setVisibility(View.VISIBLE);
                                            AnimatorSet animatorSet = new AnimatorSet();
                                            animatorSet.setDuration(0);
                                            animatorSet.playTogether(animatorList);
                                            animatorSet.start();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });


                            }
                            break;
                        }
                    }
                } else if (oldFocus instanceof AbsListView && newFocus instanceof AbsListView) {
                    if (attacheViews.indexOf(oldFocus) >= 0 && attacheViews.indexOf(newFocus) >= 0) {

                        AbsListView a = (AbsListView) oldFocus;
                        AbsListView b = (AbsListView) newFocus;

                        MyOnItemSelectedListener oldOn = (MyOnItemSelectedListener) onItemSelectedListenerList.get(oldFocus);
                        MyOnItemSelectedListener newOn = (MyOnItemSelectedListener) onItemSelectedListenerList.get(newFocus);


                        int factorX = 0, factorY = 0;
                        Rect rect = new Rect();
                        View firstView = (View) b.getSelectedView();
                        firstView.getLocalVisibleRect(rect);
                        if (Math.abs(rect.left - rect.right) > firstView.getMeasuredWidth()) {
                            factorX = (Math.abs(rect.left - rect.right) - firstView.getMeasuredWidth()) / 2 - 1;
                            factorY = (Math.abs(rect.top - rect.bottom) - firstView.getMeasuredHeight()) / 2;
                        }

                        List<Animator> animatorList = new ArrayList<Animator>(3);
                        animatorList.addAll(getScaleAnimator(firstView, true));
                        animatorList.addAll(getScaleAnimator(a.getSelectedView(), false));

                        animatorList.addAll(getMoveAnimator(firstView, factorX, factorY));
                        mTarget.setVisibility(View.VISIBLE);

                        mAnimatorSet = new AnimatorSet();


                        mAnimatorSet.setDuration(mFocusTranslateDuration);
                        mAnimatorSet.playTogether(animatorList);
                        mAnimatorSet.start();

                        oldOn.oldFocus = null;
                        oldOn.newFocus = null;

                        newOn.oldFocus = null;
                        if (newOn.newFocus != null && newOn.oldFocus != null) {
                            newOn.newFocus = null;
                        } else {
                            newOn.newFocus = b.getSelectedView();
                        }

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    };


    /**
     * 缩放动画
     *
     * @param view
     * @param isScale
     * @return
     */
    private List<Animator> getScaleAnimator(View view, boolean isScale) {
        List<Animator> animatorList = new ArrayList<>();
        try {
            if (!this.mScalable)
                return animatorList;
            float scaleBefore = 1.0f;
            float scaleAfter = this.mScale;
            if (!isScale) {
                scaleBefore = this.mScale;
                scaleAfter = 1.0f;
            }

            ObjectAnimator scaleX = new ObjectAnimator().ofFloat(view, "scaleX", scaleBefore, scaleAfter);
            ObjectAnimator scaleY = new ObjectAnimator().ofFloat(view, "scaleY", scaleBefore, scaleAfter);

            animatorList.add(scaleX);
            animatorList.add(scaleY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return animatorList;
    }

    public List<Animator> getMoveAnimator(View newFocus, int factorX, int factorY) {
        List<Animator> animatorList = new ArrayList<>();
        int newXY[];
        int oldXY[];
        try {
            newXY = getLocation(newFocus);
            oldXY = getLocation(this.mTarget);
            int newWidth;
            int newHeight;
            int oldWidth = this.mTarget.getMeasuredWidth();
            int oldHeight = this.mTarget.getMeasuredHeight();
            if (this.mScalable) {
                float scaleWidth = newFocus.getMeasuredWidth() * this.mScale;
                float scaleHeight = newFocus.getMeasuredHeight() * this.mScale;
                float offset = 0.5f;
                newWidth = (int) (scaleWidth + this.mMargin * 2 + offset);
                newHeight = (int) (scaleHeight + this.mMargin * 2 + offset);

                newXY[0] = (int) (newXY[0] - (newWidth - newFocus.getMeasuredWidth()) / 2.0f + factorX);
                newXY[1] = (int) (newXY[1] - (newHeight - newFocus.getMeasuredHeight()) / 2.0f + 0.5 + factorY);
            } else {
                newWidth = newFocus.getMeasuredWidth();
                newHeight = newFocus.getMeasuredHeight();
            }

            if (oldWidth == 0 && oldHeight == 0) {
                oldWidth = newWidth;
                oldHeight = newHeight;
            }

            PropertyValuesHolder valuesWidthHolder = PropertyValuesHolder.ofInt("width", oldWidth, newWidth);
            PropertyValuesHolder valuesHeightHolder = PropertyValuesHolder.ofInt("height", oldHeight, newHeight);
            PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofFloat("translationX", oldXY[0], newXY[0]);
            PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofFloat("translationY", oldXY[1], newXY[1]);

            final ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(this.mTarget, valuesWidthHolder, valuesHeightHolder, valuesXHolder, valuesYHolder);

            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int width = (int) animation.getAnimatedValue("width");
                    int height = (int) animation.getAnimatedValue("height");
                    float translationX = (float) animation.getAnimatedValue("translationX");
                    float translationY = (float) animation.getAnimatedValue("translationY");

                    View view = (View) scaleAnimator.getTarget();
                    if (view != null) {
                        view.getLayoutParams().width = width;
                        view.getLayoutParams().height = height;
                        if (width > 0) {
                            view.requestLayout();
                            view.postInvalidate();
                        }
                    }

                }
            });
            animatorList.add(scaleAnimator);
        } catch (Exception e) {

        }
        return animatorList;
    }

    protected int[] getLocation(View view) {
        int[] location = new int[2];
        try {
            view.getLocationOnScreen(location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return location;
    }

    protected class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public View oldFocus = null;
        public View newFocus = null;
        public AnimatorSet animatorSet;
        public AdapterView.OnItemSelectedListener onItemSelectedListener;

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (onItemSelectedListener != null && parent != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }
                if (newFocus == null)
                    return;
                newFocus = view;

                Rect rect = new Rect();
                view.getLocalVisibleRect(rect);
                ViewGroup vg = (ViewGroup) newFocus.getParent();

                int factorX = 0, factorY = 0;
                if (Math.abs(rect.left - rect.right) > newFocus.getMeasuredWidth()) {
                    factorX = (Math.abs(rect.left - rect.right) - newFocus.getMeasuredWidth()) / 2 - 1;
                    factorY = (Math.abs(rect.top - rect.bottom) - newFocus.getMeasuredHeight()) / 2;

                }


                List<Animator> animatorList = new ArrayList<Animator>(3);
                animatorList.addAll(getScaleAnimator(newFocus, true));
                if (oldFocus != null)
                    animatorList.addAll(getScaleAnimator(oldFocus, false));
                animatorList.addAll(getMoveAnimator(newFocus, factorX, factorY));
                mTarget.setVisibility(View.VISIBLE);

                if (animatorSet != null && animatorSet.isRunning())
                    animatorSet.end();
                animatorSet = new AnimatorSet();
                animatorSet.setDuration(mFocusTranslateDuration);
                animatorSet.playTogether(animatorList);
                animatorSet.start();


                oldFocus = newFocus;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onNothingSelected(parent);
            }
        }
    }


    private class VisibleScope {
        public boolean isVisible;
        public View oldFocus;
        public View newFocus;
    }

    protected VisibleScope checkVisibleScope(View oldFocus, View newFocus) {
        VisibleScope scope = new VisibleScope();
        try {
            scope.oldFocus = oldFocus;
            scope.newFocus = newFocus;
            scope.isVisible = true;
            if (attacheViews.indexOf(oldFocus) >= 0 && attacheViews.indexOf(newFocus) >= 0) {
                return scope;
            }

            if (oldFocus != null && newFocus != null) {
                if (oldFocus.getParent() != newFocus.getParent()) {
                    if ((attacheViews.indexOf(newFocus.getParent()) < 0) || (attacheViews.indexOf(oldFocus.getParent()) < 0 && attacheViews.indexOf(newFocus.getParent()) > 0)) {
                        mTarget.setVisibility(View.INVISIBLE);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(getScaleAnimator(oldFocus, false));
                        animatorSet.setDuration(0).start();
                        scope.isVisible = false;
                        return scope;
                    } else {
                        mTarget.setVisibility(View.VISIBLE);
                    }
                    if (attacheViews.indexOf(oldFocus.getParent()) < 0) {
                        scope.oldFocus = null;
                    }

                } else {
                    if (attacheViews.indexOf(newFocus.getParent()) < 0) {
                        mTarget.setVisibility(View.INVISIBLE);
                        scope.isVisible = false;
                        return scope;
                    }
                }
            }
            mTarget.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return scope;
    }

    public interface FocusListener {
        void onFocusChanged(View oldFocus, View newFocus);
    }

}
