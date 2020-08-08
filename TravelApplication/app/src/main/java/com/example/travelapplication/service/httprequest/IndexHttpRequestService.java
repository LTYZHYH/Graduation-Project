package com.example.travelapplication.service.httprequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IndexHttpRequestService {
    @GET
    Call<Iterable<Object>> getPicture(@Url String url);
}
