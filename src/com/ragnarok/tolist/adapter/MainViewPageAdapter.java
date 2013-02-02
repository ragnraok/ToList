package com.ragnarok.tolist.adapter;

import com.ragnarok.tolist.fragment.TodoFragment;
import com.ragnarok.tolist.fragment.TodoTagFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPageAdapter extends FragmentPagerAdapter {

	private final Fragment[] fragments = {null, null};
	private final String[] title = {"List", "Tags"};
	
	public MainViewPageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		if (pos == 0) {
			if (fragments[0] == null) {
				fragments[0] = new TodoFragment();
			}
		}
		else if (pos == 1) {
			if (fragments[1] == null) {
				fragments[1] = new TodoTagFragment();
			}
		}
		return fragments[pos];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return title[position];
	}


}
