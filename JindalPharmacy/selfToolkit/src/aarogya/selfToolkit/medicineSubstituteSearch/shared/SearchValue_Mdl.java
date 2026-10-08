package aarogya.selfToolkit.medicineSubstituteSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue;
    private String subsituteMedicine;

    public SearchValue_Mdl() {
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

    public String getSubsituteMedicine() {
        return subsituteMedicine;
    }

    public void setSubsituteMedicine(String subsituteMedicine) {
        this.subsituteMedicine = subsituteMedicine;
    }

}
