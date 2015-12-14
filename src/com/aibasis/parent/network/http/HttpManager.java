package com.aibasis.parent.network.http;

import android.content.Context;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * Created by gexiao2 on 2015/11/23.
 */
public class HttpManager {

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static final String APPLICATION_JSON = "application/json";

    public HttpManager(){}

    public static String openUrl(Context context,String url, String method,APIParameters params) {
        HttpResponse response = requestHttpExecute(context,url, method,params);
        String result = readResponse(response);
        return result;
    }

    private static HttpResponse requestHttpExecute(Context context,String url,String method,APIParameters params) {
        HttpClient client = null;
        HttpResponse response = null;

        try {
            client = getNewHttpClient();
            client.getParams().setParameter("http.route.default-proxy", NetStateManager.getAPN());
            HttpPost httpPost = null;
            String result;
            if(method.equals("GET")) {
                //url = url + "?" + params.encodeUrl();
                //e = new HttpGet(url);
            } else if (method.equals("POST")){
                httpPost = new HttpPost(url);

                httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                String encoderJson = params.encodeToJson();
                StringEntity se = new StringEntity(encoderJson);
                se.setContentType(CONTENT_TYPE_TEXT_JSON);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                httpPost.setEntity(se);
            }

            response = client.execute(httpPost);
            StatusLine status1 = response.getStatusLine();
            int statusCode1 = status1.getStatusCode();
            if(statusCode1 != 200) {

                System.err.println("statusCode:"+statusCode1);

                result = readResponse(response);

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            shutdownHttpClient(client);
        }

        return response;
    }

    public static void shutdownHttpClient(HttpClient client) {
        if(client != null) {
            try {
                client.getConnectionManager().closeExpiredConnections();
            } catch (Exception e) {

            }
        }
    }

    public static String readResponse(HttpResponse response){
        if(response == null) {
            return null;
        } else {
            HttpEntity entity = response.getEntity();
            Object inputStream = null;
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            try {
                inputStream = entity.getContent();
                Header e = response.getFirstHeader("Content-Encoding");
                if(e != null && e.getValue().toLowerCase().indexOf("gzip") > -1) {
                    inputStream = new GZIPInputStream((InputStream)inputStream);
                }

                byte[] buffer = new byte[8192];

                int readBytes1;
                while((readBytes1 = ((InputStream)inputStream).read(buffer)) != -1) {
                    content.write(buffer, 0, readBytes1);
                }

                String result = new String(content.toByteArray(), "UTF-8");

                String var9 = result;
                return var9;
            } catch (IOException var19) {
            } finally {
                if(inputStream != null) {
                    try {
                        ((InputStream)inputStream).close();
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }

                if(content != null) {
                    try {
                        content.close();
                    } catch (IOException var17) {
                        var17.printStackTrace();
                    }
                }

            }
        }

        return null;
    }

    public static HttpClient getNewHttpClient() {
        try {
            BasicHttpParams e = new BasicHttpParams();
            HttpProtocolParams.setVersion(e, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(e, "UTF-8");
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(e, registry);
            HttpConnectionParams.setConnectionTimeout(e, 25000);
            HttpConnectionParams.setSoTimeout(e, 20000);
            DefaultHttpClient client = new DefaultHttpClient(ccm, e);
            return client;
        } catch (Exception var4) {
            return new DefaultHttpClient();
        }
    }

}
