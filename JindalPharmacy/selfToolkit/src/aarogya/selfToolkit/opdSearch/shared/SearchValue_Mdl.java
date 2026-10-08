package aarogya.selfToolkit.opdSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue;
    private OPDFilterType[] filterType = null;

    public enum OPDFilterType {

        CANCEL_DAYS, SERVICE_DAYS, OPD_ONLY, DAYCARE_ONLY, ALLOW_SITE_FOR_DIAGNOSTIC, ALLOW_SITE_FOR_PHARMACY,
        ALLOW_SITE_FOR_IPD, ALLOW_SITE_FOR_OPD, ALLOW_SITE_FOR_DAYCARE, TPA_ONLY, NON_TPA_ONLY;

        OPDFilterType() {
        }
    }

    public SearchValue_Mdl() {
    }

    public OPDFilterType[] getFilterType() {
        return filterType;
    }

    public void setFilterType(OPDFilterType[] filterType) {
        this.filterType = filterType;
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
