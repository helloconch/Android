package com.android.testing.widget.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.miercnnew.app.R;
import com.miercnnew.utils.DataTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：StepsViewIndicator 指示器
 */
public class HorizontalStepsViewIndicator extends View {
    //定义默认的高度
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    //完成线的高度
    private float mCompletedLineHeight;
    //圆的半径
    private float mCircleRadius;
    //完成的默认图片
    private Drawable mCompleteIcon;
    //正在进行的默认图片
    private Drawable mAttentionIcon;
    //结束的图片
    private Drawable mEndIcon;
    //未获得奖励的图片
    private Drawable mUnAWardIcon;
    //已获得奖励的图片
    private Drawable mAWardedIcon;
    //默认的背景图
    private Drawable mDefaultIcon;
    //视图高度
    //该view的Y轴基准位置
    private float mCenterY;
    //该view的高度
    private float mHeight;
    //左上方的Y位置
    private float mLeftY;
    //右下方的位置
    private float mRightY;
    //当前有几部流程
    public static int mStepNum = 7;
    //两条连线之间的间距
    private float mLinePadding;
    //定义所有圆的圆心点位置的集合
    private List<Float> mCircleCenterPointPositionList;
    //未完成Paint
    private Paint mUnCompletedPaint;
    //完成paint
    private Paint mCompletedPaint;
    //定义默认未完成线的颜色
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_line_color);
    //定义默认完成线的颜色
    private int mCompletedLineColor = ContextCompat.getColor(getContext(), R.color.completed_line_color);
    private PathEffect mEffects;
    //正在进行position
    private int mComplectingPosition = -1;
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;

    int mAwareIconSize;

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    public float getCircleRadius() {
        return mCircleRadius;
    }


    public HorizontalStepsViewIndicator(Context context) {
        super(context);
        init(context);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    /**
     * init
     */
    private void init(Context context) {
        mAwareIconSize = DataTools.dip2px(context, 20);
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPointPositionList = new ArrayList<Float>();//初始化

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.FILL);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径
        mCircleRadius = 0.4f * defaultStepIndicatorNum;
        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_sign_signed);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_sign_seven_days);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_sign_no_signed);//未完成的icon
        mEndIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_sign_today);//结束最后的icon
        mUnAWardIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_medal_gray);//未奖励的icon
        mAWardedIcon = ContextCompat.getDrawable(getContext(), R.drawable.fight_medal_colour);//奖励的icon
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = defaultStepIndicatorNum * 2;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = (int) (defaultStepIndicatorNum * 1.2);
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        //确定沿着组件底部开始绘制的基准高度
        mCenterY = (h - mCircleRadius);
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;
        //获取两点之间连线距离
        int paddingLeftOrRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        mLinePadding = (w - paddingLeftOrRight * 2 - mStepNum * mCircleRadius * 2) / (mStepNum - 1);
        for (int i = 0; i < mStepNum; i++) {
            //计算各个中心点坐标X
            float x = paddingLeftOrRight + i * mCircleRadius * 2 + i * mLinePadding + mCircleRadius;
            mCircleCenterPointPositionList.add(x);
        }
        /**
         * set listener
         */
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);
        //-----------------------画线-------draw line-----------------------------------------------
        for (int i = 0; i < mCircleCenterPointPositionList.size() - 1; i++) {
            //前一个ComplectedXPosition
            final float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);
            //判断在完成之前的所有点
            int offset = (int) mCircleRadius;
            if (i < mComplectingPosition) {
                //判断在完成之前的所有点，画完成的线
                canvas.drawRect(preComplectedXPosition + mCircleRadius - offset,
                        mLeftY, afterComplectedXPosition - mCircleRadius + offset, mRightY, mCompletedPaint);
            } else {
                canvas.drawRect(preComplectedXPosition + mCircleRadius - offset,
                        mLeftY, afterComplectedXPosition - mCircleRadius + offset, mRightY, mUnCompletedPaint);
            }
        }
        //-----------------------画线-------draw line-----------------------------------------------


        //-----------------------画图标-----draw icon-----------------------------------------------
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterY - mCircleRadius),
                    (int) (currentComplectedXPosition + mCircleRadius), (int) (mCenterY + mCircleRadius));
            if (i < mComplectingPosition) {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            } else if (i == mCircleCenterPointPositionList.size() - 1) {
                if (i != mComplectingPosition) {
                    mEndIcon.setBounds(rect);
                    mEndIcon.draw(canvas);
//                    handleAward(canvas, currentComplectedXPosition, mUnAWardIcon);
                } else {
                    mAttentionIcon.setBounds(rect);
                    mAttentionIcon.draw(canvas);
                }
                handleAward(canvas, currentComplectedXPosition, mAWardedIcon);

            } else if (i == mComplectingPosition && mCircleCenterPointPositionList.size() != 1) {
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            } else {
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }

    private void handleAward(Canvas canvas, float currentComplectedXPosition, Drawable mIcon) {
        Rect awardRect;

//        int left = (int) (currentComplectedXPosition - mCircleRadius);
//        int top = (int) (mCenterY - mCircleRadius);
//        int right = (int) (currentComplectedXPosition + mCircleRadius);
//        int bottom = (int) (mCenterY + mCircleRadius);

        int left = (int) (currentComplectedXPosition - mAwareIconSize / 2);
        int top = (int) (mCenterY - mCircleRadius - mCircleRadius);
        int right = left + mAwareIconSize;
        int bottom = top + mAwareIconSize;

        awardRect = new Rect(left, top, right, bottom);
        mIcon.setBounds(awardRect);
        mIcon.draw(canvas);
    }


    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum) {
        this.mStepNum = stepNum;
        invalidate();
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    public void setComplectingPosition(int complectingPosition) {
        this.mComplectingPosition = complectingPosition;
        invalidate();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor) {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon) {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon) {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon) {
        this.mAttentionIcon = attentionIcon;
    }

    /**
     * 设置结束的图片
     *
     * @param endIcon
     */
    public void setEndIcon(Drawable endIcon) {
        this.mEndIcon = endIcon;
    }

    /**
     * 设置未奖励图片
     *
     * @param unAwardIcon
     */
    public void setUnAwardIcon(Drawable unAwardIcon) {
        this.mUnAWardIcon = unAwardIcon;
        invalidate();
    }

    /**
     * 设置奖励图片
     *
     * @param awardedIcon
     */
    public void setAwardedIcon(Drawable awardedIcon) {
        this.mAWardedIcon = awardedIcon;
        invalidate();
    }

    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener {
        void ondrawIndicator();
    }
}
