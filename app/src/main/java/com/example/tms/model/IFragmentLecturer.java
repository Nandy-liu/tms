package com.example.tms.model;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IFragmentLecturer {
    interface IView extends IBaseView {
        void onSearchedAll(ArrayList<Map<String,String>> lecturers);

        void onSearchedAccount(ArrayList<Map<String,String>> accounts);

        void onSearched(ArrayList<Map<String, String>> lecturers);

        void showMessage(String message);
    }
    interface IPresenter{
        void searchAll();

        void searchAccount();

        void search(String lecturer_id, String lecturer_name);
    }
}
