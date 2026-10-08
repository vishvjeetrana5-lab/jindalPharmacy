package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.Objects;

public class DmLocalSettingPrinter_Mdl implements Serializable, IsSerializable {

    private String printerCode, printerName;
    private DmEnum.DmPrinterImageType printerImageType = DmEnum.DmPrinterImageType.RGB;

    public DmLocalSettingPrinter_Mdl() {
    }

    public String getPrinterName() {
        return printerName;
    }

    public String getPrinterCode() {
        return printerCode;
    }

    public void setPrinterCode(String printerCode) {
        this.printerCode = printerCode;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public DmEnum.DmPrinterImageType getPrinterImageType() {
        return printerImageType;
    }

    public void setPrinterImageType(DmEnum.DmPrinterImageType printerImageType) {
        this.printerImageType = printerImageType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.printerCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmLocalSettingPrinter_Mdl other = (DmLocalSettingPrinter_Mdl) obj;
        if (!Objects.equals(this.printerCode, other.printerCode)) {
            return false;
        }
        return true;
    }
}
