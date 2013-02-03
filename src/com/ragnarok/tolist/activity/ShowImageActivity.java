package com.ragnarok.tolist.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.utility.Constant;

public class ShowImageActivity extends SherlockActivity {

	private ImageView imageView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);
		imageView = (ImageView) findViewById(R.id.image);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle.containsKey(Constant.IMAGE)) {
			byte[] data = bundle.getByteArray(Constant.IMAGE);
			Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
			this.imageView.setImageBitmap(image);
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
