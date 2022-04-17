package com.example.tms.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.tms.R;
import com.example.tms.ui.Fragment.FragmentAsk;
import com.example.tms.ui.Fragment.FragmentCheckIn;
import com.example.tms.ui.Fragment.FragmentDiscuss;
import com.example.tms.ui.adaptor.PagerAdaptor;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
@SuppressLint("NonConstantResourceId")
public class ActivityStudentCourse extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pager);
        Intent intent = getIntent();
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ImageView imageView = findViewById(R.id.course_img);
        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        FragmentAsk askFragment = new FragmentAsk(intent);
        FragmentCheckIn chechinFragment=new FragmentCheckIn(intent);
        FragmentDiscuss discussFragment=new FragmentDiscuss(intent);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(chechinFragment);
        fragments.add(askFragment);
        fragments.add(discussFragment);
        PagerAdaptor adaptor = new PagerAdaptor(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adaptor);
        Glide.with(getApplicationContext()).load(intent.getStringExtra("course_img")).into(imageView);
    }
}
