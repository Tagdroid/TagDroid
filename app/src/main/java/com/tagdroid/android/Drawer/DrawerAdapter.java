package com.tagdroid.android.Drawer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tagdroid.android.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;

    public DrawerAdapter(Context context, int layoutResourceID){
        super(context, layoutResourceID);
        this.context = context;
        this.layoutResID = layoutResourceID;
        setItems();
    }

    private void setItems() {
        Resources res = context.getResources();

        // Containing the list of our items
        drawerItemList = new ArrayList<>();
        // "Emulate" the Header
        drawerItemList.add(new DrawerItem());
        // Onglets principaux avec icones
        TypedArray titlesArray = res.obtainTypedArray(R.array.drawer_items_titles);
        TypedArray iconesArray = res.obtainTypedArray(R.array.drawer_items_icons);
        TypedArray iconesSelectedArray = res.obtainTypedArray(R.array.drawer_items_icons_selected);

        for (int i = 0; i < titlesArray.length(); i ++)
            drawerItemList.add(new DrawerItem()
                    .setTitle(titlesArray.getString(i))
                    .setIcon(iconesArray.getResourceId(i, 0))
                    .setIconSelected(iconesSelectedArray.getResourceId(i, 0)));

        // Divider
        drawerItemList.add(new DrawerItem()
                .setDivider());

        // Onglets secondaires sans icones
        titlesArray = res.obtainTypedArray(R.array.drawer_items_plus_titles);
        for (int i = 0; i < titlesArray.length(); i++)
            drawerItemList.add(new DrawerItem()
                    .setTitle(titlesArray.getString(i)));
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        // Header
        if (position == 0)
            return LayoutInflater.from(context).inflate(R.layout.main_drawer_header, parent, false);
        // Divider
        if (drawerItemList.get(position).isDivider)
            return divider(parent);

        Log.d("getView", ""+position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_drawer_item, parent, false);

        DrawerItem drawerItem = drawerItemList.get(position);
        DrawerItemHolder drawerItemHolder = new DrawerItemHolder();

        drawerItemHolder.title = (TextView) convertView.findViewById(R.id.title);
        drawerItemHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
        drawerItemHolder.counter = (TextView) convertView.findViewById(R.id.counter);
        convertView.setTag(drawerItemHolder);

        drawerItemHolder.title.setText(drawerItem.getTitle());


        if (drawerItem.getIconId() != 0) {
            if (((ListView)parent).isItemChecked(position))
                drawerItemHolder.icon.setImageResource(drawerItem.getIconSelectedId());
            else
                drawerItemHolder.icon.setImageResource(drawerItem.getIconId());
        } else
            drawerItemHolder.icon.setVisibility(View.GONE);


        if (((ListView)parent).isItemChecked(position)) {
            drawerItemHolder.title.setTextColor(context.getResources().getColor(R.color.bleu_tag));
            drawerItemHolder.title.setTypeface(null, Typeface.BOLD);
        }

        if (drawerItem.counter != 0) {
            drawerItemHolder.counter.setVisibility(View.VISIBLE);
            drawerItemHolder.counter.setText(""+drawerItem.counter);
        } else
            drawerItemHolder.counter.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return !drawerItemList.get(position).isDivider;
    }
    @Override
    public int getCount(){
        return drawerItemList.size();
    }

    private View divider(ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.main_drawer_divider, parent, false);
    }


    private static class DrawerItemHolder {
        public TextView title;
        public ImageView icon;
        public TextView counter;
    }
}
