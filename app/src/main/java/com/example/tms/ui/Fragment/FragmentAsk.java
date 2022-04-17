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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
public class FragmentAsk extends BaseFragment<IStudentCourse.IView,StudentCourseImpl> implements IStudentCourse.IView {

    private Intent mIntent;
    private ListView mAskList;
    private SQLiteDatabase mDb;
    private ImageButton mImageButton;
    private EditText mEditText;

    public FragmentAsk(Intent intent) {
        mIntent = intent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAskList = getView().findViewById(R.id.ask_list);
        mImageButton = getView().findViewById(R.id.ask_submit);
        mEditText = getView().findViewById(R.id.ask_et);

        mDb = new DBOpenHelper(getContext(), "test_db").getWritableDatabase();

        getMVPPresenter().queryAskInfo(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"));
        initListener();
    }

    private void initListener() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                mEditText.setText("");
                getMVPPresenter().askSubmit(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"),text);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_student_ask, container, false);

        return inflate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAskLoaded(ArrayList<Map<String, Object>> asks) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), asks, R.layout.item_ask,
                new String[]{"name","ask", "answer", "person_img"},
                new int[]{R.id.ask_name,R.id.ask_tv, R.id.ask_answer, R.id.ask_img});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv=(ImageView)view;
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        mAskList.setAdapter(simpleAdapter);
    }

    @Override
    public void onCheckInLoaded(ArrayList<Map<String, Object>> chechins) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onDiscussLoaded(ArrayList<Map<String, Object>> discusses) {

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
