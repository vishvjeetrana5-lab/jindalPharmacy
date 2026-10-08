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
public class Error_Mdl implements Serializable, IsSerializable {

    public String status_cd;
    public ErrorDetails_Mdl error;

    public Error_Mdl() {
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public ErrorDetails_Mdl getError() {
        return error;
    }

    public void setError(ErrorDetails_Mdl error) {
        this.error = error;
    }

}
