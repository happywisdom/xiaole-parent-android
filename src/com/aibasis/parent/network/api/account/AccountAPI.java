package com.aibasis.parent.network.api.account;

import android.content.Context;
import com.aibasis.parent.network.api.AbstractAPI;
import com.aibasis.parent.network.http.APIParameters;
import com.aibasis.parent.network.http.RequestListener;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class AccountAPI extends AbstractAPI{

    private static final String SERVER_URL_PRIX = API_SERVER + "/account";

    public AccountAPI(Context context) {
        super(context);
    }

    public void register(String username,String password,RequestListener listener) {
        APIParameters params = new APIParameters();
        params.put("username",username);
        params.put("password",password);
        requestAsync(SERVER_URL_PRIX+"/register",params,HTTP_METHOD_POST,listener);
    }

    public void login(String username,String password,RequestListener listener) {
        APIParameters params = new APIParameters();
        params.put("username",username);
        params.put("password",password);
        requestAsync(SERVER_URL_PRIX+"/login",params,HTTP_METHOD_POST,listener);
    }
}
