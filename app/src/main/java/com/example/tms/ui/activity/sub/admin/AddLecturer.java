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

@SuppressLint("NonConstantResourceId")
public class AddLecturer extends AppCompatActivity {
    @BindView(R.id.edit_lecturer_id)
    public EditText id;
    @BindView(R.id.edit_lecturer_xingming)
    public EditText name;
    @BindView(R.id.edit_lecturer_age)
    public EditText mage;
    @BindView(R.id.edit_lecturer_phone)
    public EditText phone;
    @BindView(R.id.edit_lecturer_level)
    public EditText level;
    @BindView(R.id.edit_lecturer_company)
    public EditText company;
    @BindView(R.id.button_add_lecturer_ok)
    public Button mButton_add;
    @BindView(R.id.button_finish_add_lecturer)
    public Button mButton_back;
    @BindView(R.id.spinner)
    public Spinner mSpinner;
    private int mSpinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        ButterKnife.bind(this);
        initSpinner();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, R.layout.support_simple_spinner_dropdown_item);
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

    @OnClick({R.id.button_add_lecturer_ok, R.id.button_finish_add_lecturer})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_lecturer_ok:
                Map<String, String> map = new HashMap<>();
                map.put("id", id.getText().toString());
                map.put("name", name.getText().toString());
                map.put("sex ", getApplicationContext().getResources().getStringArray(R.array.sex)[mSpinnerPosition]);
                map.put("age", mage.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("level", level.getText().toString());
                map.put("company", company.getText().toString());
                Api api = HttpUtil.getInstance().getApi();
                Call<ResponseOther> task = api.LECTURER_POST(map);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        if (response.body().getMessage().equals("success")) {
                            Toast.makeText(AddLecturer.this, "成功创建一条数据", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddLecturer.this, "输入有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(AddLecturer.this, "网络错误", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
            case R.id.button_finish_add_lecturer:
                finish();
                break;
        }
    }
}
