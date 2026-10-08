package dataman.accounts.acDisplaySetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class AcDisplaySetting_Mdl implements Serializable, IsSerializable {

    private String siteNameList, siteCodeList;
    private String dateFrom, dateTo;
    private String lstDateFrom, lstDateTo;
    private String showZeroValueAc, showNarration, showDetail;
    private String includeOpening;
    private String ledgerAcNameList, ledgerAcCodeList;
    private String closingStock;
    private String yearPrefix, yearPrefix2;

    public AcDisplaySetting_Mdl() {

    }

    public String getSiteNameList() {
        return siteNameList;
    }

    public void setSiteNameList(String siteNameList) {
        this.siteNameList = siteNameList;
    }

    public String getSiteCodeList() {
        return siteCodeList;
    }

    public void setSiteCodeList(String siteCodeList) {
        this.siteCodeList = siteCodeList;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getShowZeroValueAc() {
        return showZeroValueAc;
    }

    public void setShowZeroValueAc(String showZeroValueAc) {
        this.showZeroValueAc = showZeroValueAc;
    }

    public String getShowNarration() {
        return showNarration;
    }

    public void setShowNarration(String showNarration) {
        this.showNarration = showNarration;
    }

    public String getShowDetail() {
        return showDetail;
    }

    public void setShowDetail(String showDetail) {
        this.showDetail = showDetail;
    }

    public String getIncludeOpening() {
        return includeOpening;
    }

    public void setIncludeOpening(String includeOpening) {
        this.includeOpening = includeOpening;
    }

    public String getLedgerAcCodeList() {
        return ledgerAcCodeList;
    }

    public void setLedgerAcCodeList(String ledgerAcCodeList) {
        this.ledgerAcCodeList = ledgerAcCodeList;
    }

    public String getLedgerAcNameList() {
        return ledgerAcNameList;
    }

    public void setLedgerAcNameList(String ledgerAcNameList) {
        this.ledgerAcNameList = ledgerAcNameList;
    }

    public String getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(String closingStock) {
        this.closingStock = closingStock;
    }

    public String getLstDateFrom() {
        return lstDateFrom;
    }

    public void setLstDateFrom(String lstDateFrom) {
        this.lstDateFrom = lstDateFrom;
    }

    public String getLstDateTo() {
        return lstDateTo;
    }

    public void setLstDateTo(String lstDateTo) {
        this.lstDateTo = lstDateTo;
    }

    public String getYearPrefix() {
        return yearPrefix;
    }

    public void setYearPrefix(String yearPrefix) {
        this.yearPrefix = yearPrefix;
    }

    public String getYearPrefix2() {
        return yearPrefix2;
    }

    public void setYearPrefix2(String yearPrefix2) {
        this.yearPrefix2 = yearPrefix2;
    }
    
}
