package com.linsen.h5.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linsen.h5.BaseFragment;
import com.linsen.h5.R;
import com.linsen.h5.adapter.MeizhiListAdapter;
import com.linsen.h5.domain.Meizhi;
import com.linsen.h5.widget.MultiSwipeRefreshLayout;

@SuppressLint("ValidFragment")
public class FindFragment extends BaseFragment {
	private TextView headerTitleTv;
	MultiSwipeRefreshLayout mSwipeRefreshLayout;
	RecyclerView mRecyclerView;
	MeizhiListAdapter mMeizhiListAdapter;
	List<Meizhi> mMeizhiList;
	boolean mIsDbInited, mIsFirstTimeTouchBottom = true;
	int mOffset = 0;

	public FindFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_find, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		headerTitleTv = (TextView) findViewById(R.id.header_title_tv);
		headerTitleTv.setText("发现");
		mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setColorSchemeResources(
					R.color.refresh_progress_3, R.color.refresh_progress_2,
					R.color.refresh_progress_1);
			mSwipeRefreshLayout
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							requestDataRefresh();
						}
					});
		}
		mMeizhiList = new ArrayList<Meizhi>();
		
		mRecyclerView = (RecyclerView) findViewById(R.id.rv_meizhi);
		final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
				2, StaggeredGridLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mMeizhiListAdapter = new MeizhiListAdapter(this.getActivity(),
				mMeizhiList);
		mRecyclerView.setAdapter(mMeizhiListAdapter);
		
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView rv, int dx, int dy) {
				if (!mSwipeRefreshLayout.isRefreshing()
						&& layoutManager
								.findLastCompletelyVisibleItemPositions(new int[2])[1] >= mMeizhiListAdapter
								.getItemCount() - 2) {
					if (!mIsFirstTimeTouchBottom) {
						mSwipeRefreshLayout.setRefreshing(true);
						mOffset += 20;
						getData();
					} else {
						mIsFirstTimeTouchBottom = false;
					}
				}
			}
		});
	}

	public void getData(){
		setRefreshing(true);
	}
	
	@Override
	protected void initEvents() {

	}

	@Override
	protected void initDatas() {

	}

	public void requestDataRefresh() {

	}

	public void setRefreshing(boolean refreshing) {
		if (mSwipeRefreshLayout == null) {
			return;
		}
		if (!refreshing) {
			mSwipeRefreshLayout.setRefreshing(false);
		} else {
			mSwipeRefreshLayout.setRefreshing(true);
		}
	}

	public void setProgressViewOffset(boolean scale, int start, int end) {
		mSwipeRefreshLayout.setProgressViewOffset(scale, start, end);
	}

	public void setSwipeableChildren(
			MultiSwipeRefreshLayout.CanChildScrollUpCallback canChildScrollUpCallback) {
		mSwipeRefreshLayout
				.setCanChildScrollUpCallback(canChildScrollUpCallback);
	}

}
