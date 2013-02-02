package com.ragnarok.tolist.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ragnarok.tolist.R;
import com.ragnarok.tolist.db.ToListDB;
import com.ragnarok.tolist.object.Thing;

public class ToListAdapter extends BaseAdapter {

	private Context context = null;
	public static ArrayList<Thing> thingList = null;
	private TextView titleView = null;
	private ToListDB toListDB = null;
	
	public ToListAdapter(Context context) {
		this.context = context;
		thingList = new ArrayList<Thing>();
		this.toListDB = new ToListDB(this.context);
		addAllThingInDB();
	}
	
	public void addAllThingInDB() {
		thingList = toListDB.getAllThings();
	}
	
	public void reloadThings() {
		this.addAllThingInDB();
		this.notifyDataSetChanged();
	}
	
	public void removeThing(int index) {
		Thing toRemoveThing = thingList.get(index);
		// first remove in db
		toListDB.removeThing(toRemoveThing.title, toRemoveThing.content, toRemoveThing.pictPath);
		// then remove in thingList
		thingList.remove(index);
		this.notifyDataSetChanged();
	}
	
	public void addThing(Thing thing) {
		// first add it in thingList
		thingList.add(0, thing);
		// then add it in db
		toListDB.addThing(thing.title, thing.content, thing.getPictBitmap());
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thingList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return thingList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view  = inflater.inflate(R.layout.tolist_grid, null);
		
		Thing thing = thingList.get(pos);
		
		titleView = (TextView) view.findViewById(R.id.text_tolist_grid);
		String title = thing.title;
		if (title.length() >= 15) {
			title = title.substring(0, 14) + "...";
		}
		titleView.setText(title);
		
		if (thing.getPict() != null) {
			view.setBackgroundDrawable(thing.getPict());
			titleView.setGravity(Gravity.BOTTOM | Gravity.LEFT);
		}
		else {
			view.setBackgroundColor(Color.rgb(255 - pos * 30, 75, 75));
			titleView.setGravity(Gravity.CENTER);
		}
		
		return view;
		
	}

	public static final ArrayList<Thing> sharedThingList() {
		return thingList;
	}


	
}
