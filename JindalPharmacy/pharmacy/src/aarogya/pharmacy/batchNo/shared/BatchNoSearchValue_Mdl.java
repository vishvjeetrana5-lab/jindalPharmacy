package aarogya.pharmacy.batchNo.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class BatchNoSearchValue_Mdl implements Serializable, IsSerializable {

    private String itemCode, departmentCode, selectedBatchSQL, v_Date;      //, vCat, formDocid
    private boolean isAllwNegStk, isReceive;  //, isExpiryBatch, defaultExpiryShow
    private String barCode;
    private ItemBatchType batchType;

    public BatchNoSearchValue_Mdl() {
    }

    public boolean isIsReceive() {
        return isReceive;
    }

    public void setIsReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }

    public ItemBatchType getBatchType() {
        return batchType;
    }

    public void setBatchType(ItemBatchType batchType) {
        this.batchType = batchType;
    }

//    public boolean isDefaultExpiryShow() {
//        return defaultExpiryShow;
//    }
//
//    public void setDefaultExpiryShow(boolean defaultExpiryShow) {
//        this.defaultExpiryShow = defaultExpiryShow;
//    }
//
//    public boolean getIsExpiryBatch() {
//        return isExpiryBatch;
//    }
//
//    public void setIsExpiryBatch(boolean isExpiryBatch) {
//        this.isExpiryBatch = isExpiryBatch;
//    }
    public String getSelectedBatchSQL() {
        return selectedBatchSQL;
    }

    public void setSelectedBatchSQL(String selectedBatchSQL) {
        this.selectedBatchSQL = selectedBatchSQL;
    }

    public boolean getIsAllwNegStk() {
        return isAllwNegStk;
    }

    public void setIsAllwNegStk(boolean isAllwNegStk) {
        this.isAllwNegStk = isAllwNegStk;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
