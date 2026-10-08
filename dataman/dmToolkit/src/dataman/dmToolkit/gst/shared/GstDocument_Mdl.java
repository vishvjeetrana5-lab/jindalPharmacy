/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
public class GstDocument_Mdl implements Serializable, IsSerializable {

    private String docid;
    private String gstDocTypeCode;
    private String gstDocTypeName;
    private String gstDocNatureCode;
    private String gstDocNatureName;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getGstDocTypeCode() {
        return gstDocTypeCode;
    }

    public void setGstDocTypeCode(String gstDocTypeCode) {
        this.gstDocTypeCode = gstDocTypeCode;
    }

    public String getGstDocTypeName() {
        return gstDocTypeName;
    }

    public void setGstDocTypeName(String gstDocTypeName) {
        this.gstDocTypeName = gstDocTypeName;
    }

    public String getGstDocNatureCode() {
        return gstDocNatureCode;
    }

    public void setGstDocNatureCode(String gstDocNatureCode) {
        this.gstDocNatureCode = gstDocNatureCode;
    }

    public String getGstDocNatureName() {
        return gstDocNatureName;
    }

    public void setGstDocNatureName(String gstDocNatureName) {
        this.gstDocNatureName = gstDocNatureName;
    }

}
