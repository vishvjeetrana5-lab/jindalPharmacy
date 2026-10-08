package dataman.accounts.gstVoucherEntry.shared;

import dataman.accounts.voucherEntry.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountNameGrid_Mdl extends KeyField implements Serializable, IsSerializable {

    private String City;
    private String SCode;
    private String Nature;
    private String Group;
    private String natureManualCode;
    private String stateCode;
    private String stateName;
    private String partyTypeCode;
    private String partyTypeName;
    private String gstin;
    private String gstinDate;

    public TxtHAccountNameGrid_Mdl() {
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getNatureManualCode() {
        return natureManualCode;
    }

    public void setNatureManualCode(String natureManualCode) {
        this.natureManualCode = natureManualCode;
    }

    public String getSCode() {
        return SCode;
    }

    public void setSCode(String SCode) {
        this.SCode = SCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String Nature) {
        this.Nature = Nature;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPartyTypeCode() {
        return partyTypeCode;
    }

    public void setPartyTypeCode(String partyTypeCode) {
        this.partyTypeCode = partyTypeCode;
    }

    public String getPartyTypeName() {
        return partyTypeName;
    }

    public void setPartyTypeName(String partyTypeName) {
        this.partyTypeName = partyTypeName;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
    }

}
