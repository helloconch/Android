package com.android.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.mvp.presenter.UserPresenter;
import com.android.mvp.view.IUserView;
import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/7/25.
 */
public class MVPActivity extends AppCompatActivity implements IUserView {

    @BindView(R.id.editText)
    EditText mFirstName;
    @BindView(R.id.editText2)
    EditText mLastName;
    @BindView(R.id.editText3)
    EditText mId;

    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        mUserPresenter = new UserPresenter(this);
    }

    @OnClick({R.id.button, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                mUserPresenter.saveUser(getID(), getFirstName(), getLastName());
                break;
            case R.id.button2:
                mUserPresenter.loadUser(2);
                break;
        }
    }


    @Override
    public int getID() {
        return Integer.parseInt(mId.getText().toString());
    }

    @Override
    public String getFirstName() {
        return mFirstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return mLastName.getText().toString();
    }

    @Override
    public void setFirstName(String firstName) {
        mFirstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        mLastName.setText(lastName);
    }
}
