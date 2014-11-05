package com.tagdroid.tagdroid.Drawer;


public class CustomMenuItem {
    public String title;
    public int iconRes;
    public boolean isIcon;
    public int counter;
    public boolean isHeader;

    public CustomMenuItem(String title, int iconRes, boolean iconBool, boolean header) {
        this(title, iconRes, iconBool, header, 0);
    }

    public CustomMenuItem(String title, boolean iconBool, boolean header) {
        this(title, iconBool , header, 0);
    }

    public CustomMenuItem(String title, int iconRes, boolean iconBool, boolean header, int counter) {
        this.title = title;
        this.iconRes = iconRes;
        this.isIcon = iconBool;
        this.isHeader = header;
        this.counter = counter;
    }

    public CustomMenuItem(String title, boolean iconBool, boolean header, int counter) {
        this.title = title;
        this.isHeader = header;
        this.isIcon = iconBool;
        this.counter = counter;
    }
}
