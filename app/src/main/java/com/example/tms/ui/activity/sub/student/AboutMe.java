package com.example.tms.ui.activity.sub.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.model.domain.Student;
import com.example.tms.utils.HttpUtil;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    我的信息功能实现，主要根据登录时传过来的intent所携带的数据
 */
@SuppressLint("NonConstantResourceId")
public class AboutMe extends AppCompatActivity {
    @BindView(R.id.aboutme_id)
    public TextView mId;
    @BindView(R.id.aboutme_name)
    public EditText mName;
    @BindView(R.id.aboutme_phone)
    public EditText mPhone;
    @BindView(R.id.aboutme_department)
    public EditText mDepartment;
    @BindView(R.id.spinner)
    public Spinner mSpinner;
    @BindView(R.id.aboutme_age)
    public EditText mAge;
    @BindView(R.id.aboutme_company)
    public EditText mCompany;
    @BindView(R.id.button_save)
    public Button mButtonSave;
    @BindView(R.id.button_finish_about)
    public Button mButtonBack;
    private int mSpinnerPosition;
    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);
        Intent intentParent = getIntent();
        initSpinner();

        mApi = HttpUtil.getInstance().getApi();
        Call<Student> task = mApi.STUDENT_GET(intentParent.getStringExtra("account"),null,null);
        task.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.code()== HttpsURLConnection.HTTP_OK) {
                    Student.DataDTO dataDTO = response.body().getData().get(0);
                    mId.setText(String.valueOf(dataDTO.getId()));
                    mName.setText(dataDTO.getName());
                    mDepartment.setText(dataDTO.getDepartment());
                    mPhone.setText(String.valueOf(dataDTO.getPhone()));
                    if (dataDTO.getSex().equals("男"))
                        mSpinner.setSelection(0);
                    else
                        mSpinner.setSelection(1);
                    mAge.setText(String.valueOf(dataDTO.getAge()));
                    mCompany.setText(dataDTO.getCompany());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
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

    @OnClick({R.id.button_finish_about,R.id.button_save})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_finish_about:
                finish();
                break;
            case R.id.button_save:
                Student.DataDTO dataDTO = new Student.DataDTO();
                dataDTO.setId(Integer.valueOf(mId.getText().toString()));
                dataDTO.setName(mName.getText().toString());
                dataDTO.setSex(getApplicationContext().getResources().getStringArray(R.array.sex)[mSpinnerPosition]);
                dataDTO.setAge(Integer.valueOf(mAge.getText().toString()));
                dataDTO.setDepartment(mDepartment.getText().toString());
                dataDTO.setCompany(mCompany.getText().toString());
                Call<ResponseOther> task = mApi.STUDENT_PUT(dataDTO);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        if (response.body().getMessage().equals("success")) {
                            Toast.makeText(AboutMe.this,"修改成功", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(AboutMe.this,"修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(AboutMe.this,"修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
