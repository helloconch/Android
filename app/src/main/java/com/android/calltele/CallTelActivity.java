package com.android.calltele;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class CallTelActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        call();
    }

    /**
     * 拨打电话
     */
    private void call() {

        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(CallTelActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                //没有权限，申请权限
                ActivityCompat.requestPermissions(CallTelActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            } else {
                //用户点击不在提示权限框
                new AlertDialog.Builder(this).setMessage("app需要开启权限才能使用此功能").setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null).create().show();
            }

        } else {
            callPhone();

        }

    }

    private void callPhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:10010");
            intent.setData(data);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    //用户拒绝了授权
                    Toast.makeText(getBaseContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
