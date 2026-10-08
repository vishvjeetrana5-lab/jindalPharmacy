package dataman.accounts.bankReconciliationEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankReconciliationEntry_Mdl implements Serializable, IsSerializable {

    private String opnAsPerBank;
    private String companyBal;
    private String clearDate;
    List<GrdBankReconciliationEntry_Mdl> listBr = new ArrayList<>();

    public String getOpnAsPerBank() {
        return opnAsPerBank;
    }

    public void setOpnAsPerBank(String opnAsPerBank) {
        this.opnAsPerBank = opnAsPerBank;
    }

    public String getCompanyBal() {
        return companyBal;
    }

    public void setCompanyBal(String companyBal) {
        this.companyBal = companyBal;
    }

    public String getClearDate() {
        return clearDate;
    }

    public void setClearDate(String clearDate) {
        this.clearDate = clearDate;
    }

    public List<GrdBankReconciliationEntry_Mdl> getListBr() {
        return listBr;
    }

    public void setListBr(List<GrdBankReconciliationEntry_Mdl> listBr) {
        this.listBr = listBr;
    }
    
    
}
