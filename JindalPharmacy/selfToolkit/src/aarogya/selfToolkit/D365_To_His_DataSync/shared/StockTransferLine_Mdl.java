package aarogya.selfToolkit.D365_To_His_DataSync.shared;

public class StockTransferLine_Mdl {

    private String lineNumber;               // shd.v_SNo
    private String d365FromDepartmentCode;    // dptFrm.D365_code
    private String d365FromDepartmentName;    // dptFrm.name
    private String d365ToDepartmentCode;      // dptTo.D365_code
    private String d365ToDepartmentName;      // dptTo.name
    private String d365ItemCode;              // im.D365_code
    private String itemName;                  // im.name
    private String itemType;                  // itmt.name
    private String batchNo;                   // ib.batchNo
    private String quantity;                  // shd.quantity
    private String d365UnitCode;              // unt.D365_code
    private String unitName;

    public StockTransferLine_Mdl() {
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getD365FromDepartmentCode() {
        return d365FromDepartmentCode;
    }

    public void setD365FromDepartmentCode(String d365FromDepartmentCode) {
        this.d365FromDepartmentCode = d365FromDepartmentCode;
    }

    public String getD365FromDepartmentName() {
        return d365FromDepartmentName;
    }

    public void setD365FromDepartmentName(String d365FromDepartmentName) {
        this.d365FromDepartmentName = d365FromDepartmentName;
    }

    public String getD365ToDepartmentCode() {
        return d365ToDepartmentCode;
    }

    public void setD365ToDepartmentCode(String d365ToDepartmentCode) {
        this.d365ToDepartmentCode = d365ToDepartmentCode;
    }

    public String getD365ToDepartmentName() {
        return d365ToDepartmentName;
    }

    public void setD365ToDepartmentName(String d365ToDepartmentName) {
        this.d365ToDepartmentName = d365ToDepartmentName;
    }

    public String getD365ItemCode() {
        return d365ItemCode;
    }

    public void setD365ItemCode(String d365ItemCode) {
        this.d365ItemCode = d365ItemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getD365UnitCode() {
        return d365UnitCode;
    }

    public void setD365UnitCode(String d365UnitCode) {
        this.d365UnitCode = d365UnitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
