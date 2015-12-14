package com.aibasis.parent.network.api.relationship;

import android.content.Context;
import com.aibasis.parent.network.api.AbstractAPI;
import com.aibasis.parent.network.http.APIParameters;
import com.aibasis.parent.network.http.RequestListener;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class RelationshipAPI extends AbstractAPI{

    private static final String SERVER_URL_PRIX = API_SERVER + "/relationship";

    public static final String ROLE_FATHER = "father";

    public static final String ROLE_MOTHER = "mother";

    public static final String ROLE_GRANDFATHER = "grandfather";

    public static final String ROLE_GRANDMOTHER = "grandmother";

    public RelationshipAPI(Context context) {
        super(context);
    }

    public void bindDevice(String parentId,String deviceKey,String role,RequestListener listener) {
        APIParameters params = new APIParameters();
        params.put("parentId",parentId);
        params.put("deviceKey",deviceKey);
        params.put("role",role);
        requestAsync(SERVER_URL_PRIX + "/bind_device", params, HTTP_METHOD_POST, listener);
    }

    public void listBoundDevice(String parentId,RequestListener listener) {
        APIParameters params = new APIParameters();
        params.put("parentId", parentId);
        requestAsync(SERVER_URL_PRIX + "/list_bound_device", params, HTTP_METHOD_POST, listener);
    }
}
