package com.tagdroid.tagapi;

public interface ProgressionInterface {
    void onDownloadStart();
    void onDownloadFailed(Exception e);
    void onDownloadComplete();

    void onJSonParsingStarted();
    void onJSonParsingFailed(Exception e);
    void onJSonParsingComplete();
}
