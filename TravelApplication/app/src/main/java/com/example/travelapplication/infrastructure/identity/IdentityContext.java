package com.example.travelapplication.infrastructure.identity;

import com.example.travelapplication.exception.UserInfoNotExistException;
import com.example.travelapplication.model.User;

public class IdentityContext {

    private User user;

    private volatile static IdentityContext identityContext;

    private IdentityContext(){

    }

    public static IdentityContext getInstance() {
        if (identityContext == null) {
            synchronized (IdentityContext.class) {
                if (identityContext == null) {
                    identityContext = new IdentityContext();
                }
            }
        }
        return identityContext;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public User getCurrentUser() {
        if (user == null) {
            throw new UserInfoNotExistException();
        }
        return user;
    }

    public User getUploadLogUser() {
        return user;
    }
}
