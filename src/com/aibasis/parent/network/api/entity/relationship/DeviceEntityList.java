package com.aibasis.parent.network.api.entity.relationship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gexiao2 on 2015/11/25.
 */
public class DeviceEntityList {

    private List<DeviceEntity> deviceEntityList = new ArrayList<DeviceEntity>();

    private DeviceEntityList(){}

    public static DeviceEntityList parse(String json) throws JSONException {

        JSONArray array = new JSONArray(json);

        DeviceEntityList list = new DeviceEntityList();

        for (int i=0;i<array.length();i++) {
            JSONObject object = array.getJSONObject(i);
            DeviceEntity entity = DeviceEntity.parse(object);
            list.add(entity);
        }

        return list;
    }

    private void add(DeviceEntity deviceEntity) {
        deviceEntityList.add(deviceEntity);
    }

    public List<DeviceEntity> getDeviceEntityList() {
        return this.deviceEntityList;
    }
}
