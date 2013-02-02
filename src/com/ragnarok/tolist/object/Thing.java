package com.ragnarok.tolist.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.ragnarok.tolist.utility.Constant;

public class Thing {
	public String title = null;
	public String content = null;
	public boolean ifHasPict = false;
	public String pictPath = null;
	
	public Thing(String title, String content) {
		this.title = title;
		this.content = content;
		this.ifHasPict = false;
	}
	
	public Thing(String title, String content, String pictPath) {
		this.title = title;
		this.content = content;
		if (pictPath != null) {
			this.ifHasPict = true;
			this.pictPath = pictPath;
		}
	}
	
	/**
	 * get the background picture, if ifHasPict is false
	 * return null
	 * @return
	 */
	public Drawable getPict() {
		//Log.d(Constant.LOG_TAG, "in getPict, pictPath is " + this.pictPath + "ifHasPit is " + ifHasPict);
		if (ifHasPict == false) {
			return null;
		}
		else {
			//Log.d(Constant.LOG_TAG, "in getPict, pictPath is " + this.pictPath);
			Drawable pict = Drawable.createFromPath(Environment.getExternalStorageDirectory().getAbsoluteFile() + this.pictPath);
			//Log.d(Constant.LOG_TAG, pict == null ? "in getPict, pict is Null" : "in getPict, pict is not Null");
			return pict;
		}
	}
	
	public Bitmap getPictBitmap() {
		if (ifHasPict == false) {
			return null;
		}
		else {
			//Log.d(Constant.LOG_TAG, "in getPictBitmap, pictPath is " + this.pictPath);
			Bitmap image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsoluteFile() + this.pictPath);
			//Log.d(Constant.LOG_TAG, image == null ? "in getPictBitmap, image is Null" : "in getPictBitmap, image is not Null");
			return image;
		}
	}
	
}
