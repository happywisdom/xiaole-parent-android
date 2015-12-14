package com.aibasis.parent.network.exception;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class APIException extends RuntimeException{

    public APIException(){}

    public APIException(String message){
        super(message);
    }

    public APIException(String detailMessage,Throwable throwable) {
        super(detailMessage,throwable);
    }

    public APIException(Throwable throwable) {
        super(throwable);
    }

}
