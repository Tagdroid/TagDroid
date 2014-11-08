package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class Line {
    private Integer Accessibility;
    private String Company;
    private long CompanyId;
    private Integer Deleted;
    private Direction[] DirectionList;
    private long Id;
    private String Name;
    private long NetworkId;
    private String NetworkName;
    private String Number;
    private Operator mOperator;
    private long OperatorId;
    private Integer Order;
    private Integer Published;
    private Integer TransportMode;

    public Line(JSONObject jsonLine) throws JSONException {
        this.Accessibility = new AccessibilityValues(jsonLine.getJSONObject("AccessibilityStatus"))
                .getAccessibilityCode();
        this.Company = jsonLine.getString("Company");
        this.CompanyId = jsonLine.getInt("CompanyId");
        this.Deleted = jsonLine.getInt("Deleted");
        this.DirectionList = Direction.DirectionArray(jsonLine.getJSONArray("DirectionList"));
        this.Id = jsonLine.getInt("Id");
        this.Name = jsonLine.getString("Name");
        this.NetworkId = jsonLine.getInt("NetworkId");
        this.NetworkName = jsonLine.getString("NetworkName");
        this.Number = jsonLine.getString("Number");
        this.mOperator = new Operator(jsonLine.getJSONObject("Operator"));
        this.OperatorId = jsonLine.getInt("OperatorId");
        this.Order = jsonLine.getInt("Order");
        this.Published = jsonLine.getInt("Published");
        this.TransportMode = jsonLine.getInt("TransportMode");
    }


    public Line(Integer Accessibility,
                String Company,
                long CompanyId,
                int Deleted,
                long Id,
                String Name,
                long NetworkId,
                String NetworkName,
                String Number,
                long OperatorId,
                Integer Order,
                int Published,
                Integer TransportMode) {

        this.Accessibility = Accessibility;
        this.Company = Company;
        this.CompanyId = CompanyId;
        this.Id = Id;
        this.Name = Name;
        this.Deleted = Deleted;
        this.NetworkId = NetworkId;
        this.NetworkName = NetworkName;
        this.Number = Number;
        this.OperatorId = OperatorId;
        this.Order = Order;
        this.Published = Published;
        this.TransportMode = TransportMode;
    }

    public Integer getAccessibility() {
        return Accessibility;
    }
    public String getCompany() {
        return Company;
    }
    public long getCompanyId() {
        return CompanyId;
    }
    public Integer getDeleted() {
        return Deleted;
    }
    public Direction[] getDirectionList() {
        return DirectionList;
    }
    public long getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public long getNetworkId() {
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
    public long getOperatorId() {
        return OperatorId;
    }
    public Integer getOrder() {
        return Order;
    }
    public Integer getPublished() {
        return Published;
    }
    public Integer getTransportMode() {
        return TransportMode;
    }
}
