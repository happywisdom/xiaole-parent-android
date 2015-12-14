package com.aibasis.parent.network.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.aibasis.parent.network.netty.command.response.IMResponseCommand;
import com.aibasis.parent.network.netty.service.NetworkIntent;

/**
 * Created by sniper on 2015/11/28.
 */
public class SendMessageUtil {

    public static void sendIMMessage(Context context, IMResponseCommand cmd) {
        Intent intent = new Intent();
        intent.setAction(NetworkIntent.ACTION_IM_MESSAGE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("requestCmd", cmd);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void sendHandshakeMessage() {

    }

    public static void sendHeartBeatMessage() {

    }
}
