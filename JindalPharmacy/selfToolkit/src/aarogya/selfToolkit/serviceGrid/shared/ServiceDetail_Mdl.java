package aarogya.selfToolkit.serviceGrid.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.List;

public class ServiceDetail_Mdl extends KeyField implements Serializable, IsSerializable {
    //Service
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    //Item Details    
    private String isubCode;
    private String sgstOrUgstAmt;
    private String sgstAmt;
    private String ugstAmt;
    private String cgstAmt;
    private String igstAmt;
    private String taxableAmt;
    private String subTotal;
    private String netAmt;
    private String amount;
    private String gstRate;

    private String sgstOrUgstRate;
    private String sgstRate;
    private String ugstRate;
    private String cgstRate;
    private String igstRate;

    private String sgstAcCode;
    private String ugstAcCode;
    private String cgstAcCode;
    private String igstAcCode;

    private String sgstRcmAcCode;
    private String ugstRcmAcCode;
    private String cgstRcmAcCode;
    private String igstRcmAcCode;

    private boolean manualTax;
    private boolean manualGstType;
    private String gstTypeCode;
    private String gstTypeName;

    private String itsBasedOn;
    private String itemGSTNature;
    private String gstUnitCode;

    private String unitCode;
    private String hsnCode;
    private String hsn;
    private String v_Sno;
    private String quantity;
    private String rate;

    //GST
    private List<OverHeadITS_Mdl> lstITS;

    public ServiceDetail_Mdl() {
    }

    public String getSmfCode() {
        return smfCode;
    }

    public void setSmfCode(String smfCode) {
        this.smfCode = smfCode;
    }

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
    }

    
    public String getSgstOrUgstAmt() {
        return sgstOrUgstAmt;
    }

    public void setSgstOrUgstAmt(String sgstOrUgstAmt) {
        this.sgstOrUgstAmt = sgstOrUgstAmt;
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



    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
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

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public boolean isManualTax() {
        return manualTax;
    }

    public void setManualTax(boolean manualTax) {
        this.manualTax = manualTax;
    }
    
    public boolean isManualGstType() {
        return manualGstType;
    }

    public void setManualGstType(boolean manualGstType) {
        this.manualGstType = manualGstType;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    
    public String getV_Sno() {
        return v_Sno;
    }

    public void setV_Sno(String v_Sno) {
        this.v_Sno = v_Sno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(String cgstRate) {
        this.cgstRate = cgstRate;
    }

    public String getSgstOrUgstRate() {
        return sgstOrUgstRate;
    }

    public void setSgstOrUgstRate(String sgstOrUgstRate) {
        this.sgstOrUgstRate = sgstOrUgstRate;
    }

    public String getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(String igstRate) {
        this.igstRate = igstRate;
    }

    public String getCgstAcCode() {
        return cgstAcCode;
    }

    public void setCgstAcCode(String cgstAcCode) {
        this.cgstAcCode = cgstAcCode;
    }

    public String getIgstAcCode() {
        return igstAcCode;
    }

    public void setIgstAcCode(String igstAcCode) {
        this.igstAcCode = igstAcCode;
    }

    public String getCgstRcmAcCode() {
        return cgstRcmAcCode;
    }

    public void setCgstRcmAcCode(String cgstRcmAcCode) {
        this.cgstRcmAcCode = cgstRcmAcCode;
    }

    public String getSgstRcmAcCode() {
        return sgstRcmAcCode;
    }

    public void setSgstRcmAcCode(String sgstRcmAcCode) {
        this.sgstRcmAcCode = sgstRcmAcCode;
    }

    public String getUgstRcmAcCode() {
        return ugstRcmAcCode;
    }

    public void setUgstRcmAcCode(String ugstRcmAcCode) {
        this.ugstRcmAcCode = ugstRcmAcCode;
    }

    public String getIgstRcmAcCode() {
        return igstRcmAcCode;
    }

    public void setIgstRcmAcCode(String igstRcmAcCode) {
        this.igstRcmAcCode = igstRcmAcCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGstRate() {
        return gstRate;
    }

    public void setGstRate(String gstRate) {
        this.gstRate = gstRate;
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getServiceEvalTypeCode() {
        return serviceEvalTypeCode;
    }

    public void setServiceEvalTypeCode(String serviceEvalTypeCode) {
        this.serviceEvalTypeCode = serviceEvalTypeCode;
    }

    public String getServiceEvalTypeName() {
        return serviceEvalTypeName;
    }

    public void setServiceEvalTypeName(String serviceEvalTypeName) {
        this.serviceEvalTypeName = serviceEvalTypeName;
    }

    
    public String getServiceEvalRate() {
        return serviceEvalRate;
    }

    public void setServiceEvalRate(String serviceEvalRate) {
        this.serviceEvalRate = serviceEvalRate;
    }

    public String getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(String sgstRate) {
        this.sgstRate = sgstRate;
    }

    public String getUgstRate() {
        return ugstRate;
    }

    public void setUgstRate(String ugstRate) {
        this.ugstRate = ugstRate;
    }

    public String getSgstAcCode() {
        return sgstAcCode;
    }

    public void setSgstAcCode(String sgstAcCode) {
        this.sgstAcCode = sgstAcCode;
    }

    public String getUgstAcCode() {
        return ugstAcCode;
    }

    public void setUgstAcCode(String ugstAcCode) {
        this.ugstAcCode = ugstAcCode;
    }

      
    public ServiceDetail_Mdl clone() {
        ServiceDetail_Mdl grd_Mdl = new ServiceDetail_Mdl();

        copy(grd_Mdl);

        return grd_Mdl;
    }

    public void copy(ServiceDetail_Mdl grdService_Mdl) {
        //Item
        grdService_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdService_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());

        //Service        
        grdService_Mdl.setSmfCode(this.getSmfCode());
        grdService_Mdl.setServiceEvalTypeCode(this.getServiceEvalTypeCode());
        grdService_Mdl.setServiceEvalTypeName(this.getServiceEvalTypeName());
        grdService_Mdl.setServiceEvalRate(this.getServiceEvalRate());

        //Item Details        
        grdService_Mdl.setIsubCode(this.getIsubCode());
        grdService_Mdl.setItsBasedOn(this.getItsBasedOn());
        grdService_Mdl.setHsnCode(this.getHsnCode());
        grdService_Mdl.setHsn(this.getHsn());
        grdService_Mdl.setManualTax(this.isManualTax());
        grdService_Mdl.setManualGstType(this.isManualGstType());
        grdService_Mdl.setGstTypeCode(this.getGstTypeCode());
        grdService_Mdl.setGstTypeName(this.getGstTypeName());

        grdService_Mdl.setCgstAmt(this.getCgstAmt());
        grdService_Mdl.setSgstOrUgstAmt(this.getSgstOrUgstAmt());
        grdService_Mdl.setIgstAmt(this.getIgstAmt());
        grdService_Mdl.setTaxableAmt(this.getTaxableAmt());
        grdService_Mdl.setSubTotal(this.getSubTotal());
        grdService_Mdl.setNetAmt(this.getNetAmt());
        grdService_Mdl.setAmount(this.getAmount());

        grdService_Mdl.setSgstOrUgstRate(this.getSgstOrUgstRate());
        grdService_Mdl.setSgstRate(this.getSgstRate());
        grdService_Mdl.setUgstRate(this.getUgstRate());
        grdService_Mdl.setCgstRate(this.getCgstRate());
        grdService_Mdl.setIgstRate(this.getIgstRate());

        grdService_Mdl.setSgstAcCode(this.getSgstAcCode());
        grdService_Mdl.setUgstAcCode(this.getUgstAcCode());
        grdService_Mdl.setCgstAcCode(this.getCgstAcCode());
        grdService_Mdl.setIgstAcCode(this.getIgstAcCode());
        grdService_Mdl.setSgstRcmAcCode(this.getSgstRcmAcCode());
        grdService_Mdl.setUgstRcmAcCode(this.getUgstRcmAcCode());
        grdService_Mdl.setCgstRcmAcCode(this.getCgstRcmAcCode());
        grdService_Mdl.setIgstRcmAcCode(this.getIgstRcmAcCode());
        grdService_Mdl.setV_Sno(this.getV_Sno());
        grdService_Mdl.setQuantity(this.getQuantity());
        grdService_Mdl.setRate(this.getRate());
        grdService_Mdl.setGstRate(this.getGstRate());
        grdService_Mdl.setItemGSTNature(this.getItemGSTNature());
        grdService_Mdl.setGstUnitCode(this.getGstUnitCode());
        grdService_Mdl.setUnitCode(this.getUnitCode());

        //GST
        grdService_Mdl.setLstITS(OverHeadITS_Mdl.getITSListClone(this.getLstITS()));

    }
}
