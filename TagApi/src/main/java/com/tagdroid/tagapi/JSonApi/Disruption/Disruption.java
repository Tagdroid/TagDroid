package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

public class Disruption {
    private String BeginValidityDateString;
    private String Cause;
    private String CreateDateString;
    private String Description;
    private DisruptedLine[] disruptedLines;
    private DisruptedStopPoint[] disruptedStopPoints;
    private DisruptionType disruptionType;
    private String EndValidityDateString;
    private long Id;
    private int Latitude, Longitude;
    private String Name;
    private String Source;

    public Disruption(JSONObject jsonDisruption) throws JSONException {
        this.BeginValidityDateString= jsonDisruption.getString("BeginValidityDateString");
        this.Cause          = jsonDisruption.getString("Cause");
        this.CreateDateString  = jsonDisruption.getString("CreateDateString");
        this.Description    = jsonDisruption.getString("Description");
        this.disruptedLines = DisruptedLine.
                DisruptedLineArray(jsonDisruption.getJSONArray("DisruptedLines"));
        this.disruptedStopPoints = DisruptedStopPoint.
                DisruptedStopPointArray(jsonDisruption.getJSONArray("DisruptedStopPoints"));
        this.disruptionType = new DisruptionType(jsonDisruption.getJSONObject("DisruptionType"));
        this.EndValidityDateString = jsonDisruption.getString("EndValidityDateString");
        this.Id             = jsonDisruption.getLong("Id");
        this.Latitude       = jsonDisruption.getInt("Latitude");
        this.Longitude      = jsonDisruption.getInt("Longitude");
        this.Name           = jsonDisruption.getString("Name");
        this.Source         = jsonDisruption.getString("Source");
    }

    public Disruption(String BeginValidityDateString,
                      String Description,
                      String Cause,
                      String CreateDateString,
                      String EndValidityDateString,
                      long Id,
                      int Latitude,
                      int Longitude,
                      String Name,
                      String Source) {

        this.BeginValidityDateString = BeginValidityDateString;
        this.Cause = Cause;
        this.CreateDateString = CreateDateString;
        this.Description = Description;
        this.EndValidityDateString = EndValidityDateString;
        this.Id = Id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Name = Name;
        this.Source = Source;
    }

    public String getBeginValidityDateString() {
        return BeginValidityDateString;
    }
    public String getCause() {
        return Cause;
    }
    public String getCreateDateString() {
        return CreateDateString;
    }
    public String getDescription() {
        return Description;
    }
    public String getEndValidityDateString() {
        return EndValidityDateString;
    }
    public long getId() {
        return Id;
    }
    public int getLatitude() {
        return Latitude;
    }
    public int getLongitude() {
        return Longitude;
    }
    public String getName() {
        return Name;
    }
    public String getSource() {
        return Source;
    }

    public DisruptionType getDisruptionType() {
        return disruptionType;
    }
    public DisruptedLine[] getDisruptedLines() {
        return disruptedLines;
    }
    public DisruptedStopPoint[] getDisruptedStopPoints() {
        return disruptedStopPoints;
    }
}
