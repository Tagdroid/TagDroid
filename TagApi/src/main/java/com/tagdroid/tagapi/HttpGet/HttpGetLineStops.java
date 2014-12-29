package com.tagdroid.tagapi.HttpGet;

import android.content.Context;

import com.tagdroid.tagapi.JSon2SQL.ReadJSonLineStops;
import com.tagdroid.tagapi.ProgressionInterface;

public class HttpGetLineStops extends HttpGetTask {
    private long lineId;
    private int direction;
    public HttpGetLineStops(long lineId, int direction, ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLineStops/json?key=TAGDEV"
                        +"&LineId=" + lineId + "&Direction=" + direction,
                progressionInterface, context);
        this.lineId = lineId;
        this.direction = direction;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        new ReadJSonLineStops(responseString, lineId, direction, progressionInterface, context).execute();
    }
}
