package com.aibasis.parent.network.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class APIParameters {

    private Map<String,Object> params = new LinkedHashMap<String, Object>();

    public String encodeUrl() {
        return null;
    }

    public String encodeToJson() {
        JSONObject object = new JSONObject();

        Iterator<String> iterator = params.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                object.put(key,params.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return object.toString();
    }

    public void put(String key,String value) {
        params.put(key,value);
    }

    public void put(String key,long value) {
        params.put(key,value);
    }

    public void put(String key,int value) {
        params.put(key,value);
    }

}
