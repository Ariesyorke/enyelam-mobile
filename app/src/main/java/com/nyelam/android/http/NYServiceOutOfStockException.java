package com.nyelam.android.http;

/**
 * Created by bobi on 3/3/18.
 */

public class NYServiceOutOfStockException extends Exception {
    private String code;
    private String error;
    private String message = "Sorry, Schedule not available or out of stock";

    public NYServiceOutOfStockException() {
    }

    public NYServiceOutOfStockException(String detailMessage) {
        super(detailMessage);
    }

    public NYServiceOutOfStockException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYServiceOutOfStockException(Throwable throwable) {
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
