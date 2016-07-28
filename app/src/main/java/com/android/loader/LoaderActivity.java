package com.android.loader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int REQUEST_CODE = 1;
    private final int LOADER_ID = 0;
    private String filterName;
    SimpleCursorAdapter cursorAdapter;

    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.listResult)
    ListView listResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        ButterKnife.bind(this);

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_2, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1}, 0);

        listResult.setAdapter(cursorAdapter);


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                new AlertDialog.Builder(this).setMessage("app需要开启权限才能使用此功能").setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null).create().show();
            } else {

                //没有权限，申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE);
            }

        } else {
            initLoaderManager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLoaderManager();
                } else {
                    //用户拒绝了授权
                    Toast.makeText(getBaseContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initLoaderManager() {
        //初始化Loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        String[] pro = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        if (TextUtils.isEmpty(filterName)) {
            uri = ContactsContract.Contacts.CONTENT_URI;
        } else {
            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(filterName));
        }

        //创建Loader对象,开始异步加载数据
        return new CursorLoader(this, uri, pro, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //得到异步加载数据,更新Adapter
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //移除adapter使用的Loader,系统会释放不在使用的Loader
        cursorAdapter.swapCursor(null);
    }

    @OnClick(R.id.serch)
    public void onClick() {
        filterName = etContent.getText().toString().trim();
        //使用新的Loader(清空旧的数据)
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

}
