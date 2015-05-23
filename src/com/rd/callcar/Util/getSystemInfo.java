package com.rd.callcar.Util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class getSystemInfo {
	public static String getVersionName(Activity activity) {
		// 获取packagemanager的实例
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(activity.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo.versionName;
	}

	public static int getVersionCode(Activity activity) {
		// 获取packagemanager的实例
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(activity.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo.versionCode;
	}
}
