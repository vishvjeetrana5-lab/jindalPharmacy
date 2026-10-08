package aarogya.inventory.visitorGatePassSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdVisitorGatePassDetailsSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String time;
    private String visitorName;
    private String address;
    private String mobile;
    private String vechileType;
    private String vechileNo;
    private String personToMeet;
    private String gateOutDate;
    private String gateOutTime;
    private String purpose;
    private String cityName;
    private String entryNo;
  private String isChecked;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVechileType() {
        return vechileType;
    }

    public void setVechileType(String vechileType) {
        this.vechileType = vechileType;
    }

    public String getVechileNo() {
        return vechileNo;
    }

    public void setVechileNo(String vechileNo) {
        this.vechileNo = vechileNo;
    }

    public String getPersonToMeet() {
        return personToMeet;
    }

    public void setPersonToMeet(String personToMeet) {
        this.personToMeet = personToMeet;
    }

    public String getGateOutDate() {
        return gateOutDate;
    }

    public void setGateOutDate(String gateOutDate) {
        this.gateOutDate = gateOutDate;
    }

    public String getGateOutTime() {
        return gateOutTime;
    }

    public void setGateOutTime(String gateOutTime) {
        this.gateOutTime = gateOutTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
