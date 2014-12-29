package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisruptedStopPoint {
    /*private String ServiceLevel;
    private int Direction,
            LineId;*/
    private int StopPointId;


    public DisruptedStopPoint(JSONObject jsonDisruptionType) throws JSONException {
     /*   this.ServiceLevel = jsonDisruptionType.getString("ServiceLevel");
        this.Direction = jsonDisruptionType.getInt("Direction");
        this.LineId = jsonDisruptionType.getInt("LineId");*/
        this.StopPointId = jsonDisruptionType.getInt("StopPointId");
    }

    public DisruptedStopPoint(/*String ServiceLevel,
                               int Direction,
                               int LineId*/
                                int StopPointId) {

        /*this.ServiceLevel = ServiceLevel;
        this.Direction = Direction;
        this.LineId = LineId;*/
        this.StopPointId = StopPointId;
    }

    public static DisruptedStopPoint[] DisruptedStopPointArray(JSONArray jsonDirectionArray) throws JSONException {
        int length = jsonDirectionArray.length();
        DisruptedStopPoint[] disruptedStopPointArray = new DisruptedStopPoint[length];
        for (int i = 0; i < length; i++)
            disruptedStopPointArray[i] = (new DisruptedStopPoint(jsonDirectionArray.getJSONObject(i)));
        return disruptedStopPointArray;
    }
    /*public String getServiceLevel() {
        return ServiceLevel;
    }
    public int getDirection() {
        return Direction;
    }
    public int getLineId() {
        return LineId;
    }*/

    public int getStopPointId() {
        return StopPointId;
    }

}
