package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TDS_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tdsCategoryName;
    private String tdsCategoryCode;
    private String tdsDeductFormName;
    private String tdsDeductFormCode;
    private String tdsDeductManualCode;
    private String tdsOnAmount;
    private String orignalAmount;
    private String tdsForTag;
    private String tdsForText;
    private String debit;
    private String credit;
    private String vr_Date;

    private boolean tdsDetailBind;

    private List<TdsCategoryGrid_Mdl> tdsGridList = new ArrayList<TdsCategoryGrid_Mdl>();

    public TDS_Mdl() {
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public List<TdsCategoryGrid_Mdl> getTdsGridList() {
        return tdsGridList;
    }

    public void setTdsGridList(List<TdsCategoryGrid_Mdl> tdsGridList) {
        this.tdsGridList = tdsGridList;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

    public String getTdsCategoryCode() {
        return tdsCategoryCode;
    }

    public void setTdsCategoryCode(String tdsCategoryCode) {
        this.tdsCategoryCode = tdsCategoryCode;
    }

    public String getTdsDeductFormName() {
        return tdsDeductFormName;
    }

    public void setTdsDeductFormName(String tdsDeductFormName) {
        this.tdsDeductFormName = tdsDeductFormName;
    }

    public String getTdsDeductFormCode() {
        return tdsDeductFormCode;
    }

    public void setTdsDeductFormCode(String tdsDeductFormCode) {
        this.tdsDeductFormCode = tdsDeductFormCode;
    }

    public String getTdsOnAmount() {
        return tdsOnAmount;
    }

    public void setTdsOnAmount(String tdsOnAmount) {
        this.tdsOnAmount = tdsOnAmount;
    }

    public String getOrignalAmount() {
        return orignalAmount;
    }

    public void setOrignalAmount(String orignalAmount) {
        this.orignalAmount = orignalAmount;
    }

    public String getTdsForTag() {
        return tdsForTag;
    }

    public void setTdsForTag(String tdsForTag) {
        this.tdsForTag = tdsForTag;
    }

    public boolean isTdsDetailBind() {
        return tdsDetailBind;
    }

    public void setTdsDetailBind(boolean tdsDetailBind) {
        this.tdsDetailBind = tdsDetailBind;
    }

    public String getTdsDeductManualCode() {
        return tdsDeductManualCode;
    }

    public void setTdsDeductManualCode(String tdsDeductManualCode) {
        this.tdsDeductManualCode = tdsDeductManualCode;
    }

    public String getTdsForText() {
        return tdsForText;
    }

    public void setTdsForText(String tdsForText) {
        this.tdsForText = tdsForText;
    }

    public String getVr_Date() {
        return vr_Date;
    }

    public void setVr_Date(String vr_Date) {
        this.vr_Date = vr_Date;
    }



    public TDS_Mdl clone() {
        TDS_Mdl tDS_Mdl = new TDS_Mdl();
        List<TdsCategoryGrid_Mdl> listTdsCatGrdData = new ArrayList();

        tDS_Mdl.setOrignalAmount(this.getOrignalAmount());
        tDS_Mdl.setTdsCategoryCode(this.getTdsCategoryCode());
        tDS_Mdl.setTdsCategoryName(this.getTdsCategoryName());
        tDS_Mdl.setTdsDeductFormCode(this.getTdsDeductFormCode());
        tDS_Mdl.setTdsDeductFormName(this.getTdsDeductFormName());
        tDS_Mdl.setTdsDeductManualCode(this.getTdsDeductManualCode());
        tDS_Mdl.setTdsForTag(this.getTdsForTag());
        tDS_Mdl.setTdsForText(this.getTdsForText());
        tDS_Mdl.setTdsOnAmount(this.getTdsOnAmount());
        tDS_Mdl.setTdsDetailBind(this.isTdsDetailBind());
        tDS_Mdl.setVr_Date(this.getVr_Date());


        if (getTdsGridList() != null) {
            for (int i = 0; i < getTdsGridList().size(); i++) {
                listTdsCatGrdData.add(getTdsGridList().get(i).clone());
            }
            tDS_Mdl.setTdsGridList(listTdsCatGrdData);
        }

        return tDS_Mdl;
    }

}
