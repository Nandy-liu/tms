package com.example.tms.model;

import com.example.tms.base.IBaseView;
import com.example.tms.model.domain.Account;

public interface ILogin {
    interface loginView extends IBaseView {
        void loginResult(boolean result);
        void showMessage(String message);
    }
    interface loginPresenter{
        void login(Account.DataDTO account);
    }
}
