package com.linsen.h5.activity;

import me.drakeet.materialdialog.MaterialDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.linsen.h5.R;
import com.linsen.h5.fragment.CommonFragment;
import com.linsen.h5.fragment.FindFragment;
import com.linsen.h5.fragment.PersonalFragment;
import com.linsen.h5.view.DummyTabContent;

public class MainActivity extends FragmentActivity {
	private static final String TAB_HOME = "home";
	private static final String TAB_FIND = "find";
	private static final String TAB_ADD = "add";
	private static final String TAB_USER = "user";
	private static final String TAB_MORE = "more";
	public static final int TAB_HOME_ID = 0;
	public static final int TAB_FIND_ID = 1;
	public static final int TAB_ADD_ID = 2;
	public static final int TAB_USER_ID = 3;
	public static final int TAB_MORE_ID = 4;
	TabHost tabHost;
	private String tabFlag = null;
	private FrameLayout mContainer;

	private MaterialDialog exitTipDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabView();
	}

	private void initTabView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		mContainer = (FrameLayout) findViewById(R.id.realtabcontent);
		RelativeLayout layout = (RelativeLayout) tabHost.getChildAt(0);
		TabWidget tw = (TabWidget) layout.findViewById(android.R.id.tabs);
		tabHost.setup();
		setListener();
		addTab(tw, TAB_HOME, R.string.tab_home, R.drawable.tab_home_selector);
		addTab(tw, TAB_FIND, R.string.tab_find, R.drawable.tab_find_selector);
		addTab(tw, TAB_ADD, R.drawable.tab_add_selector);
		addTab(tw, TAB_USER, R.string.tab_user, R.drawable.tab_user_selector);
		addTab(tw, TAB_MORE, R.string.tab_more, R.drawable.tab_more_selector);
	}

	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case TAB_HOME_ID:
				return new CommonFragment();
			case TAB_FIND_ID:
				return new FindFragment();
			case TAB_USER_ID:
				return new PersonalFragment();
			case TAB_MORE_ID:
				return new CommonFragment();
			default:
				return new CommonFragment();
			}
		}

		@Override
		public int getCount() {
			return 5;
		}

	};

	@Override
	protected void onResume() {
		if (tabFlag != null) {
			if (tabFlag.equals(TAB_HOME)) {
				setCurrentTab(TAB_HOME_ID);
			} else if (tabFlag.equals(TAB_FIND)) {
				setCurrentTab(TAB_FIND_ID);
			} else if (tabFlag.equals(TAB_USER)) {
				setCurrentTab(TAB_USER_ID);
			} else if (tabFlag.equals(TAB_MORE)) {
				setCurrentTab(TAB_MORE_ID);
			} else {
				setCurrentTab(TAB_HOME_ID);
			}
		} else {
			setCurrentTab(TAB_HOME_ID);
		}
		super.onResume();
	}

	private void setListener() {
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				setTabFlag(tabId);
				Fragment fragment = null;
				if (tabId.equalsIgnoreCase(TAB_HOME)) {
					fragment = (Fragment) mFragmentPagerAdapter
							.instantiateItem(mContainer, TAB_HOME_ID);
				} else if (tabId.equalsIgnoreCase(TAB_FIND)) {
					fragment = (Fragment) mFragmentPagerAdapter
							.instantiateItem(mContainer, TAB_FIND_ID);
				} else if (tabId.equalsIgnoreCase(TAB_USER)) {
					fragment = (Fragment) mFragmentPagerAdapter
							.instantiateItem(mContainer, TAB_USER_ID);
				} else if (tabId.equalsIgnoreCase(TAB_MORE)) {
					fragment = (Fragment) mFragmentPagerAdapter
							.instantiateItem(mContainer, TAB_MORE_ID);
				} else {
					fragment = (Fragment) mFragmentPagerAdapter
							.instantiateItem(mContainer, TAB_HOME_ID);
				}
				mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
				mFragmentPagerAdapter.finishUpdate(mContainer);
			}
		};
		tabHost.setOnTabChangedListener(tabChangeListener);
	}

	public void addTab(TabWidget tw, String specStr, int tabText, int drawableId) {
		RelativeLayout tabIndicator = (RelativeLayout) LayoutInflater
				.from(this).inflate(R.layout.tab_indicator, tw, false);
		TextView tvTab = (TextView) tabIndicator.findViewById(R.id.title);
		ImageView ivTab = (ImageView) tabIndicator.findViewById(R.id.icon);
		ivTab.setBackgroundResource(drawableId);
		tvTab.setText(tabText);

		TabHost.TabSpec tSpec = tabHost.newTabSpec(specStr);
		tSpec.setIndicator(tabIndicator);
		tSpec.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec);
		// 背景透明
		// tabIndicator.getBackground().setAlpha(175);
	}

	public void addTab(TabWidget tw, String specStr, int drawableId) {
		RelativeLayout tabIndicator = (RelativeLayout) LayoutInflater
				.from(this).inflate(R.layout.tab_add_indicator, tw, false);
		ImageView ivTab = (ImageView) tabIndicator.findViewById(R.id.icon);
		ivTab.setBackgroundResource(drawableId);

		TabHost.TabSpec tSpec = tabHost.newTabSpec(specStr);
		tSpec.setIndicator(tabIndicator);
		tSpec.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec);
		tabIndicator.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,MakingActivity.class));
			}
		});
	}

	public void setCurrentTab(int tab) {
		tabHost.setCurrentTab(tab);
	}

	public void setTabFlag(String tabFlag) {
		this.tabFlag = tabFlag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (tabFlag != null && !tabFlag.equals(TAB_HOME)) {
				setCurrentTab(TAB_HOME_ID);
				return true;
			} else {
				// 退出应用
				showExitDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 退出提醒dialog
	private void showExitDialog() {
		if (exitTipDialog == null) {
			exitTipDialog = new MaterialDialog(this);
			exitTipDialog
					.setTitle("提示")
					.setMessage("确定要退出吗？")
					.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							exitTipDialog.dismiss();
							finish();
						}
					})
					.setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							exitTipDialog.dismiss();
						}
					})
					.setCanceledOnTouchOutside(false)
					.setOnDismissListener(
							new DialogInterface.OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
								}
							}).show();
		} else {
			exitTipDialog.show();
		}
	}

}
