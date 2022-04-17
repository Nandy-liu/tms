package com.example.tms.model;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IActivityStudent {
    interface view extends IBaseView {
        void onSearchedCourse(ArrayList<Map<String,Object>> courses);

        void onSearchedName(String name);

        void showMessage(String message);
    }
    interface presenter {
        void queryInfo(String account);

        void searchCourse(String account);
    }
}
