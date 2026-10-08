package aarogya.pharmacy.addBatchSingle.shared;

import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Batch_Mdl extends ItemBatch_Mdl implements Serializable, IsSerializable {

    private String entryDate;
    private boolean isBestBefore;
    private boolean isBarcodeActive;

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public boolean isIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(boolean isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public boolean isIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(boolean isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

}
