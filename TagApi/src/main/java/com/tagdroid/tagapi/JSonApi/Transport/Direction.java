package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Direction {
    private int Direction;
    private String Name;
    private long LineId;

    public Direction(JSONObject jsonDirection, long lineId) throws JSONException {
        this.Direction = jsonDirection.getInt("Direction");
        this.Name = jsonDirection.getString("Name");
        this.LineId = lineId;
    }

    public Direction(int direction, String name, long lineId) {
        this.Direction = direction;
        this.Name = name;
        this.LineId = lineId;
    }

    public static Direction[] DirectionArray(JSONArray jsonDirectionArray, long lineId) throws JSONException {
        int length = jsonDirectionArray.length();
        Direction[] directionArray = new Direction[length];
        for (int i = 0; i < length; i++)
            directionArray[i] = (new Direction(jsonDirectionArray.getJSONObject(i), lineId));
        return directionArray;
    }


    public int getDirectionId() {
        return Direction;
    }
    public String getName() {
        return Name;
    }
    public long getLineId() {
        return LineId;
    }
}
