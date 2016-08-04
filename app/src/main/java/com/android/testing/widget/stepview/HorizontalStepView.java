package com.android.testing.widget.stepview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.testing.R;
import com.android.testing.utils.AppUtils;

import java.util.List;

/**
 * 描述：StepView
 */
public class HorizontalStepView extends LinearLayout implements HorizontalStepsViewIndicator.OnDrawIndicatorListener {

    private Context mContext;
    private LinearLayout mTextContainer;
    private HorizontalStepsViewIndicator mStepsViewIndicator;
    private List<String> mTexts;
    private int mComplectingPosition;
    //定义默认未完成文字的颜色;
    private int mUnComplectedTextColor = ContextCompat.getColor(getContext(), R.color.uncompleted_font_color);
    //定义默认完成文字的颜色;
    private int mComplectedTextColor = ContextCompat.getColor(getContext(), R.color.completed_font_color);

    public HorizontalStepView(Context context) {
        this(context, null);
    }

    public HorizontalStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_horizontal_stepsview, this);
        mStepsViewIndicator = (HorizontalStepsViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (LinearLayout) rootView.findViewById(R.id.rl_text_container);
        mTextContainer.removeAllViews();
    }

    /**
     * 设置显示的文字
     *
     * @param texts
     * @return
     */
    public HorizontalStepView setStepViewTexts(List<String> texts) {
        mTexts = texts;
        mStepsViewIndicator.setStepNum(mTexts.size());
        return this;
    }

    /**
     * 设置正在进行的position
     * 从0开始
     *
     * @param complectingPosition
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorComplectingPosition(int complectingPosition) {
        mComplectingPosition = complectingPosition;
        mStepsViewIndicator.setComplectingPosition(complectingPosition);
        return this;
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    public HorizontalStepView setStepViewUnComplectedTextColor(int unComplectedTextColor) {
        mUnComplectedTextColor = unComplectedTextColor;
        return this;
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    public HorizontalStepView setStepViewComplectedTextColor(int complectedTextColor) {
        this.mComplectedTextColor = complectedTextColor;
        return this;
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
        mStepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    public HorizontalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    public HorizontalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon) {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    public HorizontalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon) {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator最后的图片
     *
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorEndIcon(Drawable endIcon) {
        mStepsViewIndicator.setEndIcon(endIcon);
        return this;
    }

    /**
     * 设置未奖励图片
     *
     * @param unAwardIcon
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorUnAwardIcon(Drawable unAwardIcon) {
        mStepsViewIndicator.setUnAwardIcon(unAwardIcon);
        return this;
    }

    /**
     * 设置奖励图片
     *
     * @param awardIcon
     * @return
     */
    public HorizontalStepView setStepsViewIndicatorAwardIcon(Drawable awardIcon) {
        mStepsViewIndicator.setAwardedIcon(awardIcon);
        return this;
    }


    @Override
    public void ondrawIndicator() {
//        mTextContainer.removeAllViews();
        List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
        if (complectedXPosition == null || complectedXPosition.size() == 0) {
            return;
        }
        boolean isHasText = mTextContainer.getChildCount() > 0 ? true : false;

        int left = (int) (complectedXPosition.get(0) - mStepsViewIndicator.getCircleRadius() / 2);
        if (mTexts != null && mTexts.size() > 0) {
            int width = ((AppUtils.getScreenDisplayMetrics(mContext).widthPixels - left * 2) / (mTexts.size() - 1));
            for (int i = 0; i < mTexts.size(); i++) {

                if (isHasText) {
                    TextView textView = (TextView) mTextContainer.getChildAt(i);
                    textView.setText(mTexts.get(i));
                    continue;
                }

                TextView textView = new TextView(getContext());
                textView.setText(mTexts.get(i));

                textView.setTextSize(12);
                if (i == 0) {
                    textView.setGravity(Gravity.LEFT);
                    LayoutParams params = new LayoutParams((int) (width / 2 + mStepsViewIndicator.getCircleRadius() / 2), LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(params);
                    params.leftMargin = left;
                } else if (i == mTexts.size() - 1) {
                    textView.setGravity(Gravity.RIGHT);
                    LayoutParams params = new LayoutParams((int) (width / 2 + mStepsViewIndicator.getCircleRadius() / 2), LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(params);
                    params.rightMargin = left;
                } else {
                    textView.setGravity(Gravity.CENTER);
                    LayoutParams params = new LayoutParams((int) (width - mStepsViewIndicator.getCircleRadius() / 5), LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(params);
                }


                //Your X coordinateA
//                float x1;
//                if (i == 0) {
//                    x1 = complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2;
//                } else if (i == mTexts.size() - 1) {
//                    x1 = complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2 - 10;
//                } else {
//                    x1 = complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius();
//                }
//                params.leftMargin = (int) x1;


                if (i < mComplectingPosition) {
                    textView.setTextColor(mUnComplectedTextColor);
                } else if (i == mComplectingPosition) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(mComplectedTextColor);
                } else {
                    textView.setTextColor(mUnComplectedTextColor);
                }

                mTextContainer.addView(textView);
            }
        }
    }
}