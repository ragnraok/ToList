package com.ragnarok.tolist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.activity.ThingEditActivity;
import com.ragnarok.tolist.adapter.ToListAdapter;
import com.ragnarok.tolist.db.ToListDB;
import com.ragnarok.tolist.object.Thing;
import com.ragnarok.tolist.utility.Constant;

public class TodoFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener {

	private RelativeLayout layout = null;
	private GridView gridView = null;
	private ActionMode actionMode = null;
	private ToListAdapter adapter = null;
	
	private static final String DONE = "Done";
	
	// Test Code
	//private ToListDB toListDB = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
		
		return layout;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.adapter.reloadThings();
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
		SherlockFragmentActivity parent =  (SherlockFragmentActivity)(this.getActivity());
		this.actionMode = parent.startActionMode(new EditThingMode(pos));
		view.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_out));
		view.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in));
		return true;
	}
	
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
	
	
}
