package com.aibasis.parent.network.netty.command.request;

import android.content.Context;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class HeartbeatRequestCommand extends AbstractRequestCommand{

    @Override
    public void decodeJson (String json) throws Exception {

    }

    @Override
    public void execute(ChannelHandlerContext handlerContext, Context context){

    }
}
