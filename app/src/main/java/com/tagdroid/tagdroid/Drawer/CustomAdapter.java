package com.tagdroid.tagdroid.Drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.tagdroid.Legacy.MainActivity;
import com.tagdroid.tagdroid.R;

public class CustomAdapter extends ArrayAdapter<CustomMenuItem> {
    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void newSection(String title) {
        add(new CustomMenuItem(title, -1, true));
    }

    public void newSubItem(String title, int iconId) {
        add(new CustomMenuItem(title, iconId, false));
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader ? 0 : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHeader;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            // The view is not a recycled one: we have to inflate
            CustomMenuItem item = getItem(position);
            ViewHolder holder = new ViewHolder();
            if (item.isHeader) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.drawer_header, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);

                holder.principatIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                if (item.iconRes > 0)
                    holder.principatIcon.setImageResource(item.iconRes);
                else
                    holder.principatIcon.setVisibility(View.GONE);

                holder.counterText = (TextView) view.findViewById(R.id.menurow_counter);
                if (item.counter > 0) {
                    holder.counterText.setVisibility(View.VISIBLE);
                    holder.counterText.setText("" + item.counter);
                } else
                    holder.counterText.setVisibility(View.GONE);
            }
            view.setTag(holder);
        }
        // TODO Automatic detection, no flags
        if (MainActivity.TITLES[0].equals("STATIONS") && position == 1
                || MainActivity.TITLES[0].equals("LIGNES") && position == 1
                || MainActivity.TITLES[0].equals("STATIONDETAIL") && position == 1
                || MainActivity.TITLES[0].equals("FAVORIS") && position == 2
                || MainActivity.TITLES[0].equals("PROXIMITE") && position == 3
                || MainActivity.TITLES[0].equals("MAP") && position == 4
                || MainActivity.TITLES[0].equals("INFO") && position == 6
                || MainActivity.TITLES[0].equals("ACTU") && position == 7
                || MainActivity.TITLES[0].equals("TICKETS") && position == 8)
            view.setBackgroundColor(view.getResources().getColor(R.color.bleu_tag_clair2));
        return view;
    }

    static class ViewHolder {
        public TextView principalText;
        public ImageView principatIcon;
        public TextView counterText;
    }
}
