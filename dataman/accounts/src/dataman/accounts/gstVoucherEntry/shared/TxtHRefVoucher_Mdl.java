package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHRefVoucher_Mdl extends KeyField implements Serializable, IsSerializable {
    //SharedFieldCode = docid
    //SharedFieldName = recId
    
    private String refV_SNo;
    private String refV_Type;
    private String refV_Date;
    private String refDocType;
    private String refDocValue;
    private String refNo;
    private String refDate;

    public TxtHRefVoucher_Mdl() {
    }

    public String getRefV_SNo() {
        return refV_SNo;
    }

    public void setRefV_SNo(String refV_SNo) {
        this.refV_SNo = refV_SNo;
    }

    public String getRefV_Type() {
        return refV_Type;
    }

    public void setRefV_Type(String refV_Type) {
        this.refV_Type = refV_Type;
    }

    public String getRefV_Date() {
        return refV_Date;
    }

    public void setRefV_Date(String refV_Date) {
        this.refV_Date = refV_Date;
    }

    public String getRefDocType() {
        return refDocType;
    }

    public void setRefDocType(String refDocType) {
        this.refDocType = refDocType;
    }

    public String getRefDocValue() {
        return refDocValue;
    }

    public void setRefDocValue(String refDocValue) {
        this.refDocValue = refDocValue;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }
            
}
