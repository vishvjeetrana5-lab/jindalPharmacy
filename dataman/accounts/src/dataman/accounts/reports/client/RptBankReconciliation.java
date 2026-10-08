package dataman.accounts.reports.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.reports.shared.RptBankReconciliationPrint_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;
import dataman.accounts.reports.shared.RptBankReconciliation_Srv;
import dataman.accounts.reports.shared.RptBankReconciliation_SrvAsync;

public class RptBankReconciliation extends DmReportLayout implements IsWidget, BlurHandler {

    public final static String widgetName = "Bank Reconciliation Report)";
    ServiceInvoker<RptBankReconciliation_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    List<RptBankReconciliationPrint_Mdl> grdBankReconciliationEntry_Mdls;

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, grdBankReconciliationEntry_Mdls, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    getReport(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public RptBankReconciliation(List<RptBankReconciliationPrint_Mdl> entry_Mdls) {
        super();
        this.grdBankReconciliationEntry_Mdls = entry_Mdls;
        SrvAsync = new ServiceInvoker<RptBankReconciliation_SrvAsync>(GWT.create(RptBankReconciliation_Srv.class), RptBankReconciliation.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        txtEvents();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
    }

    public void setDefault(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {

    }

}
