package com.tagdroid.tagapi.Actualites;

import java.net.MalformedURLException;
import java.net.URL;

public class Flux {
    public final static String[] RSSURLS = {
            "http://www.tag.fr/rss_evenement.php",
            "http://www.smtc-grenoble.org/actualites-rss"
    };
    private final static String[] RSSNAMES = {
            "TAG",
            "SMTC"
    };

    public static String[] getRssNames() {
        return RSSNAMES;
    }

    public static URL getRSSUrl(int id) {
        try {
            return new URL(RSSURLS[id]);
        } catch (MalformedURLException ignored) { }
        return null;
    }
    public static String getRSSName(int id) {
        return RSSNAMES[id];
    }
}
