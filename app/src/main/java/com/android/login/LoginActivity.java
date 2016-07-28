package com.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.android.login.model.User;
import com.android.login.model.UserInfo;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取网络,进行登录操作
        //伪代码
        success("");

    }

    private void success(String content) {

        UserInfo userInfo = JSON.parseObject(content, UserInfo.class);

        if (userInfo != null) {
            User.getInstance().reset();
            User.getInstance().setLoginName(userInfo.getLoginName());
            User.getInstance().setUserName(userInfo.getUserName());
            User.getInstance().setScore(userInfo.getScore());
            User.getInstance().setLoginStatus(true);
            User.getInstance().save();
        }

    }
}
