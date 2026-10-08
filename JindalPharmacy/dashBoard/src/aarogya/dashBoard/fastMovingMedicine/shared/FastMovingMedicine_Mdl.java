package aarogya.dashBoard.fastMovingMedicine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class FastMovingMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String medicineName, medicineCategory;
    private String totalSoldQty, salesRank;

    public FastMovingMedicine_Mdl() {
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(String medicineCategory) {
        this.medicineCategory = medicineCategory;
    }

    public String getTotalSoldQty() {
        return totalSoldQty;
    }

    public void setTotalSoldQty(String totalSoldQty) {
        this.totalSoldQty = totalSoldQty;
    }

    public String getSalesRank() {
        return salesRank;
    }

    public void setSalesRank(String salesRank) {
        this.salesRank = salesRank;
    }

}
