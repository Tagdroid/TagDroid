package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class PhysicalStop {
    private Integer Category;
    private long Id;
    private String Name;
    private Integer Latitude,
            Longitude,
            PointType,
            LogicalStopId,
            LocalityId,
            OperatorId,
            Accessibility;

    private Locality locality;
    private LogicalStop logicalStop;

    public PhysicalStop(JSONObject jsonPhysicalStop) throws JSONException {
        this.Category = jsonPhysicalStop.getInt("Category");
        this.Id = jsonPhysicalStop.getLong("Id");
        this.Name = jsonPhysicalStop.getString("Name");
        this.Latitude = jsonPhysicalStop.getInt("Latitude");
        this.Longitude = jsonPhysicalStop.getInt("Longitude");
        this.PointType = jsonPhysicalStop.getInt("PointType");
        this.LogicalStopId = jsonPhysicalStop.getInt("LogicalStopId");
        this.LocalityId = jsonPhysicalStop.getInt("LocalityId");
        this.OperatorId = jsonPhysicalStop.getInt("OperatorId");
        this.Accessibility = new AccessibilityValues(jsonPhysicalStop.getJSONObject("AccessibilityStatus"))
                .getAccessibilityCode();

        this.locality = new Locality(jsonPhysicalStop.getJSONObject("Locality"));
        this.logicalStop = new LogicalStop(jsonPhysicalStop.getJSONObject("LogicalStop"));

    }

    public PhysicalStop(Integer Category,
                        long Id,
                        String Name,
                        Integer Latitude,
                        Integer Longitude,
                        Integer PointType,
                        Integer LogicalStopId,
                        Integer LocalityId,
                        Integer OperatorId,
                        Integer Accessibility) {
        this.Category = Category;
        this.Id = Id;
        this.Name = Name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.PointType = PointType;
        this.LogicalStopId = LogicalStopId;
        this.LocalityId = LocalityId;
        this.OperatorId = OperatorId;
        this.Accessibility = Accessibility;
    }

    public long getId() {
        return Id;
    }
    public Integer getCategory() {
        return Category;
    }
    public Integer getLatitude() {
        return Latitude;
    }
    public Integer getLongitude() {
        return Longitude;
    }
    public Integer getPointType() {
        return PointType;
    }
    public Integer getLogicalStopId() {
        return LogicalStopId;
    }
    public Integer getLocalityId() {
        return LocalityId;
    }
    public Integer getOperatorId() {
        return OperatorId;
    }
    public String getName() {
        return Name;
    }
    public Integer getAccessibility() {
        return Accessibility;
    }
    public Locality getLocality() { return locality; }
    public LogicalStop getLogicalStop() {
        return logicalStop;
    }
}
