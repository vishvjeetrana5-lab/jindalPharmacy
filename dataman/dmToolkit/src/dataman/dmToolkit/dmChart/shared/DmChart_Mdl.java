package dataman.dmToolkit.dmChart.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmChart_Mdl implements Serializable, IsSerializable {

    private String groupCode;
    private String groupName;
    private String labelCode;
    private String labelName;
    private double dataValue;

    public DmChart_Mdl() {
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }

    public DmChart_Mdl clone() {
        DmChart_Mdl local = new DmChart_Mdl();

        local.setGroupCode(this.getGroupCode());
        local.setGroupName(this.getGroupName());
        local.setLabelCode(this.getLabelCode());
        local.setLabelName(this.getLabelName());
        local.setDataValue(this.getDataValue());

        return local;
    }

}
