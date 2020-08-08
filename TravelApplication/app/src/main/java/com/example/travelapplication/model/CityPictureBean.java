package com.example.travelapplication.model;

import java.io.Serializable;

//省份（当初起名字的时候没有想好。。。所以写成了city）
public class CityPictureBean implements Serializable {
    //图片标题
    private Integer cityId;
    private String cityTitle;
    private String cityPicture;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCityPicture() {
        return cityPicture;
    }

    public void setCityPicture(String cityPicture) {
        this.cityPicture = cityPicture;
    }
}
