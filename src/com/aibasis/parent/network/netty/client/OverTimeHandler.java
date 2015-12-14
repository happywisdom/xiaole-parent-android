package com.aibasis.parent.network.netty.client;

import com.aibasis.parent.network.netty.command.response.HeartbeatResponseCommand;
import com.aibasis.parent.network.netty.service.NetworkService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by sniper on 2015/11/30.
 */
public class OverTimeHandler extends ChannelInboundHandlerAdapter {

    private NetworkService mService;

    public OverTimeHandler(NetworkService service) {
        this.mService = service;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception{
        if(IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state().equals(IdleState.READER_IDLE)) {
                this.mService.reconnect();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                HeartbeatResponseCommand cmd = new HeartbeatResponseCommand();
                ctx.writeAndFlush(cmd);
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                this.mService.reconnect();
            }
        }
    }

}
