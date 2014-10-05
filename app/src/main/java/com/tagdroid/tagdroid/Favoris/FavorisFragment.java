package com.tagdroid.tagdroid.Favoris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagapi.JSonApi.Favori;
import com.tagdroid.tagdroid.Legacy.MainActivity;
import com.tagdroid.tagdroid.R;

public class FavorisFragment extends Fragment {
    private static View view;
    private Tracker tracker;

    public static FavorisFragment newInstance() {
        return new FavorisFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.fragment_favoris, container, false);
        } catch (InflateException ignored) {
        }

        MainActivity.mTitle = getActivity().getResources().getString(R.string.favoris);
        getActivity().getActionBar().setTitle(MainActivity.mTitle);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        this.tracker = EasyTracker.getInstance(this.getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_menu, menu);
    }

    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send(MapBuilder.createAppView().build());
        ListView listViewFavoris = (ListView) view.findViewById(R.id.listFavoris);

        final FavorisHelper favorisHelper = new FavorisHelper(getActivity());

        if (favorisHelper.getFavorisAdaptedArray().isEmpty())
            view.findViewById(R.id.noFavsLayout).setVisibility(View.VISIBLE);
        else {
            SimpleAdapter mSchedule = new SimpleAdapter(getActivity(),
                    favorisHelper.getFavorisAdaptedArray(),
                    R.layout.listitem_station,
                    new String[]{"nom", null, "ligne", "couleur"},
                    new int[]{R.id.titre, R.id.distance, R.id.station, R.id.fond_color});

            listViewFavoris.setAdapter(mSchedule);
            listViewFavoris.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Favori selectedFavori = favorisHelper.getFavoris()[position];
                    MainActivity.titre1 = selectedFavori.Name;
                    MainActivity.id_station1 = selectedFavori.Id.toString() + "_id_station";
                    MainActivity.ligne2 = selectedFavori.Ligne + "_ligne";
                    MainActivity.latitude1 = selectedFavori.Latitude.toString();
                    MainActivity.longitude1 = selectedFavori.Longitude.toString();
                    MainActivity.TITLES = new String[]{"STATIONDETAIL"};
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
