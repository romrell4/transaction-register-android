package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.transactionregister.eric.transactionregisterandroid.R;
import com.transactionregister.eric.transactionregisterandroid.Support.TXFragment;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),  new TXFragment[] {new BudgetFragment(), new TransactionsFragment()}));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);
	}


	private class ViewPagerAdapter extends FragmentPagerAdapter {
		private TXFragment[] fragments;

		public ViewPagerAdapter(FragmentManager fm, TXFragment[] fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments[position].getTitle();
		}
	}
}
