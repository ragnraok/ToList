package com.ragnarok.tolist.activity;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.db.ToListDB;
import com.ragnarok.tolist.object.Thing;
import com.ragnarok.tolist.utility.Constant;

public class ThingEditActivity extends SherlockActivity {
	
	private ActionBar actionbar = null;
	private static final String PHOTO = "Photo";
	private static final String ACCEPT = "Accept";
	
	private String title = null;
	private String content = null;
	private Bitmap image = null;
	
	private EditText titleEdit = null;
	private EditText contentEdit = null;
	private ImageView imageView = null;
	
	private Thing oldThing = null;
	private ToListDB tolistDB = null;
	
	private boolean ifModified = false;
	
	private static final int PHOTO_FROM_GALLERY = 1;
	private static final int PHOTO_FROM_CAMERA = 2;
	private static final int PHOTO_CLIP = 3;
	
	private static final int PHOTO_SIZE = 130;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thing_edit);

		actionbar = this.getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		this.titleEdit = (EditText) this.findViewById(R.id.edit_thing_title);
		this.contentEdit = (EditText) this.findViewById(R.id.edit_thing_content);
		this.imageView = (ImageView) this.findViewById(R.id.image_thing_image);
		
		setupView();
	}


	private void setupView() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey(Constant.TITLE) && bundle.containsKey(Constant.CONTENT) && 
				bundle.containsKey(Constant.PICT_PATH)) {
			oldThing = new Thing(bundle.getString(Constant.TITLE), bundle.getString(Constant.CONTENT), 
					bundle.getString(Constant.PICT_PATH));
			this.titleEdit.setText(oldThing.title);
			this.contentEdit.setText(oldThing.content);
			this.image = oldThing.getPictBitmap();
			this.imageView.setImageBitmap(image);
			
			this.ifModified = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(ACCEPT)
		.setIcon(R.drawable.accept)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(PHOTO)
		.setIcon(R.drawable.camera_photo)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub128
		//return super.onOptionsItemSelected(item);
		if (item.getItemId() == android.R.id.home) {
			finish();
			this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			return true;
		}
		else {
			if (item.getTitle().equals(ACCEPT)) {
				Log.d(Constant.LOG_TAG, "in save");
				title = this.titleEdit.getText().toString();
				content = this.contentEdit.getText().toString();
				if (title.length() == 0 || title.length() == 0)  {
					Toast.makeText(this, "Please input something", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				//BitmapDrawable backgroundDrawable = (BitmapDrawable) this.imageView.getBackground();
				//Log.d(Constant.LOG_TAG, backgroundDrawable == null ? "backgroundDrawable is Null" : "backgroundDrawable is not Null");
				//if (backgroundDrawable != null)
				//	image = backgroundDrawable.getBitmap();
				
				//Log.d(Constant.LOG_TAG, image == null ? "image is Null" : "image is not Null");
				
				if (tolistDB == null) {
					tolistDB = new ToListDB(this);
				}
				if (ifModified == false)
					tolistDB.addThing(title, content, image);
				else {
					tolistDB.modifyThing(oldThing.title, oldThing.content, oldThing.pictPath, title, content, image);
				}
				finish();
				this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
			
			if (item.getTitle().equals(PHOTO)) {
				getPhoto();
			}
			
			return true;
		}
		
		
	}
	
	private File tempFile = new File("/sdcard/a.jpg");
	
	private void getPhoto() {
		String[] imageFrom = {"Gallery", "Camera"};
		AlertDialog dialog = new AlertDialog.Builder(this).setItems(imageFrom, 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) { // from gallery
							getPictFromGallery();
						} 
						else {
							getPictFromCamera();
						}
					}
				}).create();
		dialog.show();
	}
	
	private void getPictFromGallery() {
		Intent intent = new Intent("android.intent.action.PICK");
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		intent.putExtra("output", Uri.fromFile(tempFile));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", PHOTO_SIZE);// 输出图片大小
		intent.putExtra("outputY", PHOTO_SIZE);
		startActivityForResult(intent, PHOTO_FROM_GALLERY);

	}
	
	private void getPictFromCamera() {
		//Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra("output", Uri.fromFile(tempFile));	
		//intent.putExtra("crop", "true");
		//intent.putExtra("aspectX", 1);// 裁剪框比例
		//intent.putExtra("aspectY", 1);
		//intent.putExtra("outputX", PHOTO_SIZE);// 输出图片大小
		//intent.putExtra("outputY", PHOTO_SIZE);
		startActivityForResult(intent, PHOTO_FROM_CAMERA);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PHOTO_FROM_GALLERY) {
				image = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
				imageView.setImageBitmap(image);
				tempFile.delete();
				
			}
			else if (requestCode == PHOTO_FROM_CAMERA) {
				Bitmap pict = data.getParcelableExtra("data");
				clipPhoto(pict);
	
			}
			else if (requestCode == PHOTO_CLIP) {
				if (data != null) {
					setImage(data);
				}
			}
			//Bitmap image = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
			//imageView.setImageBitmap(image);
			//this.image = image;
			//tempFile.delete();
		}
	}
	
	private void clipPhoto(Bitmap photo) {
		//Bitmap photo = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
		//tempFile.delete();
		Log.d(Constant.LOG_TAG, "start clip photo");
		if (photo != null) {
			Intent intent = new Intent("com.android.camera.action.CROP");
	        intent.setType("image/*");
	        intent.putExtra("data", photo);
	        intent.putExtra("crop", "true");
	        intent.putExtra("aspectX", 1);
	        intent.putExtra("aspectY", 1);
	        intent.putExtra("outputX", PHOTO_SIZE);
	        intent.putExtra("outputY", PHOTO_SIZE);
	        intent.putExtra("return-data", true);
	        startActivityForResult(intent, PHOTO_CLIP);
		}
	}
	
	private void setImage(Intent data) {
		Bitmap photo = data.getParcelableExtra("data");
		Log.d(Constant.LOG_TAG, "go to setImage");
		if (photo != null) {
			this.image = photo;
			this.imageView.setImageBitmap(photo);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		return true;
	}
	
	

}
