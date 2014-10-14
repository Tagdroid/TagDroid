package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

public class ActualAndFutureDisruption {
    private long Id;
    private String Name,
            Source,
            Description,
            Cause,
            BeginValidityDateString,
            EndValidityDateString;
    private Integer Latitude,
            Longitude;

    private DisruptionType type;
    private DisruptedLines lines;
    private DisruptedStopPoints stoppoints;

    public ActualAndFutureDisruption(JSONObject jsonAAFDisruption) throws JSONException {
        this.Id = jsonAAFDisruption.getLong("Id");
        this.Name = jsonAAFDisruption.getString("Name");
        this.Source = jsonAAFDisruption.getString("Source");
        this.Description = jsonAAFDisruption.getString("Description");
        this.Cause = jsonAAFDisruption.getString("Cause");
        this.BeginValidityDateString = jsonAAFDisruption.getString("BeginValidityDateString");
        this.EndValidityDateString = jsonAAFDisruption.getString("EndValidityDateString");
        this.Latitude = jsonAAFDisruption.getInt("Latitude");
        this.Longitude = jsonAAFDisruption.getInt("Longitude");


        this.type = new DisruptionType(jsonAAFDisruption.getJSONObject("DisruptionType"));
        this.lines = new DisruptedLines(jsonAAFDisruption.getJSONObject("DisruptedLines"));
        this.stoppoints = new DisruptedStopPoints(jsonAAFDisruption.getJSONObject("DisruptedStopPoints"));

    }

    public ActualAndFutureDisruption(long Id,
                                     String Name,
                                     String Source,
                                     String Description,
                                     String Cause,
                                     String BeginValidityDateString,
                                     String EndValidityDateString,
                                     Integer Latitude,
                                     Integer Longitude) {

        this.Id = Id;
        this.Name = Name;
        this.Source = Source;
        this.Description = Description;
        this.Cause = Cause;
        this.BeginValidityDateString = BeginValidityDateString;
        this.EndValidityDateString = EndValidityDateString;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public String getSource() {
        return Source;
    }
    public String getDescription() {
        return Description;
    }
    public String getCause() {
        return Cause;
    }
    public String getBeginValidityDateString() {
        return BeginValidityDateString;
    }
    public String getEndValidityDateString() {
        return EndValidityDateString;
    }
    public Integer getLatitude() {
        return Latitude;
    }
    public Integer getLongitude() {
        return Longitude;
    }

    public DisruptionType getDisruptionType() { return type; }
    public DisruptedLines getDisruptedLines() {
        return lines;
    }
    public DisruptedStopPoints getDisruptedStopPoints() {
        return stoppoints;
    }
}
