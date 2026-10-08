package dataman.accounts.profitLossDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ProfitLossDisplay_Mdl extends KeyField implements Serializable, IsSerializable {

    private String expensesCode, expensesName, expensesAmount;
    private String expensesChildGroupCode, expensesChildGroupName;
    private String expensesChildSubCode, expensesChildSubName;
    private String expensesChildBalance;
    private String revenueCode, revenueName, revenueAmount;
    private String revenueChildGroupCode, revenueChildGroupName;
    private String revenueChildSubCode, revenueChildSubName;
    private String revenueChildBalance;

    public ProfitLossDisplay_Mdl() {
    }

    public String getExpensesCode() {
        return expensesCode;
    }

    public void setExpensesCode(String expensesCode) {
        this.expensesCode = expensesCode;
    }

    public String getExpensesName() {
        return expensesName;
    }

    public void setExpensesName(String expensesName) {
        this.expensesName = expensesName;
    }

    public String getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(String expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public String getExpensesChildGroupCode() {
        return expensesChildGroupCode;
    }

    public void setExpensesChildGroupCode(String expensesChildGroupCode) {
        this.expensesChildGroupCode = expensesChildGroupCode;
    }

    public String getExpensesChildGroupName() {
        return expensesChildGroupName;
    }

    public void setExpensesChildGroupName(String expensesChildGroupName) {
        this.expensesChildGroupName = expensesChildGroupName;
    }

    public String getExpensesChildSubCode() {
        return expensesChildSubCode;
    }

    public void setExpensesChildSubCode(String expensesChildSubCode) {
        this.expensesChildSubCode = expensesChildSubCode;
    }

    public String getExpensesChildSubName() {
        return expensesChildSubName;
    }

    public void setExpensesChildSubName(String expensesChildSubName) {
        this.expensesChildSubName = expensesChildSubName;
    }

    public String getExpensesChildBalance() {
        return expensesChildBalance;
    }

    public void setExpensesChildBalance(String expensesChildBalance) {
        this.expensesChildBalance = expensesChildBalance;
    }

    public String getRevenueCode() {
        return revenueCode;
    }

    public void setRevenueCode(String revenueCode) {
        this.revenueCode = revenueCode;
    }

    public String getRevenueName() {
        return revenueName;
    }

    public void setRevenueName(String revenueName) {
        this.revenueName = revenueName;
    }

    public String getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(String revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public String getRevenueChildGroupCode() {
        return revenueChildGroupCode;
    }

    public void setRevenueChildGroupCode(String revenueChildGroupCode) {
        this.revenueChildGroupCode = revenueChildGroupCode;
    }

    public String getRevenueChildGroupName() {
        return revenueChildGroupName;
    }

    public void setRevenueChildGroupName(String revenueChildGroupName) {
        this.revenueChildGroupName = revenueChildGroupName;
    }

    public String getRevenueChildSubCode() {
        return revenueChildSubCode;
    }

    public void setRevenueChildSubCode(String revenueChildSubCode) {
        this.revenueChildSubCode = revenueChildSubCode;
    }

    public String getRevenueChildSubName() {
        return revenueChildSubName;
    }

    public void setRevenueChildSubName(String revenueChildSubName) {
        this.revenueChildSubName = revenueChildSubName;
    }

    public String getRevenueChildBalance() {
        return revenueChildBalance;
    }

    public void setRevenueChildBalance(String revenueChildBalance) {
        this.revenueChildBalance = revenueChildBalance;
    }

}
