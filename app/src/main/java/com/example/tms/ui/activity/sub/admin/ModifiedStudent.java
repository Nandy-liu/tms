package com.example.tms.ui.activity.sub.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.model.domain.Student;
import com.example.tms.utils.DialogConfirm;
import com.example.tms.utils.HttpUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 这里主要是点击listview中的学员信息时，对应的相应activity
 */
@SuppressLint("NonConstantResourceId")
public class ModifiedStudent extends AppCompatActivity {
    @BindView(R.id.edit_xuehao_d)
    EditText edit_xuehao_d;
    @BindView(R.id.edit_xingming_d)
    EditText edit_xingming_d;
    @BindView(R.id.edit_department_d)
    EditText edit_department_d;
    @BindView(R.id.edit_sex_d)
    EditText edit_sex_d;
    @BindView(R.id.edit_age_d)
    EditText edit_age_d;
    @BindView(R.id.edit_phone_d)
    EditText edit_phone_d;
    @BindView(R.id.edit_company_d)
    EditText edit_company_d;
    String t_d_id;
    String t_d_xingming;
    String t_d_department;
    //false代表没点删除，点的是
    boolean flag = false;
    @BindView(R.id.button_delete)
    public Button mButton_delete;
    @BindView(R.id.button_save)
    public Button mButton_baocun;
    public Handler mHandler;
    private static Intent mIntent_parent;
    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_change);
        ButterKnife.bind(this);
        mApi = HttpUtil.getInstance().getApi();
        mIntent_parent = getIntent();
        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_xuehao_d.setText(mIntent_parent.getStringExtra("id"));
        edit_xingming_d.setText(mIntent_parent.getStringExtra("name"));
        edit_department_d.setText(mIntent_parent.getStringExtra("department"));
        edit_sex_d.setText(mIntent_parent.getStringExtra("sex"));
        edit_age_d.setText(mIntent_parent.getStringExtra("age"));
        edit_phone_d.setText(mIntent_parent.getStringExtra("phone"));
        edit_company_d.setText(mIntent_parent.getStringExtra("company"));
        //获取初始值
        t_d_id = edit_xuehao_d.getText().toString();
        t_d_xingming = edit_xingming_d.getText().toString();
        t_d_department = edit_department_d.getText().toString();
    }

    @OnClick({R.id.button_delete, R.id.button_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_delete:
                mHandler = new MyHandler(ModifiedStudent.this);
                AlertDialog.Builder builder = new DialogConfirm(ModifiedStudent.this, mHandler).build();
                builder.show();
                break;
            //更改
            case R.id.button_save:
                Student.DataDTO dataDTO = new Student.DataDTO();
                dataDTO.setName(edit_xingming_d.getText().toString());
                dataDTO.setId(Integer.valueOf(edit_xuehao_d.getText().toString()));
                dataDTO.setDepartment(edit_department_d.getText().toString());
                dataDTO.setSex(edit_sex_d.getText().toString());
                dataDTO.setAge(Integer.valueOf(edit_age_d.getText().toString()));
                dataDTO.setPhone(Integer.valueOf(edit_phone_d.getText().toString()));
                dataDTO.setCompany(edit_company_d.getText().toString());
                Call<ResponseOther> task = mApi.STUDENT_PUT(dataDTO);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedStudent.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedStudent.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                break;
            default:
                break;
        }
    }

    private class MyHandler extends Handler {
        private WeakReference<AppCompatActivity> mReference;

        public MyHandler(AppCompatActivity activity) {
            mReference = new WeakReference<AppCompatActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Call<ResponseOther> task = mApi.STUDENT_DELETE(mIntent_parent.getStringExtra("id"));
                task.enqueue(new retrofit2.Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedStudent.this, "删除成功", Toast.LENGTH_SHORT).show();
                        close();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedStudent.this, "删除失败"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void close() {
        finish();
    }

}
