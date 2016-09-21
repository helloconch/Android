package com.android.views;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.android.testing.R;
import com.android.testing.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/21.
 */

public class PropertyActivity extends AppCompatActivity {
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnValueAnimator, R.id.button4, R.id.button5, R.id.button6
            , R.id.button7, R.id.button8, R.id.button9, R.id.viewProperty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnValueAnimator:
                caluValueAnimator();
                break;
            case R.id.button4:
                changeAlpha();
                break;
            case R.id.button5:
                changeRotation();
                break;
            case R.id.button6:
                translation();
                break;
            case R.id.button7:
                scale();
                break;
            case R.id.button8:
                combination();
                break;
            case R.id.button9:
                readXMLCombination();
                break;
            case R.id.viewProperty:
                viewPropertyTest();
                break;
        }
    }

    private void caluValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (Float) animation.getAnimatedValue();
                ToastUtils.show(getApplicationContext(), v + "");
            }
        });
        valueAnimator.setDuration(5000);
        valueAnimator.start();
    }

    private void changeAlpha() {
        //ObjectAnimator extends valueAnimator

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView2, "alpha", 0f, 1f, 0.2f);
        objectAnimator.setDuration(5000);
        objectAnimator.setRepeatCount(0);
//        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();
    }

    private void changeRotation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView2, "rotation", 0f, 360f);
        objectAnimator.setDuration(5000);
        objectAnimator.start();
    }

    private void translation() {
        float x = textView2.getTranslationX();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView2, "translationX", x, -500f, x);
        objectAnimator.setDuration(5000);
        objectAnimator.start();
    }

    private void scale() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView2, "scaleY", 1f, 3f, 1f);
        //实现动画监听方式一
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //实现动画监听方式二，有选择性进行重写
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

        objectAnimator.setDuration(5000);
        objectAnimator.start();
    }

    /**
     * 实现组合动画功能主要需要借助AnimatorSet这个类，这个类提供了一个play()方法，如果我们向这个方法中传入一个Animator对象(ValueAnimator或ObjectAnimator)将会返回一个AnimatorSet.Builder的实例，AnimatorSet.Builder中包括以下四个方法：
     * after(Animator anim)   将现有动画插入到传入的动画之后执行
     * after(long delay)   将现有动画延迟指定毫秒后执行
     * before(Animator anim)   将现有动画插入到传入的动画之前执行
     * with(Animator anim)   将现有动画和传入的动画同时执行
     */
    private void combination() {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(textView2, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textView2, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(textView2, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(5000);
        animSet.start();
    }

    private void readXMLCombination() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.combination_anim);
        animator.setTarget(textView2);
        animator.start();
    }

    /**
     * ViewPropertyAnimator其实算不上什么高级技巧，它的用法格外的简单，只不过和前面所学的所有属性动画的知识不同，它并不是在3.0系统当中引入的，而是在3.1系统当中附增的一个新的功能，因此这里我们把它作为整个属性动画系列的收尾部分。
     * 我们都知道，属性动画的机制已经不是再针对于View而进行设计的了，而是一种不断地对值进行操作的机制，它可以将值赋值到指定对象的指定属性上。但是，在绝大多数情况下，我相信大家主要都还是对View进行动画操作的。Android开发团队也是意识到了这一点，
     * 没有为View的动画操作提供一种更加便捷的用法确实是有点太不人性化了，于是在Android 3.1系统当中补充了ViewPropertyAnimator这个机制。
     */
    private void viewPropertyTest() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            ViewPropertyAnimator viewPropertyAnimator = textView2.animate();
            viewPropertyAnimator.alpha(0.5f).setDuration(1000);
            viewPropertyAnimator.x(500).y(500).setDuration(1000);
            viewPropertyAnimator.x(500).y(500).setInterpolator(new BounceInterpolator());
        }

    }
}
