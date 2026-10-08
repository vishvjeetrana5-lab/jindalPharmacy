package aarogya.dashBoard.advancesPendingForBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AdvancesPendingForBillSettlement_Mdl extends KeyField implements Serializable, IsSerializable {

    private String supplierName, poNo, poDate, advanceReqNo, advReqDate, advanceRefNo, advDAte, advanceForSettlement;

    public AdvancesPendingForBillSettlement_Mdl() {
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getAdvanceReqNo() {
        return advanceReqNo;
    }

    public void setAdvanceReqNo(String advanceReqNo) {
        this.advanceReqNo = advanceReqNo;
    }

    public String getAdvReqDate() {
        return advReqDate;
    }

    public void setAdvReqDate(String advReqDate) {
        this.advReqDate = advReqDate;
    }

    public String getAdvanceRefNo() {
        return advanceRefNo;
    }

    public void setAdvanceRefNo(String advanceRefNo) {
        this.advanceRefNo = advanceRefNo;
    }

    public String getAdvDAte() {
        return advDAte;
    }

    public void setAdvDAte(String advDAte) {
        this.advDAte = advDAte;
    }

    public String getAdvanceForSettlement() {
        return advanceForSettlement;
    }

    public void setAdvanceForSettlement(String advanceForSettlement) {
        this.advanceForSettlement = advanceForSettlement;
    }

}
