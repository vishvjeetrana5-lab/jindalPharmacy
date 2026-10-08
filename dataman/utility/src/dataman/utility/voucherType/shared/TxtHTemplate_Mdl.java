package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHTemplate_Mdl extends KeyField implements Serializable, IsSerializable {

    private String templateTypeCode;
    private DmWebJobTypes webJobType = DmWebJobTypes.SMS;

    public TxtHTemplate_Mdl() {
    }

    public String getTemplateTypeCode() {
        return templateTypeCode;
    }

    public void setTemplateTypeCode(String templateTypeCode) {
        this.templateTypeCode = templateTypeCode;
    }

    public DmWebJobTypes getWebJobType() {
        return webJobType;
    }

    public void setWebJobType(DmWebJobTypes webJobType) {
        this.webJobType = webJobType;
    }

}
