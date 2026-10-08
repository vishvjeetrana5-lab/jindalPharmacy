package aarogya.master.hsnMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HSNMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String HSN;
    private String vSNo;
    private String natureTag;
    private String natureText;
    private String taxBasedOnTag;
    private String taxBasedOnText;
    private String gstNatureTag;
    private String gstNatureText;

    private String itsOnPurchaseTag;
    private String itsOnPurchaseText;
    private String itsOnSaleTag;
    private String itsOnSaleText;

    private String itsOnPurchaseReturnTag;
    private String itsOnPurchaseReturnText;
    private String itsOnSaleReturnTag;
    private String itsOnSaleReturnText;

    private String acPurchaseTag;
    private String acPurchaseText;
    private String acSaleTag;
    private String acSaleText;

    private String acPurchaseReturnTag;
    private String acPurchaseReturnText;
    private String acSaleReturnTag;
    private String acSaleReturnText;

    private List<GrdRateITS_Mdl> grdRateLst = new ArrayList<GrdRateITS_Mdl>();

    public List<GrdRateITS_Mdl> getGrdRateLst() {
        return grdRateLst;
    }

    public HSNMaster_Mdl() {
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }

    public String getNatureTag() {
        return natureTag;
    }

    public void setNatureTag(String natureTag) {
        this.natureTag = natureTag;
    }

    public String getNatureText() {
        return natureText;
    }

    public void setNatureText(String natureText) {
        this.natureText = natureText;
    }

    public String getTaxBasedOnTag() {
        return taxBasedOnTag;
    }

    public void setTaxBasedOnTag(String taxBasedOnTag) {
        this.taxBasedOnTag = taxBasedOnTag;
    }

    public String getTaxBasedOnText() {
        return taxBasedOnText;
    }

    public void setTaxBasedOnText(String taxBasedOnText) {
        this.taxBasedOnText = taxBasedOnText;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getItsOnPurchaseTag() {
        return itsOnPurchaseTag;
    }

    public void setItsOnPurchaseTag(String itsOnPurchaseTag) {
        this.itsOnPurchaseTag = itsOnPurchaseTag;
    }

    public String getItsOnPurchaseText() {
        return itsOnPurchaseText;
    }

    public void setItsOnPurchaseText(String itsOnPurchaseText) {
        this.itsOnPurchaseText = itsOnPurchaseText;
    }

    public String getItsOnSaleTag() {
        return itsOnSaleTag;
    }

    public void setItsOnSaleTag(String itsOnSaleTag) {
        this.itsOnSaleTag = itsOnSaleTag;
    }

    public String getItsOnSaleText() {
        return itsOnSaleText;
    }

    public void setItsOnSaleText(String itsOnSaleText) {
        this.itsOnSaleText = itsOnSaleText;
    }

    public String getItsOnPurchaseReturnTag() {
        return itsOnPurchaseReturnTag;
    }

    public void setItsOnPurchaseReturnTag(String itsOnPurchaseReturnTag) {
        this.itsOnPurchaseReturnTag = itsOnPurchaseReturnTag;
    }

    public String getItsOnPurchaseReturnText() {
        return itsOnPurchaseReturnText;
    }

    public void setItsOnPurchaseReturnText(String itsOnPurchaseReturnText) {
        this.itsOnPurchaseReturnText = itsOnPurchaseReturnText;
    }

    public String getItsOnSaleReturnTag() {
        return itsOnSaleReturnTag;
    }

    public void setItsOnSaleReturnTag(String itsOnSaleReturnTag) {
        this.itsOnSaleReturnTag = itsOnSaleReturnTag;
    }

    public String getItsOnSaleReturnText() {
        return itsOnSaleReturnText;
    }

    public void setItsOnSaleReturnText(String itsOnSaleReturnText) {
        this.itsOnSaleReturnText = itsOnSaleReturnText;
    }

    public String getAcPurchaseTag() {
        return acPurchaseTag;
    }

    public void setAcPurchaseTag(String acPurchaseTag) {
        this.acPurchaseTag = acPurchaseTag;
    }

    public String getAcPurchaseText() {
        return acPurchaseText;
    }

    public void setAcPurchaseText(String acPurchaseText) {
        this.acPurchaseText = acPurchaseText;
    }

    public String getAcSaleTag() {
        return acSaleTag;
    }

    public void setAcSaleTag(String acSaleTag) {
        this.acSaleTag = acSaleTag;
    }

    public String getAcSaleText() {
        return acSaleText;
    }

    public void setAcSaleText(String acSaleText) {
        this.acSaleText = acSaleText;
    }

    public String getAcPurchaseReturnTag() {
        return acPurchaseReturnTag;
    }

    public void setAcPurchaseReturnTag(String acPurchaseReturnTag) {
        this.acPurchaseReturnTag = acPurchaseReturnTag;
    }

    public String getAcPurchaseReturnText() {
        return acPurchaseReturnText;
    }

    public void setAcPurchaseReturnText(String acPurchaseReturnText) {
        this.acPurchaseReturnText = acPurchaseReturnText;
    }

    public String getAcSaleReturnTag() {
        return acSaleReturnTag;
    }

    public void setAcSaleReturnTag(String acSaleReturnTag) {
        this.acSaleReturnTag = acSaleReturnTag;
    }

    public String getAcSaleReturnText() {
        return acSaleReturnText;
    }

    public void setAcSaleReturnText(String acSaleReturnText) {
        this.acSaleReturnText = acSaleReturnText;
    }

    public String getGstNatureTag() {
        return gstNatureTag;
    }

    public void setGstNatureTag(String gstNatureTag) {
        this.gstNatureTag = gstNatureTag;
    }

    public String getGstNatureText() {
        return gstNatureText;
    }

    public void setGstNatureText(String gstNatureText) {
        this.gstNatureText = gstNatureText;
    }

}
