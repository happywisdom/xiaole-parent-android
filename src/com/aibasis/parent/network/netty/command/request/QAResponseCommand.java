package com.aibasis.parent.network.netty.command.request;

import android.content.Context;
import android.util.Log;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/7/24.
 */
public class QAResponseCommand extends AbstractRequestCommand {

    private String replyContent;

    private String replyType;

    private String replyExtra;

    private String replyEmoji;

    private String replyVoiceRole;

    private String replyModule;

    private String delay;

    private long packageId = -1;

    @Override
    public void decodeJson(String json) throws Exception {
        Log.i("jijun-test",json);

        JSONObject object = new JSONObject(json);
        if(!object.isNull("replyContent")) {
            replyContent = object.getString("replyContent");
        } else {
            replyContent = "";
        }
        replyType = object.getString("replyType");

        if (!object.isNull("replyExtra")) {
            Log.d("gexiao2", "QAResponseCommand replyExtra is not null");
            replyExtra = object.getString("replyExtra");
        }

        if (!object.isNull("replyEmoji")) {
            replyEmoji = object.getString("replyEmoji");
        }

        if (!object.isNull("replyVoiceRole")) {
            replyVoiceRole = object.getString("replyVoiceRole");
        }

        if (!object.isNull("packageId")) {
            packageId = object.getLong("packageId");
        }

        if (!object.isNull("replyModule")) {
            replyModule = object.getString("replyModule");
        }

        if(!object.isNull("reply")) {
            delay = object.getString("reply");
        }
    }

    @Override
    public void execute(ChannelHandlerContext handlerContext, Context context) {
//        Intent serviceIntent = new Intent();
//        serviceIntent.setAction("com.aibasis.ce.ui.ReceiveMessageUI");
//        serviceIntent.putExtra("type", MessageBroadcastReceiverIntent.QA_RESPONSE_TYPE);
//        serviceIntent.putExtra("replyContent", replyContent);
//        serviceIntent.putExtra("replyType", replyType);
//        serviceIntent.putExtra("packageId", packageId);
//
//        if (replyExtra != null) {
//            serviceIntent.putExtra("replyExtra", replyExtra);
//        }
//
//        if (replyEmoji!=null) {
//            serviceIntent.putExtra("replyEmoji",replyEmoji);
//        }
//
//        if (replyVoiceRole != null) {
//            serviceIntent.putExtra("replyVoiceRole", replyVoiceRole);
//        }
//
//        if (replyModule != null) {
//            serviceIntent.putExtra("replyModule",replyModule);
//        }
//
//        if(delay != null) {
//            serviceIntent.putExtra("delay",delay);
//        }
//
//        context.sendBroadcast(serviceIntent);
    }
}
