package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdAcPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String contraSubCode;
    private String contraSubName;
    private String narration;
    private String serviceName;
    private String amount;
    private String siteCode;
    private String siteName;
    private boolean receipt;
    private boolean ctr;
    private String chqNo;
    private String chqDate;
    private String favourOff;

    public GrdAcPosting_Mdl() {
    }

    public String getContraSubCode() {
        return contraSubCode;
    }

    public void setContraSubCode(String contraSubCode) {
        this.contraSubCode = contraSubCode;
    }

    public String getContraSubName() {
        return contraSubName;
    }

    public void setContraSubName(String contraSubName) {
        this.contraSubName = contraSubName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isReceipt() {
        return receipt;
    }

    public void setReceipt(boolean receipt) {
        this.receipt = receipt;
    }

    public boolean isCtr() {
        return ctr;
    }

    public void setCtr(boolean ctr) {
        this.ctr = ctr;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getFavourOff() {
        return favourOff;
    }

    public void setFavourOff(String favourOff) {
        this.favourOff = favourOff;
    }

}
