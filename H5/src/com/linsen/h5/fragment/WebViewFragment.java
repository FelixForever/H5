package com.linsen.h5.fragment;

import github.chenupt.dragtoplayout.AttachUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.linsen.h5.R;
import com.linsen.h5.domain.Page;
import com.linsen.h5.fragment.style.BaseModelFragment;
import com.ypy.eventbus.EventBus;

/**
 * Created by chenupt@gmail.com on 1/30/15.
 * Description :
 */
public class WebViewFragment extends BaseModelFragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        webView = (WebView) getView().findViewById(R.id.web_view);
        webView.loadUrl("http://sina.cn/?wm=4007");

        // webView does not have scroll listener
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBus.getDefault().post(AttachUtil.isWebViewAttach(webView));
                return false;
            }
        });



    }

	@Override
	public Page getPage(String imageUrl, String imagePath) {
		return null;
	}

	@Override
	public String uploadImage() {
		return null;
	}


}
