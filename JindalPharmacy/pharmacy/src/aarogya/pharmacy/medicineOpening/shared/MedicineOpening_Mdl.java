package aarogya.pharmacy.medicineOpening.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineOpening_Mdl extends KeyField implements Serializable, IsSerializable {

    //getRecIdItemOpening
    private RecId recIdItemOpening = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String stockVoucherDate;
    private String receivedByTag;
    private String receivedByText;
    private String departmentTag;
    private String departmentText;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdMedicineDetail_Mdl> listItemDetail = new ArrayList<GrdMedicineDetail_Mdl>();

    public MedicineOpening_Mdl() {
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

    public List<GrdMedicineDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public String getStockVoucherDate() {
        return stockVoucherDate;
    }

    public void setStockVoucherDate(String stockVoucherDate) {
        this.stockVoucherDate = stockVoucherDate;
    }

    public MedicineOpening_Mdl clone() {
        MedicineOpening_Mdl medicineOpening_Mdl = new MedicineOpening_Mdl();

        copy(medicineOpening_Mdl);

        return medicineOpening_Mdl;
    }

    public void copy(MedicineOpening_Mdl medicineOpening_Mdl) {
        medicineOpening_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        medicineOpening_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        medicineOpening_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        medicineOpening_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        medicineOpening_Mdl.setRecIdItemOpening(this.getRecIdItemOpening());
        medicineOpening_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        medicineOpening_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        medicineOpening_Mdl.setVoucherDate(this.getVoucherDate());
        medicineOpening_Mdl.setStockVoucherDate(this.getStockVoucherDate());
        medicineOpening_Mdl.setDepartmentTag(this.getDepartmentTag());
        medicineOpening_Mdl.setDepartmentText(this.getDepartmentText());
        medicineOpening_Mdl.setReceivedByTag(this.getReceivedByTag());
        medicineOpening_Mdl.setReceivedByText(this.getReceivedByText());

        medicineOpening_Mdl.getListItemDetail().addAll(cloneGrdDetailList(this.getListItemDetail()));
    }

    public List<GrdMedicineDetail_Mdl> cloneGrdDetailList(List<GrdMedicineDetail_Mdl> list) {
        List<GrdMedicineDetail_Mdl> grdMedicineDetail_Mdl = null;

        if (list != null) {
            grdMedicineDetail_Mdl = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdMedicineDetail_Mdl.add(list.get(i).clone());
            }
        }

        return grdMedicineDetail_Mdl;
    }

}
