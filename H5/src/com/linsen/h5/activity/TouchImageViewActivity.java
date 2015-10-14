package com.linsen.h5.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linsen.h5.R;
import com.linsen.h5.view.TouchImageView;
import com.linsen.h5.view.TouchTextView;
import com.linsen.h5.view.TouchTextView.CallBackInterface;

public class TouchImageViewActivity extends Activity {
	private TextView textView;
	private TouchTextView ttv;
	private RelativeLayout containerView;
	private TouchImageView tiv;
	private RelativeLayout viewTivContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch);
		containerView = (RelativeLayout) findViewById(R.id.rl_c);
		viewTivContainer = (RelativeLayout) findViewById(R.id.view_tiv_container);
		textView = (TextView) findViewById(R.id.tv_t);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ttv == null) {
					ttv = new TouchTextView(TouchImageViewActivity.this,
							textView);
					containerView.addView(ttv);
				}
				textView.setVisibility(View.GONE);
				ttv.setVisibility(View.VISIBLE);
				ttv.setCallBackInterface(new CallBackInterface() {

					@Override
					public void onClick(int position) {
						textView.setVisibility(View.VISIBLE);
						ttv.setVisibility(View.GONE);
						textView.setScaleX(ttv.getScale());
						textView.setScaleY(ttv.getScale());
						textView.setRotation(ttv.getRotate());
						textView.setTranslationX(ttv.getTransX());
						textView.setTranslationY(ttv.getTransY());
					}
				});

			}
		});
		//tiv = new TouchImageView(this);
		//viewTivContainer.addView(tiv);

		// bbbBtu = (Button) findViewById(R.id.btn_b);
		// bbbBtu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// ttv.setTransX(26.9f);
		// ttv.setTransY(-47.5f);
		// ttv.setRotate(7.7f);
		// ttv.setScale(1.06f);
		// ttv.setVisibility(View.VISIBLE);
		// ttv.reDrawText();
		// }
		// });
	}

}
