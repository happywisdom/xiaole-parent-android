package com.aibasis.parent.network.exception;

/**
 * Created by gexiao2 on 2015/11/24.
 */
public class APIHttpException extends APIException{

    private final int statusCode;

    public APIHttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
