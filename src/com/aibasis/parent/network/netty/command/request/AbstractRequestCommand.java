package com.aibasis.parent.network.netty.command.request;

import android.content.Context;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public abstract class AbstractRequestCommand implements Serializable{

    public abstract void decodeJson(String json) throws Exception;

    /**
     *
     * @param handlerContext
     * @param context
     */
    public abstract void execute(ChannelHandlerContext handlerContext, Context context);
}
