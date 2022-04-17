package com.example.tms.model;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IFragmentAdmin {
    interface IPresenter{
        void searchFeedback();

        void searchCourse();

        void searchAccount();
    }
    interface IView extends IBaseView {
        void onSearchedFeedback(ArrayList<Map<String,String>> messages);

        void onSearchedAccount(ArrayList<Map<String,String>> accounts);

        void onSearchedCourse(ArrayList<Map<String, String>> courses);

        void showMessage(String message);
    }
}
