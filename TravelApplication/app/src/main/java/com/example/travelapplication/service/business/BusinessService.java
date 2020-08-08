package com.example.travelapplication.service.business;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.cache.CacheManager;
import com.example.travelapplication.cache.LruCacheWrapper;
import com.example.travelapplication.exception.ThrowableException;
import com.example.travelapplication.exception.TokenRefreshFailedException;
import com.example.travelapplication.infrastructure.utils.JwtUtils;
import com.example.travelapplication.infrastructure.utils.RetrofitUtils;
import com.example.travelapplication.service.httprequest.UserHttpRequestService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class BusinessService {

    static LruCacheWrapper<String, Object> cache = CacheManager.getInstance();

    public Retrofit refreshTokenThenReturnRetrofit() {
        refreshToken();
        return createRetrofit();
    }

    public Retrofit onlyCreatRetrofit(){
        refreshToken();
        return new Retrofit.Builder().
                baseUrl(BuildConfig.BASE_URL)
                .client(RetrofitUtils.genericClient())
                .build();
    }

    public Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(RetrofitUtils.genericClient())
                .build();
    }

    private void refreshToken() {
        if (JwtUtils.isTokenNeedRefresh()) {
            UserHttpRequestService httpRequestService = createRetrofit().create(UserHttpRequestService.class);

            Call<Map<String, Object>> tokenCall = httpRequestService.refreshToken();

            tokenCall.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.code() == 200) {
                        JwtUtils.setToken((String) response.body().get("token"));
                    } else {
                        throw new TokenRefreshFailedException(response);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    throw new ThrowableException(t);
                }
            });
        }
    }
}
