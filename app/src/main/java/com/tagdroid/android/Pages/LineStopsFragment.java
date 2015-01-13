package com.tagdroid.android.Pages;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

public class LineStopsFragment extends Page{
    private static Line ligne;
    private static Direction direction;

    private ArrayList<LineStop> lineStops;

    private void getDetailsFromSQL() {
        Log.d("Details", "getDetailsFromSQL");
        ligne = ReadSQL.getSelectedLine();
        direction = ReadSQL.getSelectedDirection();
        lineStops = ReadSQL.getStops(ligne.getId(), direction.getDirection(), getActivity());
    }

    public LineStopsFragment() {
        getDetailsFromSQL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linestops, container, false);

        final StopsArrayAdapter adapter = new StopsArrayAdapter(getActivity());

        ((ListView)view).setAdapter(adapter);
        ((ListView)view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReadSQL.setSelectedLineStop(lineStops.get(i));

                FragmentTransaction fragmentTransaction = getActivity()
                        .getFragmentManager().beginTransaction();
                StationDetailFragment stationDetailFragment = new StationDetailFragment();

                changeFragmentInterface.onChangeFragment(stationDetailFragment);

                fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout,
                        R.anim.fadein, R.anim.fadeout);
                fragmentTransaction.replace(R.id.pager, stationDetailFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.ligne)+" "+ligne.getNumber();
        //direction.getName();
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
