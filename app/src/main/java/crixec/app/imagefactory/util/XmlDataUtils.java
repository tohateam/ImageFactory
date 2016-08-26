package crixec.app.imagefactory.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import crixec.app.imagefactory.core.ImageFactory;

public class XmlDataUtils {
	public static XmlDataUtils instance;
	public static SharedPreferences xml;
	public static SharedPreferences.Editor xmlEditor;

	private XmlDataUtils() {

	}

	public static XmlDataUtils getInstance() {
		if (instance == null) {
			instance = new XmlDataUtils();
		}
		return instance;
	}

	public void init() {
		xml = PreferenceManager.getDefaultSharedPreferences(ImageFactory.APP);
		xmlEditor = xml.edit();
	}

	public static String getString(String key) {
		return xml.getString(key, "");
	}

	public static void putString(String key, String value) {
		xmlEditor.putString(key, value);
		xmlEditor.commit();
	}

	public static boolean getBoolean(String key) {
		return xml.getBoolean(key, false);
	}

	public static void putBoolean(String key, boolean value) {
		xmlEditor.putBoolean(key, value);
		xmlEditor.commit();
	}
	public static void remove(String key){
		xmlEditor.remove(key);
		xmlEditor.commit();
	}
}
