package com.tagdroid.tagapi.ReadJSon;

import android.content.Context;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSonTask;

import org.json.JSONArray;

public class ReadJSonLinesList extends ReadJSonTask {
    private Context context;
    public ReadJSonLinesList(String jsonString, ProgressionInterface progressionInterface, Context context) {
        super(jsonString, progressionInterface);
        this.context = context;
    }

    @Override
    public void readData(JSONArray jsonData) {
        return;
    }
}
