package com.android.mvp.model;

/**
 * Created by cheyanxu on 16/7/25.
 */
public interface IUserModel {

    void setID(int id);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    UserBean load(int id);
}
