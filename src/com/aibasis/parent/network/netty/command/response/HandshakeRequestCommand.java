package com.aibasis.parent.network.netty.command.response;
import org.json.JSONObject;

/**
 * Created by gexiao2 on 2015/7/24.
 */
public class HandshakeRequestCommand extends AbstractResponseCommand {

    private String deviceId;

    private String terminal;

    private String osVersion;

    private String model;

    private String resolution;

    private double protocolVersion;

    @Override
    public String encodeToJson() throws Exception {
        JSONObject object = new JSONObject();

        object.put("cmd","handshake");
        object.put("deviceId", deviceId);
        object.put("terminal", terminal);
        object.put("osVersion", osVersion);
        object.put("model", model);
        object.put("resolution", resolution);
        object.put("protocolVersion", protocolVersion);

        return object.toString();
    }

    public void setDeviceId(String memberId) {
        this.deviceId = memberId;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setProtocolVersion(double protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
