package com.tagdroid.tagapi.JSonApi.Transport;

import org.json.JSONException;
import org.json.JSONObject;

public class AccessibilityValues {
    private Integer BlindAccess,
            DeafAccess,
            MentalIllnessAccess,
            WheelChairAccess;

    public AccessibilityValues(JSONObject jsonAccessibilityValues) throws JSONException {
        this.BlindAccess = jsonAccessibilityValues.getInt("BlindAccess");
        this.DeafAccess = jsonAccessibilityValues.getInt("DeafAccess");
        this.MentalIllnessAccess = jsonAccessibilityValues.getInt("MentalIllnessAccess");
        this.WheelChairAccess = jsonAccessibilityValues.getInt("WheelChairAccess");
    }

    public Integer getAccessibilityCode() {
        return BlindAccess + 2 * DeafAccess + 4 * MentalIllnessAccess + 8 * WheelChairAccess;
    }
    public AccessibilityValues(Integer AccessibilityValuesCode) {
        BlindAccess = AccessibilityValuesCode % 2;
        AccessibilityValuesCode /=2;
        DeafAccess = AccessibilityValuesCode % 2;
        AccessibilityValuesCode /=2;
        MentalIllnessAccess = AccessibilityValuesCode % 2;
        AccessibilityValuesCode /=2;
        WheelChairAccess = AccessibilityValuesCode % 2;
    }
}
