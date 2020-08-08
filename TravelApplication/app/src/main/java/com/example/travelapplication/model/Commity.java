package com.example.travelapplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commity implements Serializable {
    private Integer commityId;

    private String commityContent;

    @JsonProperty(value = "commityTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commityTime;

    private User user;

    private TravelStrategy travelStrategy;

    private List<Reply> replyList = new ArrayList<>();

    public Integer getCommityId() {
        return commityId;
    }

    public void setCommityId(Integer commityId) {
        this.commityId = commityId;
    }

    public String getCommityContent() {
        return commityContent;
    }

    public void setCommityContent(String commityContent) {
        this.commityContent = commityContent;
    }

    public Date getCommityTime() {
        return commityTime;
    }

    public void setCommityTime(Date commityTime) {
        this.commityTime = commityTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TravelStrategy getTravelStrategy() {
        return travelStrategy;
    }

    public void setTravelStrategy(TravelStrategy travelStrategy) {
        this.travelStrategy = travelStrategy;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }
}
