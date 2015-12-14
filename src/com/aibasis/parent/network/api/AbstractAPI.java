package com.aibasis.parent.network.api;

import android.content.Context;
import com.aibasis.parent.network.http.APIParameters;
import com.aibasis.parent.network.http.AsyncAPIRunner;
import com.aibasis.parent.network.http.RequestListener;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public abstract class AbstractAPI {
    protected static final String API_SERVER = "http://101.200.182.114:8888/parent";

    protected static final String HTTP_METHOD_POST = "POST";

    protected static final String HTTP_METHOD_GET = "GET";

    protected Context context;

    public AbstractAPI(Context context) {
        this.context = context;
    }

    protected void requestAsync(String url,APIParameters params,String httpMethod,RequestListener listener) {
        new AsyncAPIRunner(context).requestAsync(url,params,httpMethod,listener);
    }
}
