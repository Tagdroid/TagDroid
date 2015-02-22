package com.tagdroid.tagapi;

import android.util.Pair;

import com.tagdroid.tagapi.JSonApi.Transport.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class LinesColors {
    public static Pair<Integer, String> matchALine(String LineName) {
        Integer i;
        i = KnownTramways.indexOf(LineName);
        if (i != -1)
            return Pair.create(Line.TRAM, KnownTramwaysColors.get(i));

        i = KnownChrono.indexOf(LineName);
        if (i != -1)
            return Pair.create(Line.CHRONO, KnownChronoColors.get(i));

        i = KnownProximo.indexOf(LineName);
        if (i != -1)
            return Pair.create(Line.PROXIMO, KnownProximoColors.get(i));

        i = KnownFlexo.indexOf(LineName);
        if (i != -1)
            return Pair.create(Line.FLEXO, KnownFlexoColors.get(i));

        return Pair.create(Line.UNKNOWN, ColorDefault);
    }

    private final static ArrayList<String> KnownTramways = new ArrayList<>(Arrays.asList(
            "A",
            "B",
            "C",
            "D",
            "E",
            "EBUS"
    ));
    private final static ArrayList<String> KnownChrono = new ArrayList<>(Arrays.asList(
            "C1",
            "C2",
            "C3",
            "C4",
            "C5",
            "C6"
    ));
    private final static ArrayList<String> KnownProximo = new ArrayList<>(Arrays.asList(
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22"
    ));
    private final static ArrayList<String> KnownFlexo = new ArrayList<>(Arrays.asList(
            "40",
            "41",
            "42",
            "43",
            "44",
            "45",
            "46",
            "47",
            "48",
            "49",
            "50",
            "51",
            "52",
            "53",
            "54",
            "55",
            "56",
            "57",
            "58",
            "60",
            "61",
            "62",
            "63",
            "65",
            "66",
            "67",
            "68",
            "69"
    ));

    // Défaut
    private final static String ColorDefault = "#888888";

    private final static ArrayList<String> KnownTramwaysColors = new ArrayList<>(Arrays.asList(
            "#0076BD",
            "#009b3A",
            "#E4007C",
            "#F39A00",
            "#58318A",
            "#5C3292"
    ));
    private final static ArrayList<String> KnownChronoColors = new ArrayList<>(Arrays.asList(
            "#ffed00",
            "#fed33c",
            "#f49712",
            "#fed300",
            "#f9b122",
            "#ffeb00"
    ));
    private final static ArrayList<String> KnownProximoColors = new ArrayList<>(Arrays.asList(
            "#94c11f",
            "#009a3e",
            "#1db6b7",
            "#c9cc00",
            "#6cbe99",
            "#74a0ce",
            "#06a68a",
            "#06a68a",
            "#74a0ce",
            "#35bad9",
            "#35bad9",
            "#35bad9"
    ));
    private final static ArrayList<String> KnownFlexoColors = new ArrayList<>(Arrays.asList(
            "#ea516c",
            "#e975ab",
            "#8164a9",
            "#8978b6",
            "#9c6eae",
            "#8978b6",
            "#f5b4cb",
            "#8978b6",
            "#ea516c",
            "#8164a9",
            "#ba328b",
            "#8978b6",
            "#f192a3",
            "#e975ab",
            "#9c6eae",
            "#9c6eae",
            "#e975ab",
            "#4b2582",
            "#502182",
            "#ba328b",
            "#f192b4",
            "#ba328b",
            "#ba328b",
            "#f192b4",
            "#8164a9",
            "#ba328b",
            "#ea516c",
            "#ea516c"
    ));
}
