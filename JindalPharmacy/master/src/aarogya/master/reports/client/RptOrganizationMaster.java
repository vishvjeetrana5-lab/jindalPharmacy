package aarogya.master.reports.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.reports.shared.RptOrganizationMaster_Srv;
import aarogya.master.reports.shared.RptOrganizationMaster_SrvAsync;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;

public class RptOrganizationMaster extends DmReportLayout implements IsWidget {

    public final String widgetName = new pmMaster().ORGANIZATION_MASTER.caption;
    private final ServiceInvoker<RptOrganizationMaster_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();

    //<editor-fold defaultstate="collapsed" desc="txtName">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtVrNo() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHOrganization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(DmShared.xNull(txtHOrganization.getTag2nd()), getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

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

    public RptOrganizationMaster() {
        super();
        srvAsync = new ServiceInvoker<RptOrganizationMaster_SrvAsync>(GWT.create(RptOrganizationMaster_Srv.class), RptOrganizationMaster.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        txtEvents();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        iniTxtVrNo();
        addControl("CODE", "Name", vl.add(txtHOrganization));
    }

    public void setDefault(HashMap<String, FieldUDT_Mdl> hashMapFilter, String applicableDate, boolean printToPrinter, SetWidgetInvoker swi) {
        if (hashMapFilter.containsKey("CODE")) {
            txtHOrganization.setText(hashMapFilter.get("CODE").getText());
            txtHOrganization.setTag(hashMapFilter.get("CODE").getTag());
            txtHOrganization.setTag2nd(DmShared.xNull(applicableDate));
        }
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    private void txtEvents() {
        txtHOrganization.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHKeyField(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrganization).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrganization.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganization).show();
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
