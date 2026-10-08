package dataman.dmBase.dmFileManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class DmAttachment_Mdl extends DmFileRuleType implements Serializable, IsSerializable{

    private String resourceAlocationId;
    private String fileLink;
    private String docId;
    private String sNo;
    private String fileName;
    private String fileSize;
    private String remarks;
    private String userName;
    private String modifyDateTime;
    private String serverDirectory;
    private String tempDocId;
    private Timestamp u_EntDt;

    public DmAttachment_Mdl() {
        this.fileLink = "";
        this.fileName = "";
        setRuleCode("");
    }

    public String getResourceAlocationId() {
        return resourceAlocationId;
    }

    public void setResourceAlocationId(String resourceAlocationId) {
        this.resourceAlocationId = resourceAlocationId;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getServerDirectory() {
        return serverDirectory;
    }

    public void setServerDirectory(String serverDirectory) {
        this.serverDirectory = serverDirectory;
    }

    public String getTempDocId() {
        return tempDocId;
    }

    public void setTempDocId(String tempDocId) {
        this.tempDocId = tempDocId;
    }

    public Timestamp getU_EntDt() {
        return u_EntDt;
    }

    public void setU_EntDt(Timestamp u_EntDt) {
        this.u_EntDt = u_EntDt;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.fileLink);
        hash = 29 * hash + Objects.hashCode(this.fileName);
        hash = 29 * hash + Objects.hashCode(getRuleCode());
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
        final DmAttachment_Mdl other = (DmAttachment_Mdl) obj;
        if (!Objects.equals(this.fileLink, other.fileLink)) {
            return false;
        }
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(getRuleCode(), other.getRuleCode())) {
            return false;
        }
        return true;
    }

}
