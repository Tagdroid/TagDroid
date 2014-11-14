package com.tagdroid.android.Drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.android.MainActivity;
import com.tagdroid.android.R;

public class CustomAdapter extends ArrayAdapter<CustomMenuItem> {
    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void newDivider(){
        add(new CustomMenuItem());
    }

    public void newHeader(int drawable) {
        add(new CustomMenuItem(drawable));
    }

    public void newSubItem(String title) {
        add(new CustomMenuItem(title));
    }

    public void newSubItemIcon(String title, int iconRes) {
        add(new CustomMenuItem(title, iconRes));
    }

    public void newSubItemIconCounter(String title, int iconRes, int counter) {
        add(new CustomMenuItem(title, iconRes, counter));
    }




    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isDivider ? 0 : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isDivider && !getItem(position).isHeader;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            // The view is not a recycled one: we have to inflate
            CustomMenuItem item = getItem(position);
            ViewHolder holder = new ViewHolder();
            if (item.isHeader){
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_header, parent, false);
                holder.drawerHeader = (ImageView) view.findViewById(R.id.drawerHeader);
                holder.drawerHeader.setBackgroundResource(item.drawable);
            } else if (item.isDivider) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_divider, parent, false);
            } else if(item.isIcon){
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_item_icon, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);
                holder.principalIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                holder.principalIcon.setImageResource(item.iconRes);

                if(item.isCounter){
                    if(item.counter!=0){
                        holder.counterText = (TextView) view.findViewById(R.id.menurow_counter);
                        holder.counterText.setVisibility(View.VISIBLE);
                        holder.counterText.setText("" + item.counter);
                    }
                   else {
                    holder.counterText.setVisibility(View.GONE);
                   }
                }
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_drawer_item, parent, false);
                holder.principalText = (TextView) view.findViewById(R.id.menurow_title);
                holder.principalText.setText(item.title);
           }
            view.setTag(holder);
        }

        // TODO Une meilleure solution ???
        if (MainActivity.actualPosition == position){
            view.setBackgroundColor(view.getResources().getColor(R.color.lighter_gray));
            TextView principalText = (TextView) view.findViewById(R.id.menurow_title);
            principalText.setTypeface(null, Typeface.BOLD);
            principalText.setTextColor(getContext().getResources().getColor(R.color.bleu_tag));

            if(getItem(position).isIcon){
                ImageView principalIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                String path[] = getContext().getResources().getResourceName(getItem(position).iconRes).split("/");
                principalIcon.setImageResource(getContext().getResources().getIdentifier(path[1]+"_selected", "drawable", getContext().getPackageName()));
            }
        }
        else{
            if(position!=0) view.setBackgroundColor(view.getResources().getColor(android.R.color.transparent));
            TextView principalText = (TextView) view.findViewById(R.id.menurow_title);

            if(principalText!=null){
                principalText.setTextColor(getContext().getResources().getColor(R.color.gris_fonce));
                principalText.setTypeface(null, Typeface.NORMAL);
            }

            if(getItem(position).isIcon){
                ImageView principalIcon = (ImageView) view.findViewById(R.id.menurow_icon);
                principalIcon.setImageResource(getItem(position).iconRes);
            }
        }


        return view;
    }

    static class ViewHolder {
        public TextView principalText;
        public ImageView principalIcon;
        public TextView counterText;
        public ImageView drawerHeader;
    }
}
