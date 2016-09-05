package com.android.softinput;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SoftInputMethodActivity extends AppCompatActivity {

    @BindView(R.id.etContent)
    EditText etContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input);
        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Point point = new Point();

        display.getSize(point);

        int x = point.x;

        int y = point.y;


        etContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideSoftInputMethod();
                }

                return false;
            }
        });
    }

    private void hideSoftInputMethod() {
        InputMethodManager immm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        immm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
    }

    public void showSoftInputMethod() {
        InputMethodManager immm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        immm.showSoftInput(etContent, 0);
    }

    public void toogleSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick(R.id.click)
    public void onClick() {
        hideSoftInputMethod();
    }
}
