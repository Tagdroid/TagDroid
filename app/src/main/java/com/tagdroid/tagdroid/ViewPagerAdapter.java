package com.tagdroid.tagdroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	private Context _context;

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
 
    /*public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;
 
        }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
        case 0:
            f=TicketActivity.newInstance(_context);
            break;
        case 1:
            f=PassActivity.newInstance(_context);
            break;
        case 2:
        	f=CombinesActivity.newInstance(_context);
        	break;
        }
        return f;
    }
    @Override
    public int getCount() {
        return 3;
    }*/
 
}