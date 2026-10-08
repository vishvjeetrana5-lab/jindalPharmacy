package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptOperationMediMast_Srv;
import aarogya.pharmacy.reports.shared.RptOperationMediMast_SrvAsync;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptOperationMediMast extends DmReportLayout implements IsWidget {

    public final String widgetName = new pmPharmacy().OPERATION_MEDICINE_MASTER.caption;
    private final ServiceInvoker<RptOperationMediMast_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();

    public RptOperationMediMast() {
        super();
        srvAsync = new ServiceInvoker<RptOperationMediMast_SrvAsync>(GWT.create(RptOperationMediMast_Srv.class), RptOperationMediMast.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        textHelpEvents();
    }

    //<editor-fold defaultstate="collapsed" desc="txtHOperationName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOperationName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, false);

    public void iniTxtHOperationName() {
        txtHOperationName.getDmGrid().addTextColumn(txtHOperationName.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHOperationName.getDmGrid().initializeGrid(txtHOperationName.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHOperationName.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

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

    public void setDefault(HashMap<String, FieldUDT_Mdl> hashMapFilter, boolean printToPrinter, SetWidgetInvoker swi) {
        if (hashMapFilter.containsKey("CODE")) {
            txtHOperationName.setText(hashMapFilter.get("CODE").getText());
            txtHOperationName.setTag(hashMapFilter.get("CODE").getTag());
        }
        if (!printToPrinter) {
            swi.setWidget(this);
        }
        onView();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        iniTxtHOperationName();
        addControl("operationName", "Operation Name", vl.add(txtHOperationName));
    }

    private void textHelpEvents() {
        txtHOperationName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOperationName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOperationName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOperationName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOperationName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

}
