package com.example.tms.model;

import android.database.sqlite.SQLiteDatabase;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IStudentCourse {
    interface IPresenter{
        void queryCheckInfo(SQLiteDatabase db,String student_id, String course_id);

        void queryAskInfo(SQLiteDatabase db,String student_id, String course_id);

        void queryDiscussInfo( SQLiteDatabase db,String student_id, String course_id);

        void checkIn(SQLiteDatabase db,String student_id, String course_id);

        void askSubmit(SQLiteDatabase db,String student_id, String course_id, String text);

        void discussSubmit(SQLiteDatabase db,String student_id, String course_id,String text);
    }
    interface IView extends IBaseView {
        void onCheckInLoaded(ArrayList<Map<String, Object>> chechins);

        void onDiscussLoaded(ArrayList<Map<String, Object>> discusses);

        void onAskLoaded(ArrayList<Map<String, Object>> asks);

        void showMessage(String message);
    }
}
