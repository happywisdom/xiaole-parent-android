package com.aibasis.parent.network.netty.command.request;

import android.content.Context;
import android.content.Intent;
import com.aibasis.parent.DemoApplication;
import com.aibasis.parent.service.UploadOssIntent;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class IMRequestCommand extends AbstractRequestCommand{

    private String deviceId;

    private String messageType;

    private String messageContent;

    private long messageId;

    private long timestamp;

    @Override
    public void decodeJson(String json) throws Exception {

        JSONObject object = new JSONObject(json);

        if (!object.isNull("deviceId")) {
            deviceId = object.getString("deviceId");
        }

        if (!object.isNull("messageType")) {
            messageType = object.getString("messageType");
        }

        if (!object.isNull("messageContent")) {
            messageContent = object.getString("messageContent");
        }

        if (!object.isNull("messageId")) {
            messageId = object.getLong("messageId");
        }

        if (!object.isNull("timestamp")) {
            timestamp = object.getLong("timestamp");
        }
    }

    @Override
    public void execute(ChannelHandlerContext handlerContext, Context context){
        //TODO
        System.out.println("收到");
        Intent intent = new Intent();
        intent.setAction(UploadOssIntent.ACTION_DOWNLOADVOICE);
        intent.putExtra("from","863175020954307");
        intent.putExtra("url",messageContent);
        context.startService(intent);
    }

}
