package com.linsen.h5;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.linsen.h5.manager.PreferenceManager;
import com.linsen.h5.utils.IntentUtil;

public abstract class BaseActivity extends Activity {
	private final String NAME=this.getClass().getName();
	private final String TAG=NAME.substring(NAME.lastIndexOf(".")+1);
	protected BaseApplication mApplication;
	protected PreferenceManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (BaseApplication) getApplication();
		pm = new PreferenceManager(this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	
	
	/** 我的输出日志 **/
	protected void LogInfo(String msg) {
		showLogInfo(TAG,msg);
	}
	protected void LogWarning(String msg) {
		showLogWarning(TAG,msg);
	}
	protected void LogDebug(String msg) {
		showLogDebug(TAG,msg);
	}
	protected void LogError(String msg) {
		showLogError(TAG,msg);
	}
	
	/** wraning输出Log日志 **/
	protected void showLogWarning(String tag, String msg) {
		Log.w(tag, msg);
	}
	
	/** Info输出Log日志 **/
	protected void showLogInfo(String tag, String msg) {
		Log.i(tag, msg);
	}
	
	/** Debug输出Log日志 **/
	protected void showLogDebug(String tag, String msg) {
		Log.d(tag, msg);
	}

	/** Error输出Log日志 **/
	protected void showLogError(String tag, String msg) {
		Log.e(tag, msg);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		IntentUtil.startActivity(this, cls, bundle);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		IntentUtil.startActivity(this, action, bundle);
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_exit);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		super.finish();
		overridePendingTransition(0, R.anim.activity_exit);
	}
}
