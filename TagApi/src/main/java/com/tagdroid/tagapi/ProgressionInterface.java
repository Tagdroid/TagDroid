package com.tagdroid.tagapi;

public interface ProgressionInterface {
    void onDownloadStart();
    void onDownloadFailed(Exception e);
    void onDownloadFailed(Integer e);
    void onDownloadComplete(String resultString);
    void onJSonParsingStarted();
    void onJSonParsingFailed(Exception e);
    void onJSonParsingFailed(String e);
    void onJSonParsingComplete();
}
