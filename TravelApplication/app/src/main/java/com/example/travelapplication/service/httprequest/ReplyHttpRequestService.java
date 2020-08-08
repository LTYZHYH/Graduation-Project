package com.example.travelapplication.service.httprequest;

import com.example.travelapplication.model.Commity;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.Reply;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ReplyHttpRequestService {
    @POST
    Call<ResponseBody> reply(@Url String url, @Body Map<String, Object> registerBody);
    @GET
    Call<Page<Reply>> getReply(@Url String url, @QueryMap Map<String, Object> params);
    @GET
    Call<Commity> getOneComment(@Url String url, @QueryMap Map<String, Object> params);
}
