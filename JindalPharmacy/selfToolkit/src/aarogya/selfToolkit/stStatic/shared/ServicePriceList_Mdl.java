package aarogya.selfToolkit.stStatic.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.RateListType;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ServicePriceList_Mdl implements Serializable, IsSerializable {

    private STEnum.PriceListEntryFrom priceListEntryFrom = STEnum.PriceListEntryFrom.NONE;
    private String entryFromNature;
    private STEnum.PriceListNature priceListNature = STEnum.PriceListNature.None;
    private String date, serviceCode, organizationServiceCode, organizationServiceName, organizationCode, siteCode;
    private String roomCatCode, consultantCode, voucherType, dayNameCode = "0", rateListCode;
    private String time, timeTo;
    private String rate, discAmt, discPercentage;
    private String printSNo;
    private RateListType rateListType = RateListType.FIX;

    public ServicePriceList_Mdl() {
}

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

    public RateListType getRateListType() {
        return rateListType;
    }

    public void setRateListType(RateListType rateListType) {
        this.rateListType = rateListType;
    }
    public String getEntryFromNature() {
        return entryFromNature;
    }

    public void setEntryFromNature(String entryFromNature) {
        this.entryFromNature = entryFromNature;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public STEnum.PriceListEntryFrom getPriceListEntryFrom() {
        return priceListEntryFrom;
    }

    public void setPriceListEntryFrom(STEnum.PriceListEntryFrom priceListEntryFrom) {
        this.priceListEntryFrom = priceListEntryFrom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getOrganizationServiceCode() {
        return organizationServiceCode;
    }

    public void setOrganizationServiceCode(String organizationServiceCode) {
        this.organizationServiceCode = organizationServiceCode;
    }

    public String getOrganizationServiceName() {
        return organizationServiceName;
    }

    public void setOrganizationServiceName(String organizationServiceName) {
        this.organizationServiceName = organizationServiceName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getRoomCatCode() {
        return roomCatCode;
    }

    public void setRoomCatCode(String roomCatCode) {
        this.roomCatCode = roomCatCode;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getDayNameCode() {
        return dayNameCode;
    }

    public void setDayNameCode(String dayNameCode) {
        this.dayNameCode = dayNameCode;
    }

    public STEnum.PriceListNature getPriceListNature() {
        return priceListNature;
    }

    public void setPriceListNature(STEnum.PriceListNature priceListNature) {
        this.priceListNature = priceListNature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }

    public String getDiscPercentage() {
        return discPercentage;
    }

    public void setDiscPercentage(String discPercentage) {
        this.discPercentage = discPercentage;
    }

}
