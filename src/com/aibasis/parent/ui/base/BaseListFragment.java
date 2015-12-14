package com.aibasis.parent.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.aibasis.parent.R;
import com.aibasis.parent.ui.EmptyLayout;

/**
 * Created by gexiao2 on 2015/12/10.
 */
public abstract class BaseListFragment extends BaseFragment{

    public static final String BUNDLE_KEY_CATEGORY = "BUNDLE_KEY_CATEGORY";

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected ListView mListView;

    protected EmptyLayout mErrorLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }


}
