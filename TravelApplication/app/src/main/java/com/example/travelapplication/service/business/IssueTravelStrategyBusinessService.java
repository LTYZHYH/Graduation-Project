package com.example.travelapplication.service.business;

import static com.example.travelapplication.infrastructure.utils.TimeFormatUtils.getFormatDatetime;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.exception.ThrowableException;
import com.example.travelapplication.exception.TokenRefreshFailedException;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.JwtUtils;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.IssueTravelStrategyHttpService;
import com.example.travelapplication.service.httprequest.UserHttpRequestService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueTravelStrategyBusinessService extends BusinessService {
    public void IssueTravelStrategy(final OnResultListener onResultListener, TravelStrategy travelStrategy, Integer cityId){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType((MultipartBody.FORM));
        builder.addFormDataPart("theme",travelStrategy.getTheme())
                .addFormDataPart("area", "area")
                .addFormDataPart("cityId", cityId.toString())
                .addFormDataPart("strategyContent", travelStrategy.getStrategyContent())
                .addFormDataPart("cost", "cost")
                .addFormDataPart("overheadCost", "cost")
                .addFormDataPart("travelDays", "7")
                .addFormDataPart("scenicNumber", "100")
                .addFormDataPart("issueTime1", getFormatDatetime(travelStrategy.getIssueTime()));
        if (travelStrategy.getStrategyPicture1() != null && !travelStrategy.getStrategyPicture1().isEmpty()){
            File img = new File(travelStrategy.getStrategyPicture1());
            builder.addFormDataPart("file1", img.getName(), RequestBody.create(MediaType.parse("image/*"), img));
        }
        //if (imgPath2 != null){
        //    File img2 = new File(imgPath2);
        //    builder.addFormDataPart("file2", img2.getName(),RequestBody.create(MediaType.parse("image/*"), img2));
        //}
        //if (imgPath3 != null){
        //    File img3 = new File(imgPath3);
        //    builder.addFormDataPart("file3", img3.getName(),RequestBody.create(MediaType.parse("image/*"), img3));
        //}
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

        String url = BuildConfig.BASE_URL+"/travelstrategy/issueStrategy";

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

    public void uploadStrategyPic(final OnResultListener onResultListener, String imgPath){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType((MultipartBody.FORM));
        if (imgPath != null){
            File img = new File(imgPath);
            builder.addFormDataPart("file", img.getName(), RequestBody.create(MediaType.parse("image/*"), img));
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

        String url = BuildConfig.BASE_URL+"/travelstrategy/uploadStrategyPic";

        Call<ResponseBody> call = issueTravelStrategyHttpService.upLoad(url, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    try {
                        onResultListener.onSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
