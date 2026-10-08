/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

/**
 *
 * @author Dataman
 */
public class Sku_Mdl extends KeyField implements IsSerializable, Serializable {

    public Sku_Mdl() {
    }

    private String allowedDecimalPlaces;

    public String getAllowedDecimalPlaces() {
        return allowedDecimalPlaces;
    }

    public void setAllowedDecimalPlaces(String allowedDecimalPlaces) {
        this.allowedDecimalPlaces = allowedDecimalPlaces;
    }

}
