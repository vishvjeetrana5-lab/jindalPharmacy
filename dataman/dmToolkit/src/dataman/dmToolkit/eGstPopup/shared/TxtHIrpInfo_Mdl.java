package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHIrpInfo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String irpTypeTag;
    private String irpTypeText;    
    private String url;


    public TxtHIrpInfo_Mdl() {
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
}
