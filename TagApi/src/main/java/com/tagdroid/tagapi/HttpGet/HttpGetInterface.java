package com.tagdroid.tagapi.HttpGet;

public interface HttpGetInterface {
    void onHttpGetStart();

    void onHttpGetDownloadFinished();
    void onHttpGetDownloadFailed();

    void onHttpGetReadJSonFinished();
    void onHttpGetReadJSonFailed(Exception e);
    void onHttpGetBadStatusCode(int statusCode, String message);
}
