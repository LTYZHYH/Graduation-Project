package com.example.travelapplication.model;

import java.io.Serializable;

public class TravelStrategyBean implements Serializable {

    private Integer strategyId;
    private String area;
    private String theme;
    private String strategyPicture1;

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getStrategyPicture1() {
        return strategyPicture1;
    }

    public void setStrategyPicture1(String strategyPicture1) {
        this.strategyPicture1 = strategyPicture1;
    }
}
