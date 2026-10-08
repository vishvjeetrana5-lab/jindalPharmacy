package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Party_Mdl extends KeyField implements Serializable, IsSerializable {

    @JsonProperty("Gstin")
    private String gstin;
    @JsonProperty("LglNm")
    private String legalName;
    @JsonProperty("TrdNm")
    private String tradeName;
    @JsonProperty("Addr1")
    private String address1;
    @JsonProperty("Addr2")
    private String address2;
    @JsonProperty("Pin")
    private String pinCode;
    @JsonProperty("Stcd")
    private String stateNumericCode;
    @JsonProperty("Loc")
    private String cityName;
    @JsonProperty("Pos")
    private String placeOfSupplyCode;
    @JsonProperty("Ph")
    private String phone;
    @JsonProperty("Em")
    private String email;
    @JsonProperty("Nm")
    private String name;

    private String cityCode;
    private String siteCode;
    private String stateName;
    private String gstinDate;
    private String transporterId;
    private String gstCountryCode;
    private String countryName;

    public Party_Mdl() {
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNumericCode() {
        return stateNumericCode;
    }

    public void setStateNumericCode(String stateNumericCode) {
        this.stateNumericCode = stateNumericCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlaceOfSupplyCode() {
        return placeOfSupplyCode;
    }

    public void setPlaceOfSupplyCode(String placeOfSupplyCode) {
        this.placeOfSupplyCode = placeOfSupplyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(String transporterId) {
        this.transporterId = transporterId;
    }

    public String getGstCountryCode() {
        return gstCountryCode;
    }

    public void setGstCountryCode(String gstCountryCode) {
        this.gstCountryCode = gstCountryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Party_Mdl clone() {
        Party_Mdl local = new Party_Mdl();
        copy(local);
        return local;
    }

    public Party_Mdl copy(Party_Mdl local) {
        super.copy(local);
        local.setGstin(this.getGstin());
        local.setLegalName(this.getLegalName());
        local.setTradeName(this.getTradeName());
        local.setAddress1(this.getAddress1());
        local.setAddress2(this.getAddress2());
        local.setPinCode(this.getPinCode());
        local.setCityCode(this.getCityCode());
        local.setCityName(this.getCityName());
        local.setPlaceOfSupplyCode(this.getPlaceOfSupplyCode());
        local.setPhone(this.getPhone());
        local.setEmail(this.getEmail());
        local.setName(this.getName());
        local.setStateName(this.getStateName());
        local.setStateNumericCode(this.getStateNumericCode());
        local.setGstinDate(this.getGstinDate());
        local.setTransporterId(this.getTransporterId());
        local.setGstCountryCode(this.getGstCountryCode());
        local.setCountryName(this.getCountryName());
        local.setSiteCode(this.getSiteCode());
        return local;
    }

}
