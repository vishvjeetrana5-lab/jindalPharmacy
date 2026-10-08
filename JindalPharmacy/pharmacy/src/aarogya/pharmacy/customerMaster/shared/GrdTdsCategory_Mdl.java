package aarogya.pharmacy.customerMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdTdsCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fieldValue;
    private String fieldName;
    private String sNo;

    public GrdTdsCategory_Mdl() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String FieldName) {
        this.fieldName = FieldName;
    }

    public String getSno() {
        return sNo;
    }

    public void setSno(String Sno) {
        this.sNo = Sno;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String FieldValue) {
        this.fieldValue = FieldValue;
    }

}
