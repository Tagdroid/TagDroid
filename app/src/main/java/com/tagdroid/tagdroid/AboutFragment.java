package com.tagdroid.tagdroid;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class AboutFragment extends Fragment implements View.OnClickListener {
    private Tracker tracker;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.tracker = EasyTracker.getInstance(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send(MapBuilder.createAppView().build());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_menu, menu);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);

        MainActivity.mTitle = getResources().getString(R.string.about);
        getActivity().getActionBar().setTitle(getResources().getString(R.string.about));

        view.findViewById(R.id.mailQuentin).setOnClickListener(this);
        view.findViewById(R.id.mailFélix).setOnClickListener(this);
        view.findViewById(R.id.mailAlexandre).setOnClickListener(this);
        view.findViewById(R.id.showlog).setOnClickListener(this);

        try {
            ((Button) view.findViewById(R.id.showlog)).setText("Version " +
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            ((Button) view.findViewById(R.id.showlog)).setText("Changelog de TagDroid");
            Toast.makeText(getActivity(), "Nom de l'application non trouvé… Étrange !", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.showlog) {
            (new ChangeLog()).start(false);
//            cl.getFullLogDialog().show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intent.setType("plain/text");

        switch (v.getId()) {
            case R.id.mailQuentin:
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tagdroid.grenoble@gmail.com"});
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.mail)));
                break;
            case R.id.mailFélix:
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"felix@piedallu.me"});
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.mail)));
                break;
            case R.id.mailAlexandre:
                Toast.makeText(getActivity(), "Alexandre n'a pas donné son adresse mail !", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
