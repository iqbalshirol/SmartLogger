package com.hackathon.smartlogger;

import java.io.Serializable;

/**
 * this interface is responsible for calling methods used in inserting data into
 * store
 * 
 * @author Endeavour
 * 
 */
public interface ISLSharedPreferences extends Serializable {

	void putInt(String key, int value);

	void putFloat(String key, float value);

	void putLong(String key, long value);

	void putBoolean(String key, boolean value);

	void putString(String key, String value);

	void remove(String key);

	void clear();

	int getInt(String key, int defValue);

	long getLong(String key, long defValue);

	float getFloat(String key, float defValue);

	boolean getBoolean(String key, boolean defValue);

	String getString(String key, String defValue);

	boolean contains(String string);
}
