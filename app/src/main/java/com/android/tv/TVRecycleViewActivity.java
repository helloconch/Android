package com.android.tv;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.testing.R;
import com.android.testing.widget.tv.recycleview.AutoLayoutManager;
import com.android.testing.widget.tv.recycleview.DividerItemDecoration;
import com.android.testing.widget.tv.recycleview.ExtRecycleView;
import com.android.testing.widget.tv.recycleview.TransformViewImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/6.
 */

public class TVRecycleViewActivity extends AppCompatActivity {

    @BindView(R.id.ry_menu_item)
    ExtRecycleView recyclerView;

    TransformViewImpl transformView;
    @BindView(R.id.ry_navigator)
    ExtRecycleView ryNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_recycleview);
        ButterKnife.bind(this);
        transformView = new TransformViewImpl(this);
        transformView.setBackground(R.drawable.border_color);
        loadRecyclerViewMenuItem();
        loadNavigator();

    }

    private void loadRecyclerViewMenuItem() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        transformView.attachTo(recyclerView);
        createOptionItemData(recyclerView, R.layout.detail_menu_item);
    }

    private void loadNavigator() {
//        GridLayoutManager layoutManager = new AutoLayoutManager(this, 1);
//        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        final Drawable divider = getResources().getDrawable(R.drawable.divider);
        ryNavigator.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL));
        ryNavigator.setLayoutManager(layoutManager);
        ryNavigator.setFocusable(false);
        transformView.attachTo(ryNavigator);
        createOptionItemData(ryNavigator, R.layout.navitator_item);
    }

    private void createOptionItemData(RecyclerView recyclerView, int id) {
        NavigatorAdapter adapter = new NavigatorAdapter(this, id);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }
}
