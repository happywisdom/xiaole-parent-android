package com.aibasis.parent.network.api.entity.relationship;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/25.
 */
public class DeviceEntity {

    private String remark;

    private String deviceId;

    private String easeId;

    private DeviceEntity() {}

    public static DeviceEntity parse(JSONObject object) throws JSONException {

        DeviceEntity entity = new DeviceEntity();

        if (!object.isNull("remark")) {
            entity.remark = object.getString("remark");
        }

        if (!object.isNull("deviceId")) {
            entity.deviceId = object.getString("deviceId");
        }

        if (!object.isNull("easeId")) {
            entity.easeId = object.getString("easeId");
        }

        return entity;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getEaseId() {
        return this.easeId;
    }
}
