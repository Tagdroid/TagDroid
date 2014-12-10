package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class LogicalStop {
    private long Id;
    private Integer LocalityId;
    private String Name;
    private Integer PointType;
    private Locality locality;

    public LogicalStop(JSONObject jsonLogicalStop) throws JSONException {
        this.Id = jsonLogicalStop.getLong("Id");
        this.LocalityId = jsonLogicalStop.getInt("LocalityId");
        this.Name = jsonLogicalStop.getString("Name");
        this.PointType = jsonLogicalStop.getInt("PointType");

        this.locality = new Locality(jsonLogicalStop.getJSONObject("Locality"));
    }

    public LogicalStop(long Id, Integer LocalityId, String Name, Integer PointType) {
        this.Id = Id;
        this.LocalityId = LocalityId;
        this.Name = Name;
        this.PointType = PointType;
    }

    public long getId() {
        return Id;
    }
    public Integer getLocalityId() {
        return LocalityId;
    }
    public String getName() {
        return Name;
    }
    public Integer getPointType() {
        return PointType;
    }
    public Locality getLocality() { return locality; }
}
