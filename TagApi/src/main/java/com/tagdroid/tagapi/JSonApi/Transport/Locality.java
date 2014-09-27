package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Locality {
    private long Id;
    private Integer InseeCode,
            Latitude,
            Longitude;
    String Name;

    public Locality(JSONObject jsonLocality) throws JSONException {
        this.Id = jsonLocality.getLong("Id");
        this.InseeCode = jsonLocality.getInt("InseeCode");
        this.Latitude = jsonLocality.getInt("Latitude");
        this.Longitude = jsonLocality.getInt("Longitude");
        this.Name = jsonLocality.getString("Name");
    }

    public Locality(long Id,
                    Integer InseeCode,
                    Integer Latitude,
                    Integer Longitude,
                    String Name) {
        this.Id = Id;
        this.InseeCode = InseeCode;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Name = Name;
    }


    public long getId() {
        return Id;
    }
    public long getInseeCode() {
        return InseeCode;
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
}
