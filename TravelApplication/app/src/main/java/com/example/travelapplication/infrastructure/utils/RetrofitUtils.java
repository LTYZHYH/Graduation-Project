package com.example.travelapplication.infrastructure.utils;

import com.example.travelapplication.exception.ThrowableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RetrofitUtils {
    public static OkHttpClient genericClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Jwt-Token", JwtUtils.getToken())
                        .build();

                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }

    public static RequestBody buildJsonRequestBody(Map<String, Object> requestBody) {
        String jsonBody;
        try {
            jsonBody = new ObjectMapper().writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new ThrowableException(e);
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    }

    public static Map<String, RequestBody> buildFormRequestBody(Map<String, String> requestDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key) == null ? "" : requestDataMap.get(key));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }

}
