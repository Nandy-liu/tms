package com.example.tms.presenter;

import android.content.Context;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IActivityLecturer;
import com.example.tms.model.domain.Course;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLecturerPresenterImpl extends BasePresenter<IActivityLecturer.view> implements IActivityLecturer.presenter {

    private final Api mApi;

    public ActivityLecturerPresenterImpl(Context context) {
        super(context);
        mApi = HttpUtil.getInstance().getApi();
    }

    @Override
    public void loadCourse(String account) {
        Call<Course> courseCall = mApi.COURSE_GET(account);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    if (response.body() != null) {
                        ArrayList<Map<String, Object>> mArrayList_mycourse = new ArrayList<>();
                        List<Course.DataDTO> data = response.body().getData();
                        for (int i = 0; i < data.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("course_name", data.get(i).getCourseName());
                            map.put("course_time", data.get(i).getCourseTime());
                            map.put("course_period", data.get(i).getCoursePeriod());
                            map.put("course_id", data.get(i).getCourseId());
                            mArrayList_mycourse.add(map);
                        }
                        getView().showSnackBar(mArrayList_mycourse);
                        //设置适配器
                        getView().updateList(mArrayList_mycourse);
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {

            }
        });
    }

    public void deleteCourse(String courseId) {
        Call<ResponseOther> task = mApi.COURSE_DELETE(courseId);
        task.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                getView().showMessage("删除成功");
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                getView().showMessage("删除失败");
            }
        });
    }
}
