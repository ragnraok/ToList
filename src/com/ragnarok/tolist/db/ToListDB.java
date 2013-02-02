package com.ragnarok.tolist.db;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;

import com.ragnarok.tolist.object.Thing;
import com.ragnarok.tolist.utility.Constant;
import com.ragnarok.tolist.utility.FileIOUtil;

public class ToListDB extends SQLiteOpenHelper {

	public static final String DB_NAME = "tolist";
	public static final int VERSION = 1;
	
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_PICT_PATH = "pict_path";
	
	private static final String CREATE_DB_SQL = "CREATE TABLE " + DB_NAME + " ( " + 
			BaseColumns._ID + " INT PRIMARY KEY, " + 
			COLUMN_TITLE + " TEXT NOT NULL, " +
			COLUMN_CONTENT + " TEXT NOT NULL, " +
			COLUMN_PICT_PATH + " TEXT " + ");";
	
	private Context context = null;
	
	public ToListDB(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DB_SQL);
		Log.d(Constant.LOG_TAG, "on db create");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * add one thing, if not contain pict, just pass null
	 * @param title
	 * @param content
	 * @param pict
	 */
	public boolean addThing(String title, String content, Bitmap pict) {
		//Log.d(Constant.LOG_TAG, "Start Add Thing");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE, title);
		values.put(COLUMN_CONTENT, content);
		if (pict != null) {
			String writePictPath = Constant.IMG_FOLDER + getWriteImgFilename();
			FileOutputStream fouts = FileIOUtil.getFileOutputStream(writePictPath);
			if (fouts != null) {
				BufferedOutputStream bos = new BufferedOutputStream(fouts);
				boolean success = pict.compress(Bitmap.CompressFormat.JPEG,
						100, bos);
				//Log.d(Constant.LOG_TAG, "successfully store thing image, path is " + writePictPath);
				try {
					bos.close();
					fouts.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				values.put(COLUMN_PICT_PATH, writePictPath);
				db.insert(DB_NAME, null, values);
		
				db.close();
				return success;
			}
			else {
				//Log.d(Constant.LOG_TAG, "in add thing pict is Null");
				return false;
			}
		}
		values.put(COLUMN_PICT_PATH, "");
		db.insert(DB_NAME, null, values);
		//Log.d(Constant.LOG_TAG, "finished add thing");
		db.close();
		return true;
	}
	
	public ArrayList<Thing> getAllThings() {
		ArrayList<Thing> things = new ArrayList<Thing>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
		cursor.moveToFirst();
		int count = cursor.getCount();
		Log.d(Constant.LOG_TAG, "in get things, thing number is " + cursor.getCount());
		for (int i = 0; i < count; i++) {
			String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
			String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
			String pictPath = cursor.getString(cursor.getColumnIndex(COLUMN_PICT_PATH));
			if (pictPath == null || pictPath.length() == 0) {
				things.add(new Thing(title, content));
			}
			else {
				things.add(new Thing(title, content, pictPath));
			}
			//Log.d(Constant.LOG_TAG, "getting things");
			if (cursor.moveToNext() == false) {
				break;
			}
		} 
		cursor.close();
		db.close();
		Collections.reverse(things);
		return things;
	}
	
	public boolean removeThing(String title, String content, String pictPath) {
		SQLiteDatabase db = this.getWritableDatabase();
		int num = db.delete(DB_NAME, COLUMN_CONTENT + "=" + "'" + content + "'" + " AND " + COLUMN_TITLE + "=" + "'" + title + "'", 
				null);
		db.close();
		if (num >= 1) {
			//Log.d(Constant.LOG_TAG, "pictPath is " + pictPath);
			if (pictPath != null && pictPath.length() >= 0) {
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + pictPath);
				if (file.isFile() && file.exists()) {
					return file.delete();
				}
			}
			return true;
		}
		else {
			return false; // not exist or remove failed
		}
	}
	
	public boolean modifyThing(String oldTitle, String oldContent, String oldImgPath, 
			String newTitle, String newContent, Bitmap newBitmap) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		// get the old image path
		Cursor cursor = db.query(DB_NAME, null, 
				COLUMN_CONTENT + "=" + "'" + oldContent + "'" + " AND " + COLUMN_TITLE + "=" + "'" + oldTitle + "'", 
				null, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() >= 1) {
			//int pathIndex = cursor.getColumnIndex(COLUMN_PICT_PATH);
			//String path = cursor.getString(pathIndex);
			
			ContentValues updateValues = new ContentValues();
			updateValues.put(COLUMN_TITLE, newTitle);
			updateValues.put(COLUMN_CONTENT, newContent);
			
			if (newBitmap != null) {
				String writePictPath = Constant.IMG_FOLDER + getWriteImgFilename();
				FileOutputStream fouts = FileIOUtil.getFileOutputStream(writePictPath);
				if (fouts != null) {
					BufferedOutputStream bos = new BufferedOutputStream(fouts);
					boolean success = newBitmap.compress(Bitmap.CompressFormat.JPEG,
							100, bos);
					try {
						bos.close();
						fouts.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateValues.put(COLUMN_PICT_PATH, writePictPath);
					db.update(DB_NAME, updateValues,
							COLUMN_CONTENT + "=" + "'" + oldContent + "'" + " AND " + COLUMN_TITLE + "=" + "'" + oldTitle + "'", null);
					db.close();
					cursor.close();
					return success;
				}
				else {
					return false;
				}
			}
			updateValues.put(COLUMN_PICT_PATH, oldImgPath);
			
			db.update(DB_NAME, updateValues,
					COLUMN_CONTENT + "=" + "'" + oldContent + "'" + " AND " + COLUMN_TITLE + "=" + "'" + oldTitle + "'", null);
			db.close();
			cursor.close();
			return true;
		}
		return false;
	}
	
	private String getWriteImgFilename() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
}




