package aarogya.inventory.stockLedger.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements IsSerializable, Serializable {

    private String sessionTable;
    private String openingQty;
    private String balanceQty;
    private String balanceValue;
    private String receiveQty;
    private String issueQty;
    private Setting_Mdl setting_Mdl = new Setting_Mdl();

    public DefaultValue_Mdl() {
    }

    public String getSessionTable() {
        return sessionTable;
    }

    public void setSessionTable(String sessionTable) {
        this.sessionTable = sessionTable;
    }

    public String getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(String openingQty) {
        this.openingQty = openingQty;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
    }

    public String getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(String receiveQty) {
        this.receiveQty = receiveQty;
    }

    public String getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(String issueQty) {
        this.issueQty = issueQty;
    }

    public Setting_Mdl getSetting_Mdl() {
        return setting_Mdl;
    }

    public void setSetting_Mdl(Setting_Mdl setting_Mdl) {
        this.setting_Mdl = setting_Mdl;
    }
}
