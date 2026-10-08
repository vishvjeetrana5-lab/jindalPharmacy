package aarogya.pharmacy.d365StockRecive.shared;

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
    private String balancingAc;
    private String balancingAcName;

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

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
    }

    public String getBalancingAcName() {
        return balancingAcName;
    }

    public void setBalancingAcName(String balancingAcName) {
        this.balancingAcName = balancingAcName;
    }

}
