package com.aibasis.parent.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import com.aibasis.parent.DemoApplication;
import com.aibasis.parent.network.netty.command.response.IMResponseCommand;
import com.aibasis.parent.network.netty.util.CommandType;
import com.aibasis.parent.network.utils.SendMessageUtil;
import com.aibasis.parent.utils.MessageType;
import com.aibasis.parent.utils.SharePreferenceUtil;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.*;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.VoiceMessageBody;
import org.jivesoftware.smack.packet.Packet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sniper on 2015/11/27.
 */
public class UploadOssService extends Service {

    private static final String TAG = "UploadOssService";

    private final IBinder mBinder = new LocalBinder();

    private OSSService ossService;
    private OSSBucket bucket;

    private String urlStr;
    private String from;

    @Override
    public void onCreate() {
        super.onCreate();
        initOssService();
        Log.i("jijun-oss","initOss");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
            return START_NOT_STICKY;

        String action = intent.getAction();

        Log.i("jijun-oss","onstartCommand " + action);

        if(UploadOssIntent.ACTION_UPLOADVOICE.equals(action)) {
            String fileName = intent.getStringExtra("FilePath");
            Log.i("jijun-oss","fileName " + fileName);
            if(!TextUtils.isEmpty(fileName)) {
                syncUpload(fileName);
            }
        } else if (UploadOssIntent.ACTION_DOWNLOADVOICE.equals(action)) {
            Log.i("jijun-do","receive action_downloadVoice");
            urlStr = intent.getStringExtra("url");
            from = intent.getStringExtra("from");
            downLoad();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }


    public class LocalBinder extends Binder {
        public LocalBinder(){
        }

        UploadOssService getService() {
            return UploadOssService.this;
        }
    }

    private void initOssService () {
        OSSLog.enableLog();

        ossService = OSSServiceProvider.getService();

        ossService.setApplicationContext(getBaseContext());
        ossService.setGlobalDefaultHostId("oss-cn-beijing.aliyuncs.com");

        ossService.setGlobalDefaultACL(AccessControlList.PRIVATE);
        ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);

        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() {
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                return OSSToolKit.generateToken("Kywj58hCJKOSQTSk", "tnQmCz77jZdxr5OBBqKb4lWx4fRziC", content);
            }
        });
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(30 * 1000);
        conf.setSocketTimeout(30 * 1000);
        conf.setMaxConcurrentTaskNum(5);
        conf.setIsSecurityTunnelRequired(false);
        ossService.setClientConfiguration(conf);

        bucket = ossService.getOssBucket("voice-record");
    }

    private void downLoad() {
        new Thread(runnable).start();
    }

    public void syncUpload(String fileName) {
        try {
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);
            String[] temp = fileName.split("/");
            final String ossfilePath = temp[temp.length-1];
            OSSData data = ossService.getOssData(bucket, DemoApplication.getInstance().getParentId() + ossfilePath);
            try {
                data.setInputstream(is, is.available(), "audio/amr");
            } catch (IOException e) {
                e.printStackTrace();
            }

            data.uploadInBackground(new SaveCallback() {
                @Override
                public void onSuccess(String objectKey) {
                    Log.d(TAG, "[onSuccess] - " + objectKey + " upload success!");
                    IMResponseCommand imResponseCommand = new IMResponseCommand();
                    imResponseCommand.setParentId(DemoApplication.getInstance().getParentId());
                    imResponseCommand.setMessageType(MessageType.MESSAGE_TYPE_AUDIO);
                    imResponseCommand.setMessageContent("http://voice-record.oss-cn-beijing.aliyuncs.com/" + DemoApplication.getInstance().getParentId() + ossfilePath);
                    imResponseCommand.setDeviceId("863175020954307");
                    SendMessageUtil.sendIMMessage(UploadOssService.this, imResponseCommand);
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    Log.d(TAG, "[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                }

                @Override
                public void onFailure(String objectKey, OSSException ossException) {
                    Log.e(TAG, "[onFailure] - upload " + objectKey + " failed!\n" + ossException.toString());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg)  {
            super.handleMessage(msg);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //urlStr = "http://voice-record.oss-cn-beijing.aliyuncs.com/jijun%2Fstorage%2Fsdcard0%2FAndroid%2Fdata%2Fcom.aibasis.parent%2F8381182%23parent%2Fjijun%2Fvoice%2Fjijun120151128T195220.amr";
            String path = "file";
            String fileName = urlStr.substring(urlStr.length()-21, urlStr.length());
            //String fileName = "jijun120151128T195220.amr";
            OutputStream output = null;
            try {
                /*
                 * 通过URL取得HttpURLConnection
                 * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                 * <uses-permission android:name="android.permission.INTERNET" />
                 */
                URL url=new URL(urlStr);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                System.out.println("jijun-download");
                //取得inputStream，并将流中的信息写入SDCard
                InputStream input=conn.getInputStream();
                System.out.println("jijun-download");
                /*
                 * 写前准备
                 * 1.在AndroidMainfest.xml中进行权限配置
                 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                 * 取得写入SDCard的权限
                 * 2.取得SDCard的路径： Environment.getExternalStorageDirectory()
                 * 3.检查要保存的文件上是否已经存在
                 * 4.不存在，新建文件夹，新建文件
                 * 5.将input流中的信息写入SDCard
                 * 6.关闭流
                 */
                System.out.println("jijun-download1");
                String SDCard= Environment.getExternalStorageDirectory()+"";
                System.out.println("jijun-download2");
                String pathName=SDCard+"/"+path+"/"+fileName;//文件存储路径
                System.out.println("jijun-download3");

                File file=new File(pathName);
                System.out.println("jijun-download4");

                System.out.println("jijun-download5");
                if(file.exists()){
                    System.out.println("exits");
                    return;
                }else{
                    System.out.println("jijun-download1");
                    String dir=SDCard+"/"+path;
                    new File(dir).mkdir();//新建文件夹
                    file.createNewFile();//新建文件
                    output=new FileOutputStream(file);
                    //读取大文件
                    byte[] buffer=new byte[4*1024];
                    while(input.read(buffer)!=-1){
                        output.write(buffer);
                    }
                    output.flush();
                }
            } catch (MalformedURLException e) {
                System.out.println(e.toString() + "jijun-do");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.toString() + "jijun-do");
                e.printStackTrace();
            } finally {
                try {
                    if(output != null) {
                        output.close();
                    }

                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/" +path + "/" + fileName);

                    if(file.exists()) {
                        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.VOICE);
                        message.setFrom(from);
                        VoiceMessageBody voiceMessageBody = new VoiceMessageBody(file, 5);
                        String var0 = Long.toHexString(System.currentTimeMillis());
                        var0 = var0.substring(6);
                        message.setMsgId(Packet.nextID() + "-" + var0);
                        message.addBody(voiceMessageBody);
                        message.status = EMMessage.Status.SUCCESS;
                        EMChatManager.getInstance().importMessage(message, true);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Message msg = new Message();
            handler.sendMessage(msg);
        }
    };

}

