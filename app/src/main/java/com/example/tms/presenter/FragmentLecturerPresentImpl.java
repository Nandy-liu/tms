package com.example.tms.presenter;

import android.content.Context;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IFragmentLecturer;
import com.example.tms.model.domain.Account;
import com.example.tms.model.domain.Lecturer;
import com.example.tms.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLecturerPresentImpl extends BasePresenter<IFragmentLecturer.IView> implements IFragmentLecturer.IPresenter {

    private final Api mApi;

    public FragmentLecturerPresentImpl(Context context) {
        super(context);
        mApi = HttpUtil.getInstance().getApi();
    }

    @Override
    public void searchAll() {
        Call<Lecturer> task = mApi.LECTURER_GET(null, null);
        ArrayList<Map<String, String>> arrayList_lecturer = new ArrayList<Map<String, String>>();
        task.enqueue(new Callback<Lecturer>() {
            @Override
            public void onResponse(Call<Lecturer> call, Response<Lecturer> response) {
                List<Lecturer.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", String.valueOf(data.get(i).getId()));
                    map.put("name", data.get(i).getName());
                    map.put("sex", data.get(i).getSex());
                    map.put("age", String.valueOf(data.get(i).getAge()));
                    map.put("level", data.get(i).getLevel());
                    map.put("phone", String.valueOf(data.get(i).getPhone()));
                    map.put("company", data.get(i).getCompany());
                    arrayList_lecturer.add(map);
                }
                getView().onSearchedAll(arrayList_lecturer);
            }

            @Override
            public void onFailure(Call<Lecturer> call, Throwable t) {

            }
        });
    }

    @Override
    public void searchAccount() {
        Call<Account> task = mApi.ACCOUNT_GET(null, "2");
        ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        task.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
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

    @Override
    public void search(String account, String name) {
        if (account == null && name == null) {
            getView().showMessage("您的查询条件为空,默认查询全部");
        }
        Call<Lecturer> task = mApi.LECTURER_GET(account, name);
        task.enqueue(new Callback<Lecturer>() {
            @Override
            public void onResponse(Call<Lecturer> call, Response<Lecturer> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Lecturer.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("lecturer_id", String.valueOf(data.get(i).getId()));
                    map.put("name", data.get(i).getName());
                    map.put("sex", data.get(i).getSex());
                    map.put("age", String.valueOf(data.get(i).getAge()));
                    map.put("level", data.get(i).getLevel());
                    map.put("phone", String.valueOf(data.get(i).getPhone()));
                    map.put("company", data.get(i).getCompany());
                    arrayList.add(map);
                }
                getView().onSearched(arrayList);
            }

            @Override
            public void onFailure(Call<Lecturer> call, Throwable t) {

            }
        });
    }
}
