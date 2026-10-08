package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String allowNegativeStockCode;
    private String allowNegativeStockName;
    private String propertyList;
    private String propertyCode;
    private String propertyName;
    private boolean accountsEditable;
    private String medicineNameAlphabetCase;
    private boolean inventoryAccountingActive;
    private boolean edit_NameIn_Inventory_ItemMAst;

    public DefaultValue_Mdl() {
    }

    public String getAllowNegativeStockCode() {
        return allowNegativeStockCode;
    }

    public void setAllowNegativeStockCode(String allowNegativeStockCode) {
        this.allowNegativeStockCode = allowNegativeStockCode;
    }

    public String getAllowNegativeStockName() {
        return allowNegativeStockName;
    }

    public void setAllowNegativeStockName(String allowNegativeStockName) {
        this.allowNegativeStockName = allowNegativeStockName;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isAccountsEditable() {
        return accountsEditable;
    }

    public void setAccountsEditable(boolean accountsEditable) {
        this.accountsEditable = accountsEditable;
    }

    public String getMedicineNameAlphabetCase() {
        return medicineNameAlphabetCase;
    }

    public void setMedicineNameAlphabetCase(String medicineNameAlphabetCase) {
        this.medicineNameAlphabetCase = medicineNameAlphabetCase;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public boolean isEdit_NameIn_Inventory_ItemMAst() {
        return edit_NameIn_Inventory_ItemMAst;
    }

    public void setEdit_NameIn_Inventory_ItemMAst(boolean edit_NameIn_Inventory_ItemMAst) {
        this.edit_NameIn_Inventory_ItemMAst = edit_NameIn_Inventory_ItemMAst;
    }

}
