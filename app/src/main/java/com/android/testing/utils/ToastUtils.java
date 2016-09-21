package com.android.testing.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/21.
 */

public class ToastUtils {
    static Toast toast;

    public static void show(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        toast.setText(content);
        toast.show();
    }
}
