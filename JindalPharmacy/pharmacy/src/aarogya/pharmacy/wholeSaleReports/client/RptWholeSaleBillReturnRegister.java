package aarogya.pharmacy.wholeSaleReports.client;

import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleBillReturnRegister_Srv;
import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleBillReturnRegister_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHParty_Mdl;
import com.google.gwt.user.client.Window;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;
import java.util.List;

public class RptWholeSaleBillReturnRegister extends DmReportLayout implements IsWidget, BlurHandler {

    String widgetName = new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption;
    ServiceInvoker<RptWholeSaleBillReturnRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
//
//    private void iniTxtHConsultant() {
//        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
//        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
//        txtHConsultant.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBillNo">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBillNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHBillNo() {
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHBillNo.getDmGrid().initializeGrid(txtHBillNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHBillNo.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedCat() {
        txtHMedCat.getDmGrid().addTextColumn(txtHMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedCat.getDmGrid().initializeGrid(txtHMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHRestrictGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHRestrictGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHRestrictGroup() {
        txtHRestrictGroup.getDmGrid().addTextColumn(txtHRestrictGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHRestrictGroup.getDmGrid().initializeGrid(txtHRestrictGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHRestrictGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);
//
//    private void iniTxtHOrganization() {
//        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
//        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
//        txtHOrganization.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="txtHLMCode">
//    interface TxtHLMCode_PrpAcs extends KeyField_T_PrpAcs<TxtHLMCode_Mdl> {
//
//        @Editor.Path("ManualCode")
//        ValueProvider<TxtHLMCode_Mdl, String> manualCode();
//
//        @Editor.Path("City")
//        ValueProvider<TxtHLMCode_Mdl, String> city();
//
//        @Editor.Path("RegNo")
//        ValueProvider<TxtHLMCode_Mdl, String> regNo();
//
//        @Editor.Path("AcName")
//        ValueProvider<TxtHLMCode_Mdl, String> acName();
//    }
//    DmTextHelpMulti<TxtHLMCode_Mdl, TxtHLMCode_PrpAcs> txtHLMCode = new DmTextHelpMulti(GWT.create(TxtHLMCode_PrpAcs.class), 6);
//
//    private void iniTxtHLMCode() {
//        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
//        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
//        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
//        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().regNo(), "Reg. No.", 100, "regNo");
//        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().acName(), "Account Name", 100, "acName");
//        txtHLMCode.getDmGrid().initializeGrid(txtHLMCode.getDmGrid().getPropertyAccess().code(), 600, 0, 3);
//        txtHLMCode.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//
//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHpaymentSource">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHpaymentSource = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 2);

    private void iniTxtHpaymentSource() {
        txtHpaymentSource.getDmGrid().addTextColumn(txtHpaymentSource.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHpaymentSource.getDmGrid().initializeGrid(txtHpaymentSource.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHpaymentSource.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCustomer">
    interface TxtHSupplierName_PrpAcs extends KeyField_T_PrpAcs<TxtHParty_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHParty_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHParty_Mdl, String> cityName();

    }
    final DmTextHelpMulti<TxtHParty_Mdl, TxtHSupplierName_PrpAcs> txtHCustomer = new DmTextHelpMulti(GWT.create(TxtHSupplierName_PrpAcs.class), 7);

    private void iniTxtHCustomer() {
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().name(), "Name", 220, "customerName");
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 80, "manualCode");
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().cityName(), "City Name", 80, "cityName");
        txtHCustomer.getDmGrid().initializeGrid(txtHCustomer.getDmGrid().getPropertyAccess().code(), 380, 0, 2);
        txtHCustomer.getHelper().addSort("customerName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedCompany">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCompany = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedCompany() {
        txtHMedCompany.getDmGrid().addTextColumn(txtHMedCompany.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHMedCompany.getDmGrid().initializeGrid(txtHMedCompany.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCompany.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        KeyFieldExtended kf = result.getData();
                        startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                    } else {
                        getReport(result.getData().getSharedKeyFieldName());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
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
//        txtHOrganization.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                SrvAsync.invokeRPC(blnCallBackWait).getOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHOrganization).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHOrganization.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganization).show();
//                        }
//                    }
//                });
//
//            }
//
//            @Override
//            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//
//            }
//        });
        txtHReportType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
//        txtHConsultant.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
//
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHConsultant.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//            }
//        });
        txtHBillNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReturnNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillNo).show();
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
        txtHMedGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHRestrictGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHResGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRestrictGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRestrictGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRestrictGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHMedCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });
        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReturnType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
//        txtHLMCode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHLMCode_Mdl>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLMCode(DmShared.xNull(txtHType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLMCode_Mdl>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHLMCode).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<TxtHLMCode_Mdl>> result) {
//                        if (result.isStatus()) {
//                            txtHLMCode.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHLMCode).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//            }
//        });

        txtHpaymentSource.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getpaymentSource(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHpaymentSource).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHpaymentSource.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHpaymentSource).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHCustomer.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHParty_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCustomer(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHParty_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCustomer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHParty_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCustomer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCustomer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMedCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCompany).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });
    }

    public RptWholeSaleBillReturnRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptWholeSaleBillReturnRegister_Srv.class), RptWholeSaleBillReturnRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHCustomer();
        addControl("customerName", "Customer Name", vl.add(txtHCustomer));

        iniTxtHReportType();
        addControl("reportType", "Report Type", vl.add(txtHReportType));

        iniTxtHVoucherType();
        addControl("saleType", "Sale Return Type", vl.add(txtHVoucherType));

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHBillNo();
        addControl("returnNo", "Return No.", vl.add(txtHBillNo));

        iniTxtHDepartment();
        addControl("departmentName", "Department", vl.add(txtHDepartment));

//        iniTxtHConsultant();
//        addControl("consultantName", "Consultant", vl.add(txtHConsultant));
        iniTxtHType();
        addControl("type", "Type", vl.add(txtHType));

//        iniTxtHOrganization();
//        addControl("organization", "Organization", vl.add(txtHOrganization));
//        iniTxtHLMCode();
//        addControl("phAccount", "Pharmacy A/c", vl.add(txtHLMCode));
        iniTxtHMedGroup();
        addControl("medicineGroup", "Medicine Group", vl.add(txtHMedGroup));

        iniTxtHMedCat();
        addControl("medicineCat", "Medicine Category", vl.add(txtHMedCat));

        iniTxtHMedicineName();
        addControl("medicineName", "Medicine Name", vl.add(txtHMedicineName));

        iniTxtHMedCompany();
        addControl("medCompany", "Medicine Company", vl.add(txtHMedCompany));

        iniTxtHRestrictGroup();
        addControl("restrictGroup", "Restricted Group", vl.add(txtHRestrictGroup));

        iniTxtHpaymentSource();
        addControl("paymentSource", "Payment Source", vl.add(txtHpaymentSource));

        swi.setWidget(this);

    }

    private void setDefaultValue() {
        txtHReportType.setTag("S");
        txtHReportType.setText("Summary");
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSite.setTag(keyField.getSharedKeyFieldCode());
                    txtHSite.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
