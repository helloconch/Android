package com.android.tv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.android.testing.R;
import com.android.testing.utils.BitmapUtils;
import com.android.testing.widget.GalleryFlow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类似效果
 * http://blog.csdn.net/sinyu890807/article/details/17482089
 */
public class ThreeDAcitvity extends AppCompatActivity {
    @BindView(R.id.topic)
    ImageView topic;
    @BindView(R.id.galleryFlow)
    GalleryFlow mGallery;
    Bitmap bitmap;
    ArrayList<BitmapDrawable> mBitmaps = new ArrayList<BitmapDrawable>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_d);
        ButterKnife.bind(this);

        mGallery.setBackgroundColor(Color.GRAY);
        mGallery.setSpacing(50);
        mGallery.setFadingEdgeLength(0);
        mGallery.setGravity(Gravity.CENTER_VERTICAL);


//        mGallery.setSpacing(spacing);
//        ((GalleryAdapter)mGallery.getAdapter()).notifyDataSetChanged();
//
//        mGallery.setMaxZoom(maxZoom);
//        ((GalleryAdapter)mGallery.getAdapter()).notifyDataSetChanged();
//
//        mGallery.setMaxRotationAngle(maxRotationAngle);
//        ((GalleryAdapter)mGallery.getAdapter()).notifyDataSetChanged();

        generateBitmaps();
        load();
        loadGallery();
    }

    private void load() {

        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.topic);

        bitmap = BitmapUtils.createReflectedBitmap(srcBitmap);
//      Bitmap bitmap = BitmapUtils.createBorderBitmap(srcBitmap);
        topic.setImageBitmap(bitmap);

    }

    private void loadGallery() {

        GalleryAdapter adapter = new GalleryAdapter();
        mGallery.setAdapter(adapter);
    }

    private void generateBitmaps() {
        int[] ids =
                {
                        R.drawable.a,
                        R.drawable.b,
                        R.drawable.c,
                        R.drawable.d,
                        R.drawable.e,
                        R.drawable.f,
                        R.drawable.g,
                        R.drawable.h,
                        R.drawable.i,
                        R.drawable.j,
                        R.drawable.k,
                        R.drawable.l,
                };

        for (int id : ids) {
            Bitmap bitmap = createReflectedBitmapById(id);
            if (null != bitmap) {
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                drawable.setAntiAlias(true);
                mBitmaps.add(drawable);
            }
        }
    }

    private Bitmap createReflectedBitmapById(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap reflectedBitmap = BitmapUtils.createReflectedBitmap(bitmap);
            return reflectedBitmap;
        }

        return null;
    }

    private class GalleryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mBitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new MyImageView(ThreeDAcitvity.this);
                convertView.setLayoutParams(new Gallery.LayoutParams(110, 184));
            }

            ImageView imageView = (ImageView) convertView;
            imageView.setImageDrawable(mBitmaps.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            return imageView;
        }
    }

    private class MyImageView extends ImageView {
        public MyImageView(Context context) {
            this(context, null);
        }

        public MyImageView(Context context, AttributeSet attrs) {
            super(context, attrs, 0);
        }

        public MyImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }

}
