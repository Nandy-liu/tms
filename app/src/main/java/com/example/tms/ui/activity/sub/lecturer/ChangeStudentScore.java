package com.example.tms.ui.activity.sub.lecturer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    该界面主要用于修改学员分数的信息
 */
@SuppressLint("NonConstantResourceId")
public class ChangeStudentScore extends AppCompatActivity {
    @BindView(R.id.button_score_change)
    public Button button_change;
    @BindView(R.id.button_score_back)
    public Button button_back;
    private Intent intent;
    @BindView(R.id.t_id)
    public TextView textView1;
    @BindView(R.id.t_name)
    public TextView textView2;
    @BindView(R.id.t_department)
    public TextView textView3;
    @BindView(R.id.tc_course_name)
    public TextView textView4;
    @BindView(R.id.edit_score)
    public EditText editText;
    public void init() {
        intent = getIntent();
        textView1.setText(intent.getStringExtra("student_id"));
        textView2.setText(intent.getStringExtra("name"));
        textView3.setText(intent.getStringExtra("department"));
        textView4.setText(intent.getStringExtra("course_name"));
        editText.setText(intent.getStringExtra("score"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_student_score);
        ButterKnife.bind(this);
        init();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_score_change:
                        Api api = HttpUtil.getInstance().getApi();
                        String student_course_id = intent.getStringExtra("student_course_id");
                        String score = editText.getText().toString();

                        Map<String,String> map = new HashMap<>();
                        map.put("student_course_id",student_course_id);
                        map.put("score",score);

                        Call<ResponseOther> task = api.STUDENT_COURSE_PUT(map);
                        task.enqueue(new Callback<ResponseOther>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseOther> call, @NonNull Response<ResponseOther> response) {
                                if (response.code()== HttpsURLConnection.HTTP_OK) {
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseOther> call, Throwable t) {
                                Toast.makeText(ChangeStudentScore.this,"网络错误",Toast.LENGTH_SHORT).show();

                            }
                        });
                        Toast.makeText(ChangeStudentScore.this,"修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case R.id.button_score_back:
                        setResult(1);
                        finish();
                        break;
                    default:
                }
            }
        };
        button_change.setOnClickListener(listener);
        button_back.setOnClickListener(listener);
    }


}
