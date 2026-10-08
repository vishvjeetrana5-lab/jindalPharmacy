package dataman.accounts.profitLossDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdProfitLossReport_Mdl implements Serializable, IsSerializable {

    private String expensesCode;
    private String expensesName;
    private String isExpensesChild;
    private String isExpensesBold;
    private String expensesChildBalance;
    private String expensesAmount;

    private String revenueCode;
    private String revenueName;
    private String isRevenueChild;
    private String isRevenueBold;
    private String revenueChildBalance;
    private String revenueAmount;

    public GrdProfitLossReport_Mdl() {
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

    public String getIsExpensesChild() {
        return isExpensesChild;
    }

    public void setIsExpensesChild(String isExpensesChild) {
        this.isExpensesChild = isExpensesChild;
    }

    public String getIsExpensesBold() {
        return isExpensesBold;
    }

    public void setIsExpensesBold(String isExpensesBold) {
        this.isExpensesBold = isExpensesBold;
    }

    public String getExpensesChildBalance() {
        return expensesChildBalance;
    }

    public void setExpensesChildBalance(String expensesChildBalance) {
        this.expensesChildBalance = expensesChildBalance;
    }

    public String getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(String expensesAmount) {
        this.expensesAmount = expensesAmount;
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

    public String getIsRevenueChild() {
        return isRevenueChild;
    }

    public void setIsRevenueChild(String isRevenueChild) {
        this.isRevenueChild = isRevenueChild;
    }

    public String getIsRevenueBold() {
        return isRevenueBold;
    }

    public void setIsRevenueBold(String isRevenueBold) {
        this.isRevenueBold = isRevenueBold;
    }

    public String getRevenueChildBalance() {
        return revenueChildBalance;
    }

    public void setRevenueChildBalance(String revenueChildBalance) {
        this.revenueChildBalance = revenueChildBalance;
    }

    public String getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(String revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

}
