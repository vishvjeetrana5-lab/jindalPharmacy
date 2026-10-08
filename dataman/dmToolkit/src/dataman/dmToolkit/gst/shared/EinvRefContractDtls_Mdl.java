/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
public class EinvRefContractDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("RecAdvDt")
    private String receiptAdviceDate;
    @JsonProperty("RecAdvRefr")
    private String receiptAdviceNo;
    @JsonProperty("TendRefr")
    private String tenderRefNo;
    @JsonProperty("ContrRefr")
    private String contractNo;
    @JsonProperty("ExtRefr")
    private String extraRefNo;
    @JsonProperty("ProjRefr")
    private String projectRefNo;
    @JsonProperty("PORefr")
    private String poRefNo;
    @JsonProperty("PORefDt")
    private String poRefDate;
    
    private String docid;
    private String vSNo;

    public EinvRefContractDtls_Mdl() {
    }

    public String getReceiptAdviceDate() {
        return receiptAdviceDate;
    }

    public void setReceiptAdviceDate(String receiptAdviceDate) {
        this.receiptAdviceDate = receiptAdviceDate;
    }

    public String getReceiptAdviceNo() {
        return receiptAdviceNo;
    }

    public void setReceiptAdviceNo(String receiptAdviceNo) {
        this.receiptAdviceNo = receiptAdviceNo;
    }

    public String getTenderRefNo() {
        return tenderRefNo;
    }

    public void setTenderRefNo(String tenderRefNo) {
        this.tenderRefNo = tenderRefNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getExtraRefNo() {
        return extraRefNo;
    }

    public void setExtraRefNo(String extraRefNo) {
        this.extraRefNo = extraRefNo;
    }

    public String getProjectRefNo() {
        return projectRefNo;
    }

    public void setProjectRefNo(String projectRefNo) {
        this.projectRefNo = projectRefNo;
    }

    public String getPoRefNo() {
        return poRefNo;
    }

    public void setPoRefNo(String poRefNo) {
        this.poRefNo = poRefNo;
    }

    public String getPoRefDate() {
        return poRefDate;
    }

    public void setPoRefDate(String poRefDate) {
        this.poRefDate = poRefDate;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

}
