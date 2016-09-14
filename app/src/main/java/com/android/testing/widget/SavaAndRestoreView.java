package com.android.testing.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * save：用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
 * restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
 */
public class SavaAndRestoreView extends View {
    Paint mPaint;

    public SavaAndRestoreView(Context context) {
        super(context);
        init();
    }

    public SavaAndRestoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);

    }

    /**
     * 1.当对自定义控件指定的宽高为
     * android:layout_width="wrap_content"
     * android:layout_height="wrap_content"
     * android:layout_alignParentBottom="true"
     * 此时画布canvas中坐标，按照父容器中坐标进行绘制，原点（0,0）在父容器左上角，并且属性 android:layout_alignParentBottom="true"不起作用。
     * <p>
     * <p>
     * 2.当对自定义控件指定具体的宽高
     * android:layout_width="400dp"
     * android:layout_height="400dp"
     * android:layout_alignParentBottom="true"
     * <p>
     * 此时画布canvas中坐标，按照自身大小区域进行绘制。原点（0,0）就在自身左上角，并且属性 android:layout_alignParentBottom="true"起作用。
     *
     * @param canvas
     */

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(30, 30, 200, 150, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawText("ABC", 10, 10, mPaint);
        canvas.save();
        //此时对画布进行顺时针旋转90度，坐标系跟随旋转
        //按照中心点 进行90度旋转
        canvas.rotate(90, w / 2, h / 2);
        //按照原点进行90度旋转
//        canvas.rotate(90);
        //在旋转后的画布进行绘制
        canvas.drawText("EFG", 20, 20, mPaint);
        //恢复之前保存状态
        canvas.restore();
        //在恢复后的画布，进行再次绘制
        canvas.drawText("MMM", 20, 20, mPaint);
    }
}
