package com.aibasis.parent.network.api.entity.relationship;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/25.
 */
public class BindDeviceResult {

    private String result;

    private String remark;

    private BindDeviceResult(){}

    public static BindDeviceResult parse(String json) throws JSONException{

        JSONObject object = new JSONObject(json);
        BindDeviceResult result = new BindDeviceResult();

        if (object.isNull("result")) {
            result.setResult(object.getString("result"));
        }

        if (object.isNull("remark")) {
            result.setRemark(object.getString("remark"));
        }

        return result;
    }

    public String getResult() {
        return this.result;
    }

    public String getRemark() {
        return this.remark;
    }

    private void setResult(String result) {
        this.result = result;
    }

    private void setRemark(String remark) {
        this.remark = remark;
    }
}
