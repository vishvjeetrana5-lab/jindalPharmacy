package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.Objects;

public class DmLocalSettingPrintMap_Mdl implements Serializable, IsSerializable {

    private String moduleName, menuText, reportName, printerCode;
    private String fieldUniqueId = "";

    public DmLocalSettingPrintMap_Mdl() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String printName) {
        this.reportName = printName;
    }

    public String getPrinterCode() {
        return printerCode;
    }

    public void setPrinterCode(String printerCode) {
        this.printerCode = printerCode;
    }

    public String getFieldUniqueCode() {
        return fieldUniqueId;
    }

    public void setFieldUniqueCode(String uniqueCode) {
        fieldUniqueId = uniqueCode.trim();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.moduleName);
        hash = 89 * hash + Objects.hashCode(this.menuText);
        hash = 89 * hash + Objects.hashCode(this.reportName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmLocalSettingPrintMap_Mdl other = (DmLocalSettingPrintMap_Mdl) obj;
        if (!Objects.equals(this.moduleName, other.moduleName)) {
            return false;
        }
        if (!Objects.equals(this.menuText, other.menuText)) {
            return false;
        }
        if (!Objects.equals(this.reportName, other.reportName)) {
            return false;
        }
        return true;
    }

}
