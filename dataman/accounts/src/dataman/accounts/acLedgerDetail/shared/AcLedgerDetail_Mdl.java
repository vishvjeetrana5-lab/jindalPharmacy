package dataman.accounts.acLedgerDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AcLedgerDetail_Mdl extends KeyField implements Serializable, IsSerializable {
    private String particulars;
    private String dtVoucher;
    private String voucherType;
    private String drCr;
    private String amtDr;
    private String amtCr;
    private String amtBalance;
    private String moduleName;
    private String menuText;
    private String v_Prefix;
    private String site_Code;
    
    public AcLedgerDetail_Mdl() {
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getDrCr() {
        return drCr;
    }

    public void setDrCr(String drCr) {
        this.drCr = drCr;
    }

    public String getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(String amtDr) {
        this.amtDr = amtDr;
    }

    public String getAmtCr() {
        return amtCr;
    }

    public void setAmtCr(String amtCr) {
        this.amtCr = amtCr;
    }

    public String getAmtBalance() {
        return amtBalance;
    }

    public void setAmtBalance(String amtBalance) {
        this.amtBalance = amtBalance;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getV_Prefix() {
        return v_Prefix;
    }

    public void setV_Prefix(String v_Prefix) {
        this.v_Prefix = v_Prefix;
    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }
    
    
}
