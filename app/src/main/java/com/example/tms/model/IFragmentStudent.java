package com.example.tms.model;

import com.example.tms.base.IBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface IFragmentStudent {
    interface IPresenter{
        void searchAll(String account, String name, String department);

        void searchAccount();

        void search(String account,String name,String department);
    }
    interface IView extends IBaseView {
        void onSearchedAll(ArrayList<Map<String,String>> students);

        void onSearchedAccount(ArrayList<Map<String,String>> accounts);

        void onSearched(ArrayList<Map<String, String>> student);

        void showMessage(String message);
    }
}
