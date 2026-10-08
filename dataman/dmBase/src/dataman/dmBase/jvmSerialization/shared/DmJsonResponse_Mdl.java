package dataman.dmBase.jvmSerialization.shared;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmJsonResponse_Mdl<T> implements Serializable,IsSerializable{
    
    @SerializedName(value = "status")
    private boolean status = false;
    
    @SerializedName(value = "value1")
    private String value1 = "";
    
    @SerializedName(value = "value2")
    private String value2 = "";
    
    @SerializedName(value = "errorMessage")
    private String errorMessage = "";
    
    @SerializedName(value = "errorNumber")
    private int errorNumber = 0;
    
    @SerializedName(value = "data")
    private T data;

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
