package dataman.accounts.balanceSheetCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BalanceSheetDisplayCompare_Mdl extends KeyField implements Serializable, IsSerializable {

    private String a_LHeadCode, a_LHeadNameName, a_LAmount,a_LDrCr;
    private String a_LChildGroupCode, a_LChildGroupName;
    private String a_LChildSubCode, a_LChildSubName;
    private String a_LChildBalance;
    private String lstA_LChildBalance,lstA_LAmount,lstA_LDrCr;

    public BalanceSheetDisplayCompare_Mdl() {
    }

    public String getA_LHeadCode() {
        return a_LHeadCode;
    }

    public void setA_LHeadCode(String a_LHeadCode) {
        this.a_LHeadCode = a_LHeadCode;
    }

    public String getA_LHeadNameName() {
        return a_LHeadNameName;
    }

    public void setA_LHeadNameName(String a_LHeadNameName) {
        this.a_LHeadNameName = a_LHeadNameName;
    }

    public String getA_LAmount() {
        return a_LAmount;
    }

    public void setA_LAmount(String a_LAmount) {
        this.a_LAmount = a_LAmount;
    }

    public String getA_LDrCr() {
        return a_LDrCr;
    }

    public void setA_LDrCr(String a_LDrCr) {
        this.a_LDrCr = a_LDrCr;
    }

    public String getA_LChildGroupCode() {
        return a_LChildGroupCode;
    }

    public void setA_LChildGroupCode(String a_LChildGroupCode) {
        this.a_LChildGroupCode = a_LChildGroupCode;
    }

    public String getA_LChildGroupName() {
        return a_LChildGroupName;
    }

    public void setA_LChildGroupName(String a_LChildGroupName) {
        this.a_LChildGroupName = a_LChildGroupName;
    }

    public String getA_LChildSubCode() {
        return a_LChildSubCode;
    }

    public void setA_LChildSubCode(String a_LChildSubCode) {
        this.a_LChildSubCode = a_LChildSubCode;
    }

    public String getA_LChildSubName() {
        return a_LChildSubName;
    }

    public void setA_LChildSubName(String a_LChildSubName) {
        this.a_LChildSubName = a_LChildSubName;
    }

    public String getA_LChildBalance() {
        return a_LChildBalance;
    }

    public void setA_LChildBalance(String a_LChildBalance) {
        this.a_LChildBalance = a_LChildBalance;
    }

    public String getLstA_LChildBalance() {
        return lstA_LChildBalance;
    }

    public void setLstA_LChildBalance(String lstA_LChildBalance) {
        this.lstA_LChildBalance = lstA_LChildBalance;
    }

    public String getLstA_LAmount() {
        return lstA_LAmount;
    }

    public void setLstA_LAmount(String lstA_LAmount) {
        this.lstA_LAmount = lstA_LAmount;
    }

    public String getLstA_LDrCr() {
        return lstA_LDrCr;
    }

    public void setLstA_LDrCr(String lstA_LDrCr) {
        this.lstA_LDrCr = lstA_LDrCr;
    }

}
