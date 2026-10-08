/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.radiologyDoc.jvm;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author abhishek
 */
public class RadiologyDocumentRequest_Mdl {

    @SerializedName("callFor")
    public Boolean calledForHeader;

    @SerializedName("hDocId")
    public String headerDocumentId;

    @SerializedName("vHSno")
    public String headerVoucherLineItemSerialNo;

    @SerializedName("vCSno")
    public String contentVoucherLineItemSerialNo;

    @SerializedName("fId")
    public String virtualFileId;

    @SerializedName("sId")
    public String clientSessionId;

    @SerializedName("fileType")
    public String fileType;

    @SerializedName("ruleCode")
    public String ruleCode;

    @SerializedName("cDocId")
    public String contentDocumentId;

    @SerializedName("params")
    public Map<String, String> findNreplaceParams;

    public String physicalFilePath;

    public String temporaryHeaderDownloadedFilePath;
    public String temporaryContentDownloadedFilePath;

    public RadiologyDocumentRequest_Mdl() {
        findNreplaceParams = new HashMap<>();
    }

    public Map<String, String> getFindNreplaceParams() {
        return findNreplaceParams;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.virtualFileId);
        hash = 79 * hash + Objects.hashCode(this.clientSessionId);
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
        final RadiologyDocumentRequest_Mdl other = (RadiologyDocumentRequest_Mdl) obj;
        if ((virtualFileId == null) || (other.virtualFileId == null) || (virtualFileId.equals("")) || (other.virtualFileId.equals(""))) {
            return false;
        }
        if ((clientSessionId == null) || (other.clientSessionId == null) || (clientSessionId.equals("")) || (other.clientSessionId.equals(""))) {
            return false;
        }
        if (!Objects.equals(this.virtualFileId, other.virtualFileId)) {
            return false;
        }
        if (!Objects.equals(this.clientSessionId, other.clientSessionId)) {
            return false;
        }
        return true;
    }

}
