package com.example.travelapplication.service.business;

import com.example.travelapplication.exception.ThrowableException;
import com.example.travelapplication.exception.TokenRefreshFailedException;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.JwtUtils;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.IssueTravelStrategyHttpService;
import com.example.travelapplication.service.httprequest.UserHttpRequestService;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueTravelStrategyBusinessService extends BusinessService {
    public void IssueTravelStrategy(final OnResultListener onResultListener,String theme, String area, String cost,Integer cityId,
                                    String travelDay, String scenicNumber, String issueTime, String strategyContent,
                                    String imgPath1, String imgPath2, String imgPath3){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType((MultipartBody.FORM));
        builder.addFormDataPart("theme",theme)
                .addFormDataPart("area", area)
                .addFormDataPart("cityId", cityId.toString())
                .addFormDataPart("strategyContent", strategyContent)
                .addFormDataPart("cost", cost)
                .addFormDataPart("overheadCost", cost)
                .addFormDataPart("travelDays", travelDay)
                .addFormDataPart("scenicNumber", scenicNumber)
                .addFormDataPart("issueTime1", issueTime);
        if (imgPath1 != null){
            File img = new File(imgPath1);
            builder.addFormDataPart("file1", img.getName(), RequestBody.create(MediaType.parse("image/*"), img));
        }
        if (imgPath2 != null){
            File img2 = new File(imgPath2);
            builder.addFormDataPart("file2", img2.getName(),RequestBody.create(MediaType.parse("image/*"), img2));
        }
        if (imgPath3 != null){
            File img3 = new File(imgPath3);
            builder.addFormDataPart("file3", img3.getName(),RequestBody.create(MediaType.parse("image/*"), img3));
        }
        RequestBody requestBody = builder.build();

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

        IssueTravelStrategyHttpService issueTravelStrategyHttpService = onlyCreatRetrofit().create(IssueTravelStrategyHttpService.class);

        String url = Global_Variable.IP+"/travelstrategy/issueStrategy";

        Call<ResponseBody> call = issueTravelStrategyHttpService.upLoad(url, requestBody);
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
}
