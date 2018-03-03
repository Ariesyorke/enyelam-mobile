package com.nyelam.android.http;

/**
 * Created by bobi on 3/3/18.
 */

public class NYCartExpiredException extends Exception {
    private String code;
    private String error;
    private String message = "Sorry, Your Cart Session has Expired. Please Re-Order";

    public NYCartExpiredException() {
    }

    public NYCartExpiredException(String detailMessage) {
        super(detailMessage);
    }

    public NYCartExpiredException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYCartExpiredException(Throwable throwable) {
        super(throwable);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
