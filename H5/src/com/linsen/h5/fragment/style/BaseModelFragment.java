package com.linsen.h5.fragment.style;

import android.os.Bundle;

import com.linsen.h5.BaseFragment;
import com.linsen.h5.domain.Page;

public abstract class BaseModelFragment extends BaseFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void initDatas() {

	}

	public abstract Page getPage(String imageUrl,String imagePath);
	public abstract String uploadImage();

}
