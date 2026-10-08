package aarogya.pharmacy.medicineModification.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineModification_Mdl extends KeyField implements Serializable, IsSerializable {

    private String displayName, itemPacking;
    private String categoryTag, categoryText;
    private String groupTag, groupText;
    private String companyTag, companyText, companyShortName;
    private List<GrdComposition_Mdl> lstComposition = new ArrayList<GrdComposition_Mdl>();
    private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();

    public MedicineModification_Mdl() {
    }

    public List<GrdComposition_Mdl> getLstComposition() {
        return lstComposition;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getGroupTag() {
        return groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getCompanyTag() {
        return companyTag;
    }

    public void setCompanyTag(String companyTag) {
        this.companyTag = companyTag;
    }

    public String getCompanyText() {
        return companyText;
    }

    public void setCompanyText(String companyText) {
        this.companyText = companyText;
    }

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getItemPacking() {
        return itemPacking;
    }

    public void setItemPacking(String itemPacking) {
        this.itemPacking = itemPacking;
    }
}
