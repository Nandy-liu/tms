package com.example.tms.model;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IActivityLecturer {
    interface view extends IBaseView {
        void showSnackBar(ArrayList<Map<String, Object>> arrayList);
        void updateList(ArrayList<Map<String, Object>> arrayList);
        void showMessage(String message);
    }

    interface presenter {
        void loadCourse(String account);
        void deleteCourse(String courseId);
    }
}
