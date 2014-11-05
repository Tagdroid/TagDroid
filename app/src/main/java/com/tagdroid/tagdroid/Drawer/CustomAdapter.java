package com.tagdroid.tagdroid.Drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.tagdroid.MainActivity;
import com.tagdroid.tagdroid.R;

public class CustomAdapter extends ArrayAdapter<CustomMenuItem> {
    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void newSection(String title) {
        add(new CustomMenuItem(title, -1, false, true));
    }

    public void newSubItemIcon(String title, int iconId) {
        add(new CustomMenuItem(title, iconId, true, false));
    }

    public void newSubItem(String title) {
        add(new CustomMenuItem(title,false, false));
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
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_header, parent, false);
            } else if(item.isIcon){
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_item_icon, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);

                holder.principalIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                if (item.iconRes > 0)
                    holder.principalIcon.setImageResource(item.iconRes);
                else
                    holder.principalIcon.setVisibility(View.GONE);

                holder.counterText = (TextView) view.findViewById(R.id.menurow_counter);
                if (item.counter > 0) {
                    holder.counterText.setVisibility(View.VISIBLE);
                    holder.counterText.setText("" + item.counter);
                } else
                    holder.counterText.setVisibility(View.GONE);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_item, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);
           }
            view.setTag(holder);
        }
        // TODO Automatic detection, no flags
        // Faut encore améliorer ça, les onglets restent activés
        if (MainActivity.actualPosition == position){
                /*MainActivityOLD.TITLES[0].equals("STATIONS") && position == 0
                || MainActivityOLD.TITLES[0].equals("LIGNES") && position == 0
                || MainActivityOLD.TITLES[0].equals("STATIONDETAIL") && position == 0
                || MainActivityOLD.TITLES[0].equals("FAVORIS") && position == 1
                || MainActivityOLD.TITLES[0].equals("PROXIMITE") && position == 2
                || MainActivityOLD.TITLES[0].equals("MAP") && position == 3
                || MainActivityOLD.TITLES[0].equals("INFO") && position == 4
                || MainActivityOLD.TITLES[0].equals("ACTU") && position == 5
                || MainActivityOLD.TITLES[0].equals("TARIFS") && position == 6
                || MainActivityOLD.TITLES[0].equals("SETTINGS") && position == 8
                || MainActivityOLD.TITLES[0].equals("ABOUT") && position == 9
                || MainActivityOLD.TITLES[0].equals("RATE") && position == 10){*/
            view.setBackgroundColor(view.getResources().getColor(R.color.gris));
            TextView principalText = (TextView) view.findViewById(R.id.menurow_title);
            principalText.setTypeface(null, Typeface.BOLD);
            principalText.setTextColor(getContext().getResources().getColor(R.color.bleu_tag));

            if(getItem(position).isIcon){
                ImageView principalIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                String path[] = getContext().getResources().getResourceName(getItem(position).iconRes).split("/");
                principalIcon.setImageResource(getContext().getResources().getIdentifier(path[1]+"_selected", "drawable", getContext().getPackageName()));
            }
        }


        return view;
    }

    static class ViewHolder {
        public TextView principalText;
        public ImageView principalIcon;
        public TextView counterText;
    }
}
