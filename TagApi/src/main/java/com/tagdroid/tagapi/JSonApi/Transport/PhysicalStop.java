package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class PhysicalStop {
    private int Category,
            Direction;
    private long Id;
    private String Name;
    private Double Latitude, Longitude;
    private long LineId;
    private int PointType,
            LogicalStopId,
            LocalityId,
            OperatorId,
            Accessibility;

    private Locality locality;
    private LogicalStop logicalStop;

    public PhysicalStop(JSONObject jsonPhysicalStop, long LineId, int Direction) throws JSONException {
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

    public PhysicalStop(int Category,
                        int Direction,
                        long Id,
                        String Name,
                        Double Latitude,
                        int LineId,
                        Double Longitude,
                        int PointType,
                        int LogicalStopId,
                        int LocalityId,
                        int OperatorId,
                        int Accessibility) {
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
    public int getCategory() {
        return Category;
    }
    public int getDirection() {
        return Direction;
    }
    public Double getLatitude() {
        return Latitude;
    }
    public long getLineId() {
        return LineId;
    }
    public Double getLongitude() {
        return Longitude;
    }
    public int getPointType() {
        return PointType;
    }
    public int getLogicalStopId() {
        return LogicalStopId;
    }
    public int getLocalityId() {
        return LocalityId;
    }
    public int getOperatorId() {
        return OperatorId;
    }
    public String getName() {
        return Name;
    }
    public int getAccessibility() {
        return Accessibility;
    }
    public Locality getLocality() { return locality; }
    public LogicalStop getLogicalStop() {
        return logicalStop;
    }
}
