package com.linsen.h5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

import com.linsen.h5.utils.T;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class AppException extends Exception {
	private static final long serialVersionUID = 7789729129679784382L;

	private final static boolean Debug = false;// 是否保存错误日志

	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_JSON = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_TOKEN = 0x08;
	public final static byte TYPE_LOGIN = 0x09;
	public final static byte TYPE_SERVER = 0x0a;

	private byte type;
	private int code;

	public AppException(String detailMessage) {
		super(detailMessage);
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (Debug) {
			this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error, this.getCode());
			T.showShort(ctx, err);
			break;
		case TYPE_HTTP_ERROR:
			T.showShort(ctx, R.string.http_exception_error);
			break;
		case TYPE_SOCKET:
			T.showShort(ctx, R.string.socket_exception_error);
			break;
		case TYPE_NETWORK:
			T.showShort(ctx, R.string.network_not_connected);
			break;
		case TYPE_JSON:
			T.showShort(ctx, R.string.json_parser_failed);
			break;
		case TYPE_IO:
			T.showShort(ctx, R.string.io_exception_error);
			break;
		case TYPE_RUN:
			T.showShort(ctx, R.string.app_run_code_error);
			break;
		case TYPE_TOKEN:
			T.showShort(ctx, R.string.token_error);
			break;
		case TYPE_LOGIN:
			T.showShort(ctx, R.string.need_login);
			break;
		case TYPE_SERVER:
			T.showShort(ctx, R.string.server_error);
			break;
		default:
			if (!TextUtils.isEmpty(getMessage())) {
				T.showShort(ctx, getMessage());
			}
		}
	}

	public void handle(Context ctx) {
		switch (this.getType()) {
		case TYPE_LOGIN:
			if (ctx instanceof Activity) {
				break;
			}
		default:
			makeToast(ctx);
		}
	}

	@SuppressWarnings("deprecation")
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mMSW/log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString()) + "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}

	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0, e);
	}

	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0, e);
	}

	public static AppException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new AppException(TYPE_IO, 0, e);
		}
		return run(e);
	}

	public static AppException json(Exception e) {
		return new AppException(TYPE_JSON, 0, e);
	}

	public static AppException login() {
		return new AppException(TYPE_LOGIN, 0, null);
	}

	public static AppException token() {
		return new AppException(TYPE_TOKEN, 0, null);
	}

	public static AppException server() {
		return new AppException(TYPE_SERVER, 0, null);
	}

	public static AppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof SocketTimeoutException) {
			return socket(e);
		}
		return http(e);
	}

	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}
}