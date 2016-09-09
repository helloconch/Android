package com.android.testing.widget.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MyPath extends View {

    Paint paint;

    public MyPath(Context context) {
        super(context);
        init();
    }

    public MyPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);

        //普通设置
        paint.setStrokeWidth(5);//设置画笔宽度
        paint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        paint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        // paint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        //paint.setTextSize(12);//设置文字大小

        //样式设置
        //paint.setFakeBoldText(true);//设置是否为粗体文字
        //paint.setUnderlineText(true);//设置下划线
        //paint.setTextSkewX((float) -0.25);//设置字体水平倾斜度，普通斜体字是-0.25,改变符号可以改变倾斜方向。
        //paint.setStrikeThruText(true);//设置带有删除线效果

        //其它设置
        //paint.setTextScaleX(2);//只会将水平方向拉伸，高度不会变

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //1 直线路径
        //moveTo指定一个起始点，随后可以多次调用lineTo来将上一个点于当前点连接，最后可以调用close方法将图形关闭，形成封闭的图形。
        Path path = new Path();
        path.moveTo(70, 70);
        path.lineTo(120, 70);
        path.lineTo(95, 150);
        path.close();
        canvas.drawPath(path, paint);


        //2 矩形
        //CCW：counter-clockwise，逆时针方向
        // CW：clockwise，顺时针方向
        Path path1 = new Path();
        Path path2 = new Path();
        path1.addRect(100, 170, 200, 200, Path.Direction.CCW);
        path2.addRect(100, 220, 200, 250, Path.Direction.CW);

        canvas.drawPath(path1, paint);
        canvas.drawPath(path2, paint);

        String text = "这是一组测试使用的文字";

        paint.setTextSize(12);
        paint.setColor(Color.RED);
//        void drawTextOnPath (String text, Path path, float hOffset, float vOffset, Paint paint)
//        下方文字的hOffset以及vOffset分别设置为20和20，可以看出hOffset是指定文字距离起始位置的偏移，而vOffset是文字在绘制方向垂直线上的偏移。

        canvas.drawTextOnPath(text, path1, 0, 10, paint);
        canvas.drawTextOnPath(text, path2, 0, 10, paint);


        //3.圆角矩形
//        void addRoundRect (RectF rect, float[] radii, Path.Direction dir)
//        void addRoundRect (RectF rect, float rx, float ry, Path.Direction dir)

//        两个方法都可以绘制圆角矩形，不过第二个方法中，rx，ry分别对应圆角椭圆的x半径和y半径，所以，四个角的圆角都是来自于一个椭圆。
//        而方法一则不然，参数radii，必须传递进去8个参数，分别代表左上，右上，右下，左下四个角的椭圆半径。也就是从左上角开始顺时针指定。

        Path path3 = new Path();
        RectF rectF = new RectF(100, 270, 200, 300);
        path3.addRoundRect(rectF, 10, 10, Path.Direction.CCW);
        canvas.drawPath(path3, paint);

//        4 圆形，椭圆，弧形
//
//        void addCircle (float x, float y, float radius, Path.Direction dir)
//        addCircle方法来添加圆形，其中x，y代表圆心，radius代表半径，dir代表方向

        Path path4 = new Path();
        path4.addCircle(100, 310, 20, Path.Direction.CCW);
        canvas.drawPath(path4, paint);
//
//        void addOval (RectF oval, Path.Direction dir)
//        addOval方法来添加椭圆，其中oval代表椭圆的外切矩形，dir代表方向。

        Path path5 = new Path();
        RectF rectF1 = new RectF(100 + 300, 270, 200 + 300, 300);
        path5.addOval(rectF1, Path.Direction.CCW);
        canvas.drawPath(path5, paint);
//
//        void addArc (RectF oval, float startAngle, float sweepAngle)
//        addArc方法添加弧形，oval是弧形所在椭圆的外切矩形，startAngle是其实角度，sweepAngle是扫过的角度。
        Path path6 = new Path();
        RectF rect2 = new RectF(100 + 400, 270, 200 + 400, 300);
        path6.addArc(rect2, 30, 120);

        canvas.drawPath(path6, paint);


        //android Path 和 PathMeasure 进阶
        Path path7 = new Path();
        path7.addRect(400, 100, 800, 500, Path.Direction.CW);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path7, paint);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(String.valueOf(length), 800, 500, paint);


    }
}
