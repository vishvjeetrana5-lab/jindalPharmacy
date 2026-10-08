package dataman.utility.walkThroughMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WalkThroughMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdAuthority_Mdl> grdAuthority_Mdl = new ArrayList<GrdAuthority_Mdl>();

    public WalkThroughMaster_Mdl() {
    }

    public List<GrdAuthority_Mdl> getGrdAuthority_Mdl() {
        return grdAuthority_Mdl;
    }

    public void setGrdAuthority_Mdl(List<GrdAuthority_Mdl> grdAuthority_Mdl) {
        this.grdAuthority_Mdl = grdAuthority_Mdl;
    }

    public List<GrdDefaultDetail_Mdl> getGrdDefaultMdl() {
        return grdDefaultMdl;
    }

    public void setGrdDefaultMdl(List<GrdDefaultDetail_Mdl> grdDefaultMdl) {
        this.grdDefaultMdl = grdDefaultMdl;
    }
    private List<GrdDefaultDetail_Mdl> grdDefaultMdl = new ArrayList<GrdDefaultDetail_Mdl>();

    private String preparedBy;
    private String modifiedBy;

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
