package com.aibasis.parent.network.netty.protocol;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import com.aibasis.parent.network.netty.command.request.AbstractRequestCommand;
import com.aibasis.parent.network.netty.command.request.IMRequestCommand;
import com.aibasis.parent.network.netty.command.request.RequestCommandFactory;
import com.aibasis.parent.network.netty.command.response.AbstractResponseCommand;
import com.aibasis.parent.network.netty.util.ZipUtil;
import com.aibasis.parent.utils.WakeLockWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class NetworkEventCodec extends ByteToMessageCodec<AbstractResponseCommand> {

    private Context mContext;

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public NetworkEventCodec(Context context) {
        mContext = context;
    }

    @Override
    protected void encode(ChannelHandlerContext context, AbstractResponseCommand cmd, ByteBuf byteBuf) throws Exception {

        PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(mContext, NetworkEventCodec.class.getSimpleName());
        wakeLock.acquire();

        try {
            String jsonBody = cmd.encodeToJson();

            int length = jsonBody.getBytes(UTF8).length;
            byteBuf.writeInt(length);

            byte[] bytes = jsonBody.getBytes(UTF8);
            byteBuf.writeBytes(bytes);

            Log.i("jijun-size","" + (4 + bytes.length));
        } finally {
            wakeLock.release();
        }

//        try {
//            String jsonBody = cmd.encodeToJson();
//
//            System.out.println("jsonBody:" + jsonBody.getBytes(Charset.forName("ISO-8859-1")).length + " " + jsonBody);
//
//            String zipJsonBody = ZipUtil.compress(jsonBody);
//
//            System.out.println("zipJsonBody size:" + zipJsonBody.getBytes(Charset.forName("ISO-8859-1")));
//
//            int length = zipJsonBody.getBytes(Charset.forName("ISO-8859-1")).length;
//
//            //
//            byteBuf.writeInt(length + 8);
//
//            //
//            byteBuf.writeInt(0);
//
//            //
//            byte[] bytes = zipJsonBody.getBytes(Charset.forName("ISO-8859-1"));
//            byteBuf.writeBytes(bytes);
//
//            //
//            byteBuf.writeInt(0);
//        } finally {
//            wakeLock.release();
//        }
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(mContext, NetworkEventCodec.class.getSimpleName());
        wakeLock.acquire();

        Log.i("jijun-decode","decode");
//        IMRequestCommand cmd = new IMRequestCommand();
//        cmd.setMessageContent("asdasd");
//        list.add(cmd);
//        return;

        try {
            int readerIndex = byteBuf.readerIndex();

            int bodyLength = 0;

            int packageLength = 0;

            int headerLength = 4;

            if (byteBuf.readableBytes() < headerLength) {
                return;
            } else {
                bodyLength = byteBuf.getInt(readerIndex);
                packageLength = bodyLength + headerLength;
            }

            if (packageLength > headerLength) {
                if (byteBuf.readableBytes() < packageLength) {
                    return;
                } else {
                    ByteBuf bodyBytes = byteBuf.slice(readerIndex + 4, bodyLength);

                    String content = bodyBytes.toString(UTF8);

                    JSONObject json = new JSONObject(content);

                    String cmdId = json.getString("cmd");

                    AbstractRequestCommand cmd = RequestCommandFactory.newCommand(cmdId);

                    if (cmd != null) {
                        cmd.decodeJson(content);
                        list.add(cmd);
                    }
                    byteBuf.skipBytes(packageLength);
                    return;
                }
            }
            if (bodyLength <= 0) { //Unrecoverable error, have to close connection.
                context.close();
            }
        } finally {
            wakeLock.release();
        }
    }

}
