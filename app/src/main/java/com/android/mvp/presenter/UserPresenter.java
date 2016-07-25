package com.android.mvp.presenter;

import com.android.mvp.model.IUserModel;
import com.android.mvp.model.UserBean;
import com.android.mvp.model.UserModel;
import com.android.mvp.view.IUserView;

/**
 * Created by cheyanxu on 16/7/25.
 */
public class UserPresenter {
    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter(IUserView view) {
        mUserView = view;
        mUserModel = new UserModel();
    }

    public void saveUser(int id, String firstName, String lastName) {
        mUserModel.setID(id);
        mUserModel.setFirstName(firstName);
        mUserModel.setLastName(lastName);
    }

    public void loadUser(int id) {
        UserBean user = mUserModel.load(id);
        mUserView.setFirstName(user.getmFirstName());
        mUserView.setLastName(user.getmLastName());
    }
}
