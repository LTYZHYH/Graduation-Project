package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.RetrofitUtils;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.ReportHttpRequestService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportBusinessService extends BusinessService {
    public void reportStrategy(final OnResultListener onResultListener,Integer strateId,String reason){
        Map<String,Object> map = new HashMap<>();
        map.put("strategy_id", strateId);
        map.put("strategy_reason", reason);
        ReportHttpRequestService reportHttpRequestService = createRetrofit().create(ReportHttpRequestService.class);
        String url = Global_Variable.IP+"/report/reportStrategy";
        Call<ResponseBody> mapCall = reportHttpRequestService.reportStrategy(url, RetrofitUtils.buildJsonRequestBody(map));
        mapCall.enqueue(new Callback<ResponseBody>() {
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

    public void report(final OnResultListener onResultListener,Integer strateId,String rurl){
        Map<String,Object> map = new HashMap<>();
        map.put("id", strateId);
        ReportHttpRequestService reportHttpRequestService = createRetrofit().create(ReportHttpRequestService.class);
        String url = Global_Variable.IP+"/report/"+rurl;
        Call<ResponseBody> mapCall = reportHttpRequestService.report(url, RetrofitUtils.buildJsonRequestBody(map));
        mapCall.enqueue(new Callback<ResponseBody>() {
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
}
