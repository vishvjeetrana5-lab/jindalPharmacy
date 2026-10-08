package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHLabNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String labType;
    private String labDate;
    private String labTime;
    private String itemName;
    private String testTemplate;

    public TxtHLabNo_Mdl() {
    }

    public String getLabType() {
        return labType;
    }

    public void setLabType(String labType) {
        this.labType = labType;
    }

    public String getLabDate() {
        return labDate;
    }

    public void setLabDate(String labDate) {
        this.labDate = labDate;
    }

    public String getLabTime() {
        return labTime;
    }

    public void setLabTime(String labTime) {
        this.labTime = labTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(String testTemplate) {
        this.testTemplate = testTemplate;
    }

}
