package dataman.utility.overheadLineConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverheadLineConfiguration_Mdl extends KeyField implements Serializable,IsSerializable{

    private String tableAffectedTag;
    private String tableAffectedText;
    private String applicableFrom;
    private String applicableTo;
    private String overheadName;
    private String voucherTypeCode;
    private String voucherTypeShortName;
    private List<KeyFieldExtended> grdSiteName = new ArrayList<KeyFieldExtended>();
    private List<GrdOverheadLine_Mdl> grdManageLine = new ArrayList<GrdOverheadLine_Mdl>();

    public OverheadLineConfiguration_Mdl() {
    }

    public List<KeyFieldExtended> getGrdSiteName() {
        return grdSiteName;
    }

    public String getOverheadName() {
        return overheadName;
    }

    public void setOverheadName(String overheadName) {
        this.overheadName = overheadName;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public List<GrdOverheadLine_Mdl> getGrdManageLine() {
        return grdManageLine;
    }

    public String getTableAffectedTag() {
        return tableAffectedTag;
    }

    public void setTableAffectedTag(String tableAffectedTag) {
        this.tableAffectedTag = tableAffectedTag;
    }

    public String getTableAffectedText() {
        return tableAffectedText;
    }

    public void setTableAffectedText(String tableAffectedText) {
        this.tableAffectedText = tableAffectedText;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public String getVoucherTypeShortName() {
        return voucherTypeShortName;
    }

    public void setVoucherTypeShortName(String voucherTypeShortName) {
        this.voucherTypeShortName = voucherTypeShortName;
    }

}
