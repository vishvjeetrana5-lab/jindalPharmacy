package aarogya.pharmacy.saleBillSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValueWise_Mdl extends KeyField implements Serializable, IsSerializable {

    public enum FilterType {

        SALE_BILL, SALE_BILL_RETURN;

        private FilterType() {
        }
    }

    private String fieldName;
    private String fieldNameValue;
    private FilterType[] filterType = null;

    public SearchValueWise_Mdl() {
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
