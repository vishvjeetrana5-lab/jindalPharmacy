package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.TxtHCardReg_Mdl;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;
import aarogya.mis.reports.shared.RptPatientWalletLedger_Srv;
import aarogya.mis.reports.shared.RptPatientWalletLedger_SrvAsync;

public class RptPatientWalletLedger extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<RptPatientWalletLedger_SrvAsync> SrvAsync;
    final String widgetName = new pmMIS().PATIENT_WALLET_REGISTER.caption;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludeOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), widgetName ,reportProperty, new AsyncCallback<DmResult<String>>() {

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

    private void txtEvents() {

        txtHIncludeOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIncludeOpening(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludeOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludeOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludeOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSiteName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

//        txtHCardReg.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHCardReg_Mdl>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCardNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCardReg_Mdl>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHCardReg).show();
//
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<TxtHCardReg_Mdl>> result) {
//                        if (result.isStatus()) {
//                            txtHCardReg.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHCardReg).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//            }
//        });
        txtHCardReg.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHCardReg_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCardNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCardReg_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCardReg).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCardReg_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCardReg.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCardReg).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public RptPatientWalletLedger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPatientWalletLedger_Srv.class), RptPatientWalletLedger.class.getName());
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
        txtFromDate.setMandatory(true);
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());
        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHSiteName();
        addControl("site", "Site Name", vl.add(txtHSiteName));

        iniTxtHIncludeOpening();
        txtHIncludeOpening.setMandatory(true);
        addControl("includeOpening", "Include Opening", vl.add(txtHIncludeOpening));
        
        iniTxtHCardReg();
        addControl("cardReg", "Card Registration", vl.add(txtHCardReg));

        defaultValue();
        swi.setWidget(this);
    }

    //<editor-fold defaultstate="collapsed" desc="txtHCardReg">
    interface TxtHDivision_PrpAcs extends KeyField_T_PrpAcs<TxtHCardReg_Mdl> {

        @Editor.Path("UhidNo")
        ValueProvider<TxtHCardReg_Mdl, String> uhidNo();

        @Editor.Path("Mobile")
        ValueProvider<TxtHCardReg_Mdl, String> mobile();

        @Editor.Path("PersonName")
        ValueProvider<TxtHCardReg_Mdl, String> personName();
    }
    private final DmTextHelpMulti<TxtHCardReg_Mdl, TxtHDivision_PrpAcs> txtHCardReg = new DmTextHelpMulti(GWT.create(TxtHDivision_PrpAcs.class), 10, false);

    private void iniTxtHCardReg() {
        txtHCardReg.getDmGrid().addTextColumn(txtHCardReg.getDmGrid().getPropertyAccess().name(), "Reg No", 100, "regNo");
        txtHCardReg.getDmGrid().addTextColumn(txtHCardReg.getDmGrid().getPropertyAccess().uhidNo(), "Uhid No", 100, "uhid");
        txtHCardReg.getDmGrid().addTextColumn(txtHCardReg.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        txtHCardReg.getDmGrid().addTextColumn(txtHCardReg.getDmGrid().getPropertyAccess().personName(), "Person Name", 100, "personName");
        txtHCardReg.getDmGrid().initializeGrid(txtHCardReg.getDmGrid().getPropertyAccess().code(), 500,0 ,2);
        txtHCardReg.getHelper().addSort("regNo", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>


    private void iniTxtHIncludeOpening() {
        txtHIncludeOpening.getDmGrid().addTextColumn(txtHIncludeOpening.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHIncludeOpening.getDmGrid().initializeGrid(txtHIncludeOpening.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHIncludeOpening.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void defaultValue() {

        txtHIncludeOpening.setTag("Y");
        txtHIncludeOpening.setText("Yes");

    }

}
