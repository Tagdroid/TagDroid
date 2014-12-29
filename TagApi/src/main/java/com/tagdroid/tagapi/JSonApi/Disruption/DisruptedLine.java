package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisruptedLine {
    private int Direction,
            LineId;
    private String ServiceLevel;


    public DisruptedLine(JSONObject jsonDisruptionLine) throws JSONException {
        this.Direction = jsonDisruptionLine.getInt("Direction");
        this.LineId = jsonDisruptionLine.getInt("LineId");
        this.ServiceLevel = jsonDisruptionLine.getString("ServiceLevel");
    }

    public DisruptedLine(int Direction,
                          int LineId,
                          String ServiceLevel) {
        this.Direction = Direction;
        this.LineId = LineId;
        this.ServiceLevel = ServiceLevel;
    }

    public static DisruptedLine[] DisruptedLineArray(JSONArray jsonDirectionArray) throws JSONException {
        int length = jsonDirectionArray.length();
        DisruptedLine[] disruptedLineArray = new DisruptedLine[length];
        for (int i = 0; i < length; i++)
            disruptedLineArray[i] = (new DisruptedLine(jsonDirectionArray.getJSONObject(i)));
        return disruptedLineArray;
    }

    public int getDirection() {
        return Direction;
    }
    public int getLineId() {
        return LineId;
    }
    public String getServiceLevel() {
        return ServiceLevel;
    }
}
