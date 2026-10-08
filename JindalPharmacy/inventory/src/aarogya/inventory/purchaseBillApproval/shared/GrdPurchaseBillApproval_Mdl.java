package aarogya.inventory.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdPurchaseBillApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String wtaCode;
    private String billNo;
    private String billDate;
    private String docQty;
    private String accQty;
    private String unitTag;
    private String unitText;
    private String itemNameTag;
    private String itemNameText;
    private String grnNoTag;
    private String grnNoText;
    private String orderNoTag;
    private String orderNoText;
    private String vtypeTag;
    private String vtypeText;
    private String vCat;
    private String supplierTag;
    private String supplierText;
    private String SiteCode;
    private String SiteName;
    private String ApprovedBy;
    private String Approval;
    private String isStatus;
    private String isChecked;
    private String currentUser;
    private String vSNo;
    private String isView;
    private String propertiesCsv;
    private String partyBillDate;
    private String partyBillNo;
    private List<GrdItemDetail_Mdl> grdItemDetails = new ArrayList<GrdItemDetail_Mdl>();

    public GrdPurchaseBillApproval_Mdl() {
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
    }

    public String getApproval() {
        return Approval;
    }

    public void setApproval(String Approval) {
        this.Approval = Approval;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getSiteCode() {
        return SiteCode;
    }

    public void setSiteCode(String SiteCode) {
        this.SiteCode = SiteCode;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String SiteName) {
        this.SiteName = SiteName;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetails() {
        return grdItemDetails;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDocQty() {
        return docQty;
    }

    public void setDocQty(String docQty) {
        this.docQty = docQty;
    }

    public String getAccQty() {
        return accQty;
    }

    public void setAccQty(String accQty) {
        this.accQty = accQty;
    }

    public String getUnitTag() {
        return unitTag;
    }

    public void setUnitTag(String unitTag) {
        this.unitTag = unitTag;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public String getItemNameTag() {
        return itemNameTag;
    }

    public void setItemNameTag(String itemNameTag) {
        this.itemNameTag = itemNameTag;
    }

    public String getItemNameText() {
        return itemNameText;
    }

    public void setItemNameText(String itemNameText) {
        this.itemNameText = itemNameText;
    }

    public String getGrnNoTag() {
        return grnNoTag;
    }

    public void setGrnNoTag(String grnNoTag) {
        this.grnNoTag = grnNoTag;
    }

    public String getGrnNoText() {
        return grnNoText;
    }

    public void setGrnNoText(String grnNoText) {
        this.grnNoText = grnNoText;
    }

    public String getOrderNoTag() {
        return orderNoTag;
    }

    public void setOrderNoTag(String orderNoTag) {
        this.orderNoTag = orderNoTag;
    }

    public String getOrderNoText() {
        return orderNoText;
    }

    public void setOrderNoText(String orderNoText) {
        this.orderNoText = orderNoText;
    }

    public String getVtypeTag() {
        return vtypeTag;
    }

    public void setVtypeTag(String vtypeTag) {
        this.vtypeTag = vtypeTag;
    }

    public String getVtypeText() {
        return vtypeText;
    }

    public void setVtypeText(String vtypeText) {
        this.vtypeText = vtypeText;
    }

    public String getvCat() {
        return vCat;
    }

    public void setvCat(String vCat) {
        this.vCat = vCat;
    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getPropertiesCsv() {
        return propertiesCsv;
    }

    public void setPropertiesCsv(String propertiesCsv) {
        this.propertiesCsv = propertiesCsv;
    }

    public String getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(String partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

}
