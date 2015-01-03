package com.tagdroid.android.Pages;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

public class LineStopsFragment extends Page{
    Line ligne;
    Direction direction;
    ArrayList<LineStop> lineStops;

    public void setLineAndDirection(Line ligne, Direction direction) {
        this.ligne = ligne;
        this.direction = direction;
        lineStops = new ReadSQL(getActivity()).getStops(ligne, direction);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linestops, container, false);
        final StopsArrayAdapter adapter = new StopsArrayAdapter(getActivity());

        ((ListView)view).setAdapter(adapter);
        ((ListView)view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LineStop clickedStop = lineStops.get(i);

                Toast.makeText(getActivity(), "clicked "+i+ " ("+clickedStop.getName()+")", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public String getTitle() {
        return null;
    }
    @Override
    public Integer getMenuId() {
        return null;
    }

    private class StopsArrayAdapter extends ArrayAdapter<LineStop> {
        Context context;

        public StopsArrayAdapter(Context context) {
            super(context, R.layout.fragment_linestops_item, lineStops);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_linestops_item, parent, false);
            ((TextView)rowView.findViewById(R.id.stationText)).setText(lineStops.get(position).getName());

            LayerDrawable stationIcon;

            int drawableId;
            if (position == 0)
                drawableId = R.drawable.ligne_debut;
            else if (position == lineStops.size() -1)
                drawableId = R.drawable.ligne_fin;
            else
                drawableId = R.drawable.ligne_milieu;

            stationIcon = (LayerDrawable) getResources().getDrawable(drawableId);
            ((GradientDrawable)stationIcon.findDrawableByLayerId(R.id.station)).setColor(ligne.color);

            ((ImageView)rowView.findViewById(R.id.stationIcon)).setImageDrawable(stationIcon);

            return rowView;
        }
    }
}
