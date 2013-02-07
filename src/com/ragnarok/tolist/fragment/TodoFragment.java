package com.ragnarok.tolist.fragment;

import android.R.anim;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.activity.MainActivity;
import com.ragnarok.tolist.activity.ThingEditActivity;
import com.ragnarok.tolist.adapter.ToListAdapter;
import com.ragnarok.tolist.object.Thing;
import com.ragnarok.tolist.utility.Constant;

public class TodoFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener, OnTouchListener {

	private RelativeLayout layout = null;
	private GridView gridView = null;
	private View bottomLayout = null;
	private View dragView = null;
	
	private int deleteIndex = 0;	
	private WindowManager.LayoutParams params = null;
	
	private ActionMode actionMode = null;
	private ToListAdapter adapter = null;
	
	private static final String DONE = "Done";
	
	private boolean isDragging = false;
	private int originItemBackgroundColor = 0;
	private int originBottomBackgroundColor = 0;
	
	// Test Code
	//private ToListDB toListDB = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		startNotification();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = (RelativeLayout) inflater.inflate(R.layout.fragment_list, null);
		
		this.gridView = (GridView) layout.findViewById(R.id.gridview);
		this.gridView.setOnItemClickListener(this);
		this.gridView.setOnItemLongClickListener(this);
		
		//this.toListDB = new ToListDB(this.getActivity());
		//for (int i = 0; i < 10; i++) { 
		//	toListDB.addThing("Ragnarok Title", "Content", null);
		//}
		adapter = new ToListAdapter(this.getActivity());
		this.gridView.setAdapter(adapter);
		this.gridView.setOnTouchListener(this);
		
		this.bottomLayout = layout.findViewById(R.id.bottom_layout);
		this.bottomLayout.setVisibility(View.INVISIBLE);

		this.originBottomBackgroundColor = Color.parseColor("#2E2E2E");
		
		return layout;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.adapter.reloadThings();
	}
	
	private void startNotification() {
		String message = "you have " + this.adapter.getCount() + " things to do";
		TextView notifyView = new TextView(this.getActivity());
		notifyView.setText(message);
		notifyView.setTextSize(25f);
		
		NotificationManager manager = (NotificationManager) this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		PendingIntent intent = PendingIntent.getActivity(this.getActivity(), 1, 
				new Intent(this.getActivity(), MainActivity.class),  PendingIntent.FLAG_ONE_SHOT);
		notification.setLatestEventInfo(this.getActivity(), getString(R.string.app_name), message, intent);
		manager.notify(Constant.NOTIFY_ID, notification);
	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Thing thing = ToListAdapter.sharedThingList().get(arg2);
		Intent intent = new Intent(this.getActivity(), ThingEditActivity.class);
		intent.putExtra(Constant.TITLE, thing.title);
		intent.putExtra(Constant.CONTENT, thing.content);
		intent.putExtra(Constant.PICT_PATH, thing.pictPath);
		this.startActivity(intent);
		this.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		/*
		SherlockFragmentActivity parent =  (SherlockFragmentActivity)(this.getActivity());
		this.actionMode = parent.startActionMode(new EditThingMode(pos));*/
		
		this.originItemBackgroundColor = Color.rgb(255 - pos * 30, 75, 75);
		
		view.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out));
		view.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in));
		
		deleteIndex = pos;
		dragView = this.gridView.getChildAt(pos);
		dragView.destroyDrawingCache();
		dragView.setDrawingCacheEnabled(true);
		dragView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
		Bitmap tempBitmap = Bitmap.createBitmap(dragView.getDrawingCache());
		Bitmap dragBitmap = Bitmap.createScaledBitmap(tempBitmap, (int)(tempBitmap.getWidth() * 1.1), 
				(int)(tempBitmap.getHeight() * 1.1), false);
		dragView.setVisibility(View.INVISIBLE);
		addDragBitmapInScreen(dragBitmap);
		isDragging = true;
		return true;
	}
	
	private void addDragBitmapInScreen(Bitmap dropBitmap) {
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = dragView.getLeft() + 8;
		params.y = (int) (dragView.getTop() + 45 * 0.01);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.alpha = 0.8f;
		
		ImageView imageView = new ImageView(this.getActivity());
		imageView.setImageBitmap(dropBitmap);
		WindowManager windowManager = this.getActivity().getWindowManager();
		windowManager.addView(imageView, params);
		
		this.dragView = imageView;
		showDeleteView();
	}
	
	private void showDeleteView() {
		int height = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();
		TranslateAnimation animation = new TranslateAnimation(0, 0, height * 1.5f, 0);
		animation.setDuration(500);
		this.bottomLayout.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				bottomLayout.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.d(Constant.LOG_TAG, "onTouch");
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (isDragging) {
				dragView.setBackgroundColor(originItemBackgroundColor);
				bottomLayout.setBackgroundColor(originBottomBackgroundColor);
				int x = (int) event.getX();
				int y = (int) event.getY();

				// update dragView position;
				params.x = x - dragView.getWidth() / 2;
				params.y = y - dragView.getHeight() / 2;
				if (x <= 0) {
					params.x = 0;
				}
				if (y <= 0) {
					params.y = 0;
				}
				WindowManager windowManager = this.getActivity()
						.getWindowManager();
				windowManager.updateViewLayout(dragView, params);

				// check if delete
				int bottomY = bottomLayout.getTop();
				if (y >= bottomY) {
					dragView.setBackgroundColor(Color.RED);
					bottomLayout.setBackgroundColor(Color.RED);
				}
				return true;
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {	
			if (isDragging) {
				int y = (int) event.getY();
				int bottomY = bottomLayout.getTop();
				if (y >= bottomY) {
					this.adapter.removeThing(deleteIndex);
					Animation animation = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in);
					animation.setDuration(1000);
					this.gridView.startAnimation(animation);
				}
				restoreOriginView();
				isDragging = false;
				return true;
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			restoreOriginView();
		}
		return false;
	}
	
	private void restoreOriginView() {
		if (dragView != null) {
			WindowManager windowManager = this.getActivity().getWindowManager();
			windowManager.removeView(dragView);
		}
		dragView = null;
		this.bottomLayout.setBackgroundColor(originBottomBackgroundColor);
		Animation fadeOutAnim = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out);
		this.bottomLayout.startAnimation(fadeOutAnim);
		fadeOutAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				bottomLayout.setVisibility(View.INVISIBLE);
			}
		});

		this.adapter.reloadThings();
	}
	
/*	
	private final class EditThingMode implements ActionMode.Callback {
		
		private int elemntIndex = 0;
		
		public EditThingMode(int index) {
			this.elemntIndex = index;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			//return false;
			menu.add(DONE)
			.setIcon(R.drawable.done)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			if (item.getTitle().equals(DONE)) {
				adapter.removeThing(this.elemntIndex);
			}
			if (actionMode != null) {
				actionMode.finish();
			}
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			
		}
		
	}
*/	
	
}
