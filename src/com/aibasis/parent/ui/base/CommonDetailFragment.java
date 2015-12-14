package com.aibasis.parent.ui.base;

import android.os.AsyncTask;
import android.webkit.WebView;
import com.aibasis.parent.ui.EmptyLayout;

import java.io.Serializable;

/**
 * Created by gexiao2 on 2015/12/10.
 */
public abstract class CommonDetailFragment <T extends Serializable> extends BaseFragment{

    protected String mId;

    protected EmptyLayout mEmptyLayout;

    protected int mCommentCount = 0;

    protected WebView mWebView;

    protected T mDetail;

    private AsyncTask<String, Void, T> mCacheTask;



}
