package com.example.tms.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>> extends AppCompatActivity {
    private P presenter;
    private V view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter == null) {
            this.presenter = bindPresenter();
        }
        if (view == null) {
            this.view = bindView();
            this.presenter.attachView(this.view);
        }
    }

    public P getMVPPresenter() {
        return presenter;
    }

    public V getMVPView() {
        return view;
    }

    public abstract P bindPresenter();

    public abstract V bindView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.presenter != null) {
            this.presenter.detachView();
        }
    }
}
