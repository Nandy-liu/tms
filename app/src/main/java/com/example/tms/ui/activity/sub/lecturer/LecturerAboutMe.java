package com.example.tms.ui.activity.sub.lecturer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Lecturer;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    用于展示讲师信息
 */
@SuppressLint("NonConstantResourceId")
public class LecturerAboutMe extends AppCompatActivity{
    private SQLiteDatabase db;
    @BindView(R.id.t_aboutme_id)
    public TextView t_aboutme_id;
    @BindView(R.id.t_aboutme_name)
    public TextView t_aboutme_name;
    @BindView(R.id.spinner)
    public Spinner mSpinner;
    @BindView(R.id.t_aboutme_age)
    public TextView t_aboutme_age;
    @BindView(R.id.t_aboutme_level)
    public TextView t_aboutme_level;
    @BindView(R.id.t_aboutme_phone)
    public TextView t_aboutme_phone;
    @BindView(R.id.t_aboutme_company)
    public TextView t_aboutme_company;
    @BindView(R.id .button_finish_about_t)
    public Button button_finish_about_t;
    //用于传递信息
    private Intent mIntentParent;
    private int mSpinnerPosition;
    private Api mApi;
private  ArrayAdapter l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_about_me);
        ButterKnife.bind(this);
        //获取上个activity穿过来的intent
        mIntentParent = getIntent();
        initSpinner();

        mApi = HttpUtil.getInstance().getApi();
        Call<Lecturer> task = mApi.LECTURER_GET(mIntentParent.getStringExtra("account"),null);
        task.enqueue(new Callback<Lecturer>() {
            @Override
            public void onResponse(Call<Lecturer> call, Response<Lecturer> response) {
                if (response.code()== HttpsURLConnection.HTTP_OK) {
                    Lecturer.DataDTO dataDTO = response.body().getData().get(0);
                    t_aboutme_id.setText(String.valueOf(dataDTO.getId()));
                    t_aboutme_name.setText(dataDTO.getName());
                    t_aboutme_level.setText(dataDTO.getLevel());
                    t_aboutme_phone.setText(String.valueOf(dataDTO.getPhone()));
                    if (dataDTO.getSex().equals("男"))
                        mSpinner.setSelection(0);
                    else
                        mSpinner.setSelection(1);
                    t_aboutme_age.setText(String.valueOf(dataDTO.getAge()));
                    t_aboutme_company.setText(dataDTO.getCompany());
                }
            }

            @Override
            public void onFailure(Call<Lecturer> call, Throwable t) {

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


    //用于点击返回后销毁当前活动
    @OnClick({R.id.button_finish_about_t,R.id.button_save})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_finish_about_t:
                finish();
                break;
            case R.id.button_save:
                Lecturer.DataDTO dataDTO = new Lecturer.DataDTO();
                dataDTO.setId(Integer.valueOf(t_aboutme_id.getText().toString()));
                dataDTO.setName(t_aboutme_name.getText().toString());
                dataDTO.setSex(getApplicationContext().getResources().getStringArray(R.array.sex)[mSpinnerPosition]);
                dataDTO.setAge(Integer.valueOf(t_aboutme_age.getText().toString()));
                dataDTO.setLevel(t_aboutme_level.getText().toString());
                dataDTO.setCompany(t_aboutme_company.getText().toString());
                Call<ResponseOther> task = mApi.LECTURER_PUT(dataDTO);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        if (response.body().getMessage().equals("success")) {
                            Toast.makeText(LecturerAboutMe.this,"修改成功", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(LecturerAboutMe.this,"修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(LecturerAboutMe.this,"修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
