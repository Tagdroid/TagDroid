package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;


public class LineStop {
    private long Id;
    private String Name;
    private int Position;
    private double Latitude, Longitude;
    private LogicalStop logicalStop;
    private long LogicalStopId;
    private Locality locality;
    private long LocalityId;

    private long LineId;
    private int Direction;

    public LineStop(JSONObject jsonPhysicalStop, long LineId, int Direction, int position) throws JSONException {
        this.Id = jsonPhysicalStop.getLong("Id");
        this.Name = jsonPhysicalStop.getString("Name");
        this.Position = position;
        this.LogicalStopId = jsonPhysicalStop.getInt("LogicalStopId");
        this.Latitude = jsonPhysicalStop.getDouble("Latitude");
        this.Longitude = jsonPhysicalStop.getDouble("Longitude");
        this.LocalityId = jsonPhysicalStop.getInt("LocalityId");

        this.Direction = Direction;
        this.LineId = LineId;

        this.locality = new Locality(jsonPhysicalStop.getJSONObject("Locality"));
        this.logicalStop = new LogicalStop(jsonPhysicalStop.getJSONObject("LogicalStop"));
    }

    public LineStop(long Id, String Name, int position,
                    long LogicalStopId, int LocalityId,
                    double Latitude, double Longitude,
                    long LineId, int Direction) {
        this.Id = Id;
        this.Name = Name;
        this.Position = position;
        this.LogicalStopId = LogicalStopId;
        this.LocalityId = LocalityId;
        this.Direction = Direction;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.LineId = LineId;
    }

    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public int getPosition() {
        return Position;
    }
    public long getLogicalStopId() {
        return LogicalStopId;
    }
    public long getLocalityId() {
        return LocalityId;
    }
    public double getLatitude() {
        return Latitude;
    }
    public double getLongitude() {
        return Longitude;
    }
    public long getLineId() {
        return LineId;
    }
    public int getDirection() {
        return Direction;
    }

    public Locality getLocality() { return locality; }
    public LogicalStop getLogicalStop() {
        return logicalStop;
    }
}
