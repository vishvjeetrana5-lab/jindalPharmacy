package aarogya.pharmacy.saleBillReturnProvisional.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHVType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String documentNatureCode;
    private String documentTypeCode;

    public TxtHVType_Mdl() {
    }

    public String getDocumentNatureCode() {
        return documentNatureCode;
    }

    public void setDocumentNatureCode(String documentNatureCode) {
        this.documentNatureCode = documentNatureCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
}
