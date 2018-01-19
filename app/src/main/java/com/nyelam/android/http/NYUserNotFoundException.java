package com.nyelam.android.http;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/19/2018.
 */

public class NYUserNotFoundException extends Exception {
    public NYUserNotFoundException() {
    }

    public NYUserNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public NYUserNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYUserNotFoundException(Throwable throwable) {
        super(throwable);
    }
}