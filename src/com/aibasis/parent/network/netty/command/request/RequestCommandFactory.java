package com.aibasis.parent.network.netty.command.request;

import com.aibasis.parent.network.netty.util.CommandType;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by gexiao2 on 2015/11/26.
 */
public class RequestCommandFactory {

    private static Map<String, Class<? extends AbstractRequestCommand>> registry = new Hashtable<>();

    static {
        register(CommandType.HEARTBEAT,HeartbeatRequestCommand.class);
        register(CommandType.IM,IMRequestCommand.class);
    }

    public static void register(String cmdId, Class<? extends AbstractRequestCommand> clazz) {

        if (clazz != null) {
            registry.put(cmdId, clazz);
        }
    }

    public static AbstractRequestCommand newCommand(String cmdId) {
        Class<? extends AbstractRequestCommand> clazz = registry.get(cmdId);

        if (clazz != null) {
            if (AbstractRequestCommand.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

}
