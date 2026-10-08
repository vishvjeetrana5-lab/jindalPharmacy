package dataman.dmToolkit.tdsCategoryPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TdsPopupFields_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tdsCategoryName;
    private String tdsCategoryCode;
    private String tdsDeductFormName;
    private String tdsDeductFormCode;
    private String tdsDeductManualCode;
    private String tdsOnAmount;
    private String orignalAmount;
    private String tdsForTag;
    private String tdsForText;

    public TdsPopupFields_Mdl() {
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

    public String getTdsCategoryCode() {
        return tdsCategoryCode;
    }

    public void setTdsCategoryCode(String tdsCategoryCode) {
        this.tdsCategoryCode = tdsCategoryCode;
    }

    public String getTdsDeductFormName() {
        return tdsDeductFormName;
    }

    public void setTdsDeductFormName(String tdsDeductFormName) {
        this.tdsDeductFormName = tdsDeductFormName;
    }

    public String getTdsDeductFormCode() {
        return tdsDeductFormCode;
    }

    public void setTdsDeductFormCode(String tdsDeductFormCode) {
        this.tdsDeductFormCode = tdsDeductFormCode;
    }

    public String getTdsDeductManualCode() {
        return tdsDeductManualCode;
    }

    public void setTdsDeductManualCode(String tdsDeductManualCode) {
        this.tdsDeductManualCode = tdsDeductManualCode;
    }

    public String getTdsOnAmount() {
        return tdsOnAmount;
    }

    public void setTdsOnAmount(String tdsOnAmount) {
        this.tdsOnAmount = tdsOnAmount;
    }

    public String getOrignalAmount() {
        return orignalAmount;
    }

    public void setOrignalAmount(String orignalAmount) {
        this.orignalAmount = orignalAmount;
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

}
