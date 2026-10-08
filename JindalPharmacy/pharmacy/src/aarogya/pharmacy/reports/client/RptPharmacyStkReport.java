package aarogya.pharmacy.reports.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.pharmacy.reports.shared.RptPharmacyStkReport_Srv;
import aarogya.pharmacy.reports.shared.RptPharmacyStkReport_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptPharmacyStkReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmPharmacy().Medicine_Stock_Status.caption;
    private final ServiceInvoker<RptPharmacyStkReport_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    // private final DmTextBox txtAdmissionDate = new DmTextBox(12, true);
    // private final DmTextBox txtToDate = new DmTextBox(12, true);
    // private final DmTextBoxNumeric txtFromAge = new DmTextBoxNumeric(0, false, 2, false);
    // private final DmTextBoxNumeric txtToAge = new DmTextBoxNumeric(0, false, 2, false);

    //<editor-fold defaultstate="collapsed" desc="txtDepartment">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtDepartment() {
        txtDepartment.getDmGrid().addTextColumn(txtDepartment.getDmGrid().getPropertyAccess().name(), "Select All:-", 1, "name");
        txtDepartment.getDmGrid().initializeGrid(txtDepartment.getDmGrid().getPropertyAccess().code(), 200, 0,2);//, 1
        txtDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtMdcCmpny">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtMdcCmpny = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtMdcCmpny() {
        txtMdcCmpny.getDmGrid().addTextColumn(txtMdcCmpny.getDmGrid().getPropertyAccess().name(), "Select All:-", 1, "name");
        txtMdcCmpny.getDmGrid().initializeGrid(txtMdcCmpny.getDmGrid().getPropertyAccess().code(), 200, 0, 2);//, 1
        txtMdcCmpny.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtMdcCtg">
    // private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtStudentCity = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtMdcCtg = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtMdcCtg() {
        txtMdcCtg.getDmGrid().addTextColumn(txtMdcCtg.getDmGrid().getPropertyAccess().name(), "Select All:-", 1, "name");
        txtMdcCtg.getDmGrid().initializeGrid(txtMdcCtg.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtMdcCtg.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtMdcGrp">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtMdcGrp = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtMdcGrp() {
        txtMdcGrp.getDmGrid().addTextColumn(txtMdcGrp.getDmGrid().getPropertyAccess().name(), "Select All:-", 1, "NAME");
        txtMdcGrp.getDmGrid().initializeGrid(txtMdcGrp.getDmGrid().getPropertyAccess().code(), 200, 0, 2);//, 1
        txtMdcGrp.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    interface GridMed_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("Unit")
        ValueProvider<TxtHMedicine_Mdl, String> unit();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();
    }

    //<editor-fold defaultstate="collapsed" desc="txtMedicine">
    final DmTextHelpMulti<TxtHMedicine_Mdl, GridMed_PrpAcs> txtMedicine = new DmTextHelpMulti(GWT.create(GridMed_PrpAcs.class), 10);

    private void iniTxtMedicine() {
        txtMedicine.getDmGrid().addTextColumn(txtMedicine.getDmGrid().getPropertyAccess().name(), "Meds:-", 1, "name");
        txtMedicine.getDmGrid().addTextColumn(txtMedicine.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");

        txtMedicine.getDmGrid().initializeGrid(txtMedicine.getDmGrid().getPropertyAccess().fieldHashCode(), 500, 0, 2);//, 1
        txtMedicine.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10,true);

    private void iniTxtSite() {
        txtSite.getDmGrid().addTextColumn(txtSite.getDmGrid().getPropertyAccess().name(), "Select All:-", 1, "name");
        txtSite.getDmGrid().initializeGrid(txtSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);//, 1
        txtSite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtfilterOn">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtfilterOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);
    // final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtStudentCity = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtfilterOn() {
        txtfilterOn.getDmGrid().addTextColumn(txtfilterOn.getDmGrid().getPropertyAccess().name(), "Select:-", 180, "name");

        txtfilterOn.getDmGrid().initializeGrid(txtfilterOn.getDmGrid().getPropertyAccess().code(), 180, 0, 1);//0,2
        txtfilterOn.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        KeyFieldExtended kf = result.getData();// kf store sharedKeyFieldName and 3 other
                        //  startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                    } else {
                        getReport(result.getData().getSharedKeyFieldName());
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }

        }
        );
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtAdmissionDate) {
//            txtAdmissionDate.setText(DmClient.returnDate(txtAdmissionDate.getText()));
//        }
//        else if (event.getSource() == txtToDate) {
//            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
//        }
    }

    private void txtHelpEvents() {

        txtDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        //////
        txtMdcCmpny.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtMdcCmpny(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMdcCmpny).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtMdcCmpny.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMdcCmpny).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        //
        txtMdcCtg.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtMdcCtg(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMdcCtg).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtMdcCtg.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMdcCtg).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        //

        txtMdcGrp.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtMdcGrp(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMdcGrp).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtMdcGrp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMdcGrp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHMedicine_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMedicine).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtfilterOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtfilterOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtfilterOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtfilterOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtfilterOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    public RptPharmacyStkReport(SetWidgetInvoker swi) {
        super();
        srvAsync
                = new ServiceInvoker<>(GWT.create(RptPharmacyStkReport_Srv.class
                ), RptPharmacyStkReport.class
                        .getName());

        setTitle(this.asWidget(), widgetName);

        // txtAdmissionDate.addBlurHandler(this);
        // txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {

//        addControl("admissionDate", "Admission Date", vl.add(txtAdmissionDate));
//         txtAdmissionDate.setText(DmClient.getStartDate());
        iniTxtDepartment();
        addControl("department", "Department", vl.add(txtDepartment));

        iniTxtMdcCmpny();
        addControl("medicineCompany", "Medicine Company", vl.add(txtMdcCmpny));

        iniTxtMdcCtg();
        addControl("medicineCategory", "Medicine Category", vl.add(txtMdcCtg));

        iniTxtMdcGrp();
        addControl("medicineGroup", "Medicine Group", vl.add(txtMdcGrp));

        iniTxtMedicine();
        addControl("medicine", "Medicine", vl.add(txtMedicine));

        iniTxtSite();
        addControl("site", "Site", vl.add(txtSite));

        iniTxtfilterOn();
        addControl("filterOn", "Meds. Curr. Stk", vl.add(txtfilterOn));

        // addControl("fromAdmission", "From Admission", vl.add(txtAdmissionDate));
        swi.setWidget(this);

    }
    
      private void setDefaultValue() {
        //txtHValueOn.setTag("L");
        //txtHValueOn.setText("Landed Rate");

        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtSite.setTag(keyField.getSharedKeyFieldCode());
                    txtSite.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

}
