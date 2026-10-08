package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptItemWiseProfitability_Srv;
import aarogya.pharmacy.reports.shared.RptItemWiseProfitability_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHCompany_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptItemWiseProfitability extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().ITEM_WISE_PROFITABILITY.caption;

    ServiceInvoker<RptItemWiseProfitability_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMedCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedCat() {
        txtHMedCat.getDmGrid().addTextColumn(txtHMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedCat.getDmGrid().initializeGrid(txtHMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCat.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHCompany_Mdl, String> shortName();
    }

    final DmTextHelpMulti<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelpMulti(GWT.create(TxtHCompany_PrpAcs.class), 7, false);

    private void iniTxtHCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 100, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 2);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicineName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), widgetName, reportProperty, new AsyncCallback<DmResult<String>>() {

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

    private void txtHelpEvents() {

        txtHMedCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHCompany_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHMedicineName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptItemWiseProfitability(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptItemWiseProfitability_Srv.class), RptItemWiseProfitability.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
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

        iniTxtHMedCat();
        addControl("medCat", "Medicine Category", vl.add(txtHMedCat));

        iniTxtHCompany();
        addControl("medMake", "Medicine Company", vl.add(txtHCompany));

        iniTxtHMedicineName();
        addControl("medicineName", "Medicine Name", vl.add(txtHMedicineName));

        swi.setWidget(this);
    }

}
