package com.tagdroid.tagapi.JSonApi.TimeTable;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Time {
    private int PassingTime;
    private long VehicleJourneyId;
    private String VehicleJourneyRef;

    public Time(JSONObject jsonTime) throws JSONException {
        this.PassingTime = jsonTime.getInt("PassingTime");
        this.VehicleJourneyId = jsonTime.getLong("VehicleJourneyId");
        this.VehicleJourneyRef = jsonTime.getString("VehicleJourneyRef");
    }

    public Time(int PassingTime, long VehicleJourneyId, String VehicleJourneyRef) {
        this.PassingTime = PassingTime;
        this.VehicleJourneyId = VehicleJourneyId;
        this.VehicleJourneyRef = VehicleJourneyRef;
    }

    public static ArrayList<Time> StopPassingTimeList(JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<Time > StopPassingTimeArray = new ArrayList<>();
        for (int i=0; i < length; i++)
            StopPassingTimeArray.add(new Time(jsonArray.getJSONObject(i)));
        return StopPassingTimeArray;
    }

    public int getPassingTime() {
        return PassingTime;
    }
    public long getVehicleJourneyId() {
        return VehicleJourneyId;
    }
    public String getVehicleJourneyRef() {
        return VehicleJourneyRef;
    }
}
