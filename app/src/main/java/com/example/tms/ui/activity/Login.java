package com.example.tms.ui.activity;

import static com.example.tms.utils.MD5.md5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.tms.R;
import com.example.tms.base.BaseActivity;
import com.example.tms.model.ILogin;
import com.example.tms.model.domain.Account;
import com.example.tms.presenter.LoginPresenterImpl;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

/*
 * 登陆界面的配置
 */
@SuppressLint("NonConstantResourceId")
public class Login extends BaseActivity<ILogin.loginView, LoginPresenterImpl> implements ILogin.loginView {
    private int mRole = 1;
    private SQLiteDatabase mDb;
    private SharedPreferences mSharedPreferences;
    @BindView(R.id.checkbox_remember)
    public CheckBox mCheckBox;
    @BindView(R.id.edit_account)
    public EditText mEditTextAccount;
    @BindView(R.id.edit_password)
    public EditText mEditTextPassword;
    @BindView(R.id.button_change_password)
    public Button mBtn_change_password;
    @BindView(R.id.load_radiogroup)
    public RadioGroup mRadioGroup;
    @BindView(R.id.imagebutton)
    public ImageButton mShowPassword;
    @BindView(R.id.input_layout_account)
    public TextInputLayout mInputLayoutAccount;
    @BindView(R.id.input_layout_pwd)
    public TextInputLayout mInputLayoutPwd;
    @BindView(R.id.btn_login)
    public Button mBtnLogin;
    private static final int STUDENT = 1;
    private static final int LECTURER = 2;
    private static final int ADMIN = 3;
    private String mAccount;
    private String mPassword;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isRemember = mSharedPreferences.getBoolean("remember_password", false);
        if (isRemember) {
            mEditTextAccount.setText(mSharedPreferences.getString("account", ""));
            mEditTextPassword.setText(mSharedPreferences.getString("password", ""));
            mCheckBox.setChecked(true);
        }
        mEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mInputLayoutPwd.setErrorEnabled(false);
    }

    @Override
    public LoginPresenterImpl bindPresenter() {
        return new LoginPresenterImpl(getApplicationContext());
    }

    @Override
    public ILogin.loginView bindView() {
        return this;
    }

    @OnTouch(R.id.imagebutton)
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.visible, null));
            mEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.invisible, null));
            mEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        v.performClick();
        return false;
    }

    @OnCheckedChanged({R.id.radiobutton_student, R.id.radiobutton_admin, R.id.radiobutton_lecturer})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        mInputLayoutAccount.setHint("请输入您的账号");
        if (checked) {
            switch (button.getId()) {
                case R.id.radiobutton_student:
                    mRole = STUDENT;
                    break;
                case R.id.radiobutton_admin:
                    mRole = ADMIN;
                    break;
                case R.id.radiobutton_lecturer:
                    mRole = LECTURER;
                    break;
            }
        }
    }

    @OnClick({R.id.btn_login, R.id.button_change_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //获取学员输入的账号密码
                mAccount = mEditTextAccount.getText().toString();
                mPassword = md5(mEditTextPassword.getText().toString());
                Account.DataDTO dataDTO = new Account.DataDTO(Integer.valueOf(mAccount), mRole, mPassword);
                getMVPPresenter().login(dataDTO);
                break;
            case R.id.button_change_password:
                Intent intent = new Intent(Login.this, PasswordChange.class);
                startActivity(intent);
                break;
        }
    }

    @OnFocusChange(R.id.edit_password)
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (mEditTextPassword.getText().toString().equals("")) {
                mInputLayoutPwd.setError("不能为空！");
            }
        }
    }

    @OnTextChanged(R.id.edit_password)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mEditTextPassword.getText().toString().equals("")) {
            mInputLayoutPwd.setError("不能为空！");
        } else {
            if (mInputLayoutPwd.getCounterMaxLength() < mEditTextPassword.length()) {
                mInputLayoutPwd.setError("超出字数限制！");
            } else {
                mInputLayoutPwd.setErrorEnabled(false);
            }
        }
    }


    @Override
    public void loginResult(boolean result) {
        if (result) {
            isRemember();
            Intent intent;
            switch (mRole) {
                case STUDENT:
                    intent = new Intent(Login.this, ActivityStudent.class);
                    intent.putExtra("account", mAccount);
                    startActivity(intent);
                    break;
                case LECTURER:
                    intent = new Intent(Login.this, ActivityLecturer.class);
                    intent.putExtra("account", mAccount);
                    startActivity(intent);
                    break;
                case ADMIN:
                    intent = new Intent(Login.this, ActivityAdmin.class);
                    intent.putExtra("account", mAccount);
                    startActivity(intent);
                    break;
                default:
            }
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    //用于实现记住密码的方法，这里使用SharedPreference存放临时数据
    private void isRemember() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //检查复选框是否被选中
        if (mCheckBox.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("account", mEditTextAccount.getText().toString());
            editor.putString("password", mEditTextPassword.getText().toString());
        } else {
            editor.clear();
        }
        editor.apply();
    }
}
