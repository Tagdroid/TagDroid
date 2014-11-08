package com.tagdroid.android.Legacy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tagdroid.tagapi.HttpGet.HttpApiTask;
import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSon.ReadJSonStops;
import com.tagdroid.tagapi.ReadJSonTask;
import com.tagdroid.tagapi.SQLApi.Transport.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;
import com.tagdroid.android.R;

import org.json.JSONArray;


public class TestActivity extends ActionBarActivity implements View.OnClickListener, ProgressionInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUI();
        HttpApiTask httpApiTask = new HttpApiTask(this, "transport/v2/GetPhysicalStops/json?key=TAGDEV");
        httpApiTask.setProgressBar((ProgressBar) findViewById(R.id.dlProgress));
        httpApiTask.execute();
    }
    private void setUI() {
        findViewById(R.id.id).setEnabled(false);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button).setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button : getNameFromId(Integer.valueOf(
                            ((EditText) findViewById(R.id.id)).getText().toString())
            );
        }
    }

    private void readJSon(String jsonQueryResult) {
        ReadJSonTask readJSonTask = new ReadJSonStops(jsonQueryResult, this, this);
        readJSonTask.setProgressBar((ProgressBar)findViewById(R.id.sqlProgress));
        readJSonTask.execute();
    }

    private void getNameFromId(Integer id) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("TagDatabase.db", this, null);

        PhysicalStopDAO physicalStopDAO = new PhysicalStopDAO(dbHelper, false, false, -1, -1);
        PhysicalStop testStop = physicalStopDAO.select(id);
        if (testStop != null) {
            String nom = testStop.getName();
            Toast.makeText(this, "Nom : " + nom, Toast.LENGTH_SHORT).show();
            Log.d("lectureSQL", nom + " " + testStop.getId());
        } else {
            Toast.makeText(this, "L'arrêt n'est pas dans la base de données…", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }

    @Override
    public void onDownloadStart() {
        Toast.makeText(this, "Début du téléchargement…", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Début du téléchargement…");
    }

    @Override
    public void onDownloadFailed(Integer e) {
        Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Failed to download file");
        Log.e("Download", "StatusCode : "+e);
    }

    @Override
    public void onDownloadFailed(Exception e) {
        Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Failed to download file");
        e.printStackTrace();
    }

    @Override
    public void onDownloadComplete(String resultString) {
        Toast.makeText(this, "Le téléchargement est terminé.", Toast.LENGTH_SHORT).show();
        Log.d("Download", "Download finished !");
        readJSon(resultString);
    }

    @Override
    public void onJSonParsingStarted() {

    }

    @Override
    public void onJSonParsingFailed(Exception e) {

    }

    @Override
    public void onJSonParsingFailed(String e) {
        Log.e("JSonParsing", e);
    }

    @Override
    public void onJSonParsingComplete() {
        findViewById(R.id.id).setEnabled(true);
        findViewById(R.id.button).setEnabled(true);
        Log.d("JSonParsing", "Finished !");
    }
}
