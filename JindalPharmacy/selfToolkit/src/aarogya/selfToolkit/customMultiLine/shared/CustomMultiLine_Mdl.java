package aarogya.selfToolkit.customMultiLine.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class CustomMultiLine_Mdl implements Serializable, IsSerializable {

    private String consultantCode;
    private String subject;
    private String remark;
    private CustomMultiLineNature nature;

    public CustomMultiLine_Mdl() {
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CustomMultiLineNature getNature() {
        return nature;
    }

    public void setNature(CustomMultiLineNature nature) {
        this.nature = nature;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
