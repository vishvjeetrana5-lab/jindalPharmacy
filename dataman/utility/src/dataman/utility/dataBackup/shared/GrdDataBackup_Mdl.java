package dataman.utility.dataBackup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdDataBackup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String elementType, elementTypeCode, elementName, elementLocation, backupLocation, status, backupFileLocation;
    private boolean checked = false;

    public GrdDataBackup_Mdl() {
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementLocation() {
        return elementLocation;
    }

    public void setElementLocation(String elementLocation) {
        this.elementLocation = elementLocation;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getElementTypeCode() {
        return elementTypeCode;
    }

    public void setElementTypeCode(String elementTypeCode) {
        this.elementTypeCode = elementTypeCode;
    }

    public String getBackupFileLocation() {
        return backupFileLocation;
    }

    public void setBackupFileLocation(String backupFileLocation) {
        this.backupFileLocation = backupFileLocation;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.elementTypeCode);
        hash = 67 * hash + Objects.hashCode(this.elementName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdDataBackup_Mdl other = (GrdDataBackup_Mdl) obj;
        if (!Objects.equals(this.elementTypeCode, other.elementTypeCode)) {
            return false;
        }
        if (!Objects.equals(this.elementName, other.elementName)) {
            return false;
        }
        return true;
    }
}
