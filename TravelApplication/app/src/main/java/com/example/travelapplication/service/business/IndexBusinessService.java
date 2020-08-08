package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.IndexHttpRequestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexBusinessService extends BusinessService{
    public void getIndexData(final OnResultListener onResultListener){

        IndexHttpRequestService indexHttpRequestService = createRetrofit().create(IndexHttpRequestService.class);

        String url = Global_Variable.IP + "/city/initIndex";

        Call<Iterable<Object>> cityPictureBeanCall = indexHttpRequestService.getPicture(url);

        cityPictureBeanCall.enqueue(new Callback<Iterable<Object>>() {
            @Override
            public void onResponse(Call<Iterable<Object>> call, Response<Iterable<Object>> response) {
                if (response.code() == 200){
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Iterable<Object>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
