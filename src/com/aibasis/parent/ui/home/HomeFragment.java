package com.aibasis.parent.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;
import com.aibasis.parent.R;
import com.aibasis.parent.adapter.BabySpinnerAdapter;
import com.aibasis.parent.domain.Baby;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gexiao2 on 2015/12/9.
 */
public class HomeFragment extends Fragment{

    private WebView homeWebView;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Baby> babys = new ArrayList<Baby>();
        babys.add(new Baby("吉军"));
        babys.add(new Baby("葛霄"));
        babys.add(new Baby("李晨"));

        spinner = (Spinner) getView().findViewById(R.id.spinner_baby);
        BabySpinnerAdapter babySpinnerAdapter = new BabySpinnerAdapter(babys, getActivity());
        spinner.setAdapter(babySpinnerAdapter);

        homeWebView = (WebView) getView().findViewById(R.id.home_webview);
        homeWebView.getSettings().setJavaScriptEnabled(true);
        homeWebView.setWebViewClient(new MyWebViewClient());

        homeWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void changeActivity() {
                Log.i("jijun-changeActivity", "change");
            }
        }, "bridge");

//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                homeWebView.loadUrl("file:///android_asset/ability-chart.html");
//            }
//        });

        homeWebView.loadUrl("file:///android_asset/ability-chart.html");
    }

    public class MyWebViewClient extends WebViewClient {

        public boolean shouldOverviewUrlLoading(WebView view,String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
