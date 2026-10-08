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
 * @author Ashish
 */
public class EInvEwbDtlsResponse_Mdl implements Serializable, IsSerializable{
    @JsonProperty("TransId") 
    public Object transId;
    @JsonProperty("TransName") 
    public String transName;
    @JsonProperty("TransMode") 
    public String transMode;
    @JsonProperty("Distance") 
    public int distance;
    @JsonProperty("TransDocNo") 
    public String transDocNo;
    @JsonProperty("TransDocDt") 
    public String transDocDt;
    @JsonProperty("VehNo") 
    public String vehNo;
    @JsonProperty("VehType") 
    public String vehType;   

    public EInvEwbDtlsResponse_Mdl() {
    }

    public Object getTransId() {
        return transId;
    }

    public void setTransId(Object transId) {
        this.transId = transId;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTransDocNo() {
        return transDocNo;
    }

    public void setTransDocNo(String transDocNo) {
        this.transDocNo = transDocNo;
    }

    public String getTransDocDt() {
        return transDocDt;
    }

    public void setTransDocDt(String transDocDt) {
        this.transDocDt = transDocDt;
    }

    public String getVehNo() {
        return vehNo;
    }

    public void setVehNo(String vehNo) {
        this.vehNo = vehNo;
    }

    public String getVehType() {
        return vehType;
    }

    public void setVehType(String vehType) {
        this.vehType = vehType;
    }
    
    
}
