package com.example.travelapplication.service.business;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.AreaBusinessHttpRequestService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AreaBusinessService extends BusinessService{
    public void getAreaByCityId(final OnResultListener onResultListener, Integer cityId){
        Map<String, Object> params = new HashMap<>();
        params.put("city_id", cityId);
        getArea(onResultListener, params);
    }

    public void getArea(final OnResultListener onResultListener, Map<String, Object> params){
        AreaBusinessHttpRequestService areaBusinessHttpRequestService = createRetrofit().create(AreaBusinessHttpRequestService.class);

        String url = BuildConfig.BASE_URL+"/area/getAreaByCityId";

        Call<Iterable<Object>> call = areaBusinessHttpRequestService.getArea(url, params);

        call.enqueue(new Callback<Iterable<Object>>() {
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
