package com.ruben.videosfutbol;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	private FragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		mAdapter = new FragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(2); //Esto es para que retenga en memoria 2 paginas a cada lado de la actual, como tenemos 3
									     //paginas siempre tendremos las 3 en memoria.

		mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
		int initialPage = mAdapter.getCount() / 2; //vamos a empezar en la pestanya del medio, es decir, en la de HOY
		mIndicator.setViewPager(mPager, initialPage);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
