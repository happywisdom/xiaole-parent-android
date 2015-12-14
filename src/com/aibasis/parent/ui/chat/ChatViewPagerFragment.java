package com.aibasis.parent.ui.chat;

import android.os.Bundle;
import com.aibasis.parent.R;
import com.aibasis.parent.adapter.ViewPageFragmentAdapter;
import com.aibasis.parent.ui.base.BaseViewPagerFragment;

/**
 * Created by gexiao2 on 2015/12/10.
 */
public class ChatViewPagerFragment extends BaseViewPagerFragment {

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.chat_viewpage_arrays);
        adapter.addTab(title[0], "latest", ChatAllHistoryFragment.class,
                getBundle(0));
        adapter.addTab(title[1],"contact",ContactlistFragment.class,getBundle(1));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }
}
