package dataman.accounts.acDisplaySetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import dataman.accounts.self.shared.AcEnum.*;

public class DisplayRoute_Mdl implements Serializable, IsSerializable {

    private PageName pageName = PageName.BalanceSheet;
    private CodeType codeType = CodeType.Group;
    private String code, name;

    public DisplayRoute_Mdl() {

    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PageName getPageName() {
        return pageName;
    }

    public void setPageName(PageName pageName) {
        this.pageName = pageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
