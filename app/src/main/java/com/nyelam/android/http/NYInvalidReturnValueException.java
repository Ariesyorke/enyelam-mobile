package com.nyelam.android.http;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYInvalidReturnValueException extends Exception {

    public NYInvalidReturnValueException() {
    }

    public NYInvalidReturnValueException(String detailMessage) {
        super(detailMessage);
    }

    public NYInvalidReturnValueException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NYInvalidReturnValueException(Throwable throwable) {
        super(throwable);
    }

}
