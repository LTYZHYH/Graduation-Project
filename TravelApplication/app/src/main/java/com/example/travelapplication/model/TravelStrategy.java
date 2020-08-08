package com.example.travelapplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class TravelStrategy implements Serializable {

    private Integer strategyId;

    private String area;//地区

    private String theme;//主题

    private String travelDays;//几日游

    private String strategyContent;//攻略

    private String overheadCost;//开销

    private String scenicNumber;//景点数

    private Integer strategyAudit;//判断该攻略是否过审

    @JsonProperty(value = "issueTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueTime;

    private String strategyPicture1;

    private String strategyPicture2;

    private String strategyPicture3;

    private User user;

    private CityPictureBean cityPictureBean;

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

    public String getTravelDays() {
        return travelDays;
    }

    public void setTravelDays(String travelDays) {
        this.travelDays = travelDays;
    }

    public String getStrategyContent() {
        return strategyContent;
    }

    public void setStrategyContent(String strategyContent) {
        this.strategyContent = strategyContent;
    }

    public String getOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(String overheadCost) {
        this.overheadCost = overheadCost;
    }

    public String getScenicNumber() {
        return scenicNumber;
    }

    public void setScenicNumber(String scenicNumber) {
        this.scenicNumber = scenicNumber;
    }

    public Integer getStrategyAudit() {
        return strategyAudit;
    }

    public void setStrategyAudit(Integer strategyAudit) {
        this.strategyAudit = strategyAudit;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getStrategyPicture1() {
        return strategyPicture1;
    }

    public void setStrategyPicture1(String strategyPicture1) {
        this.strategyPicture1 = strategyPicture1;
    }

    public String getStrategyPicture2() {
        return strategyPicture2;
    }

    public void setStrategyPicture2(String strategyPicture2) {
        this.strategyPicture2 = strategyPicture2;
    }

    public String getStrategyPicture3() {
        return strategyPicture3;
    }

    public void setStrategyPicture3(String strategyPicture3) {
        this.strategyPicture3 = strategyPicture3;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CityPictureBean getCityPictureBean() {
        return cityPictureBean;
    }

    public void setCityPictureBean(CityPictureBean cityPictureBean) {
        this.cityPictureBean = cityPictureBean;
    }
}
