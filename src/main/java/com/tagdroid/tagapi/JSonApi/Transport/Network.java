package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Network {
    private Long Id;
    private String Name;

    public Network(JSONObject jsonOperator) throws JSONException {
        this.Id = jsonOperator.getLong("Id");
        this.Name = jsonOperator.getString("Name");
    }

    public Network(long Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public Long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
}
