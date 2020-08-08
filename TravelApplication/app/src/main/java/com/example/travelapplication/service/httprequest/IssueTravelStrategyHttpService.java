package com.example.travelapplication.service.httprequest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IssueTravelStrategyHttpService {
    @POST
    Call<ResponseBody> upLoad(@Url String url, @Body RequestBody Body);
}
