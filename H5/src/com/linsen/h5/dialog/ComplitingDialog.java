package com.linsen.h5.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsen.h5.R;

public class ComplitingDialog extends Dialog {
	private Context mContext;
	private TextView textView;

	public ComplitingDialog(Context context) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = context;
		setContentView(R.layout.compliting_diloag);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		textView = (TextView) findViewById(R.id.tv_dialog);
	}
	
	public void setDialogText(String text){
		textView.setText(text);
		textView.setVisibility(View.VISIBLE);
	}
	
}
