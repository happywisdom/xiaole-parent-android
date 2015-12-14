package com.aibasis.parent.network.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import org.apache.http.HttpHost;

/**
 * Created by gexiao2 on 2015/11/24.
 */
public class NetStateManager {
    private static Context mContext;
    public static NetStateManager.NetState CUR_NETSTATE;

    static {
        CUR_NETSTATE = NetStateManager.NetState.Mobile;
    }

    public NetStateManager() {
    }

    public static HttpHost getAPN() {
        HttpHost proxy = null;
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        Cursor mCursor = null;
        if(mContext != null) {
            mCursor = mContext.getContentResolver().query(uri, (String[])null, (String)null, (String[])null, (String)null);
        }

        if(mCursor != null && mCursor.moveToFirst()) {
            String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
            if(proxyStr != null && proxyStr.trim().length() > 0) {
                proxy = new HttpHost(proxyStr, 80);
            }

            mCursor.close();
        }

        return proxy;
    }

    public static enum NetState {
        Mobile,
        WIFI,
        NOWAY;

        private NetState() {
        }
    }

    public class NetStateReceive extends BroadcastReceiver {
        public NetStateReceive() {
        }

        public void onReceive(Context context, Intent intent) {
            NetStateManager.mContext = context;
            if("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
                WifiInfo info = wifiManager.getConnectionInfo();
                if(!wifiManager.isWifiEnabled() || -1 == info.getNetworkId()) {
                    NetStateManager.CUR_NETSTATE = NetStateManager.NetState.Mobile;
                }
            }

        }
    }
}
