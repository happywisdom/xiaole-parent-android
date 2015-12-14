package com.aibasis.parent.network.netty.command.response;

import com.aibasis.parent.network.netty.util.CommandType;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gexiao2 on 2015/11/27.
 */
public class HandshakeResponseCommand extends AbstractResponseCommand implements Serializable{

    private String parentId;

    @Override
    public String encodeToJson() throws Exception {
        JSONObject object = new JSONObject();

        object.put("cmd", CommandType.HANDSHAKE);
        object.put("parentId", parentId);

        return object.toString();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String toString(){
        return this.parentId;
    }
}
