package dataman.dmToolkit.modifyReason.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModifyReason_Mdl extends KeyField implements Serializable, IsSerializable {

    private String mode;
    private String remark;
    private List<GrdCancelReason_Mdl> lstCancelReason = new ArrayList<GrdCancelReason_Mdl>();

    public ModifyReason_Mdl() {
    }

    public ModifyReason_Mdl(ModifyReason_Mdl mrMdl) {
        this(mrMdl.getMode(), mrMdl.getRemark(), mrMdl.getLstCancelReason(), mrMdl.getSharedKeyFieldCode(), mrMdl.getSharedKeyFieldName(), mrMdl.getSharedPreparedBy(), mrMdl.getSharedModifiedBy());
    }

    private ModifyReason_Mdl(String mode, String remark, List<GrdCancelReason_Mdl> lstCancelReason, String KeyFieldCode, String KeyFieldName, String sharedPreparedBy, String sharedModifiedBy) {
        super(KeyFieldCode, KeyFieldName, sharedPreparedBy, sharedModifiedBy);
        this.mode = mode;
        this.remark = remark;

        this.lstCancelReason = new ArrayList<>();
        for (GrdCancelReason_Mdl gcrMdl : lstCancelReason) {
            this.lstCancelReason.add(new GrdCancelReason_Mdl(gcrMdl));
        }
    }

    public List<GrdCancelReason_Mdl> getLstCancelReason() {
        return lstCancelReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
