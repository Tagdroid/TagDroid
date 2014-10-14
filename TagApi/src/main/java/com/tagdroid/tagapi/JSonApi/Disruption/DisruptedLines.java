package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

public class DisruptedLines {
    private String ServiceLevel;
    private Integer Direction,
            LineId;


    public DisruptedLines(JSONObject jsonDisruptionType) throws JSONException {
        this.ServiceLevel = jsonDisruptionType.getString("ServiceLevel");
        this.Direction = jsonDisruptionType.getInt("Direction");
        this.LineId = jsonDisruptionType.getInt("LineId");
    }

    public DisruptedLines(String ServiceLevel,
                          Integer Direction,
                          Integer LineId) {

        this.ServiceLevel = ServiceLevel;
        this.Direction = Direction;
        this.LineId = LineId;
    }

    public String getServiceLevel() {
        return ServiceLevel;
    }
    public Integer getDirection() {
        return Direction;
    }
    public Integer getLineId() {
        return LineId;
    }

}
