package aarogya.pharmacy.partySearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PartySearch_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyfieldCode=partyCode
    //sharedKeyfieldName=partyName
    private String cityName;
    private String cityCode;
    private String manualCode;
    private String address;
    private String mobile;
    private String stateCode;
    private String stateName;
    private String partTypeCode;
    private String partTypeName;
    private String gstinNo;
    private String gstinDate;
    private String lmCode;
    private String lmName;
    private String lmManualCode;
    private String lmIdTypeCode;
    private String lmIdTypeName;
    private String lmIdNo;

    public PartySearch_Mdl() {
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getLmManualCode() {
        return lmManualCode;
    }

    public void setLmManualCode(String lmManualCode) {
        this.lmManualCode = lmManualCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
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

    public String getPartTypeCode() {
        return partTypeCode;
    }

    public void setPartTypeCode(String partTypeCode) {
        this.partTypeCode = partTypeCode;
    }

    public String getPartTypeName() {
        return partTypeName;
    }

    public void setPartTypeName(String partTypeName) {
        this.partTypeName = partTypeName;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getLmIdTypeCode() {
        return lmIdTypeCode;
    }

    public void setLmIdTypeCode(String lmIdTypeCode) {
        this.lmIdTypeCode = lmIdTypeCode;
    }

    public String getLmIdTypeName() {
        return lmIdTypeName;
    }

    public void setLmIdTypeName(String lmIdTypeName) {
        this.lmIdTypeName = lmIdTypeName;
    }

    public String getLmIdNo() {
        return lmIdNo;
    }

    public void setLmIdNo(String lmIdNo) {
        this.lmIdNo = lmIdNo;
    }

}
