package aarogya.master.reports.client;

import aarogya.master.reports.shared.RptSharingMaster_Srv;
import aarogya.master.reports.shared.RptSharingMaster_SrvAsync;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.HashMap;
import java.util.List;

public class RptSharingMaster extends DmReportLayout implements IsWidget {

    public final String widgetName = new pmMaster().SHARING_MASTER.caption;
    private final ServiceInvoker<RptSharingMaster_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtApplicableFrom = new DmTextBox(12, false);

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHConsultant.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmShared.xNull(txtApplicableFrom.getText()), getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

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

    public RptSharingMaster() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptSharingMaster_Srv.class), RptSharingMaster.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        txtEvents();
        txtHConsultant.setEnabled(false);
        txtApplicableFrom.setEnabled(false);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        iniTxtHConsultant();
        addControl("consultantCode", "Consultant", vl.add(txtHConsultant));
        addControl("applicableFrom", "Applicable From", vl.add(txtApplicableFrom));
    }

    public void setDefault(HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (hashMapFilter.containsKey("consultantCode")) {
            txtHConsultant.setText(hashMapFilter.get("consultantCode").getText());
            txtHConsultant.setTag(hashMapFilter.get("consultantCode").getTag());
            txtApplicableFrom.setText(hashMapFilter.get("applicableFrom").getTag());
        }
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

}
