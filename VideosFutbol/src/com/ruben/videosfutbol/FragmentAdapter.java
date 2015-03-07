package com.ruben.videosfutbol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	private String[] titles = { "Ayer", "Hoy", "Mañana" };

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public CharSequence getPageTitle(int position) {

		return titles[position];
	}

	@Override
	public Fragment getItem(int position) {

		return MyFragment.newInstance("This is fragment " + position, position);
	}

	@Override
	public int getCount() {

		return titles.length;
	}

}

