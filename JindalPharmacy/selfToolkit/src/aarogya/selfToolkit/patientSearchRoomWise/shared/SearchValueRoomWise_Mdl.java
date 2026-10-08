package aarogya.selfToolkit.patientSearchRoomWise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValueRoomWise_Mdl extends KeyField implements Serializable, IsSerializable {

    public enum FilterType {

        NOT_DISCHARGED, DISCHARGED, NOT_BILLED, NOT_BILLED_APPROVED, EMERGENCY_ONLY, IPD_ONLY, FEMALE_ONLY, TPA, NON_TPA,
        ALLOW_SITE_FOR_DIAGNOSTIC, ALLOW_SITE_FOR_PHARMACY, ALLOW_SITE_FOR_IPD, ALLOW_SITE_FOR_OPD, ALLOW_SITE_FOR_DAYCARE,ALLOW_WITHOUT_ROOM_CONFIRMATION;

        private FilterType() {
        }
    }

    private String fieldName;
    private String fieldNameValue;
    private String patientType;
    private FilterType[] filterType = null;

    public SearchValueRoomWise_Mdl() {
    }

    public FilterType[] getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType[] filterType) {
        this.filterType = filterType;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameValue() {
        return fieldNameValue;
    }

    public void setFieldNameValue(String fieldNameValue) {
        this.fieldNameValue = fieldNameValue;
    }
}
