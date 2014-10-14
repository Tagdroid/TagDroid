package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

public class DisruptionType {
    private String Name;
    private Integer Code,
            Id;


    public DisruptionType(JSONObject jsonDisruptionType) throws JSONException {
        this.Name = jsonDisruptionType.getString("Name");
        this.Code = jsonDisruptionType.getInt("Code");
        this.Id = jsonDisruptionType.getInt("Id");
    }

    public DisruptionType(String Name,
                          Integer Code,
                          Integer Id) {

        this.Name = Name;
        this.Code = Code;
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }
    public Integer getCode() {
        return Code;
    }
    public Integer getId() {
        return Id;
    }

}
