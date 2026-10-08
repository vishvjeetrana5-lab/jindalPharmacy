package dataman.utility.importProcedure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.utility.exportProcedure.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable,IsSerializable{

    private String procedureName;
    private String fieldName;
    private String fieldNameValue;

    public SearchValue_Mdl() {
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
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
