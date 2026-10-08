package dataman.accounts.reports.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.profitLossDisplay.shared.ProfitLossReport_Mdl;
import dataman.accounts.reports.shared.RptProfitLoss_Srv;
import dataman.accounts.reports.shared.RptProfitLoss_SrvAsync;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class RptProfitLoss extends DmReportLayout implements IsWidget {

    final String widgetName = "Profit & Loss Display";
    final ServiceInvoker<RptProfitLoss_SrvAsync> SrvAsync;
    final ProfitLossReport_Mdl profitLossReport_Mdl;
    final AcDisplaySetting_Mdl acDisplaySettingMdl;
    final DmTextBox txtFromDate = new DmTextBox();
    final DmTextBox txtToDate = new DmTextBox();
    final DmTextBox txtClosing = new DmTextBox();
    final DmTextBox txtHZeroValue = new DmTextBox();
    final DmTextBox txtHDetail = new DmTextBox();
    final DmTextBox txtHNarration = new DmTextBox();
    final DmTextBox txtHOpening = new DmTextBox();
    final DmTextBox txtHLedger = new DmTextBox();
    final DmTextBox txtHSite = new DmTextBox();

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, profitLossReport_Mdl, new AsyncCallback<DmResult<String>>() {

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

    public RptProfitLoss(AcDisplaySetting_Mdl acDisplaySettingMdl, ProfitLossReport_Mdl profitLossReport_Mdl) {
        super();
        this.acDisplaySettingMdl = acDisplaySettingMdl;
        this.profitLossReport_Mdl = profitLossReport_Mdl;
        SrvAsync = new ServiceInvoker<RptProfitLoss_SrvAsync>(GWT.create(RptProfitLoss_Srv.class), RptProfitLoss.class.getName());
        setTitle(this.asWidget(), widgetName);
        manageDisplay();
        setDefaultValues();
        initialize();
    }

    private void manageDisplay() {
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtHSite.setEnabled(false);
        txtHZeroValue.setEnabled(false);
        txtHDetail.setEnabled(false);
        txtHNarration.setEnabled(false);
        txtHOpening.setEnabled(false);
        txtHLedger.setEnabled(false);
        txtClosing.setEnabled(false);
    }

    private void setDefaultValues() {
        txtFromDate.setText(DmShared.dNull(acDisplaySettingMdl.getDateFrom()));
        txtToDate.setText(DmShared.dNull(acDisplaySettingMdl.getDateTo()));
        txtHSite.setTag(DmShared.xNull(acDisplaySettingMdl.getSiteCodeList()));
        txtHSite.setText(DmShared.xNull(acDisplaySettingMdl.getSiteNameList()));
        txtHZeroValue.setTag(DmShared.xNull(acDisplaySettingMdl.getShowZeroValueAc()));
        txtHZeroValue.setText(DmShared.iNull(acDisplaySettingMdl.getShowZeroValueAc()).intValue() == 1 ? "Yes" : "No");
        txtHDetail.setTag(DmShared.xNull(acDisplaySettingMdl.getShowDetail()));
        txtHDetail.setText(DmShared.iNull(acDisplaySettingMdl.getShowDetail()).intValue() == 1 ? "Yes" : "No");
        txtHNarration.setTag(DmShared.xNull(acDisplaySettingMdl.getShowNarration()));
        txtHNarration.setText(DmShared.iNull(acDisplaySettingMdl.getShowNarration()).intValue() == 1 ? "Yes" : "No");
        txtHOpening.setTag(DmShared.xNull(acDisplaySettingMdl.getIncludeOpening()));
        txtHOpening.setText(DmShared.iNull(acDisplaySettingMdl.getIncludeOpening()).intValue() == 1 ? "Yes" : "No");
        txtHLedger.setTag(DmShared.xNull(acDisplaySettingMdl.getLedgerAcCodeList()));
        txtHLedger.setText(DmShared.xNull(acDisplaySettingMdl.getLedgerAcNameList()));
        txtClosing.setText(DmShared.xNull(acDisplaySettingMdl.getClosingStock()));
    }

    private void initialize() {
        addControl("fromDate", "From Date", txtFromDate);
        addControl("toDate", "To Date", txtToDate);
        addControl("siteName", "Site Name", txtHSite);
        addControl("showValue", "Show Zero Value A/c", txtHZeroValue);
        addControl("showDetail", "Show Detail", txtHDetail);
        addControl("showNarration", "Show Narration", txtHNarration);
        addControl("includeOpening", "Include Opening", txtHOpening);
        addControl("acLedger", "A/c Ledger", txtHLedger);
        addControl("closingStock", "Closing Stock", txtClosing);
    }

    public void setDefault(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }
}
