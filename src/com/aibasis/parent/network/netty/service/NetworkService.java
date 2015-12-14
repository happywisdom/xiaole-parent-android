package com.aibasis.parent.network.netty.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.aibasis.parent.DemoApplication;
import com.aibasis.parent.network.api.message.IMManager;
import com.aibasis.parent.network.netty.client.NetworkHandler;
import com.aibasis.parent.network.netty.client.OverTimeHandler;
import com.aibasis.parent.network.netty.command.request.IMRequestCommand;
import com.aibasis.parent.network.netty.command.response.*;
import com.aibasis.parent.network.netty.protocol.NetworkEventCodec;
import com.aibasis.parent.utils.SharePreferenceUtil;
import com.aibasis.parent.utils.ThreadManager;
import com.aibasis.parent.utils.WakeLockWrapper;
import com.hovans.android.service.ServiceUtil;
import com.hovans.android.service.WorkerService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Random;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class NetworkService extends WorkerService{

    private static boolean isConnectAlreadyScheduled = false;
    private static final String HOST = "101.200.182.114";
   // private static final String HOST = "192.168.11.185";
    private static final int PORT = 5001;

    private final static int readerIdleTimeSeconds = 15;
    private final static int writerIdleTimeSeconds = 5;
    private final static int allIdleTimeSeconds = 20;

    private Channel mChannel;
    private NetworkHandler handler = new NetworkHandler(NetworkService.this);

    private boolean activeKillConnection = false;

    @Override
    public String getWorkerTag() {
        return NetworkService.class.getSimpleName();
    }

    public void onCreate() {
        super.onCreate();
        start();
    }

    @Override
    public void onWorkerRequest(Intent intent, int i) {
        if (NetworkIntent.ACTION_CONNECT_SESSION.equals(intent.getAction())) {
            if (mChannel != null) {
                //disconnectSessionIfItNeeds();
            }
            connectSessionIfItNeeds();
        } else if (NetworkIntent.ACTION_HEARTBEAT.equals(intent.getAction())) {
            if (checkConnection() == false) {
                connectSessionIfItNeeds();
            }
        } else if (NetworkIntent.ACTION_CHECK_SESSION.equals(intent.getAction())) {
            scheduleToReconnect();
        } else if (NetworkIntent.ACTION_DISCONNECT_SESSION.equals(intent.getAction())) {
           // activeKillConnection = true;
            disconnectSessionIfItNeeds();
        } else if (NetworkIntent.ACTION_IM_MESSAGE.equals(intent.getAction())) {
            IMResponseCommand cmd = (IMResponseCommand) intent.getSerializableExtra("requestCmd");
            handler.sendCommand(cmd);
        }
    }

    /**
     * Session
     */
    boolean checkConnection() {
        boolean result = false;
        if (mChannel != null && mChannel.isActive() == true) {
            //If it needs you should send a packet through the channel.
            result = true;
        }

        return result;
    }

    /**
     * connection.
     */
    void disconnectSessionIfItNeeds() {
        mChannel.disconnect();
    }

    /**
     * Schedule a reconnect event by using {@link android.app.AlarmManager}
     */
    public void scheduleToReconnect() {
        if (NetworkService.isConnectAlreadyScheduled == true) {
            return;
        }

        if (INTERVAL_RECONNECT_EXPONENTIAL_BACKOFF < INTERVAL_RECONNECT_MAXIMUM) {
            INTERVAL_RECONNECT_EXPONENTIAL_BACKOFF += new Random().nextInt(1000);

            ServiceUtil.startSchedule(this, NetworkIntent.ACTION_CONNECT_SESSION, INTERVAL_RECONNECT_EXPONENTIAL_BACKOFF);
            INTERVAL_RECONNECT_EXPONENTIAL_BACKOFF *= 2;
            NetworkService.isConnectAlreadyScheduled = true;
        }
    }

    void connectSessionIfItNeeds() {
        if (checkConnection() == false) {
            //start();
            //ServiceUtil.startSchedule(this, NettyIntent.ACTION_CHECK_SESSION, INTERVAL_WAIT_FOR_RESPONSE);
            //ServiceUtil.stopSchedule(this, NettyIntent.ACTION_HEARTBEAT);
        }
    }

    public void reconnect() {
        if (!activeKillConnection) {
            start();
        }
    }

    public void reconnect(final AbstractResponseCommand cmd) {
        ThreadManager.offer(new Runnable() {

            @Override
            public void run() {
                PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(NetworkService.this, getWorkerTag());
                wakeLock.acquire();
                try {
                    NioEventLoopGroup group = new NioEventLoopGroup();

                    final Bootstrap bootstrap = new Bootstrap();

                    bootstrap.channel(NioSocketChannel.class);

                    ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast("idleStateHandler",new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
                            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 0));
                            channel.pipeline().addLast(new NetworkEventCodec(NetworkService.this));
                            channel.pipeline().addLast(handler);
                            channel.pipeline().addLast(new OverTimeHandler(NetworkService.this));
                        }
                    };

                    bootstrap.handler(initializer);

                    bootstrap.group(group);

                    ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST,
                            PORT));

                    mChannel = future.channel();

                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                handler.sendCommand(cmd);
                            } else {
                                Log.i("jijun-test","failed");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    wakeLock.release();
                }
            }
        });
    }

    void start() {
        ThreadManager.offer(new Runnable() {

            @Override
            public void run() {
                PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(NetworkService.this, getWorkerTag());
                wakeLock.acquire();
                try {
                    Log.i("gexiao", "onCreate service enter try");

                    activeKillConnection = false;
                    NioEventLoopGroup group = new NioEventLoopGroup();

                    final Bootstrap bootstrap = new Bootstrap();

                    bootstrap.channel(NioSocketChannel.class);

                    ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast("idleStateHandler",new IdleStateHandler(readerIdleTimeSeconds,writerIdleTimeSeconds,allIdleTimeSeconds));
                            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 0));
                            channel.pipeline().addLast(new NetworkEventCodec(NetworkService.this));
                            channel.pipeline().addLast(handler);
                            channel.pipeline().addLast(new OverTimeHandler(NetworkService.this));
                        }
                    };

                    bootstrap.handler(initializer);

                    bootstrap.group(group);

                    ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST,
                            PORT));

                    mChannel = future.channel();

                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                HandshakeResponseCommand cmd = new HandshakeResponseCommand();
                                SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(NetworkService.this);
                                cmd.setParentId(sharePreferenceUtil.getParentId());
                                handler.sendCommand(cmd);
                            } else {
                                channelFuture.cause().printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    wakeLock.release();
                }
            }
        });
    }

    static final long INTERVAL_WAIT_FOR_RESPONSE = 30 * 1000;

    static final long INTERVAL_RECONNECT_MINIMUM = 10 * 1000;

    static long INTERVAL_RECONNECT_EXPONENTIAL_BACKOFF = INTERVAL_RECONNECT_MINIMUM;

    static final long INTERVAL_RECONNECT_MAXIMUM = 30 * 60 * 1000;
}
