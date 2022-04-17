package com.example.tms.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Feedback;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFeedback extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        final EditText editText = findViewById(R.id.et_message);
        Button button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(AddFeedback.this, "留言不可以为空", Toast.LENGTH_SHORT).show();
                } else {
                    Feedback.DataDTO dataDTO = new Feedback.DataDTO();
                    dataDTO.setAccount(Integer.valueOf(getIntent().getStringExtra("account")));
                    dataDTO.setMessage(editText.getText().toString());

                    Api api = HttpUtil.getInstance().getApi();
                    Call<ResponseOther> task = api.FEEDBACK_POST(dataDTO);
                    task.enqueue(new Callback<ResponseOther>() {
                        @Override
                        public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseOther> call, Throwable t) {

                        }
                    });

                    Toast.makeText(AddFeedback.this, "留言成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
