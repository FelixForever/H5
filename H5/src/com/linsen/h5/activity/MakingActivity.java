package com.linsen.h5.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.linsen.h5.AppException;
import com.linsen.h5.BaseApplication;
import com.linsen.h5.R;
import com.linsen.h5.dialog.ComplitingDialog;
import com.linsen.h5.domain.Page;
import com.linsen.h5.domain.Result;
import com.linsen.h5.domain.Works;
import com.linsen.h5.fragment.style.BaseModelFragment;
import com.linsen.h5.fragment.style.ModelOneFragment;
import com.linsen.h5.photopicker.ImagePickerPlusActivity;
import com.linsen.h5.photopicker.ItemImageInfo;
import com.linsen.h5.utils.T;
import com.linsen.h5.utils.URLs;
import com.linsen.h5.view.tabstrip.CirclePagerSlidingTabStrip;
import com.linsen.h5.view.tabstrip.FragmentViewPagerAdapter;

public class MakingActivity extends FragmentActivity implements OnClickListener {
	private static final String TAG = "MakingActivity";
	private String[] contents;
	private CirclePagerSlidingTabStrip tabs;
	private FragmentViewPagerAdapter adapter;
	public List<BaseModelFragment> fragments = new ArrayList<BaseModelFragment>();
	private ViewPager pager;
	private ArrayList<ItemImageInfo> datas;
	private ImageView addIv;
	private ImageView removeIv;
	private ImageView previewIv;
	private int currentPage = 0;
	private MaterialDialog removePageTipDialog;

	int screenWidth;
	int screenHeight;

	public static final int REQ_CODE_INIT = 1;
	public static final int REQ_CODE_ADD_PAGE = 2;
	private String filePath;
	private int pageSize = 0;

	private ImageView backiv;
	private ImageView menuMoreIv;

	private List<Page> mPages = new ArrayList<Page>();
	private MyTask myTask;
	int result = 0;
	private ComplitingDialog complitingDialog;

	private List<String> imagePaths;
	private List<String> imageUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_making);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		chooseImgs();
		addIv = (ImageView) findViewById(R.id.iv_add);
		removeIv = (ImageView) findViewById(R.id.iv_remove);
		previewIv = (ImageView) findViewById(R.id.iv_preview);
		backiv = (ImageView) findViewById(R.id.iv_back);
		menuMoreIv = (ImageView) findViewById(R.id.iv_menu_more);
		addIv.setOnClickListener(this);
		removeIv.setOnClickListener(this);
		previewIv.setOnClickListener(this);
		backiv.setOnClickListener(this);
		menuMoreIv.setOnClickListener(this);
		complitingDialog = new ComplitingDialog(this);

		imagePaths = new ArrayList<String>();
		imageUrls = new ArrayList<String>();
	}

	private void init() {
		pageSize = datas.size();
		contents = new String[pageSize];
		for (int i = 0; i < pageSize; i++) {
			fragments.add(new ModelOneFragment(datas.get(i).filePath));
			contents[i] = i + 1 + "";
		}
		tabs = (CirclePagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new FragmentViewPagerAdapter(
				this.getSupportFragmentManager(), pager, fragments, contents);
		// pager.setAdapter(adapter);
		tabs.setViewPager(pager);

		tabs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Log.e("onPageSelected", "position = " + position);
				currentPage = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQ_CODE_INIT:
			if (resultCode == RESULT_OK) {
				datas = (ArrayList<ItemImageInfo>) data
						.getSerializableExtra(ImagePickerPlusActivity.EXTRA_PICK_RETURN_DATA);
				init();
			} else {
				T.showShort(this, "操作取消！");
				finish();
			}
			break;
		case REQ_CODE_ADD_PAGE:
			if (resultCode == RESULT_OK) {
				ArrayList<ItemImageInfo> mDatas = (ArrayList<ItemImageInfo>) data
						.getSerializableExtra(ImagePickerPlusActivity.EXTRA_PICK_RETURN_DATA);
				filePath = mDatas.get(0).filePath;

				adapter.addPage(currentPage + 1, new ModelOneFragment(filePath));
				datas.add(currentPage + 1, mDatas.get(0));
				pageSize = fragments.size();
				if (currentPage < pageSize) {
					currentPage++;
				}

				tabs.setViewPager(pager);
				pager.setCurrentItem(currentPage);
			} else {
				T.showShort(this, "操作取消！");
			}
			break;
		}
	}

	private void chooseImgs() {
		Intent intent = new Intent(this, ImagePickerPlusActivity.class);
		intent.putExtra(ImagePickerPlusActivity.EXTRA_PICK_PHOTO_COUNT, 9); // 9张
		intent.putExtra(ImagePickerPlusActivity.EXTRA_DISK_CACHE_PATH,
				Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/diskcache");
		startActivityForResult(intent, REQ_CODE_INIT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			addPage();
			break;
		case R.id.iv_remove:
			removePage();
			break;
		case R.id.iv_preview:
			preview();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_menu_more:
			break;
		default:
			break;
		}
	}

	private void removePage() {
		if (removePageTipDialog == null) {
			removePageTipDialog = new MaterialDialog(this);
			removePageTipDialog
					.setTitle("提示")
					.setMessage("确定要删除当前页吗？")
					.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (fragments.size() == 1) {
								removePageTipDialog.dismiss();
								finish();
							} else {
								adapter.removePage(currentPage);
								datas.remove(currentPage);

								if (currentPage >= fragments.size()) {
									currentPage--;
								}

								tabs.setViewPager(pager);
								pager.setCurrentItem(currentPage);
								removePageTipDialog.dismiss();
							}
						}
					})
					.setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							removePageTipDialog.dismiss();
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
			removePageTipDialog.show();
		}
	}

	private void addPage() {
		Intent intent = new Intent(this, ImagePickerPlusActivity.class);
		intent.putExtra(ImagePickerPlusActivity.EXTRA_PICK_PHOTO_COUNT, 1); // 1张
		intent.putExtra(ImagePickerPlusActivity.EXTRA_DISK_CACHE_PATH,
				Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/diskcache");
		startActivityForResult(intent, REQ_CODE_ADD_PAGE);
	}

	private void preview() {
		if (BaseApplication.getInstance().isNetWorkAvailable(this)) {
			if (myTask != null
					&& myTask.getStatus() == AsyncTask.Status.RUNNING) {
				myTask.cancel(true);
			}
			myTask = new MyTask();
			myTask.execute();
		}
	}

	public class MyTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!complitingDialog.isShowing()) {
				complitingDialog.show();
			}
		}

		@Override
		protected Integer doInBackground(Void... params) {
			result = 0;
			for (BaseModelFragment fragment : fragments) {
				imagePaths.add(fragment.uploadImage());
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			uploadImages();
		}
	}

	private void uploadImages() {
		KJHttp kjh = KJHttp.create();
		HttpParams params = new HttpParams();
		params.put("token", BaseApplication.getInstance().getToken());
		for (int i = 0; i < imagePaths.size(); i++) {
			params.put("UploadImageForm[images][" + i + "]", new File(
					imagePaths.get(i)));
		}
		kjh.post(URLs.UPLOAD_IMG_URL, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					Log.e(TAG, "onSuccess:" + t);
					Result result;
					try {
						result = Result.parse(t);
						if (result.getResultCode() == 0) {
							T.showShort(MakingActivity.this, "图片上传成功！");
						} else {
							T.showShort(MakingActivity.this,
									result.getResultMsg());
						}
						if (complitingDialog.isShowing()) {
							complitingDialog.dismiss();
						}
					} catch (AppException e1) {
						e1.printStackTrace();
						T.showShort(MakingActivity.this, "操作失败！");
						if (complitingDialog.isShowing()) {
							complitingDialog.dismiss();
						}
					}
				}
			}
		});
	}

	private void uploadWorks() {
		for (int i = 0; i < fragments.size(); i++) {
			BaseModelFragment fragment = fragments.get(i);
			mPages.add(fragment.getPage(imageUrls.get(i), imagePaths.get(i)));
		}
		Works works = new Works();
		works.setSign("sign");
		works.setAuthor("author");
		works.setTitle("title");
		works.setPages(mPages);

		KJHttp kjh = KJHttp.create();
		HttpParams httpParams = new HttpParams();
		Gson g = new Gson();
		String temps = g.toJson(works);
		httpParams.put("temps", temps);
		kjh.post(URLs.updateWeiji, httpParams, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						result = 0;
					} else {
						result = 1;
					}
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				result = 0;
			}
		});
	}
}
