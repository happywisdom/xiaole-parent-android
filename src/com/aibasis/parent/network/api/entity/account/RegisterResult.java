package com.aibasis.parent.network.api.entity.account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/24.
 */
public class RegisterResult {

    private String result;

    private String parentId;

    private String easeId;

    private String easePassword;

    public static final String SUCCESS = "success";

    public static final String USERNAME_EXISTS = "username_exists";

    private RegisterResult() {
    }

    public static RegisterResult parse(String json) throws JSONException {
        JSONObject object = new JSONObject(json);

        RegisterResult result = new RegisterResult();

        if (!object.isNull("result")) {
            result.setResult(object.getString("result"));
        }

        if (!object.isNull("parentId")) {
            result.setParentId(object.getString("parentId"));
        }

        if(!object.isNull("easeId")) {
            result.setEaseId(object.getString("easeId"));
        }

        if(!object.isNull("easePassword")) {
            result.setEasePassword(object.getString("easePassword"));
        }

        return result;
    }

    public String getResult() {
        return this.result;
    }

    public String getParentId() {
        return this.parentId;
    }

    private void setResult(String result) {
        this.result = result;
    }

    private void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEaseId() {
        return easeId;
    }

    public void setEaseId(String easeId) {
        this.easeId = easeId;
    }

    public String getEasePassword() {
        return easePassword;
    }

    public void setEasePassword(String easePassword) {
        this.easePassword = easePassword;
    }
}
