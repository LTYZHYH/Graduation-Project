package com.example.travelapplication.service.httprequest;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LoginHttpRequestService {
    @POST
    Call<Map<String, Object>> login(@Url String url, @Body RequestBody requestBody);

}
