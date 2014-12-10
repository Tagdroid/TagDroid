package com.tagdroid.tagapi.HttpGet;

import com.tagdroid.tagapi.HttpGetTask;
import com.tagdroid.tagapi.ProgressionInterface;

public class HttpGetLinesList extends HttpGetTask {
    public HttpGetLinesList(ProgressionInterface progressionInterface) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLines/json?key=TAGDEV&OperatorId=1",
                progressionInterface);
    }
}