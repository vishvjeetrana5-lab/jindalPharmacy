package aarogya.selfToolkit.taxSummary.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TaxSummary_Mdl extends KeyField implements Serializable, IsSerializable {

    private String rate;
    private String cgstAmt;
    private String sgstAmt;
    private String ugstAmt;
    private String sgstOrUgstAmt;
    private String igstAmt;
    private String taxableAmt;
    private String totalAmt;

    public TaxSummary_Mdl() {
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getUgstAmt() {
        return ugstAmt;
    }

    public void setUgstAmt(String ugstAmt) {
        this.ugstAmt = ugstAmt;
    }

    public String getSgstOrUgstAmt() {
        return sgstOrUgstAmt;
    }

    public void setSgstOrUgstAmt(String sgstOrUgstAmt) {
        this.sgstOrUgstAmt = sgstOrUgstAmt;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

}
