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
import com.example.tms.utils.DialogConfirm;
import com.example.tms.utils.HttpUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class ModifiedCourse extends AppCompatActivity{
    @BindView(R.id.et_lecturer_name)
    EditText edit_lecturer_name;
    @BindView(R.id.et_course_name)
    EditText edit_course_name;
    @BindView(R.id.et_course_time)
    EditText edit_course_time;
    @BindView(R.id.et_course_period)
    EditText edit_course_period;
    String lecturer_name = "";
    String course_name = "";
    @BindView(R.id.button_delete_course)
    public Button mButton_delete;
    @BindView(R.id.button_change_course)
    public Button mButton_baocun;
    private Api mApi;
    private Intent mIntent_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_set);
        ButterKnife.bind(this);
        mApi = HttpUtil.getInstance().getApi();

        mIntent_parent = getIntent();
        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_lecturer_name.setText(mIntent_parent.getStringExtra("lecturer_name"));
        edit_course_name.setText(mIntent_parent.getStringExtra("course_name"));
        edit_course_time.setText(mIntent_parent.getStringExtra("course_time"));
        edit_course_period.setText(mIntent_parent.getStringExtra("course_period"));
        //获取初始值
        lecturer_name = edit_lecturer_name.getText().toString();
        course_name = edit_course_name.getText().toString();
    }

   @OnClick({R.id.button_delete_course,R.id.button_change_course})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_delete_course:
                MyHandler myHandler = new MyHandler(ModifiedCourse.this);
                AlertDialog.Builder builder = new DialogConfirm(ModifiedCourse.this, myHandler).build();
                builder.show();
                break;
            //更改*
            case R.id.button_change_course:
                Map<String,String> map = new HashMap<>();
                map.put("course_id",mIntent_parent.getStringExtra("course_id"));
                map.put("course_name",edit_course_name.getText().toString());
                map.put("course_time",edit_course_time.getText().toString());
                map.put("course_period",edit_course_period.getText().toString());
                Call<ResponseOther> task = mApi.COURSE_PUT(map);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedCourse.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedCourse.this, "修改失败", Toast.LENGTH_SHORT).show();
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
                Call<ResponseOther> task = mApi.COURSE_DELETE(mIntent_parent.getStringExtra("course_id"));
                task.enqueue(new retrofit2.Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(ModifiedCourse.this, "删除成功", Toast.LENGTH_SHORT).show();
                        close();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(ModifiedCourse.this, "删除失败"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void close() {
        finish();
    }
}
