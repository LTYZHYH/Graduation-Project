package com.example.travelapplication.service.httprequest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RegisterHttpRequestService {
    @POST
    Call<ResponseBody> register(@Url String url, @Body Map<String, Object> registerBody);
}
