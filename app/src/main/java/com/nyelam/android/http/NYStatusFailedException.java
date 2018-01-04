package com.nyelam.android.http;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYStatusFailedException extends Exception{
    private String code;
    private String error;
    private String message;

    public NYStatusFailedException() {
    }

    public NYStatusFailedException(String detailMessage) {
        super(detailMessage);
    }

    public NYStatusFailedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYStatusFailedException(Throwable throwable) {
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
