package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdWebJobs_Mdl extends KeyField implements Serializable, IsSerializable {

    private String templateTypeCode;
    private String templateTypeName;

    public GrdWebJobs_Mdl() {
    }

    public String getTemplateTypeCode() {
        return templateTypeCode;
    }

    public void setTemplateTypeCode(String templateTypeCode) {
        this.templateTypeCode = templateTypeCode;
    }

    public String getTemplateTypeName() {
        return templateTypeName;
    }

    public void setTemplateTypeName(String templateTypeName) {
        this.templateTypeName = templateTypeName;
    }

}
