package com.linsen.h5.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class BitmapHelper {
	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取图片名称获取图片的资源id的方法
	 * 
	 * @param imageName
	 * @return
	 */
	public static int getResource(String imageName, Context context) {
		int resId = context.getResources().getIdentifier(imageName, "drawable",
				context.getPackageName());
		return resId;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmapByAssets(Context context, String url) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open(url);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 判断asset是否存在图片
	 * 
	 * @param context
	 * @param pt
	 * @return
	 */
	public static boolean hasAssetsFile(Context context, String pt) {
		AssetManager am = context.getAssets();
		try {
			String[] names = am.list("");
			for (int i = 0; i < names.length; i++) {
				if (names[i].equals(pt.trim())) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean saveToLocal(Bitmap bm, String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			bm.compress(CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 将图片变为圆角
	 * 
	 * @param bitmap
	 *            原Bitmap图片
	 * @param pixels
	 *            图片圆角的弧度(单位:像素(px))
	 * @return 带有圆角的图片(Bitmap 类型)
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static int getImageHeight(Resources res, int resId, int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		final int height = options.outHeight;
		final int width = options.outWidth;

		return reqWidth * height / width;
	}

	public static Bitmap createRepeater(int width, Bitmap src) {
		int count = (width + src.getWidth() - 1) / src.getWidth();// 计算出平铺填满所给width（宽度）最少需要的重复次数
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth() * count,
				src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {

			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		// bitmap = getResizedBitmap(bitmap, bitmap.getHeight(),
		// width);//缩小至与所给width值一样大小
		return bitmap;
	}

	public static Bitmap createBitmap(Bitmap bmp, int cWidth, int cHeight) {
		int srcWidth = bmp.getWidth();// 获取图片的原始宽度
		int srcHeight = bmp.getHeight();// 获取图片原始高度
		int destWidth = 0;
		int destHeight = 0;
		// 缩放的比例
		double ratioX = (double) cWidth / srcWidth;
		double ratioY = (double) cHeight / srcHeight;

		destWidth = (int) (srcWidth * ratioX);
		destHeight = (int) (srcHeight * ratioY);

		if (srcWidth < cWidth) {
			if (srcHeight >= cHeight) {
				ratioX = (double) cWidth / srcWidth;
				destWidth = cWidth;
				destHeight = (int) (srcHeight * ratioX);
			} else {
				ratioX = (double) cWidth / srcWidth;
				ratioY = (double) cHeight / srcHeight;
				if (ratioX > ratioY) {
					destWidth = cWidth;
					destHeight = (int) (srcHeight * ratioX);
				} else {
					destWidth = (int) (srcWidth * ratioY);
					destHeight = cHeight;
				}
			}
		} else {
			if (srcHeight >= cHeight) {
				ratioX = (double) cWidth / srcWidth;
				ratioY = (double) cHeight / srcHeight;
				if (ratioX < ratioY) {
					destWidth = (int) (srcWidth * ratioY);
					destHeight = cHeight;

				} else {
					destWidth = cWidth;
					destHeight = (int) (srcHeight * ratioX);
				}
			} else {
				ratioY = (double) cHeight / srcHeight;
				destHeight = cHeight;
				destWidth = (int) (srcWidth * ratioY);
			}
		}
		return Bitmap.createScaledBitmap(bmp, destWidth, destHeight, true);
	}

	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * 截图bmp
	 * 
	 * @param view
	 * @param filePath
	 */
	public static void saveBmp(Bitmap bmp, String filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
