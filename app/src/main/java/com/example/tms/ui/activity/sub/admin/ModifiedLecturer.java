package com.example.tms.ui.activity.sub.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Lecturer;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.DialogConfirm;
import com.example.tms.utils.HttpUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class ModifiedLecturer extends AppCompatActivity {
    @BindView(R.id.edit_lecturer_id_d)
    EditText edit_lecturer_id;
    @BindView(R.id.edit_lecturer_name_d)
    EditText edit_xingming_d;
    @BindView(R.id.edit_level_d)
    EditText edit_level_d;
    @BindView(R.id.edit_lecturer_sex_d)
    EditText edit_sex_d;
    @BindView(R.id.edit_lecturer_age_d)
    EditText edit_age_d;
    @BindView(R.id.edit_lecturer_phone_d)
    EditText edit_phone_d;
    @BindView(R.id.edit_lecturer_company_d)
    EditText edit_company_d;
    String t_d_id;
    String t_d_xingming;
    String t_d_level;
    boolean flag = false;
    @BindView(R.id.call_to_lecturer)
    public ImageButton mImg1;
    @BindView(R.id.button_lecturer_delete)
    public Button mButton_delete;
    @BindView(R.id.button_lecturer_baocun)
    public Button mButton_baocun;
    private Api mApi;
    private Intent mIntent_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_change_lecturer);
        ButterKnife.bind(this);
        mApi = HttpUtil.getInstance().getApi();
        mIntent_parent = getIntent();
        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_lecturer_id.setText(mIntent_parent.getStringExtra("lecturer_id"));
        edit_xingming_d.setText(mIntent_parent.getStringExtra("name"));
        edit_level_d.setText(mIntent_parent.getStringExtra("level"));
        edit_sex_d.setText(mIntent_parent.getStringExtra("sex"));
        edit_age_d.setText(mIntent_parent.getStringExtra("age"));
        edit_phone_d.setText(mIntent_parent.getStringExtra("phone"));
        edit_company_d.setText(mIntent_parent.getStringExtra("company"));
        //获取初始值
        t_d_id = edit_lecturer_id.getText().toString();
        t_d_xingming = edit_xingming_d.getText().toString();
        t_d_level = edit_level_d.getText().toString();
    }

    @OnClick({R.id.button_lecturer_delete, R.id.button_lecturer_baocun, R.id.call_to_lecturer})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_lecturer_delete:
                MyHandler myHandler = new MyHandler(ModifiedLecturer.this);
                AlertDialog.Builder builder = new DialogConfirm(ModifiedLecturer.this, myHandler).build();
                builder.show();
                break;
            //更改
            case R.id.button_lecturer_baocun:
                Lecturer.DataDTO dataDTO = new Lecturer.DataDTO();
                dataDTO.setName(edit_xingming_d.getText().toString());
                dataDTO.setId(Integer.valueOf(edit_lecturer_id.getText().toString()));
                dataDTO.setLevel(edit_level_d.getText().toString());
                dataDTO.setSex(edit_sex_d.getText().toString());
                dataDTO.setAge(Integer.valueOf(edit_age_d.getText().toString()));
                dataDTO.setPhone(Integer.valueOf(edit_phone_d.getText().toString()));
                dataDTO.setCompany(edit_company_d.getText().toString());
                Call<ResponseOther> task = mApi.LECTURER_PUT(dataDTO);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedLecturer.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedLecturer.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                break;
            case R.id.call_to_lecturer:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + edit_phone_d.getText().toString()));
                startActivity(intent);
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
                Call<ResponseOther> task = mApi.LECTURER_DELETE(mIntent_parent.getStringExtra("id"));
                task.enqueue(new retrofit2.Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedLecturer.this, "删除成功", Toast.LENGTH_SHORT).show();
                        close();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedLecturer.this, "删除失败" + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void close() {
        finish();
    }
}
