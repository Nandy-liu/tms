package com.example.tms.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IActivityStudent;
import com.example.tms.model.domain.Student;
import com.example.tms.model.domain.StudentCourse;
import com.example.tms.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityStudentPresenteImpl extends BasePresenter<IActivityStudent.view> implements IActivityStudent.presenter {
    private static Api sApi;

    public ActivityStudentPresenteImpl(Context context) {
        super(context);
        sApi = HttpUtil.getInstance().getApi();
    }

    @Override
    public void queryInfo(String account) {
        Call<Student> task = sApi.STUDENT_GET(account,null,null);
        task.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    List<Student.DataDTO> data = null;
                    if (response.body() != null) {
                        data = response.body().getData();
                    }
                    if (data == null || data.size() == 0) {
                        getView().onSearchedName("无法获取您的个人信息");
                    } else {
                        getView().onSearchedName(data.get(0).getName() + " 欢迎您！");
                    }
                } else {
                    getView().showMessage("网络错误，code：" + code);
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                getView().showMessage("个人信息获取失败");
            }
        });
    }

    @Override
    public void searchCourse(String account) {
        Call<StudentCourse> task = sApi.STUDENT_COURSE_GET(account,null);
        task.enqueue(new Callback<StudentCourse>() {
            @Override
            public void onResponse(@NonNull Call<StudentCourse> call, @NonNull Response<StudentCourse> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    List<StudentCourse.DataDTO> data = null;
                    if (response.body() != null) {
                        data = response.body().getData();
                    }
                    if (data == null || data.size() == 0) {
                        getView().showMessage("您还没有选择任何课！");
                    } else {
                        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("course_id", data.get(i).getCourseId());
                            map.put("course_time", data.get(i).getCourseTime());
                            map.put("course_name", data.get(i).getCourseName());
                            map.put("lecturer_name", data.get(i).getName());
                            map.put("course_period", data.get(i).getCoursePeriod());
                            map.put("course_weight", data.get(i).getCourseWeight());
                            map.put("course_img", data.get(i).getCourseImg());
                            map.put("score", data.get(i).getScore());
                            arrayList.add(map);
                        }
                        getView().onSearchedCourse(arrayList);
                    }
                } else {
                    getView().showMessage("网络错误，code：" + code);
                }
            }

            @Override
            public void onFailure(Call<StudentCourse> call, Throwable t) {
                getView().showMessage("查询失败");
            }
        });
    }
}
