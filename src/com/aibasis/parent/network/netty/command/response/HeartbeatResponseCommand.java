package com.aibasis.parent.network.netty.command.response;

import android.content.Context;
import com.aibasis.parent.network.netty.util.CommandType;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class HeartbeatResponseCommand extends AbstractResponseCommand{

    @Override
    public String encodeToJson() throws Exception {
        JSONObject object = new JSONObject();

        object.put("cmd", CommandType.HEARTBEAT);

        return object.toString();
    }
}
