package aarogya.pharmacy.wholeSaleSettings.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.io.Serializable;

public class WholeSaleSetting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String pharmacyCentralStoreName;
    private String pharmacyCentralStoreCode;
    private String ewayBillLimit;
    private ModifyReason_Mdl modifyReasonMdl;

    public WholeSaleSetting_Mdl() {

    }

    public String getPharmacyCentralStoreName() {
        return pharmacyCentralStoreName;
    }

    public void setPharmacyCentralStoreName(String pharmacyCentralStoreName) {
        this.pharmacyCentralStoreName = pharmacyCentralStoreName;
    }

    public String getPharmacyCentralStoreCode() {
        return pharmacyCentralStoreCode;
    }

    public String getEwayBillLimit() {
        return ewayBillLimit;
    }

    public void setEwayBillLimit(String ewayBillLimit) {
        this.ewayBillLimit = ewayBillLimit;
    }

    public void setPharmacyCentralStoreCode(String pharmacyCentralStoreCode) {
        this.pharmacyCentralStoreCode = pharmacyCentralStoreCode;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

}
