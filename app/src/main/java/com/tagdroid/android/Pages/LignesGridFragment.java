package com.tagdroid.android.Pages;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LignesGridFragment extends Page {
    @Override
    public String getTitle() {
        return getString(R.string.lines);
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_lignes;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.d("Test menu item", "SEARCH");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lignes_grid, container, false);

        ArrayList<Line> tramways = ReadSQL.getLinesByType(getActivity(), Line.TRAM);
        ((GridView) view.findViewById(R.id.tramGrid)).setAdapter(new LigneAdapter(getActivity(), tramways));

        ArrayList<Line> chrono = ReadSQL.getLinesByType(getActivity(), Line.CHRONO);
        ((GridView) view.findViewById(R.id.chronoGrid)).setAdapter(new LigneAdapter(getActivity(), chrono));

        ArrayList<Line> proximo = ReadSQL.getLinesByType(getActivity(), Line.PROXIMO);
        ((GridView) view.findViewById(R.id.proximoGrid)).setAdapter(new LigneAdapter(getActivity(), proximo));

        ArrayList<Line> flexo = ReadSQL.getLinesByType(getActivity(), Line.FLEXO);
        ((GridView) view.findViewById(R.id.flexoGrid)).setAdapter(new LigneAdapter(getActivity(), flexo));

        ArrayList<Line> unknown = ReadSQL.getLinesByType(getActivity(), Line.UNKNOWN);
        ((GridView) view.findViewById(R.id.unknownGrid)).setAdapter(new LigneAdapter(getActivity(), unknown));

        if (unknown.size() > 0)
            view.findViewById(R.id.unknownCardView).setVisibility(View.VISIBLE);
        else
            Log.d("LignesGridFragment", "No unknown lines");

        return view;
    }

    private static int BlackorWhite(int color) {
        double brightness = Math.sqrt(
                Color.red(color) * Color.red(color) * 241 +
                        Color.green(color) * Color.green(color) * 691 +
                        Color.blue(color) * Color.blue(color) * 68);
        if (brightness < 4690)
            return Color.WHITE;
        else
            return Color.BLACK;
    }

    public class LigneAdapter extends BaseAdapter {
        ArrayList<Line> lignes;
        private Context context;

        public LigneAdapter(Context context, ArrayList<Line> lignes) {
            this.context = context;
            this.lignes = lignes;
            Collections.sort(this.lignes, new Comparator<Line>() {
                @Override
                public int compare(Line l1, Line l2) {
                    return l1.getNumber().compareTo(l2.getNumber());
                }
            });
        }

        public int getCount() {
            return lignes.size();
        }

        public Object getItem(int position) {
            return this;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View oldView, ViewGroup parent) {
            if (oldView != null)
                return oldView;

            final Line ligne = lignes.get(position);
            String nom = ligne.getNumber();
            int couleur = ligne.color;

            Button lineButton = new Button(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                lineButton.setStateListAnimator(null);

            lineButton.setHeight(lineButton.getWidth());
            lineButton.setText(nom);
            if (nom.length() > 3)
                lineButton.setTextSize(14);
            else
                lineButton.setTextSize(16);
            lineButton.setTextColor(BlackorWhite(couleur));
            lineButton.getBackground().setColorFilter(couleur, PorterDuff.Mode.SRC_OVER);

            lineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DirectionsDialogFragment().setDetails(ligne, ligne.getDirectionList())
                            .show(getFragmentManager().beginTransaction(), "directions");
                }
            });
            return lineButton;
        }
    }


    public static class DirectionsDialogFragment extends DialogFragment {
        private Direction[] directions;
        private Line line;

        public DirectionsDialogFragment setDetails(Line line, Direction[] directions) {
            this.setRetainInstance(true);
            this.line       = line;
            this.directions = directions;
            return this;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ArrayList<String> directionNames = new ArrayList<>();
            for (Direction direction : directions)
                directionNames.add(direction.getName());

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.choix_directions)
                    .setItems(directionNames.toArray(new String[2]),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ReadSQL.setSelectedLineAndDirection(line, directions[which]);

                                    LineStopsFragment lineStopsFragment = new LineStopsFragment();
                                    changeFragmentInterface.onChangeFragment(lineStopsFragment);

                                    getActivity().getFragmentManager().beginTransaction()
                                            .replace(R.id.pager, lineStopsFragment).commit();
                                }
                            })
                    .create();
        }
    }
}
