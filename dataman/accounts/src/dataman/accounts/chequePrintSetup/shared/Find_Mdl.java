package dataman.accounts.chequePrintSetup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String bankCode;
    private String bankName;
    private String authSign;
    private String companyName;
    private String paperWidth;
    private String paperHeight;

    public Find_Mdl() {
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAuthSign() {
        return authSign;
    }

    public void setAuthSign(String authSign) {
        this.authSign = authSign;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPaperWidth() {
        return paperWidth;
    }

    public void setPaperWidth(String paperWidth) {
        this.paperWidth = paperWidth;
    }

    public String getPaperHeight() {
        return paperHeight;
    }

    public void setPaperHeight(String paperHeight) {
        this.paperHeight = paperHeight;
    }

   
  

 
}
