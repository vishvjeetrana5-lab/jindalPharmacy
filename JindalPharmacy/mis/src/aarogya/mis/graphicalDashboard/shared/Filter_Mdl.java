package aarogya.mis.graphicalDashboard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Filter_Mdl implements Serializable, IsSerializable {

    private String dateFrom;
    private String dateto;
    private String serviceCode;
    private String topCompanyCount;
    private String topItemCount;
    private String paymentMode;
    private String siteCodeOPD;
    private String siteNameOPD;
    private String siteCodeIPD;
    private String siteNameIPD;
    private String siteCodeDiagnostic;
    private String siteNameDiagnostic;
    private String siteCodePharmacy;
    private String siteNamePharmacy;

    public Filter_Mdl() {
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateto() {
        return dateto;
    }

    public void setDateto(String dateto) {
        this.dateto = dateto;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTopCompanyCount() {
        return topCompanyCount;
    }

    public void setTopCompanyCount(String topCompanyCount) {
        this.topCompanyCount = topCompanyCount;
    }

    public String getTopItemCount() {
        return topItemCount;
    }

    public void setTopItemCount(String topItemCount) {
        this.topItemCount = topItemCount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getSiteCodeOPD() {
        return siteCodeOPD;
    }

    public void setSiteCodeOPD(String siteCodeOPD) {
        this.siteCodeOPD = siteCodeOPD;
    }

    public String getSiteCodeIPD() {
        return siteCodeIPD;
    }

    public void setSiteCodeIPD(String siteCodeIPD) {
        this.siteCodeIPD = siteCodeIPD;
    }

    public String getSiteCodeDiagnostic() {
        return siteCodeDiagnostic;
    }

    public void setSiteCodeDiagnostic(String siteCodeDiagnostic) {
        this.siteCodeDiagnostic = siteCodeDiagnostic;
    }

    public String getSiteCodePharmacy() {
        return siteCodePharmacy;
    }

    public void setSiteCodePharmacy(String siteCodePharmacy) {
        this.siteCodePharmacy = siteCodePharmacy;
    }

    public String getSiteNameOPD() {
        return siteNameOPD;
    }

    public void setSiteNameOPD(String siteNameOPD) {
        this.siteNameOPD = siteNameOPD;
    }

    public String getSiteNameIPD() {
        return siteNameIPD;
    }

    public void setSiteNameIPD(String siteNameIPD) {
        this.siteNameIPD = siteNameIPD;
    }

    public String getSiteNameDiagnostic() {
        return siteNameDiagnostic;
    }

    public void setSiteNameDiagnostic(String siteNameDiagnostic) {
        this.siteNameDiagnostic = siteNameDiagnostic;
    }

    public String getSiteNamePharmacy() {
        return siteNamePharmacy;
    }

    public void setSiteNamePharmacy(String siteNamePharmacy) {
        this.siteNamePharmacy = siteNamePharmacy;
    }
}
