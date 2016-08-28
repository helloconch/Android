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
import android.widget.TextView;

import com.android.testing.R;
import com.android.testing.widget.MyBall;
import com.android.testing.widget.MyScroolView;
import com.android.testing.widget.SlideMenu;
import com.android.testing.widget.stepview.HorizontalStepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/8/3.
 */
public class CustomViewsActivity extends AppCompatActivity {


    HorizontalStepView horizontalStepView;
    @BindView(R.id.mySurfaceView)
    SurfaceView mySurfaceView;
    @BindView(R.id.main_area)
    MyScroolView mainArea;
    @BindView(R.id.main_text)
    TextView mainText;
    @BindView(R.id.slideMenu)
    SlideMenu slideMenu;
    @BindView(R.id.myBall)
    MyBall myBall;


    // SurfaceHolder负责维护SurfaceView上绘制的内容
    private SurfaceHolder holder;
    private Paint paint;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_main);
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
     * Canvas lockCanvas(Rect dirty):锁定SurfaceView上Rect划分的区域，获取该Surface上的Canvas
     * unlockCanvasAndPost(canvas):释放绘图、提交所绘制的图形，需要注意，
     * 当调用SurfaceHolder上的unlockCanvasAndPost方法之后，该方法之前所绘制的图形还处于缓冲之中，
     * 下一次lockCanvas()方法锁定的区域可能会“遮挡”它
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

    /**
     * <br/>
     * scroolBy是基于view子内容当前位置进行增量式的相对滑动
     * <br/>
     * scroolTo则是基于view子内容的绝对坐标进行滑动
     * 需要强调的是，滑动的是view的子内容而不是view本身，不管我们如何调用，调用者mainArea是没有任何变化的。
     * getScrollX()返回的滑动距离是view的左边缘的X轴减去该view子内容左边缘的X轴。
     *
     * @param view
     */
    @OnClick({R.id.scroolBy, R.id.scroolTo, R.id.useScroll, R.id.slide,
            R.id.ballMove, R.id.ballMove1, R.id.ballMove2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scroolBy:
                //正值向左移动 负值向右，但是x轴的正方向指向右边
                mainArea.scrollBy(100, 0);
                break;
            case R.id.scroolTo:
                mainArea.scrollTo(0, 0);
                break;

            case R.id.useScroll:
                mainArea.smoothScroll(100, 0);
                break;
            case R.id.slide:
                slideMenu.switchMenu();
                break;
            case R.id.ballMove:
                myBall.start();
                break;
            case R.id.ballMove1:
                myBall.start1();
                break;
            case R.id.ballMove2:
                myBall.start2();
                break;
        }
    }


}
