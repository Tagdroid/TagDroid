package com.tagdroid.tagapi.JSonApi.Disruption;

import org.json.JSONException;
import org.json.JSONObject;

/*
    Pour plus de cohérence et pour ne pas risquer de ses planter, ça peut être mieux de mettre
    les arguments dans l'ordre alphabétique… Sinon on risque d'avoir des conflits ^^''
    (C'est une norme comme une autre quoi…)
 */

// J'ai modifié ton code pour avoir un ordre alphabétique ;) Tu peux virer ce comm dès que tu l'as lu !

public class DisruptionType {
    private Integer Code;
    private Integer Id;
    private String Name;


    public DisruptionType(JSONObject jsonDisruptionType) throws JSONException {
        this.Code = jsonDisruptionType.getInt("Code");
        this.Id = jsonDisruptionType.getInt("Id");
        this.Name = jsonDisruptionType.getString("Name");
    }

    public DisruptionType(Integer Code,
                          Integer Id,
                          String Name) {
        this.Code = Code;
        this.Id = Id;
        this.Name = Name;
    }

    public Integer getCode() {
        return Code;
    }
    public Integer getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
}
