package aarogya.pharmacy.walkInSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue;
    private String formV_Cat;

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

    public String getFormV_Cat() {
        return formV_Cat;
    }

    public void setFormV_Cat(String formV_Cat) {
        this.formV_Cat = formV_Cat;
    }

}
