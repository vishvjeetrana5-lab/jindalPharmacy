package dataman.webJobs.emailTemplateMast.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class EmailTemplateGridEdit_Mdl extends KeyField implements Serializable, IsSerializable {

    private String procName;
    private String rptName;
    private String fileName;

    public EmailTemplateGridEdit_Mdl() {
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
