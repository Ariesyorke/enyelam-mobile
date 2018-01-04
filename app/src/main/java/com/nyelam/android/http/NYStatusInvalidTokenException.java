package com.nyelam.android.http;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYStatusInvalidTokenException extends Exception {
    public NYStatusInvalidTokenException() {
    }

    public NYStatusInvalidTokenException(String detailMessage) {
        super(detailMessage);
    }

    public NYStatusInvalidTokenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYStatusInvalidTokenException(Throwable throwable) {
        super(throwable);
    }
}