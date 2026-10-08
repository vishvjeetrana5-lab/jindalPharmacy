package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import net.sf.jasperreports.engine.JasperPrint;

public class DmPrintToPrinter_Mdl implements Serializable,IsSerializable{

    private DmReportProperty_Mdl reportProperty;
    private JasperPrint jasperPrint;

    public DmPrintToPrinter_Mdl() {

    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public DmReportProperty_Mdl getReportProperty() {
        return reportProperty;
    }

    public void setReportProperty(DmReportProperty_Mdl reportProperty) {
        this.reportProperty = reportProperty;
    }
}
