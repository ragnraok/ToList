package com.ragnarok.tolist.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ragnarok.tolist.R;
import com.ragnarok.tolist.activity.ThingEditActivity;
import com.ragnarok.tolist.db.ToListDB;
import com.ragnarok.tolist.object.Thing;
import com.ragnarok.tolist.utility.Constant;

public class ToTagListAdapter extends BaseAdapter {
	
	private class TypeValuePair {
		public static final int TAG = 1;
		public static final int THING = 2;
		
		public int type = 0;
		public Thing value = null;
		
		public TypeValuePair(int type, Thing value) {
			this.type = type;
			this.value = value;
		}
	}

	private Context context = null;
	private ArrayList<Thing> allThings = null;
	private ArrayList<TypeValuePair> thingStringList = null;
	private ToListDB toListDB = null;
	
	public ToTagListAdapter(Context context) {
		this.context = context;
		this.toListDB = new ToListDB(this.context);
		addAllThings();
		setupThingStringList();
	}
	
	public void addAllThings() {
		this.allThings = toListDB.getAllThings();
	}
	
	public void setupThingStringList() {
		thingStringList = new ArrayList<TypeValuePair>();
		Collections.sort(allThings, new Comparator<Thing>() {

			@Override
			public int compare(Thing lhs, Thing rhs) {
				// TODO Auto-generated method stub
				return lhs.title.compareTo(rhs.title);
			}
		});
		String currentTag = allThings.get(0).title.substring(0, 1);
		thingStringList.add(new TypeValuePair(TypeValuePair.TAG, allThings.get(0)));
		thingStringList.add(new TypeValuePair(TypeValuePair.THING, allThings.get(0)));
		
		for (int i = 1; i < allThings.size(); i++) {
			Thing thing = allThings.get(i);
			String tag = thing.title.substring(0, 1);
			if (!tag.equals(currentTag)) {
				currentTag = tag;
				thingStringList.add(new TypeValuePair(TypeValuePair.TAG, thing));
				thingStringList.add(new TypeValuePair(TypeValuePair.THING, thing));
			}
			else {
				thingStringList.add(new TypeValuePair(TypeValuePair.THING, thing));
			}
		}
	}
	
	public void reloadThings() {
		addAllThings();
		setupThingStringList();
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thingStringList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return thingStringList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TypeValuePair pair = this.thingStringList.get(position);
		final Thing thing = pair.value;
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (pair.type == TypeValuePair.TAG) {
			View view = inflater.inflate(R.layout.totag_line, null);
			TextView textView = (TextView) view.findViewById(R.id.text_tag);
			textView.setText(thing.title.substring(0, 1));
			
			return view;
		}
		else {
			final View view = inflater.inflate(R.layout.totitle_line, null);
			TextView textView = (TextView) view.findViewById(R.id.text_title);
			String title = thing.title;
			if (title.length() >= 15) {
				title = title.substring(0, 15);
			}
			textView.setText(title);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					view.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
					Intent intent = new Intent(context, ThingEditActivity.class);
					intent.putExtra(Constant.TITLE, thing.title);
					intent.putExtra(Constant.CONTENT, thing.content);
					intent.putExtra(Constant.PICT_PATH, thing.pictPath);
					context.startActivity(intent);
					((SherlockFragmentActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			});
			return view;
		}
	}

}
