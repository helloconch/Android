package com.android.testing.widget.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.testing.R;

import java.util.Locale;

/**
 * 滑动View
 */
public class SlideTabView extends HorizontalScrollView {

    //默认
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    //可拉伸
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    //水平容器
    private LinearLayout tabsContainer;
    //标签数量
    private int tabCount;
    private String[] itemsTitle;
    //当前所处的位置
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    //指示器画笔
    private Paint rectPaint;
    //分割线画笔
    private Paint dividerPaint;
    //指示器颜色
    private int underlineColor = 0xFF666666;
    //分割线颜色
    private int dividerColor = 0x1A000000;
    //是否展开
    private boolean shouldExpand = true;
    //是否需要大写
    private boolean textAllCaps = false;
    //绘制底部线条
    private boolean drawUnderline = false;
    //绘制分割线条
    private boolean drawDividerLine = false;
    //滚动偏移量
    private int scrollOffset = 52;
    //指示器高度
    private int indicatorHeight = 4;
    //底部线条高度
    private int underlineHeight = 2;
    //分割线内边距
    private int dividerPadding = 12;
    //标签内边距
    private int tabPadding = 24;
    //分割线宽度
    private int dividerWidth = 1;
    //标签页字体大小
    private int tabTextSize = 12;
    //标签页字体颜色
    private int tabTextColor = 0xFF666666;
    //标签页选中颜色
    private int tabSelectColor;
    //标签字体
    private Typeface tabTypeface = null;
    //标签字体样式
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private Locale locale;
    private SlideTabItemClickListener slideTabItemClickListener;

    public SlideTabView(Context context) {
        super(context);
    }

    public SlideTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.slideTagViewStyleable);
        drawUnderline = a.getBoolean(R.styleable.slideTagViewStyleable_drawUnderline, true);
        underlineColor = a.getColor(R.styleable.slideTagViewStyleable_underLineColor, underlineColor);
        tabTextColor = a.getColor(R.styleable.slideTagViewStyleable_tabTextColor, tabTextColor);
        tabSelectColor = a.getColor(R.styleable.slideTagViewStyleable_tabSelectColor, tabTextColor);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    /**
     * 添加标签项
     *
     * @param items
     */
    public void addTextTab(String[] items) {
        this.itemsTitle = items;
        this.tabCount = this.itemsTitle.length;
        for (int i = 0; i < items.length; i++) {
            TextView tab = new TextView(getContext());
            if (i == this.currentPosition) {
                tab.setTextColor(tabSelectColor);
            }
            tab.setText(items[i]);
            tab.setGravity(Gravity.CENTER);
            tab.setFocusableInTouchMode(true);
            tab.setFocusable(true);
            tab.requestFocus();
            tab.setSingleLine();
            addTab(i, tab);
        }
    }

    /**
     * 设置选中项
     *
     * @param myPos
     */
    public void setSelectPos(int myPos) {
        this.currentPosition = myPos;
        notifyDataSetChanged(this.currentPosition);
    }

    /**
     * 改变指示器颜色
     *
     * @param resId
     */
    public void setIndicatorColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideTabItemClickListener != null) {
                    slideTabItemClickListener.itemClick(position);
                }
                notifyDataSetChanged(position);
            }
        });
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    public void notifyDataSetChanged(final int currentPosition) {
        this.currentPosition = currentPosition;
        tabsContainer.removeAllViews();
        addTextTab(this.itemsTitle);
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                scrollToChild(currentPosition, 0);
            }
        });

    }

    /**
     * 改变标签页样式
     */
    public void updateTabStyles() {
//        for (int i = 0; i < tabCount; i++) {
//            View v = tabsContainer.getChildAt(i);
//            v.setBackgroundResource(tabBackgroundResId);
//            if (v instanceof TextView) {
//                TextView tab = (TextView) v;
//                tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
//                tab.setTypeface(tabTypeface, tabTypefaceStyle);
//                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
//                // pre-ICS-build
//                if (textAllCaps) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                        tab.setAllCaps(true);
//                    } else {
//                        tab.setText(tab.getText().toString().toUpperCase(locale));
//                    }
//                }
//            }
//        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        //开始绘制指示器
        rectPaint.setColor(tabSelectColor);

        //在当前选中位置处画线
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }


        // 绘制底部线条
        if (drawUnderline) {
            rectPaint.setColor(underlineColor);
//            canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
            canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        }

        //绘制分割线条
        if (drawDividerLine) {
            dividerPaint.setColor(dividerColor);
            for (int i = 0; i < tabCount - 1; i++) {
                View tab = tabsContainer.getChildAt(i);
                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
            }
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface SlideTabItemClickListener {
        void itemClick(int pos);
    }

    public void setSlideTabItemClickListener(SlideTabItemClickListener slideTabItemClickListener) {
        this.slideTabItemClickListener = slideTabItemClickListener;
    }
}
