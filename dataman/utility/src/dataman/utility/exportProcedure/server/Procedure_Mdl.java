package dataman.utility.exportProcedure.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Procedure_Mdl implements Serializable, IsSerializable {

    private String procedureName;
    private String dropProcedure;
    private String createProcedure;

    public Procedure_Mdl() {
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
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

}
