package dataman.utility.userPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class RoomChangeEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ipdDocId;
    private String allotedDate;
    private String allotedTime;
    private String status;
    private String colorStatus;

    public RoomChangeEntry_Mdl() {
    }

    public String getIpdDocId() {
        return ipdDocId;
    }

    public void setIpdDocId(String ipdDocId) {
        this.ipdDocId = ipdDocId;
    }

    public String getAllotedDate() {
        return allotedDate;
    }

    public void setAllotedDate(String allotedDate) {
        this.allotedDate = allotedDate;
    }

    public String getAllotedTime() {
        return allotedTime;
    }

    public void setAllotedTime(String allotedTime) {
        this.allotedTime = allotedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColorStatus() {
        return colorStatus;
    }

    public void setColorStatus(String colorStatus) {
        this.colorStatus = colorStatus;
    }

}
