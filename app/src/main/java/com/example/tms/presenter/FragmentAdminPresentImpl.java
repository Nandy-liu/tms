package com.example.tms.presenter;

import android.content.Context;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IFragmentAdmin;
import com.example.tms.model.domain.Account;
import com.example.tms.model.domain.Course;
import com.example.tms.model.domain.Feedback;
import com.example.tms.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminPresentImpl extends BasePresenter<IFragmentAdmin.IView> implements IFragmentAdmin.IPresenter {

    private final Api mApi;

    public FragmentAdminPresentImpl(Context context) {
        super(context);
        mApi = HttpUtil.getInstance().getApi();
    }

    @Override
    public void searchFeedback() {
        Call<Feedback> task = mApi.FEEDBACK_GET();
        task.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Feedback.DataDTO> data = response.body().getData();
                if (data.size() == 0) {
                    getView().showMessage("还没有任何留言哦~");
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("account", String.valueOf(data.get(i).getAccount()));
                        map.put("name", data.get(i).getName());
                        map.put("company", data.get(i).getCompany());
                        map.put("message", data.get(i).getMessage());
                        arrayList.add(map);
                    }
                    getView().onSearchedFeedback(arrayList);
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {

            }
        });
    }

    @Override
    public void searchCourse() {
        Call<Course> task = mApi.COURSE_GET(null);
        task.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Course.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("course_id", String.valueOf(data.get(i).getCourseId()));
                    map.put("lecturer_name", data.get(i).getName());
                    map.put("course_name", data.get(i).getCourseName());
                    map.put("course_weight", String.valueOf(data.get(i).getCourseWeight()));
                    map.put("course_time", data.get(i).getCourseTime());
                    map.put("course_period", data.get(i).getCoursePeriod());
                    arrayList.add(map);
                }
                getView().onSearchedCourse(arrayList);
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {

            }
        });
    }

    @Override
    public void searchAccount() {
        Call<Account> task = mApi.ACCOUNT_GET(null, "3");
        task.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Account.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("account", String.valueOf(data.get(i).getAccount()));
                    map.put("password", data.get(i).getPassword());
                    map.put("role", String.valueOf(data.get(i).getRole()));
                    arrayList.add(map);
                }
                getView().onSearchedAccount(arrayList);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }
}
