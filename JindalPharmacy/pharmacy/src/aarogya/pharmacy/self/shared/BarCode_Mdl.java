package aarogya.pharmacy.self.shared;

import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class BarCode_Mdl implements Serializable, IsSerializable {

    private String itemCsv;
    private MedicineSearch_Mdl medicineDetail;
    private BatchNo_Mdl batchDetails;

    public BarCode_Mdl() {
    }

    public String getItemCsv() {
        return itemCsv;
    }

    public void setItemCsv(String itemCsv) {
        this.itemCsv = itemCsv;
    }

    public MedicineSearch_Mdl getMedicineDetail() {
        return medicineDetail;
    }

    public void setMedicineDetail(MedicineSearch_Mdl medicineDetail) {
        this.medicineDetail = medicineDetail;
    }

    public BatchNo_Mdl getBatchDetails() {
        return batchDetails;
    }

    public void setBatchDetails(BatchNo_Mdl batchDetails) {
        this.batchDetails = batchDetails;
    }
}
