/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCompany_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String purchaseDisPer;

    public TxtHCompany_Mdl() {
    }

    public String getPurchaseDisPer() {
        return purchaseDisPer;
    }

    public void setPurchaseDisPer(String purchaseDisPer) {
        this.purchaseDisPer = purchaseDisPer;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
