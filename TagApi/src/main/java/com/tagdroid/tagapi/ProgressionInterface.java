package com.tagdroid.tagapi;

public interface ProgressionInterface {
    void onDownloadStart();
    void onDownloadFailed(Exception e);
    void onDownloadProgression(int progression, int total);
    void onDownloadComplete();
}
