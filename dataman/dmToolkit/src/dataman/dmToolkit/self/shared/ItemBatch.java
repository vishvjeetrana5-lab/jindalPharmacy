package dataman.dmToolkit.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemBatch implements Serializable, IsSerializable {

    private String batchCode, batchNo;
    private String itemCode, itemName;
    private String departmentCode, departmentName;
    private String siteCode, siteName;
    private String purchaseRate, saleRate, LandedRate;
    private String currentStock;
    private LinkedHashMap<String, String> varField = new LinkedHashMap<String, String>();

    private String varKeyParse(String key) {
        return DmShared.xNull(key).trim().toLowerCase();
    }

    public void varPut(String key, String value) {
        if (varContains(varKeyParse(key))) {
            varField.replace(varKeyParse(key), value);
        } else {
            varField.put(varKeyParse(key), value);
        }
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean varContains(String key) {
        return varField.containsKey(varKeyParse(key));
    }

    public String varRemove(String key) {
        return varField.remove(key);
    }

    public void varClear() {
        varField.clear();
    }

    public LinkedHashMap<String, String> varClone() {
        return (LinkedHashMap<String, String>) varField.clone();
    }

    public String varGet(String key) {
        return varField.get(varKeyParse(key));
    }

    public List<String> varGetKeys() {
        Iterator<String> i = varField.keySet().iterator();
        List<String> stringList = new ArrayList<String>();

        while (i.hasNext()) {
            stringList.add(i.next());
        }
        return stringList;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getLandedRate() {
        return LandedRate;
    }

    public void setLandedRate(String LandedRate) {
        this.LandedRate = LandedRate;
    }

    public String getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(String currentStock) {
        this.currentStock = currentStock;
    }
}
