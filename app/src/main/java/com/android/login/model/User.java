package com.android.login.model;

import com.android.app.lib.utils.BaseUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class User implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static User instance;

    public final static String TAG = User.class.getSimpleName();

    private String loginName;

    private String userName;

    private int score;

    private boolean loginStatus;

    private User() {

    }

    public static User getInstance() {
        if (instance == null) {
            Object object = BaseUtils.restoreObject(AppConstants.CACHEDIR + TAG);
            //App第一次启动,文件不存在,则新建
            if (object == null) {
                object = new User();
                BaseUtils.saveObject(AppConstants.CACHEDIR + TAG, object);
            }

            instance = (User) object;
        }

        return instance;
    }

    /**
     * 重置
     */
    public void reset() {
        loginName = null;
        userName = null;
        score = 0;
        loginStatus = false;
    }

    /**
     * 保存
     */
    public void save() {
        BaseUtils.saveObject(AppConstants.CACHEDIR + TAG, this);
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    // -----------以下3个方法用于序列化-----------------

    public User readResolve() throws Exception {
        instance = (User) this.clone();
        return instance;
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
