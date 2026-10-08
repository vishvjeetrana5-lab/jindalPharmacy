package dataman.accounts.profitLossDisplayCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ProfitLossDisplayCompare_Mdl extends KeyField implements Serializable, IsSerializable {

    private String e_RHeadCode, e_RHeadNameName, e_RAmount,e_RDrCr;
    private String e_RChildGroupCode, e_RChildGroupName;
    private String e_RChildSubCode, e_RChildSubName;
    private String e_RChildBalance;
    private String lstE_RChildBalance,lstE_RAmount,lstE_RDrCr;

    public ProfitLossDisplayCompare_Mdl() {
    }

    public String getE_RHeadCode() {
        return e_RHeadCode;
    }

    public void setE_RHeadCode(String e_RHeadCode) {
        this.e_RHeadCode = e_RHeadCode;
    }

    public String getE_RHeadNameName() {
        return e_RHeadNameName;
    }

    public void setE_RHeadNameName(String e_RHeadNameName) {
        this.e_RHeadNameName = e_RHeadNameName;
    }

    public String getE_RAmount() {
        return e_RAmount;
    }

    public void setE_RAmount(String e_RAmount) {
        this.e_RAmount = e_RAmount;
    }

    public String getE_RDrCr() {
        return e_RDrCr;
    }

    public void setE_RDrCr(String e_RDrCr) {
        this.e_RDrCr = e_RDrCr;
    }

    public String getE_RChildGroupCode() {
        return e_RChildGroupCode;
    }

    public void setE_RChildGroupCode(String e_RChildGroupCode) {
        this.e_RChildGroupCode = e_RChildGroupCode;
    }

    public String getE_RChildGroupName() {
        return e_RChildGroupName;
    }

    public void setE_RChildGroupName(String e_RChildGroupName) {
        this.e_RChildGroupName = e_RChildGroupName;
    }

    public String getE_RChildSubCode() {
        return e_RChildSubCode;
    }

    public void setE_RChildSubCode(String e_RChildSubCode) {
        this.e_RChildSubCode = e_RChildSubCode;
    }

    public String getE_RChildSubName() {
        return e_RChildSubName;
    }

    public void setE_RChildSubName(String e_RChildSubName) {
        this.e_RChildSubName = e_RChildSubName;
    }

    public String getE_RChildBalance() {
        return e_RChildBalance;
    }

    public void setE_RChildBalance(String e_RChildBalance) {
        this.e_RChildBalance = e_RChildBalance;
    }

    public String getLstE_RChildBalance() {
        return lstE_RChildBalance;
    }

    public void setLstE_RChildBalance(String lstE_RChildBalance) {
        this.lstE_RChildBalance = lstE_RChildBalance;
    }

    public String getLstE_RAmount() {
        return lstE_RAmount;
    }

    public void setLstE_RAmount(String lstE_RAmount) {
        this.lstE_RAmount = lstE_RAmount;
    }

    public String getLstE_RDrCr() {
        return lstE_RDrCr;
    }

    public void setLstE_RDrCr(String lstE_RDrCr) {
        this.lstE_RDrCr = lstE_RDrCr;
    }

}
