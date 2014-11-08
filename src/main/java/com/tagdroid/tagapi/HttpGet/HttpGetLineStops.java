package com.tagdroid.tagapi.HttpGet;

import com.tagdroid.tagapi.HttpGetTask;
import com.tagdroid.tagapi.ProgressionInterface;

public class HttpGetLineStops extends HttpGetTask {
    public HttpGetLineStops(Integer lineId, Integer direction, ProgressionInterface progressionInterface) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLineStops/json"
                + "?key=TAGDEV&LineId=" + lineId + "&Direction=" + direction,
                progressionInterface);
    }
}
