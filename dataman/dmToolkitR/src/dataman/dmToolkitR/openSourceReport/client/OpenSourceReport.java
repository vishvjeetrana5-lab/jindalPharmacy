package dataman.dmToolkitR.openSourceReport.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmToolkitR.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import java.util.HashMap;
import dataman.dmToolkitR.openSourceReport.shared.OpenSourceReport_Srv;
import dataman.dmToolkitR.openSourceReport.shared.OpenSourceReport_SrvAsync;
import java.util.Set;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmNotify.client.DmNotify;
import org.gwtbootstrap3.client.ui.Button;
import com.sencha.gxt.widget.core.client.info.Info;
import dataman.dmToolkitR.printerMapping.client.PrinterMapping;

public class OpenSourceReport extends DmReportLayout implements IsWidget {

    public interface CallBack {

        public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty);

        public void onPrintSuccess(String fileName);

    }

    public interface PrintSuccessCallBack {

        public void closeForm();
    }
    private CallBack callBack;
    private final String widgetName, v_Type, reportNameSuffix, reportTitle;
    private String reportName, paperSize = "", sharedPrinterName = "";
    private ServiceInvoker<OpenSourceReport_SrvAsync> srvAsync;
    private PrintSuccessCallBack printSuccessCallBack;

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        if (reportName.equalsIgnoreCase("")) {
            new DmAlertBox("Report name is blank.", (DmTextBox) null).show();
        } else {
            srvAsync.invokeRPC(true).execute(reportName + reportNameSuffix, paperSize, reportTitle, getFilter(),
                    //                    DmShared.xNull(DmClient.getRegionalLanguage()),
                    "ENGLISH",
                    reportProperty, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {

                    new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {

                    if (result.isStatus()) {

                        if (!reportProperty.isPrintToPrinter()) {
                            getReport(result.getData());
                        } else if (reportProperty.isPrintToPrinter()) {

//                            if (callBack != null) {
//                                 reportProperty = callBack.setReportProperty(reportProperty);
//                            }
                            
                            printReportToPrinter(result.getData());
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OpenSourceReport.this);

                        }
                        if (callBack != null) {
                            callBack.onPrintSuccess(result.getData());
                        }
                    } else {
                        new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        }
    }
    //=======================================================================

    private void printReportToPrinter(String fileName) {
        try {
            DmClient.printReportToPrinterDirectlyNew(sharedPrinterName, reportProperty, getReport(fileName), new DmBaseClient.IDirectPrintingCallback() {
                @Override
                public void onSuccess(String reportUrl, String printerName) {
                    //new DmNotify().getNotify("Report:" + reportTitle);
                    printSuccessCallBack.closeForm();
                }

                @Override
                public void onFailure(Exception ex) {

                    new DmAlertBox(ex.getMessage(), (Button) null).show();
                }
            });
        } catch (Exception ex) {

        }
    }
    //===============================================================================

    public OpenSourceReport(String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        super(widgetName);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, null, swi);
    }

    public OpenSourceReport(String v_Type, String forceToReportName, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        super(widgetName);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize(forceToReportName, hashMapFilter, printToPrinter, null, swi);
    }

    public OpenSourceReport(String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(widgetName);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }

    public OpenSourceReport(String sharedPrinterName, String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, PrintSuccessCallBack printSuccessCallBack, SetWidgetInvoker swi) {
        super(widgetName);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        this.printSuccessCallBack = printSuccessCallBack;
        this.sharedPrinterName = sharedPrinterName;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }

    public OpenSourceReport(String v_Type, String forceToReportName, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(widgetName);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize(forceToReportName, hashMapFilter, printToPrinter, callBack, swi);
    }

    private void initialize(String forceToReportName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        Set<String> set;
        DmTextBox txtBox;
        FieldUDT_Mdl filter;

        this.callBack = callBack;

        if (hashMapFilter != null) {
            set = hashMapFilter.keySet();
            for (String s : set) {
                if (!DmShared.xNull(s).trim().equalsIgnoreCase("")) {
                    txtBox = new DmTextBox();
                    txtBox.setEnabled(false);
                    filter = hashMapFilter.get(s);
                    txtBox.setTitle("Field Code: " + s);
                    txtBox.setText(filter.getText());
                    txtBox.setTag(filter.getTag());

                    addControl(s, filter.getFieldCaption(), txtBox);
                }
            }
        }

        reportProperty.setPrintToPrinter(printToPrinter);
        if (callBack != null) {
            reportProperty = callBack.setReportProperty(reportProperty);
        }

//        if (!printToPrinter) {
//            swi.setWidget(this);
//        }
        if (!printToPrinter
                && !(DmClient.isViewReportInNewWindow() && DmClient.getScreenDisplay() != DmEnum.ScreenDisplay.DESKTOP)) {
            swi.setWidget(this);
        }

        if (DmShared.xNull(forceToReportName).trim().equalsIgnoreCase("")) {
            reportName = DmClient.getVoucherPrintSetup().get(v_Type);
            paperSize = DmClient.getVoucherPrintSetup_PaperSize().get(v_Type);
        } else {
            reportName = forceToReportName;
            paperSize = "";
        }
        if (DmShared.xNull(reportName).equalsIgnoreCase("")) {
            srvAsync.invokeRPC(true).getReportSetting(v_Type, new AsyncCallback<DmResult<KeyFieldExtended>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<KeyFieldExtended> result) {
                    if (result.isStatus()) {
                        KeyFieldExtended lstTemp = result.getData();
                        reportName = DmShared.xNull(lstTemp.getSharedKeyFieldCode());
                        paperSize = DmShared.xNull(lstTemp.getSharedKeyFieldName());
                        if (reportName.equalsIgnoreCase("")) {
                            reportName = "";
                            paperSize = "";
                        } else {
                            DmClient.getVoucherPrintSetup().put(v_Type, reportName);
                            DmClient.getVoucherPrintSetup_PaperSize().put(v_Type, paperSize);
                        }
                        onView();
                    } else {
                        new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } else {
            onView();
        }
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }
}
