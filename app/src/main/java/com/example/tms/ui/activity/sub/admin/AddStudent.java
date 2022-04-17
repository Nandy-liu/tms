package com.example.tms.ui.activity.sub.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 管理员用于增加学员信息
 */
@SuppressLint("NonConstantResourceId")
public class AddStudent extends AppCompatActivity {
    @BindView(R.id.edit_xuehao)
    public EditText mEdit_xuehao;
    @BindView(R.id.edit_xingming)
    public EditText mEdit_xingming;
    @BindView(R.id.edit_age)
    public EditText mEdit_age;
    @BindView(R.id.edit_department)
    public EditText mEdit_department;
    @BindView(R.id.edit_phone)
    public EditText mEdit_phone;
    @BindView(R.id.edit_company)
    public EditText mEdit_company;
    @BindView(R.id.button_add_add)
    public Button mButton_add_add;
    @BindView(R.id.button_finish_add)
    public Button mButton_finish_add;
    @BindView(R.id.spinner)
    public Spinner mSpinner;
    private int mSpinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        ButterKnife.bind(this);
        initSpinner();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.sex,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner.setAdapter(adapter);
    }

    @OnClick({R.id.button_add_add, R.id.button_finish_add})
    public void onClick(View v) {
        String xuehao = mEdit_xuehao.getText().toString();
        String xingming = mEdit_xingming.getText().toString();
        String sex = getApplicationContext().getResources().getStringArray(R.array.sex)[mSpinnerPosition];
        String age = mEdit_age.getText().toString();
        String department = mEdit_department.getText().toString();
        String phone = mEdit_phone.getText().toString();
        String company = mEdit_company.getText().toString();

        switch (v.getId()) {
            case R.id.button_add_add:
                Map<String,String> map = new HashMap<>();
                map.put("name", xingming);
                map.put("id", xuehao);
                map.put("department", department);
                map.put("sex", sex);
                map.put("age", age);
                map.put("phone", phone);
                map.put("company", company);
                Api api = HttpUtil.getInstance().getApi();
                Call<ResponseOther> task = api.STUDENT_POST(map);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        if (response.body().getMessage().equals("success")) {
                            Toast.makeText(AddStudent.this, "成功创建一条数据", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddStudent.this, "输入有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(AddStudent.this, "输入有误", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.button_finish_add:
                finish();
                break;
            default:
                break;
        }

    }
}
