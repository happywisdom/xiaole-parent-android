package com.aibasis.parent.network.api.message;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class IMManager {

    private static IMManager manager = new IMManager();

    public static final String MESSAGE_TYPE = "messageType";

    public static final String MESSAGE_CONTENT = "messageContent";

    private IMManager(){}

    public static IMManager getManager() {
        return manager;
    }

    public String getNewMessageBroadcastAction() {
        return "com.aibasis.im.newmsg";
    }
}
