package com.android.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.android.testing.R;
import com.android.testing.widget.stepview.HorizontalStepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/8/3.
 */
public class CustomViewsActivity extends AppCompatActivity {


    HorizontalStepView horizontalStepView;
    @BindView(R.id.mySurfaceView)
    SurfaceView mySurfaceView;


    // SurfaceHolder负责维护SurfaceView上绘制的内容
    private SurfaceHolder holder;
    private Paint paint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
        horizontalStepView = (HorizontalStepView) findViewById(R.id.horizontalStepView);
        List<String> datas = new ArrayList<>();
        datas.add("周一");
        datas.add("周二");
        datas.add("周三");
        datas.add("周四");
        datas.add("周五");
        datas.add("周六");
        datas.add("周日");
        horizontalStepView.setStepViewTexts(datas).setStepsViewIndicatorComplectingPosition(3);
        initSurfaceView();
    }

    /**
     * Canvas lockCanvas():锁定整个SurfaceView对象，获取该Surface上的Canvas
     Canvas lockCanvas(Rect dirty):锁定SurfaceView上Rect划分的区域，获取该Surface上的Canvas
     unlockCanvasAndPost(canvas):释放绘图、提交所绘制的图形，需要注意，
     当调用SurfaceHolder上的unlockCanvasAndPost方法之后，该方法之前所绘制的图形还处于缓冲之中，
     下一次lockCanvas()方法锁定的区域可能会“遮挡”它
     */
    void initSurfaceView() {
        paint = new Paint();
        holder = mySurfaceView.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            /**
             * 当一个surface的格式或大小发生改变时回调该方法。
             * @param surfaceHolder
             */

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            /**
             * 当surface被创建时回调该方法
             * @param surfaceHolder
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                // 锁定整个SurfaceView
                Canvas canvas = holder.lockCanvas();
                // 绘制背景
                Bitmap back = BitmapFactory.decodeResource(
                        CustomViewsActivity.this.getResources()
                        , R.mipmap.ic_launcher);
                // 绘制背景
                canvas.drawBitmap(back, 0, 0, null);
                // 绘制完成，释放画布，提交修改
                holder.unlockCanvasAndPost(canvas);
                // 重新锁一次，"持久化"上次所绘制的内容
                holder.lockCanvas(new Rect(0, 0, 0, 0));
                holder.unlockCanvasAndPost(canvas);
            }

            /**
             * 当surface将要被销毁时回调该方法
             * @param surfaceHolder
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        // 为surface的触摸事件绑定监听器

        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                // 只处理按下事件
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int cx = (int) event.getX();
                    int cy = (int) event.getY();
                    // 锁定SurfaceView的局部区域，只更新局部内容
                    Canvas canvas = holder.lockCanvas(new Rect(cx - 50,
                            cy - 50, cx + 50, cy + 50));
                    // 保存canvas的当前状态
                    canvas.save();
                    // 旋转画布
                    canvas.rotate(30, cx, cy);
                    paint.setColor(Color.RED);
                    // 绘制红色方块
                    canvas.drawRect(cx - 40, cy - 40, cx, cy, paint);
                    // 恢复Canvas之前的保存状态
                    canvas.restore();
                    paint.setColor(Color.GREEN);
                    // 绘制绿色方块
                    canvas.drawRect(cx, cy, cx + 40, cy + 40, paint);
                    // 绘制完成，释放画布，提交修改
                    holder.unlockCanvasAndPost(canvas);
                }

                return false;
            }
        });


    }
}
