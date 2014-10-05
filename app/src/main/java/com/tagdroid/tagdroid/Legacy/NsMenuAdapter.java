package com.tagdroid.tagdroid.Legacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.tagdroid.R;

public class NsMenuAdapter extends ArrayAdapter<NsMenuItemModel> {
    public NsMenuAdapter(Context context) {
        super(context, 0);
    }

    public void addHeader(int title) {
        add(new NsMenuItemModel(title, -1, true));
    }
    public void addItem(int title, int icon) {
        add(new NsMenuItemModel(title, icon, false));
    }
    public void addItem(NsMenuItemModel itemModel) {
        add(itemModel);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        NsMenuItemModel item = getItem(position);
        ViewHolder holder = null;
        View view = convertView;

        if (view == null) {
            int layout = R.layout.drawer_item;
            if (item.isHeader)
                layout = R.layout.drawer_header;

            view = LayoutInflater.from(getContext()).inflate(layout, null);


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

            TextView text1 = (TextView) view.findViewById(R.id.menurow_title);
            ImageView image1 = (ImageView) view.findViewById(R.id.menurow_icon);
            TextView textcounter1 = (TextView) view.findViewById(R.id.menurow_counter);
            view.setTag(new ViewHolder(text1, image1, textcounter1));
        }

        if (holder == null && view != null) {
            Object tag = view.getTag();
            if (tag instanceof ViewHolder) {
                holder = (ViewHolder) tag;
            }
        }


        if (item != null && holder != null) {
            if (holder.textHolder != null)
                holder.textHolder.setText("Un élément");

            if (holder.textCounterHolder != null) {
                if (item.counter > 0) {
                    holder.textCounterHolder.setVisibility(View.VISIBLE);
                    holder.textCounterHolder.setText("" + item.counter);
                } else {
                    holder.textCounterHolder.setVisibility(View.GONE);
                }
            }

            if (holder.imageHolder != null) {
                if (item.iconRes > 0) {
                    holder.imageHolder.setVisibility(View.VISIBLE);
                    holder.imageHolder.setImageResource(item.iconRes);
                } else {
                    holder.imageHolder.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }
    public static class ViewHolder {
        public final TextView textHolder;
        public final ImageView imageHolder;
        public final TextView textCounterHolder;

        public ViewHolder(TextView text1, ImageView image1, TextView textcounter1) {
            this.textHolder = text1;
            this.imageHolder = image1;
            this.textCounterHolder = textcounter1;
        }
    }
}