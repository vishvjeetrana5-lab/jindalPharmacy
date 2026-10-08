package dataman.dmToolkit.openSourceReport.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.widget.core.client.info.Info;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import java.util.HashMap;
import dataman.dmToolkit.openSourceReport.shared.OpenSourceReport_Srv;
import dataman.dmToolkit.openSourceReport.shared.OpenSourceReport_SrvAsync;
import java.util.Set;

public class OpenSourceReport extends DmReportLayout implements IsWidget {

    public interface CallBack {

        public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty);
    }

    private final String widgetName, v_Type, reportNameSuffix, reportTitle;
    private String reportName, paperSize = "";
    private ServiceInvoker<OpenSourceReport_SrvAsync> srvAsync;
    private CallBack callBack;

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        if (reportName.equalsIgnoreCase("")) {
            new DmAlertMessageBox("Report name is blank.", (DmTextBox) null).show();
        } else {
            srvAsync.invokeRPC(true).execute(isAllowPrintForAnotherUser,qr_Text,tableName, fieldValue, fieldName,reportName + reportNameSuffix, paperSize, reportTitle, getFilter(), DmShared.xNull(DmClient.getRegionalLanguage()), reportProperty, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        if (!reportProperty.isPrintToPrinter()) {
                            getReport(result.getData());
                        } else if (reportProperty.isPrintToPrinter()) {
                            if (callBack != null) {
                                reportProperty = callBack.setReportProperty(reportProperty);
                            }
                            printReportToPrinter(result.getData());
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OpenSourceReport.this);
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        }
    }

    private void printReportToPrinter(String fileName) {
        try {
            DmClient.printReportToPrinterDirectly(reportProperty, getReport(fileName), new DmBaseClient.IDirectPrintingCallback() {
                @Override
                public void onSuccess(String reportUrl, String printerName) {
                    Info.display(printerName, "Report:" + reportTitle);
                }

                @Override
                public void onFailure(Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
                }
            });
        } catch (Exception ex) {

        }
    }

    public OpenSourceReport(String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, null, swi);
    }

    public OpenSourceReport(String v_Type, String forceToReportName, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize(forceToReportName, hashMapFilter, printToPrinter, null, swi);
    }

    public OpenSourceReport(String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }
    //New
    String tableName="" , fieldValue="",  fieldName="",qr_Text="";
    public OpenSourceReport(String tableName, String fieldValue, String fieldName,String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        this.tableName=tableName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }
    boolean isAllowPrintForAnotherUser=false;
    public OpenSourceReport(boolean isAllowPrintForAnotherUser,String tableName, String fieldValue, String fieldName,String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter,CallBack callBack, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        this.tableName=tableName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        this.isAllowPrintForAnotherUser=isAllowPrintForAnotherUser;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }
       
    public OpenSourceReport(String qrText,String tableName, String fieldValue, String fieldName,String v_Type, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(true);
        srvAsync = new ServiceInvoker<OpenSourceReport_SrvAsync>(GWT.create(OpenSourceReport_Srv.class), OpenSourceReport.class.getName());
        this.widgetName = widgetName;
        this.reportNameSuffix = DmShared.xNull(reportNameSuffix).trim();
        this.v_Type = DmShared.iNull(v_Type).toString();
        this.reportTitle = reportTitle;
        this.tableName=tableName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        this.qr_Text=qrText;
        setTitle(this.asWidget(), widgetName);
        initialize("", hashMapFilter, printToPrinter, callBack, swi);
    }

    public OpenSourceReport(String v_Type, String forceToReportName, String reportNameSuffix, String reportTitle, String widgetName, HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, CallBack callBack, SetWidgetInvoker swi) {
        super(true);

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
        removeExcelButton();
        removeWordButton();
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
        this.callBack = callBack;

        if (!printToPrinter) {
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
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
