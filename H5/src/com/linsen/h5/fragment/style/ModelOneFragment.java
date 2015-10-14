package com.linsen.h5.fragment.style;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

import org.kymjs.kjframe.KJBitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linsen.h5.R;
import com.linsen.h5.domain.ModelOne;
import com.linsen.h5.domain.Page;
import com.linsen.h5.photopicker.ImagePickerPlusActivity;
import com.linsen.h5.photopicker.ItemImageInfo;
import com.linsen.h5.utils.BitmapHelper;
import com.linsen.h5.utils.T;
import com.linsen.h5.view.TouchImageView;
import com.linsen.h5.view.TouchImageView.TivCallBack;
import com.linsen.h5.view.TouchTextView;
import com.linsen.h5.view.TouchTextView.CallBackInterface;

@SuppressLint("ValidFragment")
public class ModelOneFragment extends BaseModelFragment {
	private static final int TEMP_ID = 1001;
	private TextView textView;
	private TouchTextView ttv;
	private RelativeLayout containerView;
	private TouchImageView tiv;
	private RelativeLayout viewTivContainer;
	private String filePath;
	private MyTask myTask;
	private MaterialDialog editTextDialog;
	private EditText textEt;
	private String text = "";
	private ModelOne modelOne;
	public static final String IMAGE_PATH = "H5";
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
			"images/screenshots");

	public ModelOneFragment() {
		super();
	}

	public ModelOneFragment(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_model_one, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		containerView = (RelativeLayout) findViewById(R.id.rl_c);
		viewTivContainer = (RelativeLayout) findViewById(R.id.view_tiv_container);
		textView = (TextView) findViewById(R.id.tv_t);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ttv == null) {
					ttv = new TouchTextView(getActivity(), textView);
					containerView.addView(ttv);
				}
				textView.setVisibility(View.GONE);
				ttv.setVisibility(View.VISIBLE);
				ttv.setCallBackInterface(new CallBackInterface() {

					@Override
					public void onClick(int position) {
						switch (position) {
						case 0:
							textView.setVisibility(View.VISIBLE);
							ttv.setVisibility(View.GONE);
							textView.setScaleX(ttv.getScale());
							textView.setScaleY(ttv.getScale());
							textView.setRotation(ttv.getRotate());
							textView.setTranslationX(ttv.getTransX());
							textView.setTranslationY(ttv.getTransY());
							break;
						case 1:
							// 编辑文字
							showEditTextDialog();
							break;
						default:
							break;
						}

					}
				});

			}
		});

		tiv = new TouchImageView(getActivity());
		tiv.setTivCallBack(new TivCallBack() {

			@Override
			public void onClick(float x, float y) {
				T.showShort(mActivity, "更换图片");
				Intent intent = new Intent(mActivity,
						ImagePickerPlusActivity.class);
				intent.putExtra(ImagePickerPlusActivity.EXTRA_PICK_PHOTO_COUNT,
						1); // 1张
				intent.putExtra(ImagePickerPlusActivity.EXTRA_DISK_CACHE_PATH,
						Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "/diskcache");
				startActivityForResult(intent, 1);
			}
		});
		updateData();
		text = textView.getText().toString();

	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void initDatas() {

	}

	private void updateData() {
		if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
			myTask.cancel(true);
		}
		myTask = new MyTask(filePath, screenWidth, screenHeight);
		myTask.execute();
	}

	public class MyTask extends AsyncTask<Void, Void, Bitmap> {
		private Bitmap keyBitmap;
		final int w;
		final int h;
		final String imageUrl;
		KJBitmap kjb;

		public MyTask(String imageUrl, int w, int h) {
			this.imageUrl = imageUrl;
			this.w = w;
			this.h = h;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			kjb = KJBitmap.create();
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			keyBitmap = kjb.getBitmapFromCache(filePath);
			if (keyBitmap == null) {
				keyBitmap = kjb.getBitmapFromNet(imageUrl, w, h);
				if (keyBitmap != null) {
					kjb.putBitmapToMC(imageUrl, keyBitmap);
				}
			}
			return BitmapHelper.createBitmap(keyBitmap, w, h);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result == null) {
				T.showShort(mActivity, "错误");
			} else {
				tiv.bmpReady(result);
				viewTivContainer.removeAllViews();
				viewTivContainer.addView(tiv);
			}
		}
	}

	private void showEditTextDialog() {
		editTextDialog = new MaterialDialog(mActivity);
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.enter_dialog_content, null);
		textEt = (EditText) view.findViewById(R.id.et_pincode);
		textEt.setText(text);
		editTextDialog.setTitle("文字")
				.setPositiveButton("确定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						text = textEt.getText().toString();
						if (text.length() <= 0) {
							T.showShort(mActivity, "请输入文字！");
						} else {
							editTextDialog.dismiss();
							updateTtv();
						}
					}
				}).setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						editTextDialog.dismiss();
					}
				}).setCanceledOnTouchOutside(false)
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
					}
				}).setContentView(view).show();
	}

	private void updateTtv() {
		textView.setText(text);
		ttv.updateTextView(textView);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				ArrayList<ItemImageInfo> datas = (ArrayList<ItemImageInfo>) data
						.getSerializableExtra(ImagePickerPlusActivity.EXTRA_PICK_RETURN_DATA);
				filePath = datas.get(0).filePath;
				updateData();
			} else {
				T.showShort(mActivity, "操作取消！");
			}
			break;
		}
	}

	@Override
	public Page getPage(String imageUrl,String imagePath) {
		Page page = new Page();
		page.setTempId(TEMP_ID);
		modelOne = new ModelOne();
		if (ttv == null) {
			modelOne.setRotate(0.0f);
			modelOne.setScale(1.0f);
			modelOne.setTransX(0.0f);
			modelOne.setTransY(0.0f);
		} else {
			modelOne.setRotate(ttv.getRotate());
			modelOne.setScale(ttv.getScale());
			modelOne.setTransX(ttv.getTransX());
			modelOne.setTransY(ttv.getTransY());
		}
		if (imagePath != null) {
			modelOne.setImagePath(imagePath);
		} else {
			modelOne.setImagePath("");
		}
		if (imageUrl != null) {
			modelOne.setImageUrl(imageUrl);
		} else {
			modelOne.setImageUrl("");
		}
		Gson g = new Gson();
		String medias = g.toJson(modelOne);
		page.setMedias(medias);
		return page;
	}

	@Override
	public String uploadImage() {
		Bitmap bmp = tiv.creatNewPhoto();
		String complitePhotoPath = UUID.randomUUID().toString() + ".png";
		File photoFile = new File(FILE_PIC_SCREENSHOT, complitePhotoPath);
		// 判断文件夹是否存在
		File filePath = FILE_PIC_SCREENSHOT;
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		BitmapHelper.saveBmp(bmp, photoFile.getAbsolutePath());
		return photoFile.getAbsolutePath();
	}

}
