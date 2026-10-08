package aarogya.pharmacy.irpInfoPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class IrpInfoPopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String irpTypeTag;
    private String irpTypeText;    
    private String url;
    private String isActiveCode;
    private String isActiveText;

    public IrpInfoPopup_Mdl() {
    }

    public String getIrpTypeTag() {
        return irpTypeTag;
    }

    public void setIrpTypeTag(String irpTypeTag) {
        this.irpTypeTag = irpTypeTag;
    }

    public String getIrpTypeText() {
        return irpTypeText;
    }

    public void setIrpTypeText(String irpTypeText) {
        this.irpTypeText = irpTypeText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsActiveCode() {
        return isActiveCode;
    }

    public void setIsActiveCode(String isActiveCode) {
        this.isActiveCode = isActiveCode;
    }

    public String getIsActiveText() {
        return isActiveText;
    }

    public void setIsActiveText(String isActiveText) {
        this.isActiveText = isActiveText;
    }

}
