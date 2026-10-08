package aarogya.mis.reports.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.RptEmergencyStatistics_Srv;
import aarogya.mis.reports.shared.RptEmergencyStatistics_SrvAsync;
import aarogya.mis.reports.shared.TxtHOPDType_Mdl;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptEmergencyStatistics extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmMIS().EMERGENCY_STATISTICS_REPORT.caption;
    ServiceInvoker<RptEmergencyStatistics_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(15, true);
    DmTextBox txtToDate = new DmTextBox(15, true);
    DmTextBoxTime txtFromTime = new DmTextBoxTime(false);
    DmTextBoxTime txtToTime = new DmTextBoxTime(false);

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Consultant Name", 1, "NAME");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHConsultant.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDType">
    interface TxtHOPDNo_PrpAcs extends KeyField_T_PrpAcs<TxtHOPDType_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHOPDType_Mdl, String> description();

    }

    final DmTextHelpMulti<TxtHOPDType_Mdl, TxtHOPDNo_PrpAcs> txtHOPDType = new DmTextHelpMulti(GWT.create(TxtHOPDNo_PrpAcs.class), 7, false);

    private void iniTxtHOPDType() {
        txtHOPDType.getDmGrid().addTextColumn(txtHOPDType.getDmGrid().getPropertyAccess().name(), "Short Name", 100, "short_Name");
        txtHOPDType.getDmGrid().addTextColumn(txtHOPDType.getDmGrid().getPropertyAccess().description(), "Description", 150, "description");
        txtHOPDType.getDmGrid().initializeGrid(txtHOPDType.getDmGrid().getPropertyAccess().code(), 250, 0, 2);

        txtHOPDType.getHelper().addSort("description", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

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
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtEvents() {
        txtHConsultant.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHOPDType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHOPDType_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOPDType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHOPDType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDType).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHOPDType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHOPDType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptEmergencyStatistics(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptEmergencyStatistics_Srv.class), RptEmergencyStatistics.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        addControl("fromTime", "From Time", vl.add(txtFromTime));

        addControl("toTime", "To Time", vl.add(txtToTime));

        iniTxtHOPDType();
        addControl("opdType", "OPD Type", vl.add(txtHOPDType));

        iniTxtHConsultant();
        addControl("consultant", "Consultant", vl.add(txtHConsultant));

        swi.setWidget(this);
    }

}
