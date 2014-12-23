package com.tagdroid.android.Pages;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;



public class StationDetailFragment extends Page {
    @Override
    public String getTitle() {
        return "Victor Hugo";
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_detail, container, false);


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                /**
                 * Get the new data from you data source.
                 * The swipeRefreshLayout needs to be notified when the data is returned
                 * in order for it to stop the animation.
                 **/
                handler.post(refreshing);
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    private final Runnable refreshing = new Runnable(){
        public void run(){
            try {
                if(false){
                    // RE-Run after 1 Second
                    handler.postDelayed(this, 1000);
                }else{
                    // Stop the animation once we are done fetching data.
                    swipeLayout.setRefreshing(false);
                    /**
                     * You can add code to update your list with the new data.
                     **/
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
