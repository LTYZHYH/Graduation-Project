package com.example.travelapplication.service.business;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.Commity;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.CommentHttpRequestService;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentBusinessService extends BusinessService{
    public void issueComment(final OnResultListener onResultListener, String comment, Integer strategyId){
        Map<String,Object> params = new HashMap<>();
        params.put("comment", comment);
        params.put("strategy_id", strategyId);

        String url = BuildConfig.BASE_URL + "/commity/comment";

        CommentHttpRequestService commentHttpRequestService = refreshTokenThenReturnRetrofit().create(CommentHttpRequestService.class);
        Call<ResponseBody> commentCall = commentHttpRequestService.comment(url,params);
        commentCall.enqueue(new Callback<ResponseBody>() {
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

    public void getComment(final OnResultListener onResultListener, BaseFilterCondition CommentFilterCondition,Integer strategyId){
        Map<String,Object> params = new HashMap<>();
        params.put("strategyId", strategyId);
        params.put("sort", "commityId");
        if (CommentFilterCondition.getPageSize() != null){
            params.put("size",CommentFilterCondition.getPageSize());
        }
        if (CommentFilterCondition.getPageNum() != null){
            params.put("number",CommentFilterCondition.getPageNum());
        }

        String url = BuildConfig.BASE_URL + "/commity/getComment";

        CommentHttpRequestService commentHttpRequestService = createRetrofit().create(CommentHttpRequestService.class);
        Call<Page<Commity>> commityCall = commentHttpRequestService.getComment(url,params);
        commityCall.enqueue(new Callback<Page<Commity>>() {
            @Override
            public void onResponse(Call<Page<Commity>> call, Response<Page<Commity>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Commity>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
