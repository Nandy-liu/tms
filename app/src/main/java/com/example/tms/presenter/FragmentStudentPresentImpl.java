package com.example.tms.presenter;

import android.content.Context;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IFragmentStudent;
import com.example.tms.model.domain.Account;
import com.example.tms.model.domain.Student;
import com.example.tms.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStudentPresentImpl extends BasePresenter<IFragmentStudent.IView> implements IFragmentStudent.IPresenter {

    private final Api mApi;

    public FragmentStudentPresentImpl(Context context) {
        super(context);
        mApi = HttpUtil.getInstance().getApi();
    }


    @Override
    public void searchAll(String account, String name, String department) {
        Call<Student> task = mApi.STUDENT_GET(account, name, department);
        task.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Student.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", data.get(i).getName());
                    map.put("id", String.valueOf(data.get(i).getId()));
                    map.put("sex", data.get(i).getSex());
                    map.put("age", String.valueOf(data.get(i).getAge()));
                    map.put("department", data.get(i).getDepartment());
                    map.put("phone", String.valueOf(data.get(i).getPhone()));
                    map.put("company", data.get(i).getCompany());
                    arrayList.add(map);
                }
                getView().onSearchedAll(arrayList);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
    }

    @Override
    public void searchAccount() {
        Call<Account> task = mApi.ACCOUNT_GET(null, "1");
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

    @Override
    public void search(String account,String name,String department) {
        if (account==null&&name==null&&department==null) {
            getView().showMessage("您的查询条件为空,默认查询全部");
        }
        Call<Student> task = mApi.STUDENT_GET(account, name, department);
        task.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                List<Student.DataDTO> data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", data.get(i).getName());
                    map.put("id", String.valueOf(data.get(i).getId()));
                    map.put("sex", data.get(i).getSex());
                    map.put("age", String.valueOf(data.get(i).getAge()));
                    map.put("department", data.get(i).getDepartment());
                    map.put("phone", String.valueOf(data.get(i).getPhone()));
                    map.put("company", data.get(i).getCompany());
                    arrayList.add(map);
                }
                getView().onSearched(arrayList);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
    }
}
