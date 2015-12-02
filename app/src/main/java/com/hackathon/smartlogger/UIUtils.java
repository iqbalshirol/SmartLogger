package com.hackathon.smartlogger;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UIUtils {

	private static ProgressDialog mProgressDialog;
	private static final Hashtable<String, SoftReference<Typeface>> fontCache = new Hashtable<String, SoftReference<Typeface>>();
	private static Locale defaultDateLocale=Locale.ENGLISH;
	private static Object progressObject = new Object();
	/**
	 * To create Custom font for the called view.
	 * 
	 * @param v
	 *            View, which is called to set the custom font
	 * @param ctx
	 *            context of the view
	 * @param attrs
	 *            attributes passed in the tag
	 * @param attributeSet
	 *            attribute set created in the attrs.xml
	 * @param fontId
	 *            Custom font which is to be set
	 */
	public static void setCustomFont(View v, Context ctx, AttributeSet attrs, int[] attributeSet, int fontId) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, attributeSet);
		String customFont = a.getString(fontId);
		setCustomFont(v, ctx, customFont);
		a.recycle();
	}

	/**
	 * OVerloaded method of setCustomFont
	 * 
	 * @param v
	 *            View, which is called to set the custom font
	 * @param ctx
	 *            context of the view
	 * @param asset
	 *            Custom font which is to be set
	 * @return true, if it is properly set the customFont false, if it is not
	 *         able to set the customFont
	 */
	private static boolean setCustomFont(View v, Context ctx, String asset) {
		if (TextUtils.isEmpty(asset))
			return false;
		Typeface tf = null;
		try {

			tf = getFont(ctx, asset);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else {
				((Button) v).setTypeface(tf);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * To get the font type
	 * 
	 * @param c
	 *            context of the view
	 * @param name
	 *            Custom font which is to retrieve
	 * @return Returns the custom font reference
	 */
	private static Typeface getFont(Context c, String name) {
		synchronized (fontCache) {
			if (fontCache.get(name) != null) {
				SoftReference<Typeface> ref = fontCache.get(name);
				if (ref.get() != null) {
					return ref.get();
				}
			}

			Typeface typeface = Typeface.createFromAsset(c.getAssets(), name);
			fontCache.put(name, new SoftReference<Typeface>(typeface));

			return typeface;
		}
	}

	/**
	 * Draw image in circular shape Note: change the pixel size if you want
	 * image small or large
	 * 
	 * @param bitmap
	 * @return
	 */

	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffff0000;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(5, 5, 5, 5);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setStrokeWidth(400); // set the size
		paint.setDither(true); // set the dither to true
		paint.setStyle(Paint.Style.STROKE); // set to STOKE
		paint.setStrokeJoin(Paint.Join.ROUND); // set the join to round you want
		paint.setStrokeCap(Paint.Cap.ROUND); // set the paint cap to round too
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static void startProgressBar(Context context, String message) {
		synchronized (progressObject ) {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(context, "", message);
			}
		}
	}

	public static void stopProgressBar() {
		synchronized (progressObject) {
			try {
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			} catch (IllegalArgumentException e) {
				Log.e("UiUtils", e.getMessage());
			}
		}
	}

	/**
	 * Generate random GUID
	 * 
	 * @param context
	 * @return
	 */
	public static String getGUID(Context context) {
		synchronized(progressObject){
		/*
		 * String uniqueID = UUID.randomUUID().toString(); return uniqueID;
		 */
		return UUID.randomUUID().toString();
		}
	}

	public static CharSequence getTime(Long msgCreatedTime) {
		Date date = new Date(msgCreatedTime);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
		String min = cal.get(Calendar.MINUTE) < 10 ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE) + "";
		return hour + ":" + min + (cal.get(Calendar.AM_PM) == 0 ? " AM" : " PM");
	}

	public static boolean isSameDate(Date previousDate, Date currentDate) {

		Calendar previousCalendar = Calendar.getInstance();
		Calendar currentCalendar = Calendar.getInstance();
		previousCalendar.setTime(previousDate);
		currentCalendar.setTime(currentDate);

		boolean sameDay = previousCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
				&& previousCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateWithFormat(String dateFormat) {
		Date currentDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, defaultDateLocale);
		return sdf.format(currentDate);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDateWithFormat(long milliSeconds, String dateFormat) {
		Date currentDate = new Date(milliSeconds);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,defaultDateLocale);
		return sdf.format(currentDate);
	}

	public static void hideKeyboard(EditText editText) {

		InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static void playRingtone(Context context) {
		AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		int ringerMode = audiomanager.getRingerMode();
		if (ringerMode == 2) {
			try {
				Uri notification = RingtoneManager.getActualDefaultRingtoneUri(context,
						RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(context, notification);
				r.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (ringerMode == 1) {
			vibrator.vibrate(800);
		}
	}


	public static Bitmap getblurBitmap(Bitmap sentBitmap, int radius) throws OutOfMemoryError {

		Bitmap bitmap = sentBitmap.copy(Bitmap.Config.ARGB_8888, true);
		//Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return null;
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = i / divsum;
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static byte[] getCompressedBlurredbytes(File file, int width, int height, int mimeType, String guid) {
		Bitmap bmp = null;

		if (bmp == null) {
			return null;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// Compress the bitmap using ByteArrayOutputStream
		bmp.compress(CompressFormat.JPEG, 10, bos);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()), null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap sampledecodedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()), null,
				options);
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Getting the blurred image
		Bitmap blurredBitmap = null;
		try {
			blurredBitmap = UIUtils.getblurBitmap(sampledecodedBitmap, 15);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream bosBlurred = new ByteArrayOutputStream();
		// Again compressing the bitmap
		if (blurredBitmap == null)
			Log.d("UIUtils", "Null");
		else
			blurredBitmap.compress(CompressFormat.JPEG, 10, bosBlurred);
		byte[] compressBlurred = bosBlurred.toByteArray();
		try {
			bosBlurred.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compressBlurred;
	}

	private static void copyfile(Bitmap bmp, String filePath) {
		// create a file to write bitmap data
		File f = new File(filePath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();

		// write the bytes in file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(bitmapdata);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getKeyFromMilliseconds(Long milliseconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy",defaultDateLocale);
		Date createdDate = new Date(milliseconds);
		String date = sdf.format(createdDate);
		
		Date formattedDate = null;
		try {
			formattedDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		date = formattedDate.getTime()+"";
		return date;
	}

	/**
	 * loading smileys from assets
	 */
	public static Bitmap getImage(Context context, String path) {
		AssetManager mngr = context.getAssets();
		InputStream in = null;
		try {
			in = mngr.open("emoticons/" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Bitmap temp = BitmapFactory.decodeStream(in, null, null);
		return  BitmapFactory.decodeStream(in, null, null);
	}
	
	
	public static String convertFileSize(long fileSize) {
		long fSize = fileSize/1024;
		double m = fSize/1024.0;
		double g = fSize/1048576.0;
		double t = fSize/1073741824.0;
		
		String size = null;
		DecimalFormat dec = new DecimalFormat("0.00");
		DecimalFormat nodec = new DecimalFormat("0");
		
		if (t > 1) {
			size = dec.format(t).concat(" TB");
	    } else if (g > 1) {
	    	size = dec.format(g).concat(" GB");
	    } else if (m > 1) {
	    	size = dec.format(m).concat(" MB");
	    } else {
	    	size = nodec.format(fSize).concat(" KB");
	    }
		return size;
		
	}
	
	@SuppressLint("DefaultLocale")
	public static String getFormattedTime(String time) {
		Float milliValue = Float.valueOf(time);
		long millis = Long.parseLong(time);
		float value = milliValue/1000;
		int roundValue = Math.round(value);
		millis = roundValue*1000;
		String duration = "";

		if (TimeUnit.MILLISECONDS.toHours(millis) > 0) {
			// HH:MM:SS Format
			duration = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		} else {
			// MM:SS Format
			duration = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		}
		return duration;
	}
	
	public static float getDisplayMetrics(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int densityDpi = (int) (metrics.density * 160f);
		float formFactor = 0;
		if (densityDpi <= 160) {
			formFactor = 0.5f;
		} else if (densityDpi > 160 && densityDpi <= 240) {
			formFactor = 0.75f;
		} else if (densityDpi > 240 && densityDpi <= 320) {
			formFactor = 1f;
		} else if (densityDpi > 320) {
			formFactor = 1.5f;
		}
		return formFactor;
	}
	
	public static boolean isVersionKitkatOrAbove() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	public static boolean isVersionLollipop() {
		return Build.VERSION.SDK_INT == 21/*Build.VERSION_CODES.LOLLIPOP*/;
	}
	
	public static void rotateBitmapAsPerExifOrientation(String sourcPath, Context mContext) {
		int rotate = getExifOrientation(sourcPath);
		if (rotate == 0)
			return;
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		Bitmap bitmap;
		Bitmap bitmap1 = null;
		/*try {
			bitmap = BitmapFactory.decodeFile(sourcPath);
			bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {*/
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				bitmap = BitmapFactory.decodeFile(sourcPath, options);
				bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} catch (OutOfMemoryError e2) {
				e2.printStackTrace();
				Toast.makeText(mContext, "Out of Memory", Toast.LENGTH_SHORT).show();
			}
		//}
		try {

			if (bitmap1 != null) {
				FileOutputStream stream = new FileOutputStream(sourcPath);
				bitmap1.compress(CompressFormat.JPEG, 100, stream);
				stream.flush();
				stream.close();
			}
		} catch (Exception e) {
			Log.e("Could not save", e.toString());
		}
	}
	
	public static int getExifOrientation(String sourcepath) {

		int rotate = 0;
		try {
			File imageFile = new File(sourcepath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			default:
				// Do Nothing
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rotate;
	}

	/**
	* Check if the android application is being sent in the background (i.e behind
	* another application's Activity).
	* @param context
	*            the context
	* @return true if another application will be above this one.
	*/
	public static boolean isAppRunning(Context context) {
		// check with the first task(task in the foreground)
		// in the returned list of tasks
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
		if (services.get(0).topActivity.getPackageName().toString()
				.equalsIgnoreCase(context.getPackageName().toString())) {
			// your application is running in the background
			return true;
		}
		return false;
	}
}
