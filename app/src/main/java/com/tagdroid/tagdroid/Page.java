package com.tagdroid.tagdroid;

import android.app.Fragment;

public abstract class Page extends Fragment {
    public abstract String  getTitle();
    public abstract Integer getMenuId();
}
