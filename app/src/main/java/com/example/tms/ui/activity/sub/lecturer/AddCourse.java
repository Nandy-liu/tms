package com.example.tms.ui.activity.sub.lecturer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Course;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class AddCourse extends AppCompatActivity {
    @BindView(R.id.l_add_cn)
    public EditText mName;
    @BindView(R.id.l_add_cw)
    public EditText mWeight;
    @BindView(R.id.l_add_ct)
    public EditText mTime;
    @BindView(R.id.l_add_cp)
    public EditText mPeriod;
    @BindView(R.id.l_add_btn)
    public Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        ButterKnife.bind(this);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course.DataDTO dataDTO = new Course.DataDTO();
                dataDTO.setLecturerId(Integer.valueOf(getIntent().getStringExtra("account")));
                dataDTO.setCourseName(mName.getText().toString());
                String s = mWeight.getText().toString();
                dataDTO.setCourseWeight(Integer.valueOf(s.equals("")?"0":s));
                dataDTO.setCourseTime(mTime.getText().toString());
                dataDTO.setCoursePeriod(mPeriod.getText().toString());

                Api api = HttpUtil.getInstance().getApi();
                Call<ResponseOther> task = api.COURSE_POST(dataDTO);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(AddCourse.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(AddCourse.this, "添加失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
