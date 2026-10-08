package aarogya.master.environmentSettingSite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdDiagnosticFile_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fileName;

    public GrdDiagnosticFile_Mdl() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
