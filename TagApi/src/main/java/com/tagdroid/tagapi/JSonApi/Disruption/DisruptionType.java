package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

/*
    Pour plus de cohérence et pour ne pas risquer de ses planter, ça peut être mieux de mettre
    les arguments dans l'ordre alphabétique… Sinon on risque d'avoir des conflits ^^''
    (C'est une norme comme une autre quoi…)
 */

public class DisruptionType {
    private long Code;
    private long Id;
    private String Name;


    public DisruptionType(JSONObject jsonDisruptionType) throws JSONException {
        this.Code = jsonDisruptionType.getInt("Code");
        this.Id = jsonDisruptionType.getInt("Id");
        this.Name = jsonDisruptionType.getString("Name");
    }

    public DisruptionType(int Code,
                          int Id,
                          String Name) {
        this.Code = Code;
        this.Id = Id;
        this.Name = Name;
    }

    public long getCode() {
        return Code;
    }
    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
}
