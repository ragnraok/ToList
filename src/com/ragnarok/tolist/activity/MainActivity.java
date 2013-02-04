package com.ragnarok.tolist.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.adapter.MainViewPageAdapter;
import com.viewpagerindicator.UnderlinePageIndicator;

public class MainActivity extends SherlockFragmentActivity {
	
	private ActionBar actionBar = null;
	private UnderlinePageIndicator indicator = null;;
	private ViewPager viewPager = null;

	private static final String NEW = "New";
	private static final String SETTING = "Setting";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_main);
		this.actionBar = this.getSupportActionBar();
		
		this.viewPager = (ViewPager) findViewById(R.id.pager);
		this.viewPager.setAdapter(new MainViewPageAdapter(getSupportFragmentManager()));
		
		this.indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		this.indicator.setViewPager(viewPager);
		this.indicator.setFadeDelay(500);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		
		menu.add(NEW)
		.setIcon(R.drawable.content_new)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(SETTING)
		.setIcon(R.drawable.action_settings)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().equals(NEW)) {
			Intent intent = new Intent(this, ThingEditActivity.class);
			this.startActivity(intent);
			this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		else if (item.getTitle().equals(SETTING)) {
			
		}
		
		return true;
	}

}
