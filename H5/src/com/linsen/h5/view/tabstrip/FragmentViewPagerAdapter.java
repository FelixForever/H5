package com.linsen.h5.view.tabstrip;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.linsen.h5.fragment.style.BaseModelFragment;

public class FragmentViewPagerAdapter extends PagerAdapter implements
		ViewPager.OnPageChangeListener {
	private String[] titles;
	private List<BaseModelFragment> fragments;
	private FragmentManager fragmentManager;
	private ViewPager viewPager;
	private int currentPageIndex = 0;

	public FragmentViewPagerAdapter(FragmentManager fragmentManager,
			ViewPager viewPager, List<BaseModelFragment> fragments, String[] titles) {
		this.fragments = fragments;
		this.fragmentManager = fragmentManager;
		this.viewPager = viewPager;
		this.viewPager.setAdapter(this);
		this.viewPager.setOnPageChangeListener(this);
		this.titles = titles;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (fragments.size() > position) {
			container.removeView(fragments.get(position).getView());
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			fragmentManager.executePendingTransactions();
		}

		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView());
		}

		return fragment.getView();
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
	}

	@Override
	public void onPageSelected(int i) {
		fragments.get(currentPageIndex).onPause();
		if (fragments.get(i).isAdded()) {
			fragments.get(i).onResume();
		}
		currentPageIndex = i;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	public void removePage(int position) {
		fragments.remove(position);
		titles = new String[fragments.size()];
		for (int i = 0; i < fragments.size(); i++) {
			titles[i] = i + 1 + "";
		}
		notifyDataSetChanged();
		viewPager.setAdapter(this);
	}
	
	public void addPage(int position,BaseModelFragment fragment){
		fragments.add(position, fragment);
		titles = new String[fragments.size()];
		for (int i = 0; i < fragments.size(); i++) {
			titles[i] = i + 1 + "";
		}
		notifyDataSetChanged();
		viewPager.setAdapter(this);
	}
}
