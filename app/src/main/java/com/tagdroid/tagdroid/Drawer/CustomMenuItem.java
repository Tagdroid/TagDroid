package com.tagdroid.tagdroid.Drawer;


public class CustomMenuItem {
    public String title;
    public int iconRes;
    public int counter;
    public boolean isHeader;

    public CustomMenuItem(String title, int iconRes, boolean header) {
        this(title, iconRes, header, 0);
    }

    public CustomMenuItem(String title, int iconRes, boolean header, int counter) {
        this.title = title;
        this.iconRes = iconRes;
        this.isHeader = header;
        this.counter = counter;
    }
}
