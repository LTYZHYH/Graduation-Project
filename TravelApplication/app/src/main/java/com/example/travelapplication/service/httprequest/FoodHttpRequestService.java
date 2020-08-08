package com.example.travelapplication.service.httprequest;

import com.example.travelapplication.model.Food;
import com.example.travelapplication.model.Page;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface FoodHttpRequestService {
    @GET
    Call<Page<Food>> getAllFood(@Url String url,@QueryMap Map<String, Object> params);
    @GET
    Call<Page<Food>> getFoodByArea(@Url String url, @QueryMap Map<String, Object> params);
    @GET
    Call<Page<Food>> getFoodByLike(@Url String url, @QueryMap Map<String, Object> params);
    @POST
    Call<ResponseBody> addFood(@Url String url, @Body RequestBody Body);
}
