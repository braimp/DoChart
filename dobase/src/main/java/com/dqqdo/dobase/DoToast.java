/*
    ShengDao Android Client, NToast
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.dqqdo.dobase;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 *Toast 封装类。
 */
public class DoToast {

	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;
	
	public static void shortToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_SHORT);
	}
	
	public static void shortToast(Context context, String text) {
		if(!TextUtils.isEmpty(text) && !"".equals(text.trim())){
			showToast(context, text, Toast.LENGTH_SHORT);
		}
	}

	public static void longToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_LONG);
	}
	
	public static void longToast(Context context, String text) {
		if(!TextUtils.isEmpty(text) && !"".equals(text.trim())){
			showToast(context, text, Toast.LENGTH_LONG);
		}
	}
	
	public static void showToast(Context context, int resId, int duration) {
		if (context == null){
			return;
		}
		if (context != null && context instanceof Activity) {
	        if(((Activity) context).isFinishing()) {
	            return;
	        }
		}
		String text = context.getString(resId);
		showToast(context, text, duration);
	}
	
	public static void showToast(Context context, String s, int duration) {
		if (context == null){
			return;
		}
		if (context != null && context instanceof Activity) {
	        if(((Activity) context).isFinishing()) {
	            return;
	        }
		}
		if(!TextUtils.isEmpty(s) && !"".equals(s.trim())){
			if (toast == null) {
				toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
				toast.show();
				oneTime = System.currentTimeMillis();
			} else {
				if (s == null) s="null";
				twoTime = System.currentTimeMillis();
				if (s.equals(oldMsg)) {
					if (twoTime - oneTime > Toast.LENGTH_SHORT) {
						toast.show();
					}
				} else {
					oldMsg = s;
					toast.setText(s);
					toast.show();
				}
			}
			oneTime = twoTime;
		}
	}
}
