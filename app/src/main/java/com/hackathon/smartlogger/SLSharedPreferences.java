package com.hackathon.smartlogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This class is responsible for storing application related primitive
 * information into store.
 * 
 * @author Endeavour
 */
public class SLSharedPreferences implements ISLSharedPreferences {

	private static final long serialVersionUID = 1L;
	private final SharedPreferences mPreferences;
	public final Editor mEditor;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            A Context object to allow access to the default
	 *            mSharedPreferences object.
	 */

	@SuppressLint("CommitPrefEdits")
	public SLSharedPreferences(Context context) {
		mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences),
				Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	@Override
	public void putInt(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	@Override
	public void putLong(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
	}

	@Override
	public void putFloat(String key, float value) {
		mEditor.putFloat(key, value);
		mEditor.commit();
	}

	@Override
	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	@Override
	public void putString(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	@Override
	public int getInt(String key, int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	@Override
	public String getString(String key, String defValue) {
		return mPreferences.getString(key, defValue);
	}

	@Override
	public boolean contains(String key) {
		return mPreferences.contains(key);
	}

	@Override
	public void remove(String key) {
		mEditor.remove(key);
		mEditor.commit();
	}

	@Override
	public void clear() {
		mEditor.clear();
		mEditor.commit();
	}

}
