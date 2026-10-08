/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;


public class EInvData_Mdl implements Serializable, IsSerializable {

    @JsonProperty("AckNo")
    private String ackNo;
    @JsonProperty("AckDt")
    private String ackDt;
    @JsonProperty("Irn")
    private String irn;
    @JsonProperty("SignedInvoice")
    private String signedInvoice;
    @JsonProperty("SignedQRCode")
    private String signedQRCode;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("EwbNo")
    private String ewbNo;
    @JsonProperty("EwbDt")
    private String ewbDt;
    @JsonProperty("EwbValidTill")
    private String ewbValidTill;
    @JsonProperty("ExtractedSignedInvoiceData")
    private ExtractedSignedInvoiceData_Mdl extractedSignedInvoiceData;
    @JsonProperty("ExtractedSignedQrCode")
    private ExtractedSignedQrCode_Mdl extractedSignedQrCode;
    @JsonProperty("QrCodeImage")
    private String qrCodeImage;
    @JsonProperty("JwtIssuer")
    private String jwtIssuer;    

    public EInvData_Mdl() {
    }

    
    public String getAckNo() {
        return ackNo;
    }

    public void setAckNo(String ackNo) {
        this.ackNo = ackNo;
    }

    public String getAckDt() {
        return ackDt;
    }

    public void setAckDt(String ackDt) {
        this.ackDt = ackDt;
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

    public String getSignedInvoice() {
        return signedInvoice;
    }

    public void setSignedInvoice(String signedInvoice) {
        this.signedInvoice = signedInvoice;
    }

    public String getSignedQRCode() {
        return signedQRCode;
    }

    public void setSignedQRCode(String signedQRCode) {
        this.signedQRCode = signedQRCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEwbNo() {
        return ewbNo;
    }

    public void setEwbNo(String ewbNo) {
        this.ewbNo = ewbNo;
    }

    public String getEwbDt() {
        return ewbDt;
    }

    public void setEwbDt(String ewbDt) {
        this.ewbDt = ewbDt;
    }

    public String getEwbValidTill() {
        return ewbValidTill;
    }

    public void setEwbValidTill(String ewbValidTill) {
        this.ewbValidTill = ewbValidTill;
    }

    public ExtractedSignedInvoiceData_Mdl getExtractedSignedInvoiceData() {
        return extractedSignedInvoiceData;
    }

    public void setExtractedSignedInvoiceData(ExtractedSignedInvoiceData_Mdl extractedSignedInvoiceData) {
        this.extractedSignedInvoiceData = extractedSignedInvoiceData;
    }

    public ExtractedSignedQrCode_Mdl getExtractedSignedQrCode() {
        return extractedSignedQrCode;
    }

    public void setExtractedSignedQrCode(ExtractedSignedQrCode_Mdl extractedSignedQrCode) {
        this.extractedSignedQrCode = extractedSignedQrCode;
    }

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

}
