package dataman.accounts.financialBookClose.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class FinancialBookClose_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherTypeName;
    private String voucherTypeCode;
    private String vPrefix;
    private String voucherDate;
    private String contraCode;
    private String contraText;
    private String contraTextAccDesc;

    public FinancialBookClose_Mdl() {
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getContraCode() {
        return contraCode;
    }

    public void setContraCode(String contraCode) {
        this.contraCode = contraCode;
    }

    public String getContraText() {
        return contraText;
    }

    public void setContraText(String contraText) {
        this.contraText = contraText;
    }

    public String getContraTextAccDesc() {
        return contraTextAccDesc;
    }

    public void setContraTextAccDesc(String contraTextAccDesc) {
        this.contraTextAccDesc = contraTextAccDesc;
    }

}
