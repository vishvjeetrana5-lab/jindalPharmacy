package aarogya.master.cardDeactive.shared;

import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class CardDeactivate_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vrTypeCode;
    private String vrTypeName;
    private String vrDate;
    private String vrNo;
    private String remark;
    private double balance;
    private RecId recIdDeactive = new RecId();
    private RegisteredWallet_Mdl cardRegMdl;

    public CardDeactivate_Mdl() {
    }

    public RegisteredWallet_Mdl getCardRegMdl() {
        return cardRegMdl;
    }

    public void setCardRegMdl(RegisteredWallet_Mdl cardRegMdl) {
        this.cardRegMdl = cardRegMdl;
    }

    public String getVrTypeCode() {
        return vrTypeCode;
    }

    public void setVrTypeCode(String vrTypeCode) {
        this.vrTypeCode = vrTypeCode;
    }

    public String getVrTypeName() {
        return vrTypeName;
    }

    public void setVrTypeName(String vrTypeName) {
        this.vrTypeName = vrTypeName;
    }

    public String getVrDate() {
        return vrDate;
    }

    public void setVrDate(String vrDate) {
        this.vrDate = vrDate;
    }

    public String getVrNo() {
        return vrNo;
    }

    public void setVrNo(String vrNo) {
        this.vrNo = vrNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public RecId getRecIdDeactive() {
        return recIdDeactive;
    }

    public void setRecIdDeactive(RecId recIdDeactive) {
        this.recIdDeactive = recIdDeactive;
    }

}
