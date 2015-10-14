package com.linsen.h5.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linsen.h5.R;
import com.linsen.h5.domain.Stylepage;
import com.linsen.h5.domain.Weiji;
import com.linsen.h5.fragment.style.SimpleFragment;
import com.linsen.h5.photopicker.ImagePickerPlusActivity;
import com.linsen.h5.photopicker.ItemImageInfo;
import com.linsen.h5.utils.NetWorkUtil;
import com.linsen.h5.utils.T;
import com.linsen.h5.utils.URLs;
import com.linsen.h5.view.TabletTransformer;

public class StyleActivity extends FragmentActivity {
	public static final String FILES = "files";
	public static final String CURRENT_INDEX = "currentIndex";
	private final int CAMERA_WITH_DATA = 3023;
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	private final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory()
					+ "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;
	public static final int CROP = 1096;
	private final int PIC_BACK = 2015;
	private PopupWindow imgWindow;
	private ImageView imgDelete;
	private Context context;
	private PopupWindow musicWin;
	private final int SELECT_MUSIC = 1008;
	// 上传到后台
	private Weiji weiji = new Weiji();
	private List<Stylepage> pageList = new ArrayList<Stylepage>();

	TextView tvmusic;
	private ViewPager mPager;
	private PageAdapter mAdapter;
	private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
	private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
	private int mSelectedItem;

	private ArrayList<ItemImageInfo> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stylemain);
		Intent intent = new Intent(this, ImagePickerPlusActivity.class);
		intent.putExtra(ImagePickerPlusActivity.EXTRA_PICK_PHOTO_COUNT, 9); // 9张
		intent.putExtra(ImagePickerPlusActivity.EXTRA_DISK_CACHE_PATH,
				Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/diskcache");
		startActivityForResult(intent, 1);
	}

	protected void initViews() {
		mAdapter = new PageAdapter(this.getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.container);
		pageList.addAll(this.getPages(datas));
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mPager.setOffscreenPageLimit(10);
		mPager.setPageMargin((int) getResources().getDimensionPixelOffset(
				R.dimen.small_s_space));
		mPager.setPageTransformer(true, new TabletTransformer());
	}

	protected void initEvents() {

	}

	class PageAdapter extends FragmentStatePagerAdapter {
		public PageAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Stylepage stylepage = pageList.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("stylepage", stylepage);
			Fragment fragment;
			switch (weiji.getStyle()) {
			case 0: // 简单模式
				fragment = new SimpleFragment();
				break;
			default:
				fragment = new SimpleFragment();
				break;
			}
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public float getPageWidth(int position) {
			return 0.9f;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentStatePagerAdapter.POSITION_NONE;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				datas = (ArrayList<ItemImageInfo>) data
						.getSerializableExtra(ImagePickerPlusActivity.EXTRA_PICK_RETURN_DATA);
				initViews();
			} else {
				T.showShort(context, "操作取消！");
				finish();
			}
			break;
		}
	}

	private String allword = "";
	String presign = "";
	String basepath = "";
	int userid = 0;

	public void saveWeiji() {
		if (!NetWorkUtil.isNetworkConnected(this)) {
			T.showLong(context, "网络未连接！");
			return;
		}
		if (allword.equals("")) {
			allword = "美页秀秀，酷炫的html5特效";
		}
		allword = allword.trim();
		final KJHttp kjh = KJHttp.create();
		final HttpParams params = new HttpParams();
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		String username = sharedPreferences.getString("username", "123");
		userid = sharedPreferences.getInt("userid", 0);
		weiji.setUserid(userid);
		weiji.setUsername(username);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		String dateStr = df.format(new Date());// new Date()为获取当前系统时间
		basepath = URLs.BasePath + userid + "/" + dateStr + "/";
		presign = userid + dateStr;
		weiji.setPresign(presign);
		weiji.setBasepath(basepath);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String dateStr1 = df1.format(new Date());// new Date()为获取当前系统时间
		weiji.setSavetime(dateStr1);
		weiji.setAudiopath1("/weiji/Uploads/" + basepath + "1.mp3");
		weiji.setAudiopath2("/weiji/Uploads/" + basepath + "2.mp3");
		weiji.setTitle("一页，酷炫的html5特效");
		weiji.setAllword("一页，酷炫的html5特效");
		Gson g = new Gson();
		String weijiStr = g.toJson(weiji);
		params.put("weijiStr", weijiStr);
		kjh.post(URLs.saveWeiji, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						T.showShort(context, "网络开小差啦！");
						return;
					}
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				T.showShort(context, "小伙伴抱歉，上传失败了");
			}
		});
	}

	public void deleteWeiji() {
		KJHttp kjh = KJHttp.create();
		HttpParams params = new HttpParams();
		params.put("presign", weiji.getPresign());
		kjh.post(URLs.deleteWeiji, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						T.showShort(context, "网络开小差啦！");
						return;
					}
				}
			}
		});
	}

	public void deletePage(Stylepage stylepage) {
		KJHttp kjh = KJHttp.create();
		HttpParams params = new HttpParams();
		params.put("pagesign", stylepage.getPagesign());
		kjh.post(URLs.deletePage, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						T.showShort(context, "网络开小差啦！");
						return;
					}
				}
			}
		});
	}

	public void updateWeiji() {
		KJHttp kjh = KJHttp.create();
		HttpParams params = new HttpParams();
		Gson g = new Gson();
		String weijiStr = g.toJson(weiji);
		params.put("weijiStr", weijiStr);
		kjh.post(URLs.updateWeiji, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						T.showShort(context, "网络开小差啦！");
						return;
					}
				}
			}
		});
	}
	
	public List<Stylepage> getPages(ArrayList<ItemImageInfo> myiles)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String dateStr=df.format(new Date());// new Date()为获取当前系统时间
		final KJHttp kjh = KJHttp.create();
		final HttpParams params = new HttpParams();
		List<Stylepage> stylepages =new ArrayList<Stylepage>();
		for(int i=0;i<myiles.size();i++)
		{

			Stylepage stylepage =new Stylepage();
			stylepage.setLocalbackimg(myiles.get(i).filePath);
			stylepage.setPresign(presign);
			stylepage.setPagesign(userid + dateStr + i);
			String imgName=myiles.get(i).filePath.substring(myiles.get(i).filePath.lastIndexOf("/")+1);
			stylepage.setBackimg("/weiji/Uploads/" + basepath + imgName);
			if(i==0)
				weiji.setThumbimage("/weiji/Uploads/" + basepath + imgName);
			stylepage.setBasepath(basepath);
			stylepages.add(stylepage);
		}
		Gson g=new Gson();
		String pageListStr= g.toJson(stylepages);
		params.put("pageListStr", pageListStr);
		kjh.post(URLs.savePages, params, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					if (t.indexOf("<") != -1 || t.indexOf(">") != -1) {
						T.showShort(context, "网络开小差啦！");
						return;
					}
				}
			}
		});
		return stylepages;
	}
	//{"username":"linsen","title":"《美好的一天》","audiopath":"xxx","temps":[{},{}]}
	//主体大概是这样，有新元素再加；格式都是一样，唯一不同的是temps这个字段，它的内容大概是底下这样是一个列表，列表的一个元素代表一页，每页可以是不同的模版，不同的模版会有不同的参数。
	//"temps":[{"tempId":254,"rotate":2.617,"scale":1.089,"transX":120.982,"transY":234.89,"imageUrl":"http://www.h5.com/imagePath/31531379.jpg","imagePath":"/storage/0/h5/img/542162323.jpg","text":"H5真是太炫了\n我很喜欢！"，"textSize":18,"textColor":#8c8c8c},{...}]
	//imageUrl是上传图片时后台返回的，imagePath是手机本地存储的
	
	
	
	
}
