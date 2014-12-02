package com.tagdroid.android.Drawer;

public class DrawerItem {
    String title;
    private int iconId = 0;
    private int iconSelectedId = 0;
    public  int counter = 0;

    public boolean isDivider = false;

    public DrawerItem() {
    }

    public DrawerItem setDivider() {
        this.isDivider = true;
        return this;
    }

    public DrawerItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public DrawerItem setIcon(int imageId) {
        this.iconId = imageId;
        return this;
    }

    public DrawerItem setIconSelected(int imageId) {
        this.iconSelectedId = imageId;
        return this;
    }

    public DrawerItem setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    public String getTitle() {
        return title;
    }
    public int getIconId() {
        return iconId;
    }
    public int getIconSelectedId() {
        return iconSelectedId;
    }
}
