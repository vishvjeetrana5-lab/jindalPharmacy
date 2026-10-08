package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.DmMonths;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProvisionalStatement_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdProvisionalStatement_Mdl> lstService = new ArrayList<>();
    private DmMonths month;
    private String site, siteCode, currentYear;
    private RecId recId = new RecId();
    private String voucherTypeCode;

    public ProvisionalStatement_Mdl() {
    }

    public List<GrdProvisionalStatement_Mdl> getLstService() {
        return lstService;
    }

    public DmMonths getMonth() {
        return month;
    }

    public void setMonth(DmMonths month) {
        this.month = month;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }
}
