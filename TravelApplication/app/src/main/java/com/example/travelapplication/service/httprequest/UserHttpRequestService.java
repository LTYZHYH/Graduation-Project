package com.example.travelapplication.service.httprequest;

import com.example.travelapplication.model.Favorite;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.model.User;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface UserHttpRequestService {
    @PUT("/login")
    Call<Map<String, Object>> refreshToken();
    @GET
    Call<User> getUserInformation(@Url String url);
    @GET
    Call<Page<TravelStrategy>> getMyStrategy(@Url String url, @QueryMap Map<String, Object> params);
    @GET
    Call<Page<Favorite>> getMyFavorite(@Url String url, @QueryMap Map<String, Object> params);
    @POST
    Call<ResponseBody> addUserPic(@Url String url, @Body RequestBody Body);
    @POST
    Call<ResponseBody> cancelFavorite(@Url String url, @Body RequestBody Body);
    @POST
    Call<ResponseBody> upData(@Url String url, @Body RequestBody requestBody);
}
