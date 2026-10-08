package dataman.utility.gstStructure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GstStructure_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vSNo;
    private String locationCode;
    private String locationName;
    private String gstTypeCode;
    private String gstTypeName;
    private String gstHeadCode;
    private String gstHeadName;
    private String gstTypeSNo;
    private String gstHeadSNo;
    private String amount;
    private String purchaseSalesAc;
    private String purchaseSalesAcName;
    private String inputTaxAc;
    private String inputTaxAcName;
    private String outputTaxAc;
    private String outputTaxAcName;
    private String rcmAc;
    private String rcmAcName;
    private String isTaxHead;   
    private List<GstStructure_Mdl> grdData = new ArrayList<GstStructure_Mdl>();

    public GstStructure_Mdl() {
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getGstHeadCode() {
        return gstHeadCode;
    }

    public void setGstHeadCode(String gstHeadCode) {
        this.gstHeadCode = gstHeadCode;
    }

    public String getGstHeadName() {
        return gstHeadName;
    }

    public void setGstHeadName(String gstHeadName) {
        this.gstHeadName = gstHeadName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPurchaseSalesAc() {
        return purchaseSalesAc;
    }

    public void setPurchaseSalesAc(String purchaseSalesAc) {
        this.purchaseSalesAc = purchaseSalesAc;
    }

    public String getPurchaseSalesAcName() {
        return purchaseSalesAcName;
    }

    public void setPurchaseSalesAcName(String purchaseSalesAcName) {
        this.purchaseSalesAcName = purchaseSalesAcName;
    }

    public String getInputTaxAc() {
        return inputTaxAc;
    }

    public void setInputTaxAc(String inputTaxAc) {
        this.inputTaxAc = inputTaxAc;
    }

    public String getInputTaxAcName() {
        return inputTaxAcName;
    }

    public void setInputTaxAcName(String inputTaxAcName) {
        this.inputTaxAcName = inputTaxAcName;
    }

    public String getOutputTaxAc() {
        return outputTaxAc;
    }

    public void setOutputTaxAc(String outputTaxAc) {
        this.outputTaxAc = outputTaxAc;
    }

    public String getOutputTaxAcName() {
        return outputTaxAcName;
    }

    public void setOutputTaxAcName(String outputTaxAcName) {
        this.outputTaxAcName = outputTaxAcName;
    }

    public String getRcmAc() {
        return rcmAc;
    }

    public void setRcmAc(String rcmAc) {
        this.rcmAc = rcmAc;
    }

    public String getRcmAcName() {
        return rcmAcName;
    }

    public void setRcmAcName(String rcmAcName) {
        this.rcmAcName = rcmAcName;
    }

    public List<GstStructure_Mdl> getGrdData() {
        return grdData;
    }

    public void setGrdData(List<GstStructure_Mdl> grdData) {
        this.grdData = grdData;
    }

    public String getIsTaxHead() {
        return isTaxHead;
    }

    public void setIsTaxHead(String isTaxHead) {
        this.isTaxHead = isTaxHead;
    }

    public String getGstTypeSNo() {
        return gstTypeSNo;
    }

    public void setGstTypeSNo(String gstTypeSNo) {
        this.gstTypeSNo = gstTypeSNo;
    }

    public String getGstHeadSNo() {
        return gstHeadSNo;
    }

    public void setGstHeadSNo(String gstHeadSNo) {
        this.gstHeadSNo = gstHeadSNo;
    }

    
}
