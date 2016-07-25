package com.android.mvp.view;

/**
 * Created by cheyanxu on 16/7/25.
 */
public interface IUserView {
    int getID();

    String getFirstName();

    String getLastName();

    void setFirstName(String firstName);

    void setLastName(String lastName);
}
