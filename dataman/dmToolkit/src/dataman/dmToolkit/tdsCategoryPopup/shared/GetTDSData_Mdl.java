package dataman.dmToolkit.tdsCategoryPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GetTDSData_Mdl implements Serializable, IsSerializable{

    private String vCat;
    private String tdsCatCode;
    private String currentTDSOnAmt;
    private String originalAmt;
    private String partyCode;
    private String pBillvDate;
    private String trnDocID;
    private String poDocID;

    public GetTDSData_Mdl() {
       
    }

    public String getvCat() {
        return vCat;
    }

    public void setvCat(String vCat) {
        this.vCat = vCat;
    }

    public String getTdsCatCode() {
        return tdsCatCode;
    }

    public void setTdsCatCode(String tdsCatCode) {
        this.tdsCatCode = tdsCatCode;
    }

    public String getCurrentTDSOnAmt() {
        return currentTDSOnAmt;
    }

    public void setCurrentTDSOnAmt(String currentTDSOnAmt) {
        this.currentTDSOnAmt = currentTDSOnAmt;
    }

    public String getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(String originalAmt) {
        this.originalAmt = originalAmt;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getpBillvDate() {
        return pBillvDate;
    }

    public void setpBillvDate(String pBillvDate) {
        this.pBillvDate = pBillvDate;
    }

    public String getTrnDocID() {
        return trnDocID;
    }

    public void setTrnDocID(String trnDocID) {
        this.trnDocID = trnDocID;
    }

    public String getPoDocID() {
        return poDocID;
    }

    public void setPoDocID(String poDocID) {
        this.poDocID = poDocID;
    }
}
