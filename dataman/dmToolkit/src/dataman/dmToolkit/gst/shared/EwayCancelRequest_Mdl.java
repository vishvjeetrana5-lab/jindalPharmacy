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

public class EwayCancelRequest_Mdl implements Serializable, IsSerializable  {

    private String ewbNo;
    private String cancelRsnCode;
    private String cancelRmrk;

    public EwayCancelRequest_Mdl() {
    }
    

    public String getEwbNo() {
        return ewbNo;
    }

    public void setEwbNo(String ewbNo) {
        this.ewbNo = ewbNo;
    }

    public String getCancelRsnCode() {
        return cancelRsnCode;
    }

    public void setCancelRsnCode(String cancelRsnCode) {
        this.cancelRsnCode = cancelRsnCode;
    }

    public String getCancelRmrk() {
        return cancelRmrk;
    }

    public void setCancelRmrk(String cancelRmrk) {
        this.cancelRmrk = cancelRmrk;
    }

}
