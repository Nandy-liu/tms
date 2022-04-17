package com.example.tms.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<V extends IBaseView, P extends BasePresenter<V>> extends Fragment {
    private P presenter;
    private V view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public void onDestroy() {
        super.onDestroy();
        if (this.presenter != null) {
            this.presenter.detachView();
        }
    }
}
