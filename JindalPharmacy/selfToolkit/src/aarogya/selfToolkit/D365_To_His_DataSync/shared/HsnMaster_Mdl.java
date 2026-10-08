package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class HsnMaster_Mdl implements Serializable, IsSerializable {

    private String code;
    private String name;
    private String hsn;
    private String hisCode;
    private String d365Status;
    private String hisTimestamp;
    private String hisStatus;
    private String itemGSTNature;
    private String purchaseITS;
    private String purchaseReturnITS;
    private String saleITS;
    private String saleReturnITS;
    private String nature;

    public HsnMaster_Mdl() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getHisCode() {
        return hisCode;
    }

    public void setHisCode(String hisCode) {
        this.hisCode = hisCode;
    }

    public String getD365Status() {
        return d365Status;
    }

    public void setD365Status(String d365Status) {
        this.d365Status = d365Status;
    }

    public String getHisTimestamp() {
        return hisTimestamp;
    }

    public void setHisTimestamp(String hisTimestamp) {
        this.hisTimestamp = hisTimestamp;
    }

    public String getHisStatus() {
        return hisStatus;
    }

    public void setHisStatus(String hisStatus) {
        this.hisStatus = hisStatus;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getPurchaseITS() {
        return purchaseITS;
    }

    public void setPurchaseITS(String purchaseITS) {
        this.purchaseITS = purchaseITS;
    }

    public String getPurchaseReturnITS() {
        return purchaseReturnITS;
    }

    public void setPurchaseReturnITS(String purchaseReturnITS) {
        this.purchaseReturnITS = purchaseReturnITS;
    }

    public String getSaleITS() {
        return saleITS;
    }

    public void setSaleITS(String saleITS) {
        this.saleITS = saleITS;
    }

    public String getSaleReturnITS() {
        return saleReturnITS;
    }

    public void setSaleReturnITS(String saleReturnITS) {
        this.saleReturnITS = saleReturnITS;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

}
