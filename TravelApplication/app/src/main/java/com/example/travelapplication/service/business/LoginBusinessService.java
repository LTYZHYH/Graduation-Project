package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.RetrofitUtils;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.LoginHttpRequestService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginBusinessService extends BusinessService{

    public void loginByPassword(final OnResultListener onResultListener, String userEmail, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_email", userEmail);
        params.put("password", password);
        login(onResultListener, params);
    }

    private void login(final OnResultListener onResultListener, Map<String, Object> params) {
        LoginHttpRequestService loginHttpRequestService = createRetrofit().create(LoginHttpRequestService.class);

        String url = Global_Variable.IP+"/user/login";

        Call<Map<String, Object>> mapCall = loginHttpRequestService.login(url, RetrofitUtils.buildJsonRequestBody(params));

        mapCall.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body().get("token"));
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
