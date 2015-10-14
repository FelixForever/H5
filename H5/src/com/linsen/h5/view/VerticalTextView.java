package com.linsen.h5.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linsen.h5.R;

public class VerticalTextView extends LinearLayout implements
		View.OnTouchListener {
	CallBackInterface callBack;
	int lastX;
	int lastY;

	GestureDetector mGesture = null;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private LinearLayout lytv;
	Context context;

	public VerticalTextView(Context context1, AttributeSet attrs) {
		super(context1, attrs);
		context = context1;

		// 获取SurfaceHolder接口
		// 设置屏幕保持开启状态
		this.setKeepScreenOn(true);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.lytextview, this, false);
		tv1 = (TextView) view.findViewById(R.id.tv1);
		tv2 = (TextView) view.findViewById(R.id.tv2);
		tv3 = (TextView) view.findViewById(R.id.tv3);
		lytv = (LinearLayout) view.findViewById(R.id.lytv);
		tv1.setOnTouchListener(this);
		tv2.setOnTouchListener(this);
		tv3.setOnTouchListener(this);
		this.addView(view);
		// this.getPaddingLeft();
	}

	/*
	 * public VerticalTextView(Context context, AttributeSet attrs, int
	 * defStyle) { super(context, attrs, defStyle); }
	 */

	int signclick = 0;
	long starttime;
	long movetime;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if (action == 0) {
			signclick = 0;
		} else
			signclick++;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			starttime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			movetime = System.currentTimeMillis();
			int duration = (int) movetime - (int) starttime;
			if (duration < ViewConfiguration.getLongPressTimeout()) {
				break;
			}
			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;
			int left = this.getLeft() + dx;
			int top = this.getTop() + dy;
			int right = this.getRight() + dx;
			int bottom = this.getBottom() + dy;
			this.layout(left, top, right, bottom);
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			break;
		case MotionEvent.ACTION_UP:
			if (signclick == 1) {
				callBack.onMyClick();
			}
			if (signclick == 2) {
				callBack.onMove();
			}
			break;
		}
		return false;
	}

	public void setFunction(CallBackInterface myCallBack) {
		callBack = myCallBack;
	}

	public void setText(String text) {
		int length = text.length();
		int clumn = length / 15 + 1;
		for (int i = 0; i < clumn; i++) {
			if (i == 0)
				tv1.setText(text.toCharArray(), 0, 15);
			if (i == 1)
				tv2.setText(text.toCharArray(), 15, 15);
			if (i == 2)
				tv3.setText(text.toCharArray(), 30, 15);
		}
	}

}
