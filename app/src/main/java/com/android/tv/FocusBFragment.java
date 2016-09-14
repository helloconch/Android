package com.android.tv;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.testing.R;
import com.android.testing.widget.navigation.FocusSlideTabView;
import com.android.testing.widget.navigation.SlideTabView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/12.
 */
public class FocusBFragment extends Fragment {
    //对照表  http://fortawesome.github.io/Font-Awesome/cheatsheet/
    private static final String FONT_DIR = "fonts/";
    private static final String DEE_DIR = FONT_DIR + "fontawesome-webfont.ttf";
    @BindView(R.id.slideTabView)
    FocusSlideTabView slideTabView;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_focus_b, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), DEE_DIR);

        tv1.setTypeface(typeface);
        tv2.setTypeface(typeface);
        tv3.setTypeface(typeface);

        String[] dataS = new String[]{"推荐", "分类", "应用", "我的", "设置"};
        slideTabView.addTextTab(dataS);

        slideTabView.requestFocus();
    }
}
