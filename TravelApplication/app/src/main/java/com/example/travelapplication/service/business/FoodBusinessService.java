package com.example.travelapplication.service.business;

import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.Food;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.httprequest.FoodHttpRequestService;
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

public class FoodBusinessService extends BusinessService{
    public void getFood(final OnResultListener onResultListener, BaseFilterCondition foodFilterCondition){
        Map<String,Object> params = new HashMap<>();
        if (foodFilterCondition.getPageSize() != null){
            params.put("size",foodFilterCondition.pageSize);
        }
        if (foodFilterCondition.getPageNum() != null){
            params.put("number",foodFilterCondition.pageNum);
        }
        String url = Global_Variable.IP + "/food/getAllFood";
        FoodHttpRequestService foodHttpRequestService = createRetrofit().create(FoodHttpRequestService.class);
        Call<Page<Food>> call = foodHttpRequestService.getAllFood(url,params);
        call.enqueue(new Callback<Page<Food>>() {
            @Override
            public void onResponse(Call<Page<Food>> call, Response<Page<Food>> response) {
                if (response.code() == 200){
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Food>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void addFood(final OnResultListener onResultListener,String foodName,String foodIntroduction,String areaName,String foodPic){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType((MultipartBody.FORM));
        builder.addFormDataPart("foodName",foodName)
                .addFormDataPart("areaName", areaName)
                .addFormDataPart("foodIntroduction", foodIntroduction);
        if (foodPic != null){
            File img = new File(foodPic);
            builder.addFormDataPart("foodPhoto", img.getName(), RequestBody.create(MediaType.parse("image/*"), img));
        }
        RequestBody requestBody = builder.build();
        String url = Global_Variable.IP + "/food/serve";
        FoodHttpRequestService foodHttpRequestService = refreshTokenThenReturnRetrofit().create(FoodHttpRequestService.class);
        Call<ResponseBody> call = foodHttpRequestService.addFood(url,requestBody);
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

    public void searchFood(final OnResultListener onResultListener, BaseFilterCondition foodFilterCondition,String foodName,String areaName){
        Map<String,Object> params = new HashMap<>();
        if (foodFilterCondition.getPageSize() != null){
            params.put("size",foodFilterCondition.pageSize);
        }
        if (foodFilterCondition.getPageNum() != null){
            params.put("number",foodFilterCondition.pageNum);
        }
        if (foodName != null){
            params.put("foodName",foodName);
        }
        if (areaName != null){
            params.put("areaName",areaName);
        }
        String url = Global_Variable.IP + "/food/getFoodBySearch";
        FoodHttpRequestService foodHttpRequestService = createRetrofit().create(FoodHttpRequestService.class);
        Call<Page<Food>> call = foodHttpRequestService.getFoodByLike(url,params);
        call.enqueue(new Callback<Page<Food>>() {
            @Override
            public void onResponse(Call<Page<Food>> call, Response<Page<Food>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Food>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }

    public void searchFoodByArea(final OnResultListener onResultListener, BaseFilterCondition foodFilterCondition,String areaName){
        Map<String,Object> params = new HashMap<>();
        if (foodFilterCondition.getPageSize() != null){
            params.put("size",foodFilterCondition.pageSize);
        }
        if (foodFilterCondition.getPageNum() != null){
            params.put("number",foodFilterCondition.pageNum);
        }
        params.put("areaName",areaName);
        String url = Global_Variable.IP + "/food/getFoodByAreaName";
        FoodHttpRequestService foodHttpRequestService = createRetrofit().create(FoodHttpRequestService.class);
        Call<Page<Food>> call = foodHttpRequestService.getFoodByArea(url,params);
        call.enqueue(new Callback<Page<Food>>() {
            @Override
            public void onResponse(Call<Page<Food>> call, Response<Page<Food>> response) {
                if (response.code() == 200) {
                    onResultListener.onSuccess(response.body());
                } else {
                    onResultListener.onError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<Food>> call, Throwable t) {
                onResultListener.onError(t);
            }
        });
    }
}
