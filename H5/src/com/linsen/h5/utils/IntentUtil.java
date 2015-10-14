package com.linsen.h5.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.linsen.h5.R;

public class IntentUtil {
	public static void startActivity(Context context, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_keep_state);
	}

	public static void startActivity(Context context, String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_keep_state);
	}
}
