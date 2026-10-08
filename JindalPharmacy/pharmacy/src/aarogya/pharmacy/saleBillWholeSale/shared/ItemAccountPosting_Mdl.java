package aarogya.pharmacy.saleBillWholeSale.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ItemAccountPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String subCode;
    private String contraSub;
    private String contraText;
    private String amtdr;
    private String amtcr;
    private String recIdPrefix;
    private String v_Type;
    private String v_Prefix;
    private String v_Date;
    private String v_No;
    private String dueDate;

    public ItemAccountPosting_Mdl() {

    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getContraSub() {
        return contraSub;
    }

    public void setContraSub(String contraSub) {
        this.contraSub = contraSub;
    }

    public String getContraText() {
        return contraText;
    }

    public void setContraText(String contraText) {
        this.contraText = contraText;
    }

    public String getAmtdr() {
        return amtdr;
    }

    public void setAmtdr(String amtdr) {
        this.amtdr = amtdr;
    }

    public String getAmtcr() {
        return amtcr;
    }

    public void setAmtcr(String amtcr) {
        this.amtcr = amtcr;
    }

    public String getRecIdPrefix() {
        return recIdPrefix;
    }

    public void setRecIdPrefix(String recIdPrefix) {
        this.recIdPrefix = recIdPrefix;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_Prefix() {
        return v_Prefix;
    }

    public void setV_Prefix(String v_Prefix) {
        this.v_Prefix = v_Prefix;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
