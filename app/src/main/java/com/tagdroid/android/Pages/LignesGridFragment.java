package com.tagdroid.android.Pages;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class LignesGridFragment extends Page {
    private ArrayList<Line> allLines;
    private ArrayList<String> allLinesNumbers;

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

    private ArrayList<Line> matchKnownLines(int known_names, int known_colors, int LineType) {
        // Pour chaque type de ligne, si elle est dans la base de données on l'ajoute à l'arraylist correspondante.
        // On la supprime de allLines pour voir si il nous reste des lignes non connues.
        TypedArray knownNames = getResources().obtainTypedArray(known_names);
        TypedArray knownColors = getResources().obtainTypedArray(known_colors);

        ArrayList<Line> matchedLines = new ArrayList<>();

        for (int i = 0; i < knownNames.length(); i++) {
            int a;
            if ((a = allLinesNumbers.indexOf(knownNames.getString(i))) >= 0) {
                matchedLines.add(allLines.get(a).setColor(knownColors.getColor(i, 0)).setLineType(LineType));
                allLines.remove(a);
                allLinesNumbers.remove(a);
            }
        }
        return matchedLines;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lignes_grid, container, false);

        // Get all Lines from SQLite
        allLines = new ArrayList<>(ReadSQL.getAllLines(getActivity()));

        allLinesNumbers = new ArrayList<>();
        // Plus pratique pour contains()
        for (Line i : allLines) {
            allLinesNumbers.add(i.getNumber());
        }

        ArrayList<Line> tramways = matchKnownLines(R.array.known_tramways, R.array.known_tramways_colors, Line.TRAM);
        ((GridView) view.findViewById(R.id.tramGrid)).setAdapter(new LigneAdapter(getActivity(), tramways));

        ArrayList<Line> chrono = matchKnownLines(R.array.known_chrono, R.array.known_chrono_colors, Line.CHRONO);
        ((GridView) view.findViewById(R.id.chronoGrid)).setAdapter(new LigneAdapter(getActivity(), chrono));

        ArrayList<Line> proximo = matchKnownLines(R.array.known_proximo, R.array.known_proximo_colors, Line.PROXIMO);
        ((GridView) view.findViewById(R.id.proximoGrid)).setAdapter(new LigneAdapter(getActivity(), proximo));

        ArrayList<Line> flexo = matchKnownLines(R.array.known_flexo, R.array.known_flexo_colors, Line.FLEXO);
        ((GridView) view.findViewById(R.id.flexoGrid)).setAdapter(new LigneAdapter(getActivity(), flexo));

        if (allLines.size() > 0) {
            view.findViewById(R.id.unknownCardView).setVisibility(View.VISIBLE);
            for (Line i : allLines)
                i.setColor(getResources().getColor(R.color.ligne_default));
            ((GridView) view.findViewById(R.id.unknownGrid)).setAdapter(new LigneAdapter(getActivity(), allLines));
        } else
            Log.d("LignesGridFragment", "No unknown lines");

        return view;
    }

    public class LigneAdapter extends BaseAdapter {
        ArrayList<Line> lignes;
        private Context context;

        public LigneAdapter(Context context, ArrayList<Line> lignes) {
            this.context = context;
            this.lignes = lignes;
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

            FButton lineButton = new FButton(context);
            lineButton.setText(nom);

            if (nom.length() > 3)
                lineButton.setTextSize(14);
            else
                lineButton.setTextSize(16);

            lineButton.setTextColor(BlackorWhite(couleur));

            lineButton.setButtonColor(couleur);
            lineButton.setShadowEnabled(true);
            lineButton.setShadowHeight(7);
            lineButton.setCornerRadius(12);

            lineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment directionsDialogFragment = new DirectionsDialogFragment()
                            .setDetails(ligne, ligne.getDirectionList());
                    directionsDialogFragment.show(getFragmentManager().beginTransaction(), "directions");
                }
            });
            return lineButton;
        }
    }

    public static class DirectionsDialogFragment extends DialogFragment {
        private Direction[] directions;
        private Line line;

        public DirectionsDialogFragment() {
        }
        public DirectionsDialogFragment setDetails(Line line, Direction[] directions) {
            this.line = line;
            this.directions = directions;
            return this;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ArrayList<String> directionNames = new ArrayList<>();
            for (Direction direction : directions)
                directionNames.add(direction.getName());

            builder.setTitle(R.string.choix_directions)
                    .setItems(directionNames.toArray(new String[2]),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ReadSQL.setSelectedLineAndDirection(line, directions[which]);

                                    LineStopsFragment lineStopsFragment = new LineStopsFragment();
                                    changeFragmentInterface.onChangeFragment(lineStopsFragment);

                                    FragmentTransaction fragmentTransaction = getActivity()
                                            .getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.pager, lineStopsFragment);
                                    fragmentTransaction.commit();
                                }
                            });
            return builder.create();
        }
    }
}
