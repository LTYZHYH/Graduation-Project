package com.example.travelapplication.model;

import java.io.Serializable;

public class Food implements Serializable {
    private Integer foodId;
    private String foodName;
    private String foodPhoto;
    private String foodIntroduction;
    private Area area;

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
    }

    public String getFoodIntroduction() {
        return foodIntroduction;
    }

    public void setFoodIntroduction(String foodIntroduction) {
        this.foodIntroduction = foodIntroduction;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
