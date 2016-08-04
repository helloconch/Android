package com.android.testing.widget.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.android.testing.R;

/**
 * Created by cheyanxu on 16/8/3.
 */
public class IndicatorProgressBar extends ProgressBar {

    private TextPaint mTextPaint;
    private Drawable indicatorIcon;

    private String content = "123";
    int color;

    public IndicatorProgressBar(Context context) {
        this(context, null);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(true);


        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.IndicatorProgressBar, defStyleAttr, 0);
        if (array != null) {
            indicatorIcon = array.getDrawable(R.styleable.IndicatorProgressBar_indicatorIcon);
            color = array.getColor(R.styleable.IndicatorProgressBar_textColor, Color.WHITE);
            array.recycle();
        }
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度+指示器所占高度
        if (indicatorIcon != null) {
            final int width = getMeasuredWidth();
            final int height = getMeasuredHeight();
            final int indicatorIconHeight = indicatorIcon.copyBounds().height();
            setMeasuredDimension(width, height + indicatorIconHeight);
        }


    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(content)) {
            int offset = 2;

            canvas.save();
            double width = getWidth();
            int height = getHeight();
            int textSize = height - 10;
            mTextPaint.setTextSize(textSize);
            //边界宽度
            double current = getProgress();
            int x = (int) ((current / 100) * width / 2);
            int y = height - (height - textSize) / 2 - offset;

            canvas.drawText(content, x, y, mTextPaint);
            canvas.restore();
        }
    }

    public void setContent(String content) {
        this.content = content;
        invalidate();
    }

    public String getContent() {
        return content;
    }
}
