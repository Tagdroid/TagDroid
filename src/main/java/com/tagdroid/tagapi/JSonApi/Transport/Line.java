package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Line {
    private Integer Accessibility;
    private String Company;
    private Integer CompanyId;
    private Boolean Deleted;
    private Direction[] DirectionList;
    private Integer Id;
    private String Name;
    private Integer NetworkId;
    private String NetworkName;
    private String Number;
    private Operator mOperator;
    private Integer OperatorId;
    private Integer Order;
    private Boolean Published;
    private Integer TransportMode;

    public Line(JSONObject jsonLine) throws JSONException {
        this.Accessibility = new AccessibilityValues(jsonLine.getJSONObject("AccessibilityStatus"))
                .getAccessibilityCode();
        this.Company = jsonLine.getString("Company");
        this.CompanyId = jsonLine.getInt("CompanyId");
        this.Deleted = jsonLine.getBoolean("Deleted");
        this.DirectionList = Direction.DirectionArray(jsonLine.getJSONArray("DirectionList"));
        this.Id = jsonLine.getInt("Id");
        this.Name = jsonLine.getString("Name");
        this.NetworkId = jsonLine.getInt("NetworkId");
        this.NetworkName = jsonLine.getString("NetworkName");
        this.Number = jsonLine.getString("Number");
        this.mOperator = new Operator(jsonLine.getJSONObject("Operator"));
        this.OperatorId = jsonLine.getInt("OperatorId");
        this.Order = jsonLine.getInt("Order");
        this.Published = jsonLine.getBoolean("Published");
        this.TransportMode = jsonLine.getInt("TransportMode");
    }


    public Integer getAccessibility() {
        return Accessibility;
    }
    public String getCompany() {
        return Company;
    }
    public Integer getCompanyId() {
        return CompanyId;
    }
    public Boolean getDeleted() {
        return Deleted;
    }
    public Direction[] getDirectionList() {
        return DirectionList;
    }
    public Integer getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public Integer getNetworkId() {
        return NetworkId;
    }
    public String getNetworkName() {
        return NetworkName;
    }
    public String getNumber() {
        return Number;
    }
    public Operator getOperator() {
        return mOperator;
    }
    public Integer getOperatorId() {
        return OperatorId;
    }
    public Integer getOrder() {
        return Order;
    }
    public Boolean getPublished() {
        return Published;
    }
    public Integer getTransportMode() {
        return TransportMode;
    }
}
