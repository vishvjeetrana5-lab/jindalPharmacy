package aarogya.selfToolkit.medicineSubstituteSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdMedicineSubstituteDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String company, manualCode, stock, sku;
    private String compositionMatchStatus;
    private String matchtStatus;

    public GrdMedicineSubstituteDetails_Mdl() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCompositionMatchStatus() {
        return compositionMatchStatus;
    }

    public void setCompositionMatchStatus(String compositionMatchStatus) {
        this.compositionMatchStatus = compositionMatchStatus;
    }

    public String getMatchtStatus() {
        return matchtStatus;
    }

    public void setMatchtStatus(String matchtStatus) {
        this.matchtStatus = matchtStatus;
    }

}
