package com.android.tv;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.testing.R;

public class NavigatorAdapter extends RecyclerView.Adapter<NavigatorAdapter.ViewHolder> {

    private String[] mDataList = new String[]{
            "最热", "最受好评", "TVB", "内地", "韩剧", "美剧", "鼎级剧场", "港台", "偶像",
            "古装", "家庭", "神话", "喜剧", "战争"
    };
    private Context mContext;
    private int id;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    private OnBindListener onBindListener;
    private static final String TAG = NavigatorAdapter.class.getSimpleName();

    public interface OnBindListener {
        void onBind(View view, int i);
    }

    public NavigatorAdapter(Context context) {
        super();
        mContext = context;
    }

    public NavigatorAdapter(Context context, int id) {
        super();
        mContext = context;
        this.id = id;
    }

    public NavigatorAdapter(Context context, int id, View.OnFocusChangeListener onFocusChangeListener) {
        super();
        mContext = context;
        this.id = id;
        this.mOnFocusChangeListener = onFocusChangeListener;
    }

    public void setOnBindListener(OnBindListener onBindListener) {
        this.onBindListener = onBindListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int resId = R.layout.detail_menu_item;
        if (this.id > 0) {
            resId = this.id;
        }
        View view = LayoutInflater.from(mContext).inflate(resId, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mDataList.length == 0) {
            Log.d(TAG, "mDataset has no data!");
            return;
        }
        viewHolder.mTextView.setText(mDataList[i]);
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        if (onBindListener != null) {
            onBindListener.onBind(viewHolder.itemView, i);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_menu_title);
        }
    }

}
