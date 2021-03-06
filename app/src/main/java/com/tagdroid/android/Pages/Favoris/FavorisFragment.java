package com.tagdroid.android.Pages.Favoris;

import android.os.Bundle;
import android.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tagdroid.android.R;

public class FavorisFragment extends Fragment {
    private static View view;

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

       /* MainActivityOLD.mTitle = getActivity().getResources().getString(R.string.favoris);
        getActivity().getActionBar().setTitle(MainActivityOLD.mTitle);*/
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void onResume() {
        super.onResume();
        ListView listViewFavoris = (ListView) view.findViewById(R.id.listFavoris);

        final FavorisHelper favorisHelper = new FavorisHelper(getActivity());

        if (favorisHelper.getFavorisAdaptedArray().isEmpty())
            view.findViewById(R.id.noFavsLayout).setVisibility(View.VISIBLE);
        else {
          /*  SimpleAdapter mSchedule = new SimpleAdapter(getActivity(),
                    favorisHelper.getFavorisAdaptedArray(),
                    R.layout.legacy_listitem_station,
                    new String[]{"nom", null, "ligne", "couleur"},
                    new int[]{R.id.titre, R.id.distance, R.id.station, R.id.fond_color});

            listViewFavoris.setAdapter(mSchedule);
            listViewFavoris.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Favori selectedFavori = favorisHelper.getFavoris()[position];
                    MainActivityOLD.titre1 = selectedFavori.Name;
                    MainActivityOLD.id_station1 = selectedFavori.Id.toString() + "_id_station";
                    MainActivityOLD.ligne2 = selectedFavori.Ligne + "_ligne";
                    MainActivityOLD.latitude1 = selectedFavori.Latitude.toString();
                    MainActivityOLD.longitude1 = selectedFavori.Longitude.toString();
                    MainActivityOLD.TITLES = new String[]{"STATIONDETAIL"};
                    MainActivityOLD.adapter.notifyDataSetChanged();
                }
            });*/
        }
    }
}
