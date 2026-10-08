package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GUIRow_Mdl extends KeyField implements Serializable, IsSerializable {

    private String srNo, description;
    private List<GUIColumn_Mdl> guiCol = new ArrayList<GUIColumn_Mdl>();

    public GUIRow_Mdl() {
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GUIColumn_Mdl> getGuiCol() {
        return guiCol;
    }

    public GUIRow_Mdl clone() {
        GUIRow_Mdl local = new GUIRow_Mdl();

        super.copy(local);
        local.getGuiCol().addAll(cloneGUIColumn(this.getGuiCol()));

        return local;
    }

    public List<GUIColumn_Mdl> cloneGUIColumn(List<GUIColumn_Mdl> list) {
        List<GUIColumn_Mdl> colMdl = null;
        if (list != null) {
            colMdl = new ArrayList<GUIColumn_Mdl>();
            for (GUIColumn_Mdl column : list) {
                colMdl.add(column.clone());
            }
        }
        return colMdl;
    }
}
