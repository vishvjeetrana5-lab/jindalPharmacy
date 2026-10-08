package aarogya.dashBoard.billDueNotPaid.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldNameValue;
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
