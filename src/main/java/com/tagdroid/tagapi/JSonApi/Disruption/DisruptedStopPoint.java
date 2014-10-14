package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisruptedStopPoint {
    /*private String ServiceLevel;
    private Integer Direction,
            LineId;*/
    private Integer StopPointId;


    public DisruptedStopPoint(JSONObject jsonDisruptionType) throws JSONException {
     /*   this.ServiceLevel = jsonDisruptionType.getString("ServiceLevel");
        this.Direction = jsonDisruptionType.getInt("Direction");
        this.LineId = jsonDisruptionType.getInt("LineId");*/
        this.StopPointId = jsonDisruptionType.getInt("StopPointId");
    }

    public DisruptedStopPoint(/*String ServiceLevel,
                               Integer Direction,
                               Integer LineId*/
                                Integer StopPointId) {

        /*this.ServiceLevel = ServiceLevel;
        this.Direction = Direction;
        this.LineId = LineId;*/
        this.StopPointId = StopPointId;
    }

    public static DisruptedStopPoint[] DisruptedStopPointArray(JSONArray jsonDirectionArray) throws JSONException {
        Integer length = jsonDirectionArray.length();
        DisruptedStopPoint[] disruptedStopPointArray = new DisruptedStopPoint[length];
        for (int i = 0; i < length; i++)
            disruptedStopPointArray[i] = (new DisruptedStopPoint(jsonDirectionArray.getJSONObject(i)));
        return disruptedStopPointArray;
    }
    /*public String getServiceLevel() {
        return ServiceLevel;
    }
    public Integer getDirection() {
        return Direction;
    }
    public Integer getLineId() {
        return LineId;
    }*/

    public Integer getStopPointId() {
        return StopPointId;
    }

}
