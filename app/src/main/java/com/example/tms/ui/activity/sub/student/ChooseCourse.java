package com.example.tms.ui.activity.sub.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Course;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.model.domain.StudentCourse;
import com.example.tms.ui.adaptor.ChooseCourseAdapter;
import com.example.tms.utils.HttpUtil;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@SuppressLint("NonConstantResourceId")
public class ChooseCourse extends AppCompatActivity {
    private ChooseCourseAdapter mChooseCourseAdapter;
    @BindView(R.id.listview_course)
    public ListView listView_course;
    @BindView(R.id.back)
    public Button button_back;
    @BindView(R.id.choose)
    public Button button_choose;
    private Intent mIntentParent;
    private Api mApi;
    private String mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        ButterKnife.bind(this);
        mIntentParent = getIntent();
        mAccount = mIntentParent.getStringExtra("account");
        mApi = HttpUtil.getInstance().getApi();
        loadCourse();
    }

    private void loadCourse() {
        Call<Course> courseCall = mApi.COURSE_GET(null);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    Course body = response.body();
                    if (body == null || body.getData().size() == 0) {
                        Toast.makeText(ChooseCourse.this, "没有获取到内容", Toast.LENGTH_SHORT).show();
                    } else {
                        List<Course.DataDTO> data = body.getData();
                        mChooseCourseAdapter = new ChooseCourseAdapter(ChooseCourse.this, data);
                        listView_course.setAdapter(mChooseCourseAdapter);
                    }
                } else {
                    Toast.makeText(ChooseCourse.this, "网络错误，code:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(ChooseCourse.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.choose,R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose:
                for (int i = 0; i < mChooseCourseAdapter.getCount(); i++) {
                    //获取子项的对象
                    Course.DataDTO it = (Course.DataDTO) mChooseCourseAdapter.getItem(i);
                    //如果是被选中的状态
                    if( it.getIsCheck()) {
                        Call<StudentCourse> task = mApi.STUDENT_COURSE_GET(mAccount, String.valueOf(it.getCourseId()));
                        task.enqueue(new Callback<StudentCourse>() {
                            @Override
                            public void onResponse(Call<StudentCourse> call, Response<StudentCourse> response) {
                                if (response.body().getData().size() != 0) {
                                    Toast.makeText(ChooseCourse.this, it.getCourseName() + "重复选中，其他课程选课成功！", Toast.LENGTH_SHORT).show();
                                }else {
                                    select(it);
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Call<StudentCourse> call, Throwable t) {
                                Toast.makeText(ChooseCourse.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void select(Course.DataDTO it) {
        StudentCourse.DataDTO dataDTO = new StudentCourse.DataDTO();
        dataDTO.setStudentId(Integer.valueOf(mAccount));
        dataDTO.setCourseId(it.getCourseId());
        dataDTO.setLecturerId(it.getLecturerId());
        Call<ResponseOther> task = mApi.STUDENT_COURSE_POST(dataDTO);
        task.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    Toast.makeText(ChooseCourse.this, it.getCourseName() + " 选课成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChooseCourse.this, "请求失败，code：" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                Toast.makeText(ChooseCourse.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
