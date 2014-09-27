package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Direction {
    private Integer Direction;
    private String Name;

    public Direction(JSONObject jsonDirection) throws JSONException {
        this.Direction = jsonDirection.getInt("Direction");
        this.Name = jsonDirection.getString("Name");
    }

    public static Direction[] DirectionArray(JSONArray jsonDirectionArray) throws JSONException {
        Integer length = jsonDirectionArray.length();
        Direction[] directionArray = new Direction[length];
        for (int i = 0; i < length; i++)
            directionArray[i] = (new Direction(jsonDirectionArray.getJSONObject(i)));
        return directionArray;
    }


    public Integer getDirection() {
        return Direction;
    }
    public String getName() {
        return Name;
    }
}
