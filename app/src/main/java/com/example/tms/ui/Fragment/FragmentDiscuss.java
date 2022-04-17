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
public class FragmentDiscuss extends BaseFragment<IStudentCourse.IView,StudentCourseImpl> implements IStudentCourse.IView {

    private final Intent mIntent;
    private ListView mDiscussList;
    private SQLiteDatabase mDb;
    private EditText mEditText;
    private ImageButton mImageButton;

    public FragmentDiscuss(Intent intent) {
        mIntent=intent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDiscussList = getView().findViewById(R.id.discuss_list);
        mImageButton = getView().findViewById(R.id.discuss_submit);
        mEditText = getView().findViewById(R.id.discuss_et);

        mDb = new DBOpenHelper(getContext(), "test_db").getWritableDatabase();

        getMVPPresenter().queryDiscussInfo(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"));
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                mEditText.setText("");
                getMVPPresenter().discussSubmit(mDb,mIntent.getStringExtra("account"), mIntent.getStringExtra("course_id"),text);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_student_discuss, container, false);
        return inflate;
    }

    @Override
    public void onCheckInLoaded(ArrayList<Map<String, Object>> chechins) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDiscussLoaded(ArrayList<Map<String, Object>> discusses) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), discusses, R.layout.item_discuss,
                new String[]{"name","discuss_content", "person_img"},
                new int[]{R.id.discuss_name,R.id.discuss_tv, R.id.discuss_img});
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
        mDiscussList.setAdapter(simpleAdapter);
    }

    @Override
    public void onAskLoaded(ArrayList<Map<String, Object>> asks) {

    }

    @Override
    public void showMessage(String message) {

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
