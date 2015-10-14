package com.linsen.h5.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.linsen.h5.AppException;
import com.linsen.h5.BaseActivity;
import com.linsen.h5.R;
import com.linsen.h5.dialog.ComplitingDialog;
import com.linsen.h5.domain.Result;
import com.linsen.h5.utils.StringUtil;
import com.linsen.h5.utils.T;
import com.linsen.h5.utils.URLs;
import com.linsen.h5.utils.ValidateUtils;

public class RegistorActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "RegistorActivity";
	private TextView headerTitleTv;
	private Button loginBtn;
	private EditText usernameEt;
	private EditText passwordEt;
	private String username;
	private String password;
	private ComplitingDialog complitingDialog;
	private ImageView backIv;

	// private HttpResult httpResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registor);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		headerTitleTv = (TextView) findViewById(R.id.header_title_tv);
		headerTitleTv.setText("注册");
		backIv = (ImageView) findViewById(R.id.header_back_left_iv);
		backIv.setVisibility(View.VISIBLE);
		loginBtn = (Button) findViewById(R.id.btn_registor);
		usernameEt = (EditText) findViewById(R.id.et_username);
		passwordEt = (EditText) findViewById(R.id.et_password);
		username = mApplication.getUsername();
		password = mApplication.getPassword();
		if (username != null && !username.equals("")) {
			usernameEt.setText(username);
		}
		if (password != null && !password.equals("")) {
			passwordEt.setText(password);
		}
		complitingDialog = new ComplitingDialog(this);
	}

	@Override
	protected void initEvents() {
		loginBtn.setOnClickListener(this);
		backIv.setOnClickListener(this);
	}

	private boolean validate() {
		username = usernameEt.getText().toString().trim();
		password = passwordEt.getText().toString().trim();
		if (StringUtil.isNull(username)) {
			T.showShort(this, "请输入账号");
			usernameEt.requestFocus();
			return false;
		}
		if (!ValidateUtils.isMobile(username)) {
			T.showShort(this, "账号格式错误");
			usernameEt.requestFocus();
			return false;
		}
		if (StringUtil.isNull(password)) {
			T.showShort(this, "请输入密码");
			passwordEt.requestFocus();
			return false;
		}
		if (password.length() < 6 || password.length() > 12) {
			T.showShort(this, "密码错误");
			passwordEt.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_registor:
			if (mApplication.isNetWorkAvailable(this) && validate()) {
				doRegistor();
			}
			break;
		case R.id.header_back_left_iv:
			defaultFinish();
			break;
		}
	}

	private void doRegistor() {
		if (!complitingDialog.isShowing()) {
			complitingDialog.show();
		}
		KJHttp kjh = KJHttp.create();
		HttpParams httpParams = new HttpParams();
		Log.e(TAG,
				"mobile:" + username + ",passwd:"
						+ StringUtil.getMD5Str(password));
		httpParams.put("mobile", username);
		httpParams.put("passwd", StringUtil.getMD5Str(password));
		kjh.post(URLs.REGISTER_URL, httpParams, new HttpCallBack() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != "") {
					Log.e(TAG, "onSuccess:" + t);
					Result result;
					try {
						result = Result.parse(t);
						if (result.getResultCode() == 0) {
							JSONObject object;
							try {
								object = new JSONObject(result.getResultBody());
								mApplication.setToken(object.getString("token"));
								Log.e(TAG, "token:" + mApplication.getToken());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							T.showShort(RegistorActivity.this, "注册成功！");
							startActivity(MainActivity.class);
							finish();
						} else {
							T.showShort(RegistorActivity.this,
									result.getResultMsg());
						}
					} catch (AppException e) {
						e.printStackTrace();
					}

				}
				if (complitingDialog.isShowing()) {
					complitingDialog.dismiss();
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				T.showShort(RegistorActivity.this, "注册失败！");
				if (complitingDialog.isShowing()) {
					complitingDialog.dismiss();
				}
			}
		});
	}

}
