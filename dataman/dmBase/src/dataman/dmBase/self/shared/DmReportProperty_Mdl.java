package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.DmExportType;
import java.io.Serializable;

public class DmReportProperty_Mdl implements Serializable, IsSerializable {

    private int noOfPrint = 1;
    private boolean printToPrinter = false;
    private DmExportType exportType = DmExportType.PDF;
    private String printerCode;

    public DmReportProperty_Mdl() {

    }

    public int getNoOfPrint() {
        return noOfPrint;
    }

    public void setPrinterCode(String printerCode) {
        this.printerCode = printerCode;
    }

    public String getPrinterCode() {
        return this.printerCode;
    }

    public void setNoOfPrint(int noOfPrint) {
        this.noOfPrint = noOfPrint;
    }

    public void setPrintToPrinter(boolean printToPrinter) {
        this.printToPrinter = printToPrinter;
    }

    public boolean isPrintToPrinter() {
        return printToPrinter;
    }

    public DmExportType getExportType() {
        return exportType;
    }

    public void setExportType(DmExportType exportType) {
        this.exportType = exportType;
    }
}
