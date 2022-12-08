package com.example.travelapplication.service.business;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.RegisterHttpRequestService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RegisterBusinessService extends BusinessService{
    public void registerNewUser(final OnResultListener onResultListener, String email, String useName,String userTelenumber,String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_email", email);
        params.put("password", password);
        params.put("user_name", useName);
        params.put("user_telenumber", userTelenumber);
        register(onResultListener, params);
    }

    private void register(final OnResultListener onResultListener, Map<String, Object> params) {
        RegisterHttpRequestService registerHttpRequestService = createRetrofit().create(RegisterHttpRequestService.class);

        String url = BuildConfig.BASE_URL+"/user/register";

        Call<ResponseBody> registerCall = registerHttpRequestService.register(url, params);

        registerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
