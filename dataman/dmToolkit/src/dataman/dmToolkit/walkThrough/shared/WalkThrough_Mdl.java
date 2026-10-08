package dataman.dmToolkit.walkThrough.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class WalkThrough_Mdl implements Serializable, IsSerializable {

    private String wtaCode;
    private String wtaV_SNo;
    private String wtfTimeStamp;
    private String authority;
    private String remark;
    private String linkedUser;
    private String authorisedBy;
    private String authorisedDateTime;
    private String status;
    private String image;
    private boolean accept;

    public WalkThrough_Mdl(){
        
    }
    
    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getWtaV_SNo() {
        return wtaV_SNo;
    }

    public void setWtaV_SNo(String wtaV_SNo) {
        this.wtaV_SNo = wtaV_SNo;
    }

    public String getWtfTimeStamp() {
        return wtfTimeStamp;
    }

    public void setWtfTimeStamp(String wtfTimeStamp) {
        this.wtfTimeStamp = wtfTimeStamp;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLinkedUser() {
        return linkedUser;
    }

    public void setLinkedUser(String linkedUser) {
        this.linkedUser = linkedUser;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public String getAuthorisedDateTime() {
        return authorisedDateTime;
    }

    public void setAuthorisedDateTime(String authorisedDateTime) {
        this.authorisedDateTime = authorisedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public int getWTAHashCode() {
        return this.hashCode();
    }
}
