package aarogya.selfToolkit.customMultiLine.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldName;
    private String fieldValue;
    private String consultantCode;
    private CustomMultiLineNature nature;

    public SearchValue_Mdl() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public CustomMultiLineNature getNature() {
        return nature;
    }

    public void setNature(CustomMultiLineNature nature) {
        this.nature = nature;
    }
}
