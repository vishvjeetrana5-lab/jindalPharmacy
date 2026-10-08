/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.gst.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author DATAMAN
 */
public class ErrorDetails_Mdl implements Serializable, IsSerializable {

    @JsonProperty("ErrorCode")
    public String error_cd;
    @JsonProperty("ErrorMessage")
    public String message;

    public ErrorDetails_Mdl() {
    }

    public String getError_cd() {
        return error_cd;
    }

    public void setError_cd(String error_cd) {
        this.error_cd = error_cd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
