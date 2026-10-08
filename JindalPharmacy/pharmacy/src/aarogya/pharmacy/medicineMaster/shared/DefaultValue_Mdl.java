/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Dataman
 */
public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String medicineNameAlphabetCase;
    private boolean inventoryAccountingActive;
    private boolean edit_NameIn_Inventory_ItemMAst;

    public DefaultValue_Mdl() {
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
