/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aarogya.pharmacy.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHBillNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String billV_SNo;
    private String pbV_Date;
    private String pbRefNo;
    private String pbRefDate;

    public TxtHBillNo_Mdl() {
    }

    public String getBillV_SNo() {
        return billV_SNo;
    }

    public void setBillV_SNo(String billV_SNo) {
        this.billV_SNo = billV_SNo;
    }

    public String getPbV_Date() {
        return pbV_Date;
    }

    public void setPbV_Date(String pbV_Date) {
        this.pbV_Date = pbV_Date;
    }

    public String getPbRefNo() {
        return pbRefNo;
    }

    public void setPbRefNo(String pbRefNo) {
        this.pbRefNo = pbRefNo;
    }

    public String getPbRefDate() {
        return pbRefDate;
    }

    public void setPbRefDate(String pbRefDate) {
        this.pbRefDate = pbRefDate;
    }

}
