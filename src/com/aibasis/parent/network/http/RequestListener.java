package com.aibasis.parent.network.http;

import com.aibasis.parent.network.exception.APIException;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public interface RequestListener {

    void onComplete(String result);

    void onAPIException(APIException exception);
}
