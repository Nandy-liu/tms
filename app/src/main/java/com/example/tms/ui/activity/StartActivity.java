package com.example.tms.ui.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tms.R;

public class StartActivity extends AppCompatActivity {
    TextView mTx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startActivity(new Intent(StartActivity.this, Login.class));
        finish();
        mTx=findViewById(R.id.countDown);
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                mTx.setText( millisUntilFinished / 1000 + "s");
            }
            @Override
            public void onFinish() {
                //
            }
        }.start();
    }
}
