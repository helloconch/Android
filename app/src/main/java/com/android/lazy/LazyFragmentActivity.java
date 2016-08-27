package com.android.lazy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.testing.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment懒加载结合ViewPager
 */
public class LazyFragmentActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lazy_fragment_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }


    private static class MyAdapter extends FragmentPagerAdapter {

        ArrayList<MyFragment> datas = new ArrayList<>();


        public MyAdapter(FragmentManager fm) {
            super(fm);
            for (int i = 0; i < 7; i++) {
                MyFragment myFragment = new MyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("item", "Hello " + i);
                myFragment.setArguments(bundle);
                datas.add(myFragment);
            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }
    }


}
