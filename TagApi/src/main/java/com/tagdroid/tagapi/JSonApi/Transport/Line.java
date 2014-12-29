package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Line {
    private long Id;
    private String Number;      // En fait le nom de la ligne (C2, etc)
    private String Name;        // Contient les noms des terminus. "Description" quoi.
    private boolean IsActive;   // Détermine si la ligne est active ou non
    private Direction[] DirectionList;  // Contient la liste des directions. //TODO en SQL

    private int TransportMode;  // Toujours 0
    private int Accessibility;  // Toujours 0,0,0,0

    private String Company;     // Toujours TAG
    private long CompanyId;     // Toujours 2
    private long NetworkId;     // Toujours 2
    private Operator mOperator; // Toujours 1
    private long OperatorId;    // --
    private int Order;          // C'est quoi ce machin… Certaines lignes ont le même.

    public Line(JSONObject jsonLine) throws JSONException {
        this.Id = jsonLine.getLong("Id");
        this.Number = jsonLine.getString("Number");
        this.Name = jsonLine.getString("Name");
        this.IsActive = jsonLine.getBoolean("Published") && !jsonLine.getBoolean("Deleted");
        this.DirectionList = Direction.DirectionArray(jsonLine.getJSONArray("DirectionList"), Id);

        this.Order = jsonLine.getInt("Order");
    }


    public Line(long Id,
                String Name,
                String Number,
                boolean IsActive,
                int Order) {

        this.Id = Id;
        this.Name = Name;
        this.Number = Number;
        this.Order = Order;
        this.IsActive = IsActive;
    }

    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public String getNumber() {
        return Number;
    }
    public boolean getIsActive() {
        return IsActive;
    }
    public Direction[] getDirectionList() {
        return DirectionList;
    }
    public int getOrder() {
        return Order;
    }
}
