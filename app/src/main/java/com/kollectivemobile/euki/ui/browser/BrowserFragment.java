package com.kollectivemobile.euki.ui.browser;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import butterknife.BindView;

public class BrowserFragment extends BaseFragment {
    @BindView(R.id.wv_main) WebView wvMain;

    private String url;

    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        fragment.url = url;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browser, container, false);
    }

    @Override
    public boolean showBack() {
        return true;
    }

    private void setUIElements() {
        wvMain.requestFocus();
        wvMain.getSettings().setLightTouchEnabled(true);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.getSettings().setGeolocationEnabled(true);
        wvMain.setSoundEffectsEnabled(true);
        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
    }
}
