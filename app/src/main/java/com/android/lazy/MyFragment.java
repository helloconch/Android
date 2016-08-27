package com.android.lazy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.testing.BaseAppFragment;
import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 懒加载 ，只在界面显示的时候，进行数据加载并显示
 */
public class MyFragment extends BaseAppFragment {
    @BindView(R.id.item1)
    TextView item1;

    @Override
    public void loadData() {


        item1.setText(value);

        isLoadDataCompleted = true;
    }

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lazy_item, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


}
