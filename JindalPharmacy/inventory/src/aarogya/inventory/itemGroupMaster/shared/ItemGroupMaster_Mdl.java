package aarogya.inventory.itemGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemGroupMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String groupUnder;
    private String groupUnderName;
    private String underCateCode;
    private String underCateName;
    private String inventoryAcTag;
    private String inventoryAcText;

    private String purchaseAcTag;
    private String purchaseAcText;
    private String salesAcTag;
    private String salesAcText;
    private String purchaseReturnAcTag;
    private String purchaseReturnAcText;
    private String salesReturnAcTag;
    private String salesReturnAcText;

    private String grIrAcTag;
    private String grIrAcText;
    private String consumptionAcTag;
    private String consumptionAcText;
    private String cogsAcTag;
    private String cogsAcText;
    private String pidAcTag;
    private String pidAcText;
    private String inTransitpidAcTag;
    private String inTransitpidAcText;

    private List<GrdRestrictedDept_Mdl> grdRestrictedDept_Mdls = new ArrayList<GrdRestrictedDept_Mdl>();
    private List<GrdDepartmentConsumptionAc_Mdl> grdDepartmentConsumptionAc = new ArrayList<GrdDepartmentConsumptionAc_Mdl>();

    public List<GrdRestrictedDept_Mdl> getGrdRestrictedDept_Mdls() {
        return grdRestrictedDept_Mdls;
    }

    public void setGrdRestrictedDept_Mdls(List<GrdRestrictedDept_Mdl> grdRestrictedDept_Mdls) {
        this.grdRestrictedDept_Mdls = grdRestrictedDept_Mdls;
    }

    public List<GrdDepartmentConsumptionAc_Mdl> getGrdDepartmentConsumptionAc() {
        return grdDepartmentConsumptionAc;
    }

    public void setGrdDepartmentConsumptionAc(List<GrdDepartmentConsumptionAc_Mdl> grdDepartmentConsumptionAc) {
        this.grdDepartmentConsumptionAc = grdDepartmentConsumptionAc;
    }

    public String getGrIrAcTag() {
        return grIrAcTag;
    }

    public void setGrIrAcTag(String grIrAcTag) {
        this.grIrAcTag = grIrAcTag;
    }

    public String getGrIrAcText() {
        return grIrAcText;
    }

    public void setGrIrAcText(String grIrAcText) {
        this.grIrAcText = grIrAcText;
    }

    public String getConsumptionAcTag() {
        return consumptionAcTag;
    }

    public void setConsumptionAcTag(String consumptionAcTag) {
        this.consumptionAcTag = consumptionAcTag;
    }

    public String getConsumptionAcText() {
        return consumptionAcText;
    }

    public void setConsumptionAcText(String consumptionAcText) {
        this.consumptionAcText = consumptionAcText;
    }

    public String getCogsAcTag() {
        return cogsAcTag;
    }

    public void setCogsAcTag(String cogsAcTag) {
        this.cogsAcTag = cogsAcTag;
    }

    public String getCogsAcText() {
        return cogsAcText;
    }

    public void setCogsAcText(String cogsAcText) {
        this.cogsAcText = cogsAcText;
    }

    public String getPidAcTag() {
        return pidAcTag;
    }

    public void setPidAcTag(String pidAcTag) {
        this.pidAcTag = pidAcTag;
    }

    public String getInTransitpidAcText() {
        return inTransitpidAcText;
    }

    public void setInTransitpidAcText(String inTransitpidAcText) {
        this.inTransitpidAcText = inTransitpidAcText;
    }

    public String getInTransitAcTag() {
        return inTransitAcTag;
    }

    public void setInTransitAcTag(String inTransitAcTag) {
        this.inTransitAcTag = inTransitAcTag;
    }

    public String getInTransitAcText() {
        return inTransitAcText;
    }

    public void setInTransitAcText(String inTransitAcText) {
        this.inTransitAcText = inTransitAcText;
    }
    private String inTransitAcTag;
    private String inTransitAcText;

    public ItemGroupMaster_Mdl() {
    }

    public String getGroupUnder() {
        return groupUnder;
    }

    public void setGroupUnder(String groupUnder) {
        this.groupUnder = groupUnder;
    }

    public String getGroupUnderName() {
        return groupUnderName;
    }

    public void setGroupUnderName(String groupUnderName) {
        this.groupUnderName = groupUnderName;
    }

    public String getUnderCateCode() {
        return underCateCode;
    }

    public void setUnderCateCode(String underCateCode) {
        this.underCateCode = underCateCode;
    }

    public String getUnderCateName() {
        return underCateName;
    }

    public void setUnderCateName(String underCateName) {
        this.underCateName = underCateName;
    }

    public String getInventoryAcTag() {
        return inventoryAcTag;
    }

    public void setInventoryAcTag(String inventoryAcTag) {
        this.inventoryAcTag = inventoryAcTag;
    }

    public String getInventoryAcText() {
        return inventoryAcText;
    }

    public void setInventoryAcText(String inventoryAcText) {
        this.inventoryAcText = inventoryAcText;
    }

    public String getPurchaseAcTag() {
        return purchaseAcTag;
    }

    public void setPurchaseAcTag(String purchaseAcTag) {
        this.purchaseAcTag = purchaseAcTag;
    }

    public String getPurchaseAcText() {
        return purchaseAcText;
    }

    public void setPurchaseAcText(String purchaseAcText) {
        this.purchaseAcText = purchaseAcText;
    }

    public String getSalesAcTag() {
        return salesAcTag;
    }

    public void setSalesAcTag(String salesAcTag) {
        this.salesAcTag = salesAcTag;
    }

    public String getSalesAcText() {
        return salesAcText;
    }

    public void setSalesAcText(String salesAcText) {
        this.salesAcText = salesAcText;
    }

    public String getPurchaseReturnAcTag() {
        return purchaseReturnAcTag;
    }

    public void setPurchaseReturnAcTag(String purchaseReturnAcTag) {
        this.purchaseReturnAcTag = purchaseReturnAcTag;
    }

    public String getPurchaseReturnAcText() {
        return purchaseReturnAcText;
    }

    public void setPurchaseReturnAcText(String purchaseReturnAcText) {
        this.purchaseReturnAcText = purchaseReturnAcText;
    }

    public String getSalesReturnAcTag() {
        return salesReturnAcTag;
    }

    public void setSalesReturnAcTag(String salesReturnAcTag) {
        this.salesReturnAcTag = salesReturnAcTag;
    }

    public String getSalesReturnAcText() {
        return salesReturnAcText;
    }

    public void setSalesReturnAcText(String salesReturnAcText) {
        this.salesReturnAcText = salesReturnAcText;
    }

    public String getPidAcText() {
        return pidAcText;
    }

    public void setPidAcText(String pidAcText) {
        this.pidAcText = pidAcText;
    }

    public String getInTransitpidAcTag() {
        return inTransitpidAcTag;
    }

    public void setInTransitpidAcTag(String inTransitpidAcTag) {
        this.inTransitpidAcTag = inTransitpidAcTag;
    }

}
