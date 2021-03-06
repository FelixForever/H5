package com.linsen.h5.fragment.style;

import java.util.ArrayList;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.linsen.h5.R;
import com.linsen.h5.domain.Stylepage;
import com.linsen.h5.photopicker.ImagePickerPlusActivity;
import com.linsen.h5.photopicker.ItemImageInfo;
import com.linsen.h5.utils.T;
import com.linsen.h5.utils.URLs;
import com.linsen.h5.view.CallBackInterface;
import com.linsen.h5.view.MyLyImageView;
import com.linsen.h5.view.VerticalTextView;

public class SimpleFragment extends Fragment {

	public static final int SELECT_WORD = 1021;
	public static final int SELECT_A_IMG = 2015;

	private Context context;
	private MyLyImageView img;
	private VerticalTextView txt;
	boolean hasMeasured = false;
	public Stylepage stylepage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.style_fragment_simple,
				container, false);
		stylepage = (Stylepage) getArguments().getSerializable("stylepage");
		if(stylepage == null){
			stylepage = new Stylepage();
		}

		img = (MyLyImageView) layout.findViewById(R.id.sim_img);
		img.setFunction(new CallBackInterface() {

			@Override
			public void onMyClick() {
				Intent intent = new Intent(context,
						ImagePickerPlusActivity.class);
				intent.putExtra(ImagePickerPlusActivity.EXTRA_PICK_PHOTO_COUNT,
						1); // 1张
				intent.putExtra(ImagePickerPlusActivity.EXTRA_DISK_CACHE_PATH,
						Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "/diskcache");
				startActivityForResult(intent, SELECT_A_IMG);
			}

			@Override
			public void onMove() {

			}
		});
		txt = (VerticalTextView) layout.findViewById(R.id.sim_txt);
		txt.setFunction(new CallBackInterface() {
			@Override
			public void onMyClick() {
				// Intent intent = new Intent(context, AddWordActivity.class);
				// ((Activity) context)
				// .startActivityForResult(intent, SELECT_WORD);
			}

			@Override
			public void onMove() {
				int[] position = new int[2];
				txt.getLocationInWindow(position);
				stylepage.setWord1xy(position[0] + "," + position[1]);
				updatePage();
			}
		});

		ViewTreeObserver vto = img.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (hasMeasured == false) {
					setViews();
					hasMeasured = true;
				}
			}
		});

		return layout;
	}

	public void updatePage() {
		KJHttp kjh = KJHttp.create();
		HttpParams params = new HttpParams();
		Gson g = new Gson();
		String pageStr = g.toJson(stylepage);
		params.put("page", pageStr);
		kjh.post(URLs.updatePage, params, new HttpCallBack() {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_WORD:
			if (data == null)
				break;
			String word = data.getStringExtra("word");
			stylepage.setWord1(word);
			setWord(word);
			break;
		case SELECT_A_IMG:
			if (data == null)
				break;
			@SuppressWarnings("unchecked")
			ArrayList<ItemImageInfo> datas = (ArrayList<ItemImageInfo>) data
					.getSerializableExtra(ImagePickerPlusActivity.EXTRA_PICK_RETURN_DATA);
			String path = datas.get(0).filePath;
			stylepage.setLocalbackimg(path);
			setImg(path);
			break;
		}
	}

	public void setViews() {
		img.setImg(stylepage.getLocalbackimg(), stylepage.getBasepath());
		if (stylepage.getWord1() == null)
			return;
		txt.setText(stylepage.getWord1());
		int[] position = new int[2];
		txt.getLocationInWindow(position);
		stylepage.setWord1xy(position[0] + "," + position[1]);
		updatePage();
	}

	public void setWord(String word) {
		txt.setText(word);
	}

	public void setImg(String path) {
		img.setImg(path, stylepage.getBasepath());
	}
}
