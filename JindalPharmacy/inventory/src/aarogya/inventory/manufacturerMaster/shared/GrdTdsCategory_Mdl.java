package aarogya.inventory.manufacturerMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdTdsCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FieldValue;
    private String FieldName;
    private String Sno;

    public GrdTdsCategory_Mdl() {
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String Sno) {
        this.Sno = Sno;
    }

    public String getFieldValue() {
        return FieldValue;
    }

    public void setFieldValue(String FieldValue) {
        this.FieldValue = FieldValue;
    }

}
