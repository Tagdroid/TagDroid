package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Line {
    private long Id;
    private String Number;      // En fait le nom de la ligne (C2, etc)
    private String Name;        // Contient les noms des terminus. "Description" quoi.
    private boolean IsActive;   // Détermine si la ligne est active ou non
    private Direction[] DirectionList;  // Contient la liste des directions.

    // Valeurs non enregistrées dans la base de données mais utilisées dans l'appli
    public int color;
    public int LineType = 0;   // Le type de ligne (0=Inconnu,1 = Tram, 2 = Chrono, 3 = Proximo, 4 = Flexo)
    public final static int UNKNOWN = 0, TRAM = 1, CHRONO = 2, PROXIMO = 3, FLEXO = 4;

/*    // Valeurs inutiles et donc non enregistrées dans la base de données
    private int TransportMode;  // Toujours 0
    private int Accessibility;  // Toujours 0,0,0,0
    private String Company;     // Toujours TAG
    private long CompanyId;     // Toujours 2
    private long NetworkId;     // Toujours 2
    private Operator mOperator; // Toujours 1
    private long OperatorId;    // --
    private int Order;          // C'est quoi ce machin… Certaines lignes ont le même.
*/
    public Line(JSONObject jsonLine) throws JSONException {
        this.Id = jsonLine.getLong("Id");
        this.Number = jsonLine.getString("Number");
        this.Name = jsonLine.getString("Name");
        this.IsActive = jsonLine.getBoolean("Published") && !jsonLine.getBoolean("Deleted");
        this.DirectionList = Direction.DirectionArray(jsonLine.getJSONArray("DirectionList"), Id);
    }

    public Line(long Id, String Number, String Name, boolean IsActive, int LineType, int color) {
        this.Id = Id;
        this.Number = Number;
        this.Name = Name;
        this.IsActive = IsActive;
        this.LineType = LineType;
        this.color = color;
    }
    public void setDirectionList(Direction[] directionList) {
        this.DirectionList = directionList;
    }

    public Line setColor(int color) {
        this.color = color;
        return this;
    }
    public int getColor() {
        return color;
    }

    public Line setLineType(int lineType) {
        this.LineType = lineType;
        return this;
    }
    public int getLineType() {
        return LineType;
    }

    public long getId() {
        return Id;
    }
    public String getNumber() {
        return Number;
    }
    public String getName() {
        return Name;
    }
    public boolean getIsActive() {
        return IsActive;
    }
    public Direction[] getDirectionList() {
        return DirectionList;
    }
}
