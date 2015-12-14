package com.aibasis.parent.network.netty.command.response;

import com.aibasis.parent.network.netty.util.CommandType;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class IMResponseCommand extends AbstractResponseCommand implements Serializable{

    private String messageType;

    private String messageContent;

    private String deviceId;

    private String parentId;

    @Override
    public String encodeToJson() throws Exception {

        JSONObject object = new JSONObject();

        object.put("cmd", CommandType.IM);
        object.put("messageType",messageType);
        object.put("messageContent",messageContent);
        object.put("deviceId",deviceId);
        object.put("parentId",parentId);
        object.put("messageId",System.currentTimeMillis());
        object.put("timestamp",System.currentTimeMillis());

        return object.toString();
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
