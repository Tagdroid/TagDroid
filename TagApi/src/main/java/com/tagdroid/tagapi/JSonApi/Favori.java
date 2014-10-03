package com.tagdroid.tagapi.JSonApi;

public class Favori {
    public Long Id;
    public String Name;
    public String Ligne;
    public Double Latitude, Longitude;

    public Favori(long Id,
                  String Name,
                  String Ligne,
                  Double Latitude,
                  Double Longitude) {
        this.Id = Id;
        this.Name = Name;
        this.Ligne = Ligne;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
}
