package com.aibasis.parent.network.netty.client;

import android.os.PowerManager;
import android.util.Log;
import com.aibasis.parent.network.netty.command.request.AbstractRequestCommand;
import com.aibasis.parent.network.netty.command.response.AbstractResponseCommand;
import com.aibasis.parent.network.netty.command.response.HandshakeRequestCommand;
import com.aibasis.parent.network.netty.service.NetworkService;
import com.aibasis.parent.utils.WakeLockWrapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by gexiao2 on 2015/11/26.
 */
public class NetworkHandler extends SimpleChannelInboundHandler<AbstractRequestCommand> {

    private NetworkService mService;

    private ChannelHandlerContext mHandlerContext;

    public NetworkHandler(NetworkService service) {
        this.mService = service;
    }

    public void sendCommand(final AbstractResponseCommand cmd) {
        PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(mService, NetworkHandler.class.getSimpleName());
        wakeLock.acquire();

        mHandlerContext.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                ChannelFuture future = mHandlerContext.writeAndFlush(cmd);

                future.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            Log.i("jijun-test-service", "command has sent:" + cmd.encodeToJson());
                        } else {
                            mService.reconnect(cmd);
                        }
                    }
                });
            }
        });

        wakeLock.release();
    }

    public void resendCommand(final AbstractResponseCommand cmd, final HandshakeRequestCommand handshake) {
        PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(mService, NetworkHandler.class.getSimpleName());
        wakeLock.acquire();

        mHandlerContext.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                ChannelFuture future = mHandlerContext.writeAndFlush(handshake);

                future.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            mHandlerContext.writeAndFlush(cmd);
                        } else {
                           Log.i("jijun-test","resend failed");
                        }
                    }
                });
            }
        });

        wakeLock.release();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext handlerContext, AbstractRequestCommand cmd) throws Exception {
        Log.i("jijun-test","receive channel");
        cmd.execute(handlerContext, mService);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.i("jijun-test-service","channelRegistered");
        this.mHandlerContext = ctx;
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //mService.reconnect();
        Log.i("jijun-test-service", "channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
