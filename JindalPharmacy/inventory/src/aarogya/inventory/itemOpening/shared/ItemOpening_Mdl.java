package aarogya.inventory.itemOpening.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemOpening_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdItemOpening = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String stockVoucherDate;
    private String receivedByTag;
    private String receivedByText;
    private String departmentTag;
    private String departmentText;
    private boolean inventoryAccountingActive;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();

    public ItemOpening_Mdl() {
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecIdItemOpening() {
        return recIdItemOpening;
    }

    public void setRecIdItemOpening(RecId recIdItemOpening) {
        this.recIdItemOpening = recIdItemOpening;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getReceivedByTag() {
        return receivedByTag;
    }

    public void setReceivedByTag(String receivedByTag) {
        this.receivedByTag = receivedByTag;
    }

    public String getReceivedByText() {
        return receivedByText;
    }

    public void setReceivedByText(String receivedByText) {
        this.receivedByText = receivedByText;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public String getStockVoucherDate() {
        return stockVoucherDate;
    }

    public void setStockVoucherDate(String stockVoucherDate) {
        this.stockVoucherDate = stockVoucherDate;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }
    
    public ItemOpening_Mdl clone() {
        ItemOpening_Mdl itemOpening_Mdl = new ItemOpening_Mdl();

        copy(itemOpening_Mdl);

        return itemOpening_Mdl;
    }

    public void copy(ItemOpening_Mdl itemOpening_Mdl) {
        itemOpening_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        itemOpening_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        itemOpening_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        itemOpening_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        itemOpening_Mdl.setRecIdItemOpening(this.getRecIdItemOpening());
        itemOpening_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        itemOpening_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        itemOpening_Mdl.setVoucherDate(this.getVoucherDate());
        itemOpening_Mdl.setStockVoucherDate(this.getStockVoucherDate());
        itemOpening_Mdl.setDepartmentTag(this.getDepartmentTag());
        itemOpening_Mdl.setDepartmentText(this.getDepartmentText());
        itemOpening_Mdl.setReceivedByTag(this.getReceivedByTag());
        itemOpening_Mdl.setReceivedByText(this.getReceivedByText());
        itemOpening_Mdl.setInventoryAccountingActive(this.isInventoryAccountingActive());

        itemOpening_Mdl.getListItemDetail().addAll(cloneGrdDetailList(this.getListItemDetail()));
    }

    public List<GrdItemDetail_Mdl> cloneGrdDetailList(List<GrdItemDetail_Mdl> list) {
        List<GrdItemDetail_Mdl> grdItemDetail_Mdl = null;

        if (list != null) {
            grdItemDetail_Mdl = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdItemDetail_Mdl.add(list.get(i).clone());
            }
        }

        return grdItemDetail_Mdl;
    }
}
