package com.tagdroid.tagdroid.Pages;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tagdroid.tagdroid.ChangeLog;
import com.tagdroid.tagdroid.Page;
import com.tagdroid.tagdroid.R;

public class AboutFragment extends Page implements View.OnClickListener {
    @Override
    public String getTitle() {
        return getString(R.string.about);
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_about;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        view.findViewById(R.id.showlog).setOnClickListener(this);
        view.findViewById(R.id.mailQuentin).setOnClickListener(this);
        view.findViewById(R.id.mailFélix).setOnClickListener(this);
        view.findViewById(R.id.mailAlexandre).setOnClickListener(this);

        try {
            ((Button) view.findViewById(R.id.showlog)).setText("Version " +
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            ((Button) view.findViewById(R.id.showlog)).setText("Changelog de TagDroid");
            Toast.makeText(getActivity(), "Nom de l'application non trouvé… Étrange !", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:")).setType("plain/text");

        switch (v.getId()) {
            case R.id.showlog:
                (new ChangeLog()).start(false);
                break;
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
