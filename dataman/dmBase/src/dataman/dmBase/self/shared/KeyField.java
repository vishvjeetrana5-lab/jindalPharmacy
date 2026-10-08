package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.Date;

public class KeyField implements Serializable, IsSerializable {

    private String sharedKeyFieldCode;
    private String sharedKeyFieldName;
    private String sharedPreparedBy;
    private String sharedModifiedBy;
    private String sharedPreparedIP;
    private String sharedPreparedDate;
    private String sharedModifyDate;
    private String sharedCancelledBy;
    private String sharedCancelledDate;
    
    public KeyField() {
    }

    public KeyField(String sharedKeyFieldCode, String sharedKeyFieldName) {
        this.sharedKeyFieldCode = sharedKeyFieldCode;
        this.sharedKeyFieldName = sharedKeyFieldName;
    }

    public KeyField(String sharedKeyFieldCode, String sharedKeyFieldName, String sharedPreparedBy, String sharedModifiedBy) {
        this.sharedKeyFieldCode = sharedKeyFieldCode;
        this.sharedKeyFieldName = sharedKeyFieldName;
        this.sharedPreparedBy = sharedPreparedBy;
        this.sharedModifiedBy = sharedModifiedBy;
    }

    public KeyField(String sharedKeyFieldCode, String sharedKeyFieldName, String sharedPreparedBy, String sharedModifiedBy, String sharedPreparedIP, String sharedPreparedDate, String sharedModifyDate) {
        this.sharedKeyFieldCode = sharedKeyFieldCode;
        this.sharedKeyFieldName = sharedKeyFieldName;
        this.sharedPreparedBy = sharedPreparedBy;
        this.sharedModifiedBy = sharedModifiedBy;
        this.sharedPreparedIP = sharedPreparedIP;
        this.sharedPreparedDate = sharedPreparedDate;
        this.sharedModifyDate = sharedModifyDate;
    }

    public KeyField(String sharedKeyFieldCode, String sharedKeyFieldName, String sharedPreparedBy, String sharedModifiedBy, String sharedPreparedIP, String sharedPreparedDate, String sharedModifyDate, String sharedCancelledBy, String sharedCancelledDate) {
        this.sharedKeyFieldCode = sharedKeyFieldCode;
        this.sharedKeyFieldName = sharedKeyFieldName;
        this.sharedPreparedBy = sharedPreparedBy;
        this.sharedModifiedBy = sharedModifiedBy;
        this.sharedPreparedIP = sharedPreparedIP;
        this.sharedPreparedDate = sharedPreparedDate;
        this.sharedModifyDate = sharedModifyDate;
        this.sharedCancelledBy = sharedCancelledBy;
        this.sharedCancelledDate = sharedCancelledDate;
    }

    public String getSharedPreparedBy() {
        return sharedPreparedBy;
    }

    public void setSharedPreparedBy(String sharedPreparedBy) {
        this.sharedPreparedBy = sharedPreparedBy;
    }

    public String getSharedModifiedBy() {
        return sharedModifiedBy;
    }

    public void setSharedModifiedBy(String sharedModifiedBy) {
        this.sharedModifiedBy = sharedModifiedBy;
    }

    public int getSharedKeyFieldHashCode() {
        return this.hashCode();
    }

    public String getSharedKeyFieldCode() {
        return sharedKeyFieldCode;
    }

    public void setSharedKeyFieldCode(String sharedKeyFieldCode) {
        this.sharedKeyFieldCode = sharedKeyFieldCode;
    }

    public String getSharedKeyFieldName() {
        return sharedKeyFieldName;
    }

    public void setSharedKeyFieldName(String sharedKeyFieldName) {
        this.sharedKeyFieldName = sharedKeyFieldName;
    }

    public String getSharedPreparedIP() {
        return sharedPreparedIP;
    }

    public void setSharedPreparedIP(String sharedPreparedIP) {
        this.sharedPreparedIP = sharedPreparedIP;
    }

    public String getSharedPreparedDate() {
        return sharedPreparedDate;
    }

    public void setSharedPreparedDate(String sharedPreparedDate) {
        this.sharedPreparedDate = sharedPreparedDate;
    }

    public String getSharedModifyDate() {
        return sharedModifyDate;
    }

    public void setSharedModifyDate(String sharedModifyDate) {
        this.sharedModifyDate = sharedModifyDate;
    }

    public String getSharedCancelledBy() {
        return sharedCancelledBy;
    }

    public void setSharedCancelledBy(String sharedCancelledBy) {
        this.sharedCancelledBy = sharedCancelledBy;
    }

    public String getSharedCancelledDate() {
        return sharedCancelledDate;
    }

    public void setSharedCancelledDate(String sharedCancelledDate) {
        this.sharedCancelledDate = sharedCancelledDate;
    }

    
    
    public void copy(KeyField keyField) {
        keyField.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        keyField.setSharedKeyFieldName(this.getSharedKeyFieldName());
        keyField.setSharedPreparedBy(this.getSharedPreparedBy());
        keyField.setSharedModifiedBy(this.getSharedModifiedBy());
        keyField.setSharedModifyDate(this.getSharedModifyDate());
        keyField.setSharedPreparedDate(this.getSharedPreparedDate());
        keyField.setSharedPreparedIP(this.getSharedPreparedIP());
        keyField.setSharedCancelledBy(this.getSharedCancelledBy());
        keyField.setSharedCancelledDate(this.getSharedCancelledDate());
    }
}
