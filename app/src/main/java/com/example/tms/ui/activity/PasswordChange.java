package com.example.tms.ui.activity;

import static com.example.tms.utils.MD5.md5;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Account;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 用于学员/讲师修改密码
 */
@SuppressLint("NonConstantResourceId")
public class PasswordChange extends AppCompatActivity {
    @BindView(R.id.button_change_pwd)
    public Button mBtnChange;
    @BindView(R.id.et_c_account)
    public EditText mETAccount;
    @BindView(R.id.et_c_oldpassword)
    public EditText mETOldPassword;
    @BindView(R.id.et_c_newpassword)
    public EditText mETNewPwd;
    @BindView(R.id.et_c_confirm_newpassword)
    public EditText mETConfirm;
    private String truePassword = "";
    @BindView(R.id.textinput_newpassword)
    public TextInputLayout textInputLayout;
    private String mAccount;
    private String mConfirm;
    private String mNewPwd;
    private String mOldPwd;
    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mApi = HttpUtil.getInstance().getApi();

    }

    @OnTextChanged(R.id.et_c_newpassword)
    public void afterTextChanged(Editable s) {
        if (textInputLayout.getCounterMaxLength() < mETNewPwd.length()) {
            textInputLayout.setError("超出字数限制！");
        } else {
            textInputLayout.setErrorEnabled(false);
        }
    }

    @OnClick(R.id.button_change_pwd)
    public void onClick(View v) {
        mAccount = mETAccount.getText().toString();
        mConfirm = mETConfirm.getText().toString();
        mNewPwd = mETNewPwd.getText().toString();
        mOldPwd = mETOldPassword.getText().toString();
        if (mAccount.equals("") || mConfirm.equals("") || mNewPwd.equals("") || mOldPwd.equals("")) {
            Toast.makeText(PasswordChange.this, "不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            queryAccount();
        }
    }

    private void queryAccount() {
        Call<Account> task = mApi.ACCOUNT_GET(mAccount, null);
        task.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.body() != null) {
                    if (response.body().getData().size() == 0) {
                        Toast.makeText(PasswordChange.this, "没有找到该账户", Toast.LENGTH_SHORT).show();
                    } else {
                        truePassword = response.body().getData().get(0).getPassword();
                        //如果原密码错误
                        if (!md5(mETOldPassword.getText().toString()).equals(truePassword)) {
                            Toast.makeText(PasswordChange.this, "原密码错误！", Toast.LENGTH_SHORT).show();
                        } else {
                            //如果用户前后输入密码不同
                            if (!mETNewPwd.getText().toString().equals(mETConfirm.getText().toString())) {
                                Toast.makeText(PasswordChange.this, "前后两次输入与验证密码错误！", Toast.LENGTH_SHORT).show();
                            } else {
                                putAccount();
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });

    }

    private void putAccount() {
        Map<String, String> map = new HashMap<>();
        map.put("account", mAccount);
        map.put("password", md5(mNewPwd));
        Call<ResponseOther> task = mApi.ACCOUNT_PUT(map);
        task.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    Toast.makeText(PasswordChange.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                Toast.makeText(PasswordChange.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
