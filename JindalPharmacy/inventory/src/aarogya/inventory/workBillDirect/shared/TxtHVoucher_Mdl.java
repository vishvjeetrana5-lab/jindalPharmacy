package aarogya.inventory.workBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHVoucher_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
    SharedKeyFieldCode = v_Type as code
    SharedKeyFieldName = description as name
     */
    private String voucherCategory;
    private String guiTemplateCode;

    public TxtHVoucher_Mdl() {
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

}
