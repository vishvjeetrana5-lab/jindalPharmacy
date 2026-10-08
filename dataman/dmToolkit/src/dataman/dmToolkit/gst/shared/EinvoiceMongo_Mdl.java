/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author DATAMAN
 */
public class EinvoiceMongo_Mdl implements Serializable, IsSerializable {

    private String mongoLinkId;
    private String docid;
    private String recid;
    private String modulename;
    private String menuname;
    private String ack_no;
    private String ack_no_date;
    private String irn;
    private String extracted_Signed_Invoice_DataJson;
    private String extracted_Signed_QrCodeJson;
    private String signed_e_invoice;
    private String signed_Qr_Code;
    private String signed_Qr_Code_Img;

    public EinvoiceMongo_Mdl() {
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getAck_no() {
        return ack_no;
    }

    public void setAck_no(String ack_no) {
        this.ack_no = ack_no;
    }

    public String getAck_no_date() {
        return ack_no_date;
    }

    public void setAck_no_date(String ack_no_date) {
        this.ack_no_date = ack_no_date;
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

    public String getExtracted_Signed_Invoice_DataJson() {
        return extracted_Signed_Invoice_DataJson;
    }

    public void setExtracted_Signed_Invoice_DataJson(String extracted_Signed_Invoice_DataJson) {
        this.extracted_Signed_Invoice_DataJson = extracted_Signed_Invoice_DataJson;
    }

    public String getExtracted_Signed_QrCodeJson() {
        return extracted_Signed_QrCodeJson;
    }

    public void setExtracted_Signed_QrCodeJson(String extracted_Signed_QrCodeJson) {
        this.extracted_Signed_QrCodeJson = extracted_Signed_QrCodeJson;
    }

    public String getSigned_e_invoice() {
        return signed_e_invoice;
    }

    public void setSigned_e_invoice(String signed_e_invoice) {
        this.signed_e_invoice = signed_e_invoice;
    }

    public String getSigned_Qr_Code() {
        return signed_Qr_Code;
    }

    public void setSigned_Qr_Code(String signed_Qr_Code) {
        this.signed_Qr_Code = signed_Qr_Code;
    }

    public String getSigned_Qr_Code_Img() {
        return signed_Qr_Code_Img;
    }

    public void setSigned_Qr_Code_Img(String signed_Qr_Code_Img) {
        this.signed_Qr_Code_Img = signed_Qr_Code_Img;
    }

    public String getMongoLinkId() {
        return mongoLinkId;
    }

    public void setMongoLinkId(String mongoLinkId) {
        this.mongoLinkId = mongoLinkId;
    }

}
