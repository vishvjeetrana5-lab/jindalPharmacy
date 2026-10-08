package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GUIHelp_Mdl extends KeyField implements Serializable, IsSerializable {
    // fieldCaption as SharedFieldName, code as SharedFieldCode

    private String vSNo, fieldWidth, fieldAlignment, guiRowNo, sno;

    public GUIHelp_Mdl() {
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(String fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public String getFieldAlignment() {
        return fieldAlignment;
    }

    public void setFieldAlignment(String fieldAlignment) {
        this.fieldAlignment = fieldAlignment;
    }

    public String getGuiRowNo() {
        return guiRowNo;
    }

    public void setGuiRowNo(String guiRowNo) {
        this.guiRowNo = guiRowNo;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
