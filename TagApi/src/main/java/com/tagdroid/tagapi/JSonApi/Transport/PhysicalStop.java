package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class PhysicalStop {
    private Integer Category,
            Direction;
    private long Id;
    private String Name;
    private Double Latitude, Longitude;
    private Integer LineId,
            PointType,
            LogicalStopId,
            LocalityId,
            OperatorId,
            Accessibility;

    private Locality locality;
    private LogicalStop logicalStop;

    public PhysicalStop(JSONObject jsonPhysicalStop, Integer LineId, Integer Direction) throws JSONException {
        this.Category = 0;// jsonPhysicalStop.getInt("Category");
        this.Direction = Direction;
        this.Id = jsonPhysicalStop.getLong("Id");
        this.Name = jsonPhysicalStop.getString("Name");
        this.Latitude = jsonPhysicalStop.getDouble("Latitude");
        this.LineId = LineId;
        this.Longitude = jsonPhysicalStop.getDouble("Longitude");
        this.PointType = jsonPhysicalStop.getInt("PointType");
        this.LogicalStopId = jsonPhysicalStop.getInt("LogicalStopId");
        this.LocalityId = jsonPhysicalStop.getInt("LocalityId");
        this.OperatorId = jsonPhysicalStop.getInt("OperatorId");
        this.Accessibility = 0;//new AccessibilityValues(jsonPhysicalStop.getJSONObject("AccessibilityStatus")).getAccessibilityCode();

        this.locality = new Locality(jsonPhysicalStop.getJSONObject("Locality"));
        this.logicalStop = new LogicalStop(jsonPhysicalStop.getJSONObject("LogicalStop"));
    }
    public PhysicalStop(JSONObject jsonPhysicalStop) throws JSONException {
        new PhysicalStop(jsonPhysicalStop, 0, 0);
    }

    public PhysicalStop(Integer Category,
                        Integer Direction,
                        long Id,
                        String Name,
                        Double Latitude,
                        Integer LineId,
                        Double Longitude,
                        Integer PointType,
                        Integer LogicalStopId,
                        Integer LocalityId,
                        Integer OperatorId,
                        Integer Accessibility) {
        this.Category = Category;
        this.Direction = Direction;
        this.Id = Id;
        this.Name = Name;
        this.Latitude = Latitude;
        this.LineId = LineId;
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
    public Integer getDirection() {
        return Direction;
    }
    public Double getLatitude() {
        return Latitude;
    }
    public Integer getLineId() {
        return LineId;
    }
    public Double getLongitude() {
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
