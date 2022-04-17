package com.example.tms.presenter;

import android.content.Context;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.ILogin;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Account;
import com.example.tms.utils.HttpUtil;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl extends BasePresenter<ILogin.loginView> implements ILogin.loginPresenter{
    private static final int STUDENT = 1;
    private static final int LECTURER = 2;
    private static final int ADMIN = 3;

    public LoginPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void login(Account.DataDTO accountLocal) {
        Api api = HttpUtil.getInstance().getApi();
        Call<Account> task = api.ACCOUNT_GET(String.valueOf(accountLocal.getAccount()),null);
        task.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    Account account1 = response.body();
                    if (account1 == null || account1.getData().size() == 0) {
                        getView().showMessage("账号不存在");
                    } else {
                        handleResult(accountLocal, account1.getData());
                    }
                } else {
                    getView().showMessage("网络错误，code：" + code);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                getView().showMessage("请求失败");
            }
        });
    }

    private void handleResult(Account.DataDTO accountLocal, List<Account.DataDTO> data) {
        Account.DataDTO account = data.get(0);
        if (!accountLocal.getRole().equals(account.getRole())) {
            getView().showMessage("请选择正确的角色");
        } else {
            if (accountLocal.getPassword().equals(account.getPassword())) {
                getView().loginResult(true);
                getView().showMessage("登录成功");
            } else {
                getView().loginResult(false);
                getView().showMessage("密码错误");
            }
        }
    }
}
