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
public class EinvAdditionalDocDtls_Mdl implements Serializable, IsSerializable {

    @JsonProperty("Url")
    private String documentUrl;
    @JsonProperty("Docs")
    private String document;
    @JsonProperty("Info")
    private String additionalInfoOfDocument;

    private String docid;
    private String vSno;

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAdditionalInfoOfDocument() {
        return additionalInfoOfDocument;
    }

    public void setAdditionalInfoOfDocument(String additionalInfoOfDocument) {
        this.additionalInfoOfDocument = additionalInfoOfDocument;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

}
