package com.tagdroid.tagapi.Actualites;

public class Actualite {
    public String titre;
    public String url;
    public String description;
    public String image;
    public int fluxId;

    public Actualite(String titre, String url, String description, String image, int fluxId) {
        this.titre = titre;
        this.url = url;
        this.description = description;
        this.image = image;
        this.fluxId = fluxId;
    }
}
