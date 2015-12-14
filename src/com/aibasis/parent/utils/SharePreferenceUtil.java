package com.aibasis.parent.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gexiao2 on 2015/6/26.
 */
public class SharePreferenceUtil {

    private static final String FILE_NAME = "aibasis";

    private static final String PARENT_ID = "parentId";

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setParentId(String parentId) {
        editor.putString(PARENT_ID, parentId);
        editor.commit();
    }

    public String getParentId() {
        return sharedPreferences.getString(PARENT_ID, "");
    }
}
