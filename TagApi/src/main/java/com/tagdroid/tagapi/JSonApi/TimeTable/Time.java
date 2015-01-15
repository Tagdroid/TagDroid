package com.tagdroid.tagapi.JSonApi.TimeTable;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Time {
    private int PassingTime;
    private long VehicleJourneyId;
    private String VehicleJourneyRef;

    public Time(JSONObject jsonTime) throws JSONException {
        Log.d("JSON TIME",jsonTime.toString());
        this.PassingTime = StopPassingTimeList(jsonTime).getInt("PassingTime");
        this.VehicleJourneyId = StopPassingTimeList(jsonTime).getLong("VehicleJourneyId");
        this.VehicleJourneyRef = StopPassingTimeList(jsonTime).getString("VehicleJourneyRef");
    }

    public Time(int PassingTime, long VehicleJourneyId, String VehicleJourneyRef) {
        this.PassingTime = PassingTime;
        this.VehicleJourneyId = VehicleJourneyId;
        this.VehicleJourneyRef = VehicleJourneyRef;
    }

    public static JSONArray StopPassingTimeList(JSONObject jsonTime) throws JSONException {
        JSONArray jsonTimeArray = jsonTime.getJSONArray("StopPassingTimeList");
        int length = jsonTimeArray.length();
        JSONArray StopPassingTimeList = new JSONArray();
        JSONObject obj = null;
        for (int i = 0; i < length; i++) {
            obj = new JSONObject();
            obj.put("PassingTime", jsonTimeArray.getJSONObject(i).getInt("PassingTime"));
            obj.put("VehicleJourneyId", jsonTimeArray.getJSONObject(i).getLong("VehicleJourneyId"));
            obj.put("VehicleJourneyRef", jsonTimeArray.getJSONObject(i).getString("VehicleJourneyRef"));
            StopPassingTimeList.put(obj);
        }
        return StopPassingTimeList;
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
