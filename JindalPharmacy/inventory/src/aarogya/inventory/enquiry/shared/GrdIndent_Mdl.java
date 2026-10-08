package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdIndent_Mdl extends KeyField implements Serializable,IsSerializable{

    private Integer sNO;
    private String sku;



    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    private String remark;
    private String qty;
    private String scheduleDt;
    private String unit;
    private String unitCode;
    private static int counter = 0;

    public String getScheduleDt() {
        return scheduleDt;
    }

    public void setScheduleDt(String scheduleDt) {
        this.scheduleDt = scheduleDt;
    }

    public GrdIndent_Mdl() {
        this.sNO = ++counter;
    }

    public Integer getSNO() {
        return sNO;
    }

    public void setSNO(Integer sNO) {
        this.sNO = sNO;
    }

    public Integer getsNO() {
        return sNO;
    }

    public void setsNO(Integer sNO) {
        this.sNO = sNO;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        GrdIndent_Mdl.counter = counter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

}
