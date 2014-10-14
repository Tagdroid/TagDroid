package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

public class ActualAndFutureDisruption {
    private String BeginValidityDateString;
    private String Cause;
    private String Description;
    private DisruptedLine[] disruptedLines;
    private DisruptedStopPoint[] disruptedStopPoints;
    private DisruptionType disruptionType;
    private String EndValidityDateString;
    private long Id;
    private Integer Latitude,
            Longitude;
    private String Name;
    private String Source;

    public ActualAndFutureDisruption(JSONObject jsonAAFDisruption) throws JSONException {
        this.BeginValidityDateString = jsonAAFDisruption.getString("BeginValidityDateString");
        this.Cause = jsonAAFDisruption.getString("Cause");
        this.Description = jsonAAFDisruption.getString("Description");

        this.disruptedLines = DisruptedLine
                .DisruptedLineArray(jsonAAFDisruption.getJSONArray("DisruptedLines"));
        this.disruptedStopPoints = DisruptedStopPoint
                .DisruptedStopPointArray(jsonAAFDisruption.getJSONArray("DisruptedStopPoints"));
        this.disruptionType =
                new DisruptionType(jsonAAFDisruption.getJSONObject("DisruptionType"));

        this.EndValidityDateString = jsonAAFDisruption.getString("EndValidityDateString");
        this.Id = jsonAAFDisruption.getLong("Id");
        this.Latitude = jsonAAFDisruption.getInt("Latitude");
        this.Longitude = jsonAAFDisruption.getInt("Longitude");
        this.Name = jsonAAFDisruption.getString("Name");
        this.Source = jsonAAFDisruption.getString("Source");
    }

    public ActualAndFutureDisruption(String BeginValidityDateString,
                                     String Description,
                                     String Cause,
                                     String EndValidityDateString,
                                     long Id,
                                     Integer Latitude,
                                     Integer Longitude,
                                     String Name,
                                     String Source) {

        this.BeginValidityDateString = BeginValidityDateString;
        this.Cause = Cause;
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
    public String getDescription() {
        return Description;
    }
    public String getEndValidityDateString() {
        return EndValidityDateString;
    }
    public long getId() {
        return Id;
    }
    public Integer getLatitude() {
        return Latitude;
    }
    public Integer getLongitude() {
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
