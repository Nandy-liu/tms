package com.example.tms.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.tms.R;
import com.example.tms.base.BaseFragment;
import com.example.tms.model.IStudentCourse;
import com.example.tms.presenter.StudentCourseImpl;
import com.example.tms.utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.Map;
@SuppressLint("NonConstantResourceId")
public class FragmentCheckIn extends BaseFragment<IStudentCourse.IView,StudentCourseImpl> implements IStudentCourse.IView {

    private final Intent mIntent;
    private SQLiteDatabase mDb;
    private ListView mCheckinList;
    private Button mCheckinBtn;

    public FragmentCheckIn(Intent intent) {
        mIntent = intent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCheckinList = getView().findViewById(R.id.checkin_list);
        mCheckinBtn=getView().findViewById(R.id.check_btn);

        mDb = new DBOpenHelper(getContext(), "test_db").getWritableDatabase();
        getMVPPresenter().queryCheckInfo(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"));
        mCheckinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMVPPresenter().checkIn(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"));
            }
        });
    }

 

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_student_checkin, container, false);
        return inflate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCheckInLoaded(ArrayList<Map<String, Object>> chechins) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), chechins, R.layout.item_checkin,
                new String[]{"name", "checkin_date", "person_img"},
                new int[]{R.id.checkin_name, R.id.checkin_time, R.id.checkin_img});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        mCheckinList.setAdapter(simpleAdapter);
    }

    @Override
    public void onDiscussLoaded(ArrayList<Map<String, Object>> discusses) {

    }

    @Override
    public void onAskLoaded(ArrayList<Map<String, Object>> asks) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public StudentCourseImpl bindPresenter() {
        return new StudentCourseImpl(getContext());
    }

    @Override
    public IStudentCourse.IView bindView() {
        return this;
    }
}
