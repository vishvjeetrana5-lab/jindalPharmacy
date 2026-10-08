package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.info.Info;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;

public class ReportPrint extends DmReportLayout implements IsWidget {

    public interface CallBack {

        public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty);
    }
    private SetWidgetInvoker swi = null;
    String fileName = "";

    public ReportPrint(String fileName, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        this.fileName = fileName;
        initialize(printToPrinter, callBack, swi);
    }

    @Override
    public void onClose() {
        if (swi != null) {
            swi.closeWidget(this);
        }
    }

    private void initialize(boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {

        reportProperty.setPrintToPrinter(printToPrinter);
        if (callBack != null) {
            reportProperty = callBack.setReportProperty(reportProperty);
        }

        if (!printToPrinter) {
            swi.setWidget(this);
        }

        onView();
    }

    @Override
    public void onView() {
        if (!reportProperty.isPrintToPrinter()) {
            getReport(fileName);
        } else if (reportProperty.isPrintToPrinter()) {
            printReportToPrinter(fileName);
        }
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void printReportToPrinter(String fileName) {
        try {
            DmClient.printReportToPrinterDirectly(reportProperty, getReport(fileName), new DmBaseClient.IDirectPrintingCallback() {
                @Override
                public void onSuccess(String reportUrl, String printerName) {
                    Info.display(printerName, "Report:" + "Cheque Print");
                }

                @Override
                public void onFailure(Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
                }
            });
        } catch (Exception ex) {

        }
    }
}
