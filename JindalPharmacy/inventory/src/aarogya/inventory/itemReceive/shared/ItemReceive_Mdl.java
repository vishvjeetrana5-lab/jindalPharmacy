package aarogya.inventory.itemReceive.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemReceive_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdItemReceive = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String balancingAc;
    private String voucherDate;
    private String refDate;
    private String refNo;
    private String remarkHeader;
    private String receivedByTag;
    private String receivedByText;
    private String departmentTag;
    private String departmentText;
    private String lineItemCodesCsv;
    private boolean inventoryAccountingActive;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();

    public ItemReceive_Mdl() {
    }

    public ItemReceive_Mdl(ItemReceive_Mdl irMdl) {
        this(irMdl.getVoucherTypeCode(), irMdl.getVoucherTypeName(), irMdl.getBalancingAc(), irMdl.getVoucherDate(), irMdl.getRefDate(), irMdl.getRefNo(), irMdl.getRemarkHeader(), irMdl.getReceivedByTag(), irMdl.getReceivedByText(), irMdl.getDepartmentTag(), irMdl.getDepartmentText(), irMdl.getLineItemCodesCsv(), irMdl.getModifyReasonMdl(), irMdl.getListItemDetail(), irMdl.getSharedKeyFieldCode(), irMdl.getSharedKeyFieldName(), irMdl.isInventoryAccountingActive());
    }

    private ItemReceive_Mdl(String voucherTypeCode, String voucherTypeName, String balancingAc, String voucherDate, String refDate, String refNo, String remarkHeader, String receivedByTag, String receivedByText, String departmentTag, String departmentText, String lineItemCodesCsv, ModifyReason_Mdl modifyReasonMdl, List<GrdItemDetail_Mdl> listItemDetail, String KeyFieldCode, String KeyFieldName, boolean inventoryAccountingActive) {
        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.balancingAc = balancingAc;
        this.voucherDate = voucherDate;
        this.refDate = refDate;
        this.refNo = refNo;
        this.remarkHeader = remarkHeader;
        this.receivedByTag = receivedByTag;
        this.receivedByText = receivedByText;
        this.departmentTag = departmentTag;
        this.departmentText = departmentText;
        this.lineItemCodesCsv = lineItemCodesCsv;
        this.inventoryAccountingActive = inventoryAccountingActive;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;

        this.listItemDetail = new ArrayList<>();
        for (GrdItemDetail_Mdl gidMdl : listItemDetail) {
            this.listItemDetail.add(new GrdItemDetail_Mdl(gidMdl));
        }

    }

    public String getRemarkHeader() {
        return remarkHeader;
    }

    public void setRemarkHeader(String remarkHeader) {
        this.remarkHeader = remarkHeader;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public RecId getRecIdItemReceive() {
        return recIdItemReceive;
    }

    public void setRecIdItemReceive(RecId recIdItemReceive) {
        this.recIdItemReceive = recIdItemReceive;
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

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
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

    public void setListItemDetail(List<GrdItemDetail_Mdl> listItemDetail) {
        this.listItemDetail = listItemDetail;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }
}
