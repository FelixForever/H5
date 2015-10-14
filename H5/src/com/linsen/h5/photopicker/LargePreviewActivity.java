package com.linsen.h5.photopicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.linsen.h5.BaseActivity;
import com.linsen.h5.R;

public class LargePreviewActivity extends BaseActivity implements
		OnClickListener {
	private View backView;
	private Button finishBtn;

	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_preview_layout);

		filePath = getIntent().getStringExtra("filePath");
		final String orientation = getIntent().getStringExtra("orientation");

		if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(orientation)) {
			Log.e("LargePreviewActivity", "file path is " + filePath);
			finish();
			return;
		}

		final ImageView ivt = (ImageView) findViewById(R.id.zivp);
		ivt.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						ivt.getViewTreeObserver().removeGlobalOnLayoutListener(
								this);
						Bitmap bp = BitmapUtil.getBitmap(filePath);
						if (bp != null) {
							try {
								int orientationInt = Integer
										.parseInt(orientation);
								ivt.setImageBitmap(bp);
								android.graphics.Matrix matrix = new android.graphics.Matrix();
								matrix.postTranslate(
										ivt.getWidth() / 2 - bp.getWidth() / 2,
										ivt.getHeight() / 2 - bp.getHeight()
												/ 2);
								rotate(orientationInt, matrix, ivt);
								if (orientationInt == 90
										|| orientationInt == 270) {
									minZoom(bp.getHeight(), bp.getWidth(),
											matrix, ivt);
								} else {
									minZoom(bp.getWidth(), bp.getHeight(),
											matrix, ivt);
								}
								ivt.setImageMatrix(matrix);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						} else {
							LogUtil.e("LargePreviewActivity", "bitmap is null");
						}
					}
				});
		initViews();
		initEvents();

	}

	private boolean rotate(int orientationInt, android.graphics.Matrix matrix,
			ImageView iv) {
		if (orientationInt > 0 && orientationInt < 360) {
			matrix.postRotate(orientationInt, iv.getWidth() / 2,
					iv.getHeight() / 2);
			return true;
		}
		return false;
	}

	private void minZoom(int w, int h, android.graphics.Matrix matrix,
			ImageView iv) {
		float minScaleR = Math.min(((float) iv.getWidth()) / ((float) w),
				((float) iv.getHeight()) / ((float) h));
		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR, minScaleR, iv.getWidth() / 2,
					iv.getHeight() / 2);
		}
	}

	@Override
	protected void initViews() {
		backView = (View) findViewById(R.id.header_left_back_iv);
		finishBtn = (Button) findViewById(R.id.header_right_btn);
	}

	@Override
	protected void initEvents() {
		backView.setOnClickListener(this);
		finishBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left_back_iv:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.header_right_btn:
			setResult(RESULT_OK, new Intent().putExtra("filePath", filePath));
			finish();
			break;
		}
	}

}
