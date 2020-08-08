package com.example.travelapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private Integer id;

    private String userName;

    private String userTelenumber;

    private String userPhoto;

    private String userEmail;

    private String userPassword;

    private String userSalt;

    private List<TravelStrategy> travelStrategies = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTelenumber() {
        return userTelenumber;
    }

    public void setUserTelenumber(String userTelenumber) {
        this.userTelenumber = userTelenumber;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public List<TravelStrategy> getTravelStrategies() {
        return travelStrategies;
    }

    public void setTravelStrategies(List<TravelStrategy> travelStrategies) {
        this.travelStrategies = travelStrategies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return userName;
    }
}
