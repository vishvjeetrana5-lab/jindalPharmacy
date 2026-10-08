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
public class MasterEGst_Mdl  implements Serializable, IsSerializable{
    
    private EGst_Mdl eGst_Mdl = new EGst_Mdl();
    private GstDocument_Mdl gst_Mdl = new GstDocument_Mdl();
    private EGstToken_Mdl eGstToken_Mdl = new EGstToken_Mdl();

    public MasterEGst_Mdl() {
    }

    public EGst_Mdl geteGst_Mdl() {
        return eGst_Mdl;
    }

    public void seteGst_Mdl(EGst_Mdl eGst_Mdl) {
        this.eGst_Mdl = eGst_Mdl;
    }

    public EGstToken_Mdl geteGstToken_Mdl() {
        return eGstToken_Mdl;
    }

    public void seteGstToken_Mdl(EGstToken_Mdl eGstToken_Mdl) {
        this.eGstToken_Mdl = eGstToken_Mdl;
    }

    public GstDocument_Mdl getGst_Mdl() {
        return gst_Mdl;
    }

    public void setGst_Mdl(GstDocument_Mdl gst_Mdl) {
        this.gst_Mdl = gst_Mdl;
    }
}
