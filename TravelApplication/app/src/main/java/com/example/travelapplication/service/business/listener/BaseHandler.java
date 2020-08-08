package com.example.travelapplication.service.business.listener;

import android.content.Intent;

import com.example.travelapplication.activity.login.LoginActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.exception.ThrowableException;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.service.valueobject.ErrorResponse;

import retrofit2.Response;

import static com.example.travelapplication.infrastructure.utils.DecideErrorUtils.isNetWorkError;

public abstract class BaseHandler implements OnResultListener{

    private BaseView baseView;
    private LoadingView loadingView;

    public BaseHandler(BaseView baseView, LoadingView loadingView) {
        this(baseView);
        this.loadingView = loadingView;
    }

    public BaseHandler(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onError(Object object) {
        if (loadingView != null) {
            loadingView.finishLoading();
            loadingView.finishRefresh();
        }

        if (object instanceof Response) {
            this.onResponseError((Response) object);
        } else if (isNetWorkError(object)) {
            onNetworkError();
        } else if (object instanceof Throwable) {
            throw new ThrowableException((Throwable) object);
        } else {
            throw new UnsupportedOperationException("");
        }
    }

    protected void onResponseError(Response response) {
        switch (response.code()) {
            case 302:
                on302Found(response);
                break;
            case 401:
                on401Error(response);
                break;
            case 404:
                on404Error(response);
                break;
            case 422:
                on422Error(response);
                break;
            default:
                onDefaultError(response);
                break;
        }
    }

    protected void onDefaultError(Response response) {
        showResponseMessage(response);
    }

    protected void on302Found(Response response){
        SnackBarUtils.showWarningMessage(baseView.getAnchorView(),"已经收藏该攻略");
    }

    protected void on401Error(Response response) {
        Intent intent = new Intent();
        intent.setClass(baseView.getAnchorView().getContext(), LoginActivity.class);
        baseView.getAnchorView().getContext().startActivity(intent);
    }

    protected void on404Error(Response response) {
        showResponseMessage(response);
    }

    protected void on422Error(Response response) {
        SnackBarUtils.showErrorMessage(baseView.getAnchorView(), new ErrorResponse(response).getErrorResponseMessage());
    }

    protected void onNetworkError() {
        SnackBarUtils.showErrorMessage(baseView.getAnchorView(), "当前无网络或网络连接不稳定，请检查网络后再试");
    }

    private void showResponseMessage(Response response) {
        SnackBarUtils.showErrorMessage(baseView.getAnchorView(), new ErrorResponse(response).getErrorResponseMessage());
    }
}
