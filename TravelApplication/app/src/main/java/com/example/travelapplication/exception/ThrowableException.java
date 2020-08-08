package com.example.travelapplication.exception;

/**
 * Created by zhengda on 2018/1/9.
 */

public class ThrowableException extends RuntimeException {

    private Throwable sourceException;
    public ThrowableException(Throwable throwable){
        this.sourceException = throwable;
    }

    public Throwable getSourceException() {
        return sourceException;
    }
}
