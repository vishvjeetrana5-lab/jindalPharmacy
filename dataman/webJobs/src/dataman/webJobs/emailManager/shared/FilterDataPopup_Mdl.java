package dataman.webJobs.emailManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class FilterDataPopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String columnName; 
    private String dataType;
    private String helpTag;
    private String helpText;
    private String valueFrom;
    private String valueTo;
    private String contains;
    private String isChecked;

    public FilterDataPopup_Mdl() {
    }

    public String getHelpTag() {
        return helpTag;
    }

    public void setHelpTag(String helpTag) {
        this.helpTag = helpTag;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(String valueFrom) {
        this.valueFrom = valueFrom;
    }

    public String getValueTo() {
        return valueTo;
    }

    public void setValueTo(String valueTo) {
        this.valueTo = valueTo;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

}
