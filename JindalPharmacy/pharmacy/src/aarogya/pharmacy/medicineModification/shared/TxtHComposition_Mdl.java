/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

/**
 *
 * @author dataman
 */
public class TxtHComposition_Mdl extends KeyField implements Serializable, IsSerializable {

    //shareKeyFieldCode = compositionCode
    //shareKeyFieldName = compositionName
    public TxtHComposition_Mdl() {
    }

    private String restrictGroupCode;
    private String restrictGroupName;

    public String getRestrictGroupCode() {
        return restrictGroupCode;
    }

    public void setRestrictGroupCode(String restrictGroupCode) {
        this.restrictGroupCode = restrictGroupCode;
    }

    public String getRestrictGroupName() {
        return restrictGroupName;
    }

    public void setRestrictGroupName(String restrictGroupName) {
        this.restrictGroupName = restrictGroupName;
    }

}
