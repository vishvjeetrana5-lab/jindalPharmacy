package dataman.dmToolkit.stock.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class StockFifo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String sno;
    private String recQty;
    private String isuQty;
    private String stockValueDate;
    private String avgSKURate;
    private String avgLandedSKURate;
    private String isItemStockRecord;

    public StockFifo_Mdl() {
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getRecQty() {
        return recQty;
    }

    public void setRecQty(String recQty) {
        this.recQty = recQty;
    }

    public String getIsuQty() {
        return isuQty;
    }

    public void setIsuQty(String isuQty) {
        this.isuQty = isuQty;
    }

    public String getStockValueDate() {
        return stockValueDate;
    }

    public void setStockValueDate(String stockValueDate) {
        this.stockValueDate = stockValueDate;
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

    public String getIsItemStockRecord() {
        return isItemStockRecord;
    }

    public void setIsItemStockRecord(String isItemStockRecord) {
        this.isItemStockRecord = isItemStockRecord;
    }
    
    public void copy(StockFifo_Mdl stockFifo_Mdl) {
        stockFifo_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        stockFifo_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        stockFifo_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        stockFifo_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        stockFifo_Mdl.setSno(this.getSno());
        stockFifo_Mdl.setRecQty(this.getRecQty());
        stockFifo_Mdl.setIsuQty(this.getIsuQty());
        stockFifo_Mdl.setStockValueDate(this.getStockValueDate());
        stockFifo_Mdl.setAvgSKURate(this.getAvgSKURate());
        stockFifo_Mdl.setAvgLandedSKURate(this.getAvgLandedSKURate());
        stockFifo_Mdl.setIsItemStockRecord(this.getIsItemStockRecord());
    }    

}
