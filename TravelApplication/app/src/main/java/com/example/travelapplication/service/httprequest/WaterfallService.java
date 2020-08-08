package com.example.travelapplication.service.httprequest;

import com.example.travelapplication.model.WaterfallBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface WaterfallService {
//    @GET("{type}/{count}/{page}")
//    Observable<WaterfallBean> getWaterfallData(@Path("type") String type,
//                                               @Path("count") int count,
//                                               @Path("page") int page);

    @GET()
    Observable<WaterfallBean> getPicture();
}
