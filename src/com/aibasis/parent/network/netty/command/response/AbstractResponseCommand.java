package com.aibasis.parent.network.netty.command.response;

import android.content.Context;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public abstract class AbstractResponseCommand {

    public abstract String encodeToJson() throws Exception;
}

