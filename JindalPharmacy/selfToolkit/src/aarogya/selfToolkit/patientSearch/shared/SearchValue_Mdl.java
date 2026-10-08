package aarogya.selfToolkit.patientSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue;
    private FilterType[] filterType = null;

    public enum FilterType {

        WITHOUT_EXPIRED;

        private FilterType() {
        }
    }

    public SearchValue_Mdl() {
    }

    public FilterType[] getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType[] filterType) {
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
