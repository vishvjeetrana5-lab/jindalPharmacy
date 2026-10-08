package aarogya.selfToolkit.itemPropertyPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;

public class ItemProperty_Mdl implements Serializable, IsSerializable {

    private String itemCode;
    private String itemName;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String mrp;
    private String skuMrp;
    private String ibCode;
    private String batchNo;
    private String expiryDate;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String skuQtyForRate;
    private String skuName;
    private int allowDecimalPlaces;
    private boolean fromOpening;
    private String itemMakeCode;
    private String itemMakeName;
    private String lotNo;
    private String callFrom;
    private boolean isCheckExpiryDate;

    public ItemProperty_Mdl() {
    }

    public ItemProperty_Mdl(ItemProperty_Mdl ipMdl) {
        this(ipMdl.getItemCode(), ipMdl.getItemName(), ipMdl.getColourCode(), ipMdl.getColourName(), ipMdl.getSizeCode(), ipMdl.getSizeName(), ipMdl.getMrp(), ipMdl.getSkuMrp(), ipMdl.getIbCode(), ipMdl.getBatchNo(), ipMdl.getExpiryDate(), ipMdl.getManufacturingDate(), ipMdl.getBestBeforeDays(), ipMdl.getSkuQtyForRate(), ipMdl.getSkuName(), ipMdl.getAllowDecimalPlaces(), ipMdl.isFromOpening());
    }

    private ItemProperty_Mdl(String itemCode, String itemName, String colourCode, String colourName, String sizeCode, String sizeName, String mrp, String skuMrp, String ibCode, String batchNo, String expiryDate, String manufacturingDate, String bestBeforeDays, String skuQtyForRate, String skuName, int allowDecimalPlaces, boolean fromOpening) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.colourCode = colourCode;
        this.colourName = colourName;
        this.sizeCode = sizeCode;
        this.sizeName = sizeName;
        this.mrp = mrp;
        this.skuMrp = skuMrp;
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
        this.manufacturingDate = manufacturingDate;
        this.bestBeforeDays = bestBeforeDays;
        this.skuQtyForRate = skuQtyForRate;
        this.skuName = skuName;
        this.allowDecimalPlaces = allowDecimalPlaces;
        this.fromOpening = fromOpening;
    }

    public boolean isFromOpening() {
        return fromOpening;
    }

    public void setFromOpening(boolean fromOpening) {
        this.fromOpening = fromOpening;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getItemMakeName() {
        return itemMakeName;
    }

    public void setItemMakeName(String itemMakeName) {
        this.itemMakeName = itemMakeName;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public boolean isIsCheckExpiryDate() {
        return isCheckExpiryDate;
    }

    public void setIsCheckExpiryDate(boolean isCheckExpiryDate) {
        this.isCheckExpiryDate = isCheckExpiryDate;
    }

    public enum Properties implements Serializable, IsSerializable {

        BATCH_NO("BN", "Batch No", 1),
        COLOUR("CL", "Colour", 2),
        EXPIRY_DATE("ED", "Expiry Date", 3),
        MRP("MRP", "MRP", 4),
        SIZE("SZ", "Size", 5),
        ITEM_MAKE("IM", "Item Make", 6),
        LOT_NO("LN", "Lot No", 7);

        private String PropertyManualCode;
        private String PropertyName;
        private int Code;

        public String getPropertyManualCode() {
            return this.PropertyManualCode;
        }

        public String getPropertyName() {
            return this.PropertyName;
        }

        public int getPropertyCode() {
            return this.Code;
        }

        private Properties(String manualCode, String name, int code) {
            this.PropertyManualCode = manualCode;
            this.PropertyName = name;
            this.Code = code;
        }
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
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

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getBestBeforeDays() {
        return bestBeforeDays;
    }

    public void setBestBeforeDays(String bestBeforeDays) {
        this.bestBeforeDays = bestBeforeDays;
    }

    public String getPropertiesCsv() {

        String csvString = "";

        if (!DmShared.xNull(this.getBatchNo()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getBatchNo());
        }

        if (!DmShared.xNull(this.getManufacturingDate()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getManufacturingDate());
        }

        if (!DmShared.xNull(this.getExpiryDate()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getExpiryDate());
        }

        if (!DmShared.xNull(this.getColourName()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getColourName());
        }

        if (!DmShared.xNull(this.getSizeName()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getSizeName());
        }

        if (DmShared.vNull(this.getMrp()) != 0) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(this.getMrp());
        }

        return csvString;
    }

    public int getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(int allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }
}
