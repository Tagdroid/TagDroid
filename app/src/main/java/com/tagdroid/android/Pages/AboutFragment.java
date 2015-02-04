package com.tagdroid.android.Pages;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tagdroid.android.ChangeLog;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;

public class AboutFragment extends Page implements View.OnClickListener {
    @Override
    public String getTitle() {
        return getString(R.string.about);
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("page", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        view.findViewById(R.id.showlog).setOnClickListener(this);
        view.findViewById(R.id.websiteQuentin).setOnClickListener(this);
        view.findViewById(R.id.mailQuentin).setOnClickListener(this);
        view.findViewById(R.id.websiteFélix).setOnClickListener(this);
        view.findViewById(R.id.mailFélix).setOnClickListener(this);

        try {
            ((Button) view.findViewById(R.id.showlog)).setText(getString(R.string.version_) +
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            ((Button) view.findViewById(R.id.showlog)).setText(getString(R.string.tagdroid_changelog));
            Toast.makeText(getActivity(), "Nom de l'application non trouvé… Étrange !", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:")).setType("plain/text");

        switch (v.getId()) {
            case R.id.showlog:
                (new ChangeLog(getActivity())).show(true);
                break;
            case R.id.websiteQuentin:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.apropos_website_quentin))));
                break;
            case R.id.mailQuentin:
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.apropos_tagdroid_mail)});
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.mail)));
                break;
            case R.id.websiteFélix:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.apropos_felix_website))));
                break;
            case R.id.mailFélix:
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.apropos_felix_mail)});
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.mail)));
                break;
            default:
                break;
        }
    }
}
