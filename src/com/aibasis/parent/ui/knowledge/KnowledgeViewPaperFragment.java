package com.aibasis.parent.ui.knowledge;

import android.os.Bundle;
import com.aibasis.parent.R;
import com.aibasis.parent.adapter.ViewPageFragmentAdapter;
import com.aibasis.parent.network.api.entity.knowledge.KnowledgeList;
import com.aibasis.parent.ui.base.BaseViewPagerFragment;

/**
 * Created by sniper on 2015/11/25.
 */
public class KnowledgeViewPaperFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.knowledge_viewpage_arrays);
        adapter.addTab(title[0], "all", KnowledgeFragment.class,
                getBundle(KnowledgeList.CATEGORY_ALL));
        adapter.addTab(title[1], "english", KnowledgeFragment.class,
                getBundle(KnowledgeList.CATEGORY_ENGLISH));
        adapter.addTab(title[2], "math", KnowledgeFragment.class,
                getBundle(KnowledgeList.CATEGORY_MATH));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(KnowledgeFragment.BUNDLE_KEY_CATEGORY, newType);
        return bundle;
    }
}
