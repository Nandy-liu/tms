package com.example.tms.ui.activity.sub.admin;

import static com.example.tms.utils.MD5.md5;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
public class AddAdmin extends AppCompatActivity {
    SQLiteDatabase db;
    @BindView(R.id.edit_admin_account)
    public EditText mEdit_admin_account;
    @BindView(R.id.edit_admin_password)
    public EditText mEdit_admin_password;
    @BindView(R.id.button_add_admin_ok)
    public Button mChange;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.button_add_admin_ok)
    public void onClick(View v) {
        String account = mEdit_admin_account.getText().toString();
        String password = mEdit_admin_password.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("account", account);
        map.put("role", "3");
        map.put("password", md5(password));
        Api api = HttpUtil.getInstance().getApi();
        Call<ResponseOther> task = api.ACCOUNT_POST(map);
        task.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(AddAdmin.this, "增加管理员成功！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddAdmin.this, "添加失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                Toast.makeText(AddAdmin.this, "添加失败！", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
}
