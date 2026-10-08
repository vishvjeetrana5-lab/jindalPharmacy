package dataman.dmToolkit.stock.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ItemStock_Mdl implements Serializable, IsSerializable {

    private String itemCode;
    private String ibCode;    
    private String departmentCode;
    private String site_Code;
    private String stockValueDate;
    private String receiveQty;
    private String avgSKURate;
    private String avgLandedSKURate;
    private String balanceQty;
    private String isReCalculate;

    public ItemStock_Mdl() {
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }

    public String getStockValueDate() {
        return stockValueDate;
    }

    public void setStockValueDate(String stockValueDate) {
        this.stockValueDate = stockValueDate;
    }

    public String getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(String receiveQty) {
        this.receiveQty = receiveQty;
    }

    public String getAvgSKURate() {
        return avgSKURate;
    }

    public void setAvgSKURate(String avgSKURate) {
        this.avgSKURate = avgSKURate;
    }

    public String getAvgLandedSKURate() {
        return avgLandedSKURate;
    }

    public void setAvgLandedSKURate(String avgLandedSKURate) {
        this.avgLandedSKURate = avgLandedSKURate;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getIsReCalculate() {
        return isReCalculate;
    }

    public void setIsReCalculate(String isReCalculate) {
        this.isReCalculate = isReCalculate;
    }
    
    
}
