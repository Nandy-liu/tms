package com.example.tms.ui.activity.sub.admin;

import static com.example.tms.utils.MD5.md5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class ChangeAccount extends AppCompatActivity {
    @BindView(R.id.edit_account)
    public EditText mEdit_account;
    @BindView(R.id.edit_account_password)
    public EditText mEdit_password;
    @BindView(R.id.button_account_change)
    public Button mButton_account_change;
    @BindView(R.id.button_account_back)
    public Button mButton_account_back;
    @BindView(R.id.change_account_title)
    TextView mTextView;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        ButterKnife.bind(this);
        //获取进来这个活动的intent
        mIntent = getIntent();
        mEdit_account.setText(mIntent.getStringExtra("account"));
        switch (mIntent.getStringExtra("role")) {
            case "1":
                mTextView.setText("更改学员账号信息");
                break;
            case "2":
                mTextView.setText("更改讲师账号信息");
                break;
            case "3":
                mTextView.setText("更改管理员账号信息");
                break;
        }

    }
    @OnClick({R.id.button_account_change, R.id.button_account_back})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button_account_change:
                Map<String, String> map = new HashMap<>();
                map.put("account", mIntent.getStringExtra("account"));
                map.put("role", mIntent.getStringExtra("role"));
                map.put("password", md5(mEdit_password.getText().toString()));
                Api api = HttpUtil.getInstance().getApi();
                Call<ResponseOther> task = api.ACCOUNT_PUT(map);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ChangeAccount.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ChangeAccount.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.button_account_back:
                finish();
                break;
        }
    }
}
