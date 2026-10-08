package dataman.accounts.tdsCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TdsCategoryMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String applicableFrom;
    private String type;
    private String typeCode;
    private String section;
    private String tdsForTag;
    private String tdsForText;
    private String initialTurnoverAmount;
    private String tdsOnPreviousTransactionsTag;
    private String tdsOnPreviousTransactionsText;
    private List<GrdTdsCategory_Mdl> GrdData = new ArrayList<GrdTdsCategory_Mdl>();

    public TdsCategoryMaster_Mdl() {
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTdsForTag() {
        return tdsForTag;
    }

    public void setTdsForTag(String tdsForTag) {
        this.tdsForTag = tdsForTag;
    }

    public String getTdsForText() {
        return tdsForText;
    }

    public void setTdsForText(String tdsForText) {
        this.tdsForText = tdsForText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public List<GrdTdsCategory_Mdl> getGrdData() {
        return GrdData;
    }

    public String getInitialTurnoverAmount() {
        return initialTurnoverAmount;
    }

    public void setInitialTurnoverAmount(String initialTurnoverAmount) {
        this.initialTurnoverAmount = initialTurnoverAmount;
    }

    public String getTdsOnPreviousTransactionsTag() {
        return tdsOnPreviousTransactionsTag;
    }

    public void setTdsOnPreviousTransactionsTag(String tdsOnPreviousTransactionsTag) {
        this.tdsOnPreviousTransactionsTag = tdsOnPreviousTransactionsTag;
    }

    public String getTdsOnPreviousTransactionsText() {
        return tdsOnPreviousTransactionsText;
    }

    public void setTdsOnPreviousTransactionsText(String tdsOnPreviousTransactionsText) {
        this.tdsOnPreviousTransactionsText = tdsOnPreviousTransactionsText;
    }

}
