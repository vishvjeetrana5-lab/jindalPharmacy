/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.inventory.purchaseBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
public class TxtHHeaderGrnNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String grnDate;
    private String challanNo;
    private String challanDate;
    private String poNo;
    private String poDocID;
  
    public TxtHHeaderGrnNo_Mdl() {

    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoDocID() {
        return poDocID;
    }

    public void setPoDocID(String poDocID) {
        this.poDocID = poDocID;
    }

   
}
