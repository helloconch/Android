package com.android.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * http://blog.csdn.net/qibin0506/article/details/51002241
 * Created by Administrator on 2016/9/6.
 */
public class BottomSheetBehaviorActivity extends AppCompatActivity {

    @BindView(R.id.scroll)
    NestedScrollView scroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.info, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info:
                BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.select:
                select(null);
                break;
        }
    }


    public void select(View view) {
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this)
                .inflate(R.layout.list, null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(recyclerView);
        dialog.show();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                Toast.makeText(BottomSheetBehaviorActivity.this, text, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private OnItemClickListener mItemClickListener;

        public void setOnItemClickListener(OnItemClickListener li) {
            mItemClickListener = li;
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(final Adapter.Holder holder, int position) {
            holder.tv.setText("item " + position);
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.getLayoutPosition(),
                                holder.tv.getText().toString());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.text);
            }
        }


    }

    interface OnItemClickListener {
        void onItemClick(int position, String text);
    }
}
