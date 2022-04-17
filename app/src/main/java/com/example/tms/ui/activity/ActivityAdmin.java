package com.example.tms.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.tms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ActivityAdmin extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        BottomNavigationView navigationView = findViewById(R.id.nav_admin);
        NavController controller = Navigation.findNavController(this,R.id.student_course_fragment);
        NavigationUI.setupWithNavController(navigationView,controller);
    }
}
