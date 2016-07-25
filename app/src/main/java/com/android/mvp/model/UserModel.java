package com.android.mvp.model;

import android.util.SparseArray;

/**
 * Created by cheyanxu on 16/7/25.
 */
public class UserModel implements IUserModel {
    private String mFirstName;
    private String mLastName;
    private int mID;
    private SparseArray<UserBean> mUserArray = new SparseArray<UserBean>();

    @Override
    public void setID(int id) {
        mID = id;
    }

    @Override
    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        mLastName = lastName;
        UserBean userBean = new UserBean(mFirstName, mLastName);
        mUserArray.append(mID, userBean);
    }

    @Override
    public UserBean load(int id) {
        mID = id;
        UserBean userBean = mUserArray.get(mID, new UserBean("not found", "not found"));
        return userBean;
    }
}
