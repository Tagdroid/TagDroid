package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Operator {
    private String Code;
    private long Id;
    private String Name;

    public Operator(JSONObject jsonOperator) throws JSONException {
        this.Code = jsonOperator.getString("Code");
        this.Id = jsonOperator.getInt("Id");
        this.Name = jsonOperator.getString("Name");
    }

    public String getCode() {
        return Code;
    }
    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
}
