package com.aibasis.parent.network.netty.command.response;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gexiao2 on 2015/7/24.
 */
public class QARequestCommand extends AbstractResponseCommand implements Serializable {

    private String sendContent;

    private String sendType;

    @Override
    public String encodeToJson() throws Exception {
        JSONObject object = new JSONObject();
        object.put("cmd", "QA");
        object.put("deviceId", "1234567");
        object.put("sendContent", sendContent);
        object.put("sendType", sendType);
        //object.put("packageId", 0);
        object.put("packageId", System.currentTimeMillis());
        object.put("version", "public");

        return object.toString();
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}
