package com.example.tms.base;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private WeakReference<Context> weakContext;
    private WeakReference<V> weakView;
    private V proxyView;

    public BasePresenter(Context context) {
        this.weakContext = new WeakReference<Context>(context);
    }

    public Context getContext() {
        return weakContext.get();
    }

    public V getView() {
        return proxyView;
    }

    public boolean isAttachView() {
        if (this.weakView != null && this.weakView.get() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void attachView(V view) {
        this.weakView = new WeakReference<V>(view);
        ViewInvocationHandler invocationHandler = new ViewInvocationHandler(this.weakView.get());

        //在这里采用动态代理
        proxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), invocationHandler);
    }

    @Override
    public void detachView() {
        if (weakView.get() != null) {
            this.weakView.clear();
            this.weakView = null;
        }
    }

    private class ViewInvocationHandler implements InvocationHandler {
        private IBaseView mvpView;

        public ViewInvocationHandler(IBaseView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isAttachView()) {
                return method.invoke(mvpView, args);
            }
            return null;
        }
    }

}
