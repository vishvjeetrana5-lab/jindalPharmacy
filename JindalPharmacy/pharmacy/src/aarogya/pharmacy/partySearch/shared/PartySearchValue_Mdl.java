package aarogya.pharmacy.partySearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PartySearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue, v_Type;

    public PartySearchValue_Mdl() {
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

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

}
