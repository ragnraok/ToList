package com.ragnarok.tolist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.adapter.ToTagListAdapter;
import com.ragnarok.tolist.utility.Constant;

public class TodoTagFragment extends SherlockFragment {
	
	private RelativeLayout mainLayout = null;
	private ListView listview = null;
	private ToTagListAdapter adapter = null;
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(Constant.LOG_TAG, "onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.adapter.reloadThings();
		Log.d(Constant.LOG_TAG, "onResume");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d(Constant.LOG_TAG, "onSaveInstanceState");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (adapter == null) {
			this.adapter = new ToTagListAdapter(this.getSherlockActivity());
		}
		listview.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_tag_list, null);
		listview = (ListView) mainLayout.findViewById(R.id.list);
		
		return mainLayout;
	}
}
