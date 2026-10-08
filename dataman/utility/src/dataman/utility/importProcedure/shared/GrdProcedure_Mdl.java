package dataman.utility.importProcedure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdProcedure_Mdl extends KeyField implements Serializable,IsSerializable{

    private String procedureName;
    private String disProcedureName;
    private String dropProcedure;
    private String createProcedure;

    private String isChecked;

    public GrdProcedure_Mdl() {
    }

    public String getIsChecked() {
        return isChecked;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getDropProcedure() {
        return dropProcedure;
    }

    public void setDropProcedure(String dropProcedure) {
        this.dropProcedure = dropProcedure;
    }

    public String getCreateProcedure() {
        return createProcedure;
    }

    public void setCreateProcedure(String createProcedure) {
        this.createProcedure = createProcedure;
    }

    public String getDisProcedureName() {
        return disProcedureName;
    }

    public void setDisProcedureName(String disProcedureName) {
        this.disProcedureName = disProcedureName;
    }

}
