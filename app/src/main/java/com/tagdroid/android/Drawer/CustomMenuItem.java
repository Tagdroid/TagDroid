package com.tagdroid.android.Drawer;


public class CustomMenuItem {
    public boolean isDivider;
    public boolean isHeader;
    public boolean isIcon;
    public boolean isCounter;

    public String title;
    public int iconRes;
    public int drawable;
    public int counter;

/*
    public CustomMenuItem(String title, int iconRes, boolean iconBool) {
        this(title, iconRes, iconBool,0);
    }

    public CustomMenuItem(String title, boolean iconBool) {
        this(title, iconBool , 0);
    }*/

    /*** DIVIDER ***/
    public CustomMenuItem() {
        this.isDivider = true;
        this.isHeader = false;
        this.isIcon = false;
        this.isCounter = false;
    }

    /*** HEADER ***/
    public CustomMenuItem(int drawable) {
        this.isDivider = false;
        this.isHeader = true;
        this.isIcon = false;
        this.isCounter = false;

        this.drawable = drawable;
    }

    /*** ITEM SANS ICONE ***/
    public CustomMenuItem(String title) {
        this.isDivider = false;
        this.isHeader = false;
        this.isIcon = false;
        this.isCounter = false;

        this.title = title;
    }

    /*** ITEM AVEC ICONE ***/
    public CustomMenuItem(String title, int iconRes) {
        this.isDivider = false;
        this.isHeader = false;
        this.isIcon = true;
        this.isCounter = false;

        this.title = title;
        this.iconRes = iconRes;
    }

    /*** ITEM AVEC ICONE + COMPTEUR***/
    public CustomMenuItem(String title, int iconRes,  int counter) {
        this.isDivider = false;
        this.isHeader = false;
        this.isIcon = true;
        this.isCounter = true;

        this.title = title;
        this.iconRes = iconRes;
        this.counter = counter;
    }



}
