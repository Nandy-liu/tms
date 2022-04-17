package com.example.tms.ui.activity.sub.lecturer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.StudentCourse;
import com.example.tms.utils.HttpUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@SuppressLint("NonConstantResourceId")
public class LecturerCourseInfo extends AppCompatActivity {
    @BindView(R.id.listview_student_choose_info)
    public ListView listView;
    private SQLiteDatabase db;
    private String mCourseId = "";
    private String mAccount = "";
    @BindView(R.id.orderby_id)
    public Button button_department;
    @BindView(R.id.orderby_score)
    public Button button_score;
    @BindView(R.id.button_average_score)
    public Button button_aver;
    @BindView(R.id.orderby_name)
    public Button button_name;
    private Intent mIntentParent;
    @BindView(R.id.text_tongji)
    public TextView textView_tip;
    private List<StudentCourse.DataDTO> mStudentlist;
    private int mCount = 1;
    private HashMap<Integer, String> mMap;
    private final int BY_SCORE = 1;
    private final int BY_ID = 2;
    private final int BY_NAME = 3;
    private List<Map.Entry<Integer, String>> mSortTempList;
    private ArrayList<Map<String, String>> mSortedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_course_info);
        ButterKnife.bind(this);
        mIntentParent = getIntent();
        //获取课程名称+讲师姓名
        mCourseId = mIntentParent.getStringExtra("course_id");
        mAccount = mIntentParent.getStringExtra("account");
        initListener();
        loadList();
    }

    private void loadList() {
        Api api = HttpUtil.getInstance().getApi();
        Call<StudentCourse> task = api.STUDENT_COURSE_TEST_GET(null, mCourseId);
        task.enqueue(new Callback<StudentCourse>() {
            @Override
            public void onResponse(Call<StudentCourse> call, Response<StudentCourse> response) {
                if (response.body() != null) {
                    if (response.body().getData().size() == 0) {
                        Toast.makeText(LecturerCourseInfo.this, "还没有人选这门课", Toast.LENGTH_SHORT).show();
                    } else {
                        mStudentlist = response.body().getData();
                        sort(BY_ID);
                        updateList();
                        //为textview赋值
                        String tip = "选择" + mStudentlist.get(0).getCourseName() + "这门课的共有" + mStudentlist.size() + "人";
                        textView_tip.setText(tip);
                    }
                }
            }

            @Override
            public void onFailure(Call<StudentCourse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadList();
        updateList();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = mSortedList.get(position);
                Intent intent = new Intent(LecturerCourseInfo.this, ChangeStudentScore.class);
                intent.putExtra("student_id", map.get("student_id"));
                intent.putExtra("name", map.get("name"));
                intent.putExtra("department", map.get("department"));
                intent.putExtra("course_name", map.get("course_name"));
                intent.putExtra("score", map.get("score"));
                intent.putExtra("student_course_id", map.get("student_course_id"));
                startActivityForResult(intent,1);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStudentlist == null || mStudentlist.size() == 0) {
                    Toast.makeText(LecturerCourseInfo.this, "还没有人选这门课", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (v.getId()) {
                    case R.id.orderby_score:
                        sort(BY_SCORE);
                        updateList();
                        break;
                    case R.id.orderby_id:
                        sort(BY_ID);
                        updateList();
                        break;
                    case R.id.orderby_name:
                        sort(BY_NAME);
                        updateList();
                        break;
                    //查看平均成绩
                    case R.id.button_average_score:
                        double sum = 0F;
                        for (int i = 0; i < mStudentlist.size(); i++) {
                            BigDecimal bigDecimal = new BigDecimal(mStudentlist.get(i).getScore().toString());
                            BigDecimal bigDecimal2 = new BigDecimal(sum);
                            sum = bigDecimal.add(bigDecimal2).doubleValue();
                        }
                        Toast.makeText(LecturerCourseInfo.this, "平均成绩为" + sum / mStudentlist.size(), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        button_department.setOnClickListener(listener);
        button_score.setOnClickListener(listener);
        button_aver.setOnClickListener(listener);
        button_name.setOnClickListener(listener);
    }

    private void sort(int byWhat) {
        mCount = ++mCount % 2;//实现简单的升序降序切换
        mMap = new HashMap<>();
        for (int i = 0; i < mStudentlist.size(); i++) {
            StudentCourse.DataDTO dataDTO = mStudentlist.get(i);
            switch (byWhat) {
                case BY_SCORE:
                    mMap.put(i, String.valueOf(dataDTO.getScore()));
                    break;
                case BY_ID:
                    mMap.put(i, String.valueOf(dataDTO.getStudentId()));
                    break;
                case BY_NAME:
                    mMap.put(i, dataDTO.getName());
                    break;
            }
        }
        mSortTempList = new ArrayList<>(mMap.entrySet());
        if (mCount == 1) {
            mSortTempList.sort(Map.Entry.comparingByValue());
            Toast.makeText(LecturerCourseInfo.this, "升序", Toast.LENGTH_SHORT).show();
        } else {
            mSortTempList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            Toast.makeText(LecturerCourseInfo.this, "降序", Toast.LENGTH_SHORT).show();
        }
    }

    //分类排序，根据不同的选项排序
    public void updateList() {
        mSortedList = new ArrayList<Map<String, String>>();
        for (Map.Entry<Integer, String> entry : mSortTempList) {
            StudentCourse.DataDTO dataDTO = mStudentlist.get(entry.getKey());
            Map<String, String> map = new HashMap<String, String>();
            map.put("student_id", dataDTO.getStudentId().toString());
            map.put("name", dataDTO.getName());
            map.put("department", dataDTO.getCompany());
            map.put("course_name", dataDTO.getCourseName());
            map.put("score", String.valueOf(dataDTO.getScore()));
            map.put("student_course_id", String.valueOf(dataDTO.getStudentCourseId()));
            mSortedList.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(LecturerCourseInfo.this, mSortedList, R.layout.item_course_info,
                new String[]{"student_id", "name", "department", "course_name", "score"}, new int[]{R.id.course_student_id,
                R.id.course_student_name, R.id.course_student_department, R.id.course_student_coursename, R.id.course_student_score});
        listView.setAdapter(simpleAdapter);
    }
}
