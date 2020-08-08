package com.example.travelapplication.service.httprequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface AreaBusinessHttpRequestService {
    @GET
    Call<Iterable<Object>> getArea(@Url String url, @QueryMap Map<String,Object> areaMap);
}
