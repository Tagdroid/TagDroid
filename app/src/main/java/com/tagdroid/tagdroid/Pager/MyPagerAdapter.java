package com.tagdroid.tagdroid.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
		protected static final String[] CONTENT = new String[] {"1","2","3","4","5","6"};
		private int mCount = CONTENT.length;
		
        public MyPagerAdapter(FragmentManager fm) {
                super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	return MyFragment.newInstance(CONTENT[position % CONTENT.length]);
        }
        
        @Override
        public int getCount() {
            return mCount;
        }
        

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
            }
        }
}
