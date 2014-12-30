package com.tagdroid.tagapi;

public interface ProgressionInterface {
    void onDownloadStart();
    void onDownloadFailed(Exception e);
    void onDownloadComplete();
}
