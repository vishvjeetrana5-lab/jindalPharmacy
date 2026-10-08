package dataman.webJobs.smsTemplate.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdFieldName_Mdl extends KeyField implements Serializable,IsSerializable{

    private String fieldName;

    public GrdFieldName_Mdl() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFormattedFieldName() {
        return "{" + fieldName + "}";
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
