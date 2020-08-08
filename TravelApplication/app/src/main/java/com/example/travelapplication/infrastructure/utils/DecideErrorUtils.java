package com.example.travelapplication.infrastructure.utils;

import android.view.View;

import com.example.travelapplication.service.valueobject.ErrorResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

public class DecideErrorUtils {
    public static void showErrorMessage(View view, Object object) {
        if (object instanceof Response) {
            SnackBarUtils.showErrorMessage(view, new ErrorResponse((Response) object).getErrorResponseMessage());
        } else if (isNetWorkError(object)) {
            SnackBarUtils.showErrorMessage(view, "当前无网络或网络连接不稳定，请检查网络后再试");
        } else {
            SnackBarUtils.showErrorMessage(view, "未处理的错误，请联系服务人员");
        }
    }

    public static Boolean isNetWorkError(Object object) {
        return object instanceof SocketTimeoutException
                || object instanceof ConnectException || object instanceof UnknownHostException;
    }
}
