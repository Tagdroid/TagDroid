package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class LogicalStop {
    private long Id;
    private int LocalityId;
    private String Name;
    private int PointType;
    private Locality locality;

    public LogicalStop(JSONObject jsonLogicalStop) throws JSONException {
        this.Id = jsonLogicalStop.getLong("Id");
        this.LocalityId = jsonLogicalStop.getInt("LocalityId");
        this.Name = jsonLogicalStop.getString("Name");
        this.PointType = jsonLogicalStop.getInt("PointType");

        this.locality = new Locality(jsonLogicalStop.getJSONObject("Locality"));
    }

    public LogicalStop(long Id, int LocalityId, String Name, int PointType) {
        this.Id = Id;
        this.LocalityId = LocalityId;
        this.Name = Name;
        this.PointType = PointType;
    }

    public long getId() {
        return Id;
    }
    public int getLocalityId() {
        return LocalityId;
    }
    public String getName() {
        return Name;
    }
    public int getPointType() {
        return PointType;
    }
    public Locality getLocality() { return locality; }
}
