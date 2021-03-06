package com.example.tms.base;

public interface IBasePresenter<V extends IBaseView> {
    void attachView(V view);

    void detachView();
}
