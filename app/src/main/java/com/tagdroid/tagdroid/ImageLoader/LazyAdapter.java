package com.tagdroid.tagdroid.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.tagdroid.ActuFragment;
import com.tagdroid.tagdroid.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
   
    public static class ViewHolder{
        public TextView titre;
        public TextView description;
        public ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        
        HashMap<String, String> actu = new HashMap<String, String>();
        actu = data.get(position);
        
        if(convertView==null){
                vi = inflater.inflate(R.layout.item_actu, null);
                holder=new ViewHolder();
                holder.titre=(TextView)vi.findViewById(R.id.titreactu);
                holder.description=(TextView)vi.findViewById(R.id.descriptionactu);
                holder.image=(ImageView)vi.findViewById(R.id.ivactu);
                vi.setTag(holder);
        }
        else
                holder=(ViewHolder)vi.getTag();
        holder.titre.setText(actu.get(ActuFragment.KEY_TITRE));
        holder.description.setText(actu.get(ActuFragment.KEY_DESCRIPTION));
		holder.image.setTag(actu.get(ActuFragment.KEY_IMAGE));

        imageLoader.DisplayImage(actu.get(ActuFragment.KEY_IMAGE), holder.image);

        return vi;
    }

}