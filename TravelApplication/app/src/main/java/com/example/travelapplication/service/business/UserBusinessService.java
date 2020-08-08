package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.RetrofitUtils;
import com.example.travelapplication.model.Favorite;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.model.User;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.UserHttpRequestService;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBusinessService extends BusinessService{
    public void getUserInformation(final OnResultListener onResultListener){
        String url = Global_Variable.IP + "/user/info";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<User> userCall = userHttpRequestService.getUserInformation(url);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void getMyStrategy(final OnResultListener onResultListener, BaseFilterCondition myStrategyFilterCondition){
        Map<String,Object> params = new HashMap<>();
        if (myStrategyFilterCondition.getPageSize() != null){
            params.put("size",myStrategyFilterCondition.getPageSize());
        }
        if (myStrategyFilterCondition.getPageNum() != null){
            params.put("number",myStrategyFilterCondition.getPageNum());
        }
        params.put("sort", "strategyId,desc");

        String url = Global_Variable.IP + "/user/getMyStrategy";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<Page<TravelStrategy>> userCall = userHttpRequestService.getMyStrategy(url,params);
        userCall.enqueue(new Callback<Page<TravelStrategy>>() {
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

    public void getMyFavorite(final OnResultListener onResultListener, BaseFilterCondition favoriteFilterCondition){
        Map<String,Object> params = new HashMap<>();
        if (favoriteFilterCondition.getPageSize() != null){
            params.put("size",favoriteFilterCondition.pageSize);
        }
        if (favoriteFilterCondition.getPageNum() != null){
            params.put("number",favoriteFilterCondition.pageNum);
        }
        params.put("sort","favoriteId");
        String url = Global_Variable.IP + "/favorite/getMyfavoriteStrategy";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<Page<Favorite>> call = userHttpRequestService.getMyFavorite(url, params);
        call.enqueue(new Callback<Page<Favorite>>() {
            @Override
            public void onResponse(Call<Page<Favorite>> call, Response<Page<Favorite>> response) {
                if (response.code() == 200){
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Favorite>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void addUserPic(final OnResultListener onResultListener,String userPhoto){
        File file = new File(userPhoto);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("image/*"),file)).build();
        String url = Global_Variable.IP + "/user/addUserPic";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<ResponseBody> call = userHttpRequestService.addUserPic(url,requestBody);
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

    public void cancelFavorite(final OnResultListener onResultListener, Integer strateId){
        Map<String,Object> params = new HashMap<>();
        params.put("strategy_id", strateId);
        String url = Global_Variable.IP + "/favorite/cancelFavorite";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<ResponseBody> call = userHttpRequestService.cancelFavorite(url, RetrofitUtils.buildJsonRequestBody(params));
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

    public void upData (final OnResultListener onResultListener,String newName,String newEmail,String newPhone){
        Map<String,Object> map = new HashMap<>();
        map.put("new_user_name", newName);
        map.put("new_user_email", newEmail);
        map.put("new_user_phone", newPhone);
        String url = Global_Variable.IP + "/user/upData";
        UserHttpRequestService userHttpRequestService = refreshTokenThenReturnRetrofit().create(UserHttpRequestService.class);
        Call<ResponseBody> call = userHttpRequestService.upData(url,RetrofitUtils.buildJsonRequestBody(map));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 202) {
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
