package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.Commity;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.Reply;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.ReplyHttpRequestService;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyBusinessService extends BusinessService{
    public void sendReply(final OnResultListener onResultListener,Integer commentId,String replyContent){
        Map<String,Object> params = new HashMap<>();
        params.put("commity_id", commentId);
        params.put("reply_content", replyContent);
        String url = Global_Variable.IP + "/reply/replys";
        ReplyHttpRequestService replyHttpRequestService = refreshTokenThenReturnRetrofit().create(ReplyHttpRequestService.class);
        Call<ResponseBody> call = replyHttpRequestService.reply(url,params);
        call.enqueue(new Callback<ResponseBody>() {
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

    public void getReply(final OnResultListener onResultListener, Integer commentId, BaseFilterCondition replyFilterCondition){
        Map<String,Object> parmas = new HashMap<>();
        parmas.put("commityId", commentId);
        if (replyFilterCondition.getPageNum() != null){
            parmas.put("number",replyFilterCondition.pageNum);
        }
        if (replyFilterCondition.getPageSize() != null){
            parmas.put("size",replyFilterCondition.pageSize);
        }

        String url = Global_Variable.IP + "/reply/getReply";
        ReplyHttpRequestService replyHttpRequestService = createRetrofit().create(ReplyHttpRequestService.class);
        Call<Page<Reply>> call = replyHttpRequestService.getReply(url,parmas);
        call.enqueue(new Callback<Page<Reply>>() {
            @Override
            public void onResponse(Call<Page<Reply>> call, Response<Page<Reply>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Reply>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void getOneComment(final OnResultListener onResultListener, Integer commentId){
        Map<String,Object> parmas = new HashMap<>();
        parmas.put("commentId", commentId);

        String url = Global_Variable.IP + "/commity/getOneComment";
        ReplyHttpRequestService replyHttpRequestService = createRetrofit().create(ReplyHttpRequestService.class);
        Call<Commity> call = replyHttpRequestService.getOneComment(url,parmas);
        call.enqueue(new Callback<Commity>() {
            @Override
            public void onResponse(Call<Commity> call, Response<Commity> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Commity> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
