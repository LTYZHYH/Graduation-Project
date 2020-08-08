package com.example.travelapplication.service.httprequest;

import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface TravelStrategyHttpRequestService {
    @GET
    Call<Page<TravelStrategy>> getTravelStrategyByCityId(@Url String url,  @QueryMap Map<String, Object> params);
    @GET
    Call<Page<TravelStrategy>> searchTravelStrategyByLike(@Url String url, @QueryMap Map<String, Object> params);
    @GET
    Call<TravelStrategy> getStrategyDatail(@Url String url, @QueryMap Map<String, Object> params);
    @POST
    Call<ResponseBody> addFavoriteStrategy(@Url String url, @Body RequestBody requestBody);
}
