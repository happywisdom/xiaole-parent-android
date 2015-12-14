package com.aibasis.parent.utils;

import android.content.Context;
import android.content.Intent;
import com.aibasis.parent.service.UploadOssIntent;

/**
 * Created by sniper on 2015/11/28.
 */
public class OssUploadUtil {

    public static void startOssUpload (Context context, String filePath) {
        Intent intent = new Intent();
        intent.setAction(UploadOssIntent.ACTION_UPLOADVOICE);
        intent.putExtra("FilePath", filePath);
        context.startService(intent);
    }
}
