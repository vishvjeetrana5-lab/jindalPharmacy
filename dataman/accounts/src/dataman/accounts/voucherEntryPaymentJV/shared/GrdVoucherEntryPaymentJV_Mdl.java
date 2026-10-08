package dataman.accounts.voucherEntryPaymentJV.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdVoucherEntryPaymentJV_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vSNo;
    private String accMCode;
    private String accCode;
    private String accName;
    private String accNature;
    private String narration;
    private String debit;
    private String credit;
    private String tds;
    private String totalAmount;
    private String favourOf;
    private String chqNo;

    private TDS_Mdl tds_Mdl = new TDS_Mdl();
    private ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
    private List<CostCenterGrid_Mdl> costCenterGrdData = new ArrayList<CostCenterGrid_Mdl>();
    private List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
    private List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();

    public GrdVoucherEntryPaymentJV_Mdl() {
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getVSNo() {
        return vSNo;
    }

    public void setVSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public String getAccMCode() {
        return accMCode;
    }

    public void setAccMCode(String accMCode) {
        this.accMCode = accMCode;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public List<CostCenterGrid_Mdl> getCostCenterGrdData() {
        return costCenterGrdData;
    }

    public void setCostCenterGrdData(List<CostCenterGrid_Mdl> strCostCenterGrdData) {
        this.costCenterGrdData = strCostCenterGrdData;
    }

    public List<GrdAdjustment_Mdl> getLstAdjustments() {
        return lstAdjustments;
    }

    public void setLstAdjustments(List<GrdAdjustment_Mdl> lstAdjustments) {
        this.lstAdjustments = lstAdjustments;
    }

    public List<GrdItemAdjustment_Mdl> getLstItemAdjustments() {
        return lstItemAdjustments;
    }

    public void setLstItemAdjustments(List<GrdItemAdjustment_Mdl> lstItemAdjustments) {
        this.lstItemAdjustments = lstItemAdjustments;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public TDS_Mdl getTds_Mdl() {
        return tds_Mdl;
    }

    public void setTds_Mdl(TDS_Mdl tds_Mdl) {
        this.tds_Mdl = tds_Mdl;
    }

    public ChequeDetail_Mdl getChequeDetail_Mdl() {
        return chequeDetail_Mdl;
    }

    public void setChequeDetail_Mdl(ChequeDetail_Mdl chequeDetail_Mdl) {
        this.chequeDetail_Mdl = chequeDetail_Mdl;
    }

    public GrdVoucherEntryPaymentJV_Mdl clone() {
        GrdVoucherEntryPaymentJV_Mdl grdVoucherEntry = new GrdVoucherEntryPaymentJV_Mdl();
        TDS_Mdl tDS_Mdl = new TDS_Mdl();
        ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
        List<CostCenterGrid_Mdl> listCostCenterGrid = new ArrayList();
        List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
        List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();

        grdVoucherEntry.setVSNo(this.getVSNo());
        grdVoucherEntry.setAccNature(this.getAccNature());
        grdVoucherEntry.setAccName(this.getAccName());
        grdVoucherEntry.setAccCode(this.getAccCode());
        grdVoucherEntry.setAccMCode(this.getAccMCode());
        grdVoucherEntry.setNarration(this.getNarration());
        grdVoucherEntry.setDebit(this.getDebit());
        grdVoucherEntry.setCredit(this.getCredit());
        grdVoucherEntry.setTds(this.getTds());
        grdVoucherEntry.setTotalAmount(this.getTotalAmount());
        grdVoucherEntry.setFavourOf(this.getFavourOf());
        grdVoucherEntry.setChqNo(this.getChqNo());

        if (tDS_Mdl != null) {
            tDS_Mdl = getTds_Mdl().clone();
            grdVoucherEntry.setTds_Mdl(tDS_Mdl);
        }

        if (chequeDetail_Mdl != null) {
            chequeDetail_Mdl = getChequeDetail_Mdl().clone();
            grdVoucherEntry.setChequeDetail_Mdl(chequeDetail_Mdl);
        }

        if (getCostCenterGrdData() != null) {
            for (int i = 0; i < getCostCenterGrdData().size(); i++) {
                listCostCenterGrid.add(getCostCenterGrdData().get(i).clone());
            }
            grdVoucherEntry.setCostCenterGrdData(listCostCenterGrid);
        }

        if (getLstAdjustments() != null) {
            for (int i = 0; i < getLstAdjustments().size(); i++) {
                lstAdjustments.add(getLstAdjustments().get(i).clone());
            }
            grdVoucherEntry.setLstAdjustments(lstAdjustments);
        }

        if (getLstItemAdjustments() != null) {
            for (int i = 0; i < getLstItemAdjustments().size(); i++) {
                lstItemAdjustments.add(getLstItemAdjustments().get(i).clone());
            }
            grdVoucherEntry.setLstItemAdjustments(lstItemAdjustments);
        }

        return grdVoucherEntry;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }
    
    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }
}
