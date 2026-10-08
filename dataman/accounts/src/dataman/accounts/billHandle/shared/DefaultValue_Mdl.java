package dataman.accounts.billHandle.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String billHandleTypeCode;
    private String billHandleTypeName;

    private String currentDate;
    private String currentTime;

    public DefaultValue_Mdl() {
    }

    public String getBillHandleTypeCode() {
        return billHandleTypeCode;
    }

    public void setBillHandleTypeCode(String billHandleTypeCode) {
        this.billHandleTypeCode = billHandleTypeCode;
    }

    public String getBillHandleTypeName() {
        return billHandleTypeName;
    }

    public void setBillHandleTypeName(String billHandleTypeName) {
        this.billHandleTypeName = billHandleTypeName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

}
