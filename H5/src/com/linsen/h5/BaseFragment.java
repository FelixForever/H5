package com.linsen.h5;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linsen.h5.utils.IntentUtil;

public abstract class BaseFragment extends Fragment {
	protected View mView;
	protected Activity mActivity;
	protected Context mContext;
	public int screenWidth;
	public int screenHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity();
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews();
		initEvents();
		initDatas();
		return mView;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	protected abstract void initViews();

	protected abstract void initEvents();

	protected abstract void initDatas();

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	protected void startActivity(Class<?> cls, Bundle bundle) {
		IntentUtil.startActivity(mContext, cls, bundle);
	}

	protected void startActivity(String action) {
		startActivity(action, null);
	}

	protected void startActivity(String action, Bundle bundle) {
		IntentUtil.startActivity(mContext, action, bundle);
	}
}
