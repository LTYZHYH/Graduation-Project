package com.example.travelapplication.service.business;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.RetrofitUtils;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.TravelStrategyHttpRequestService;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelStrategyBusinessService extends BusinessService {
    public void getTravelStrategyByCityId(final OnResultListener onResultListener, BaseFilterCondition travelStrategyFilterCondition, Integer cityId,String sort){
        Map<String,Object> params = new HashMap<>();
        params.put("cityId", cityId);
        params.put("sort", sort);
        if (travelStrategyFilterCondition.getPageSize() != null){
            params.put("size",travelStrategyFilterCondition.getPageSize());
        }
        if (travelStrategyFilterCondition.getPageNum() != null){
            params.put("number",travelStrategyFilterCondition.getPageNum());
        }

        String url = BuildConfig.BASE_URL+"/travelstrategy/getStrategyByCityId";

        TravelStrategyHttpRequestService travelStrategyHttpRequestService = refreshTokenThenReturnRetrofit().create(TravelStrategyHttpRequestService.class);
        Call<Page<TravelStrategy>> travelStrategyCall = travelStrategyHttpRequestService.getTravelStrategyByCityId(url,params);
        travelStrategyCall.enqueue(new Callback<Page<TravelStrategy>>() {
            @Override
            public void onResponse(Call<Page<TravelStrategy>> call, Response<Page<TravelStrategy>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<TravelStrategy>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void searchTravelStrategyByLike(final OnResultListener onResultListener,BaseFilterCondition travelStrategyFilterCondition,Integer cityId,String like,String sort){
        Map<String,Object> params = new HashMap<>();
        params.put("cityId", cityId);
        params.put("like", like);
        params.put("sort", sort);
        if (travelStrategyFilterCondition.getPageSize() != null){
            params.put("size",travelStrategyFilterCondition.getPageSize());
        }
        if (travelStrategyFilterCondition.getPageNum() != null){
            params.put("number",travelStrategyFilterCondition.getPageNum());
        }

        String url = BuildConfig.BASE_URL+"/travelstrategy/getStrategyByLike";

        TravelStrategyHttpRequestService travelStrategyHttpRequestService = refreshTokenThenReturnRetrofit().create(TravelStrategyHttpRequestService.class);
        Call<Page<TravelStrategy>> travelStrategyCall = travelStrategyHttpRequestService.searchTravelStrategyByLike(url,params);
        travelStrategyCall.enqueue(new Callback<Page<TravelStrategy>>() {
            @Override
            public void onResponse(Call<Page<TravelStrategy>> call, Response<Page<TravelStrategy>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<TravelStrategy>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void getStrategyDatail(final OnResultListener onResultListener,Integer strategyId){
        Map<String, Object> params = new HashMap<>();
        params.put("strategyId", strategyId);
        String url = BuildConfig.BASE_URL+"/travelstrategy/getStrategyDetila";

        TravelStrategyHttpRequestService travelStrategyHttpRequestService = createRetrofit().create(TravelStrategyHttpRequestService.class);
        Call<TravelStrategy> travelStrategyCall = travelStrategyHttpRequestService.getStrategyDatail(url,params);
        travelStrategyCall.enqueue(new Callback<TravelStrategy>() {
            @Override
            public void onResponse(Call<TravelStrategy> call, Response<TravelStrategy> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<TravelStrategy> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void addFavoriteStrategy(final OnResultListener onResultListener,Integer strategyId){
        Map<String,Object> params = new HashMap<>();
        params.put("strategy_id", strategyId);
        String url = BuildConfig.BASE_URL + "/favorite/addFavorite";
        TravelStrategyHttpRequestService travelStrategyHttpRequestService = refreshTokenThenReturnRetrofit().create(TravelStrategyHttpRequestService.class);
        Call<ResponseBody> mapCall = travelStrategyHttpRequestService.addFavoriteStrategy(url, RetrofitUtils.buildJsonRequestBody(params));
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
