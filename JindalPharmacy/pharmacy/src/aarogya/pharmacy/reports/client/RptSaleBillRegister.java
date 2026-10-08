package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptSaleBillRegister_Srv;
import aarogya.pharmacy.reports.shared.RptSaleBillRegister_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHSaleBillNo_Mdl;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptSaleBillRegister extends DmReportLayout implements IsWidget, BlurHandler {

    String widgetName = new pmPharmacy().SALE_BILL_REGISTER.caption;
    ServiceInvoker<RptSaleBillRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);
    private String category = "", recIdPrefixTo = "", vNoTo = "", recIdPrefixFrom = "", vNoFrom = "";

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHOrganization() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHOrganization.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="txtHpaymentSource">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHpaymentSource = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 2);

    private void iniTxtHpaymentSource() {
        txtHpaymentSource.getDmGrid().addTextColumn(txtHpaymentSource.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHpaymentSource.getDmGrid().initializeGrid(txtHpaymentSource.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHpaymentSource.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHConsultant.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHLMCode">
    interface TxtHLMCode_PrpAcs extends KeyField_T_PrpAcs<TxtHLMCode_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHLMCode_Mdl, String> manualCode();

        @Editor.Path("City")
        ValueProvider<TxtHLMCode_Mdl, String> city();

        @Editor.Path("RegNo")
        ValueProvider<TxtHLMCode_Mdl, String> regNo();

        @Editor.Path("AcName")
        ValueProvider<TxtHLMCode_Mdl, String> acName();
    }
    DmTextHelpMulti<TxtHLMCode_Mdl, TxtHLMCode_PrpAcs> txtHLMCode = new DmTextHelpMulti(GWT.create(TxtHLMCode_PrpAcs.class), 6);

    private void iniTxtHLMCode() {
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().regNo(), "Reg. No.", 100, "regNo");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().acName(), "Account Name", 100, "acName");
        txtHLMCode.getDmGrid().initializeGrid(txtHLMCode.getDmGrid().getPropertyAccess().code(), 600, 0, 3);
        txtHLMCode.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
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
    interface TxtHBillNo_PrpAcs extends KeyField_T_PrpAcs<TxtHSaleBillNo_Mdl> {

        @Editor.Path("RecIdPrefix")
        ValueProvider<TxtHSaleBillNo_Mdl, String> recIdPrefix();

        @Editor.Path("VNo")
        ValueProvider<TxtHSaleBillNo_Mdl, String> vNo();
    }
    DmTextHelpMulti<TxtHSaleBillNo_Mdl, TxtHBillNo_PrpAcs> txtHBillNo = new DmTextHelpMulti(GWT.create(TxtHBillNo_PrpAcs.class), 10);

    private void iniTxtHBillNo() {
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHBillNo.getDmGrid().initializeGrid(txtHBillNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHBillNo.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHBillNo.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHBillNo.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHBillNo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHFromBillNo">
    interface TxtHFromBillNo_PrpAcs extends KeyField_T_PrpAcs<TxtHSaleBillNo_Mdl> {

        @Editor.Path("RecIdPrefix")
        ValueProvider<TxtHSaleBillNo_Mdl, String> recIdPrefix();

        @Editor.Path("VNo")
        ValueProvider<TxtHSaleBillNo_Mdl, String> vNo();
    }
    DmTextHelp<TxtHSaleBillNo_Mdl, TxtHFromBillNo_PrpAcs> txtHFromBillNo = new DmTextHelp(GWT.create(TxtHFromBillNo_PrpAcs.class), 10);

    private void iniTxtHFromBillNo() {
        txtHFromBillNo.getDmGrid().addTextColumn(txtHFromBillNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHFromBillNo.getDmGrid().initializeGrid(txtHFromBillNo.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHFromBillNo.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHFromBillNo.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHFromBillNo.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHFromBillNo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHToBillNo">
    interface TxtHToBillNo_PrpAcs extends KeyField_T_PrpAcs<TxtHSaleBillNo_Mdl> {

        @Editor.Path("RecIdPrefix")
        ValueProvider<TxtHSaleBillNo_Mdl, String> recIdPrefix();

        @Editor.Path("VNo")
        ValueProvider<TxtHSaleBillNo_Mdl, String> vNo();
    }

    DmTextHelp<TxtHSaleBillNo_Mdl, TxtHToBillNo_PrpAcs> txtHToBillNo = new DmTextHelp(GWT.create(TxtHToBillNo_PrpAcs.class), 10);

    private void iniTxtHToBillNo() {
        txtHToBillNo.getDmGrid().addTextColumn(txtHToBillNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHToBillNo.getDmGrid().initializeGrid(txtHToBillNo.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHToBillNo.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHToBillNo.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHToBillNo.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHToBillNo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="txtHValueType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHValueType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHValueType() {
        txtHValueType.getDmGrid().addTextColumn(txtHValueType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHValueType.getDmGrid().initializeGrid(txtHValueType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHValueType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHSubCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSubCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSubCat() {
        txtHSubCat.getDmGrid().addTextColumn(txtHSubCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSubCat.getDmGrid().initializeGrid(txtHSubCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSubCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="txtHOperation">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOperation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOperation() {
        txtHOperation.getDmGrid().addTextColumn(txtHOperation.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHOperation.getDmGrid().initializeGrid(txtHOperation.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHOperation.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
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
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), widgetName, getFilter(), reportProperty, category, recIdPrefixFrom, vNoFrom, recIdPrefixTo, vNoTo, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        startDownloading(data.getSharedKeyFieldName(), data.getSharedKeyFieldCode());
                    } else {
                        getReport(data.getSharedKeyFieldCode());
                    }
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
        txtHOperation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOperation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOperation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOperation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOperation).show();
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
        txtHBillNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHSaleBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(category, txtHSite.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSaleBillNo_Mdl>> result) {
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
        txtHFromBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSaleBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(category, txtHSite.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFromBillNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSaleBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHFromBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFromBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSaleBillNo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    recIdPrefixFrom = t1Row.getRecIdPrefix();
                    vNoFrom = t1Row.getVNo();
                }
            }
        });
        txtHToBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSaleBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(category, txtHSite.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHToBillNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSaleBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHToBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHToBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSaleBillNo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    vNoTo = t1Row.getVNo();
                    recIdPrefixTo = t1Row.getRecIdPrefix();
                }
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
        txtHSubCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubCat).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillType(category, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHLMCode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHLMCode_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLMCode(DmShared.xNull(txtHType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLMCode_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLMCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLMCode_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLMCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLMCode).show();
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
        txtHValueType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHValueType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHValueType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHValueType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHValueType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }

        });

        txtHOrganization.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

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

    }

    public RptSaleBillRegister(SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptSaleBillRegister_Srv.class), RptSaleBillRegister.class.getName());
        if (paramHashMap != null) {
            category = (String) paramHashMap.get("category");
        }
//        if (!DmShared.xNull(category).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL)
//                && !DmShared.xNull(category).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL_CASH)) {
//            widgetName = new pmPharmacy().ESTIMATE_BILL_REGISTER.caption;
//        }
        if (!(DmShared.xNull(category).contains(new ProjectVoucherCategory().PHARMACY_BILL)
                && DmShared.xNull(category).contains(new ProjectVoucherCategory().PHARMACY_BILL_CASH))) {
            widgetName = new pmPharmacy().ESTIMATE_BILL_REGISTER.caption;
        }
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
        String caption = "";
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHReportType();
        addControl("reportType", "Report Type", vl.add(txtHReportType));

        iniTxtHVoucherType();
        if (category.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL) || category.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL_CASH)) {
            caption = "Sale Type";
        } else {
            caption = "Estimate Type";
        }
        addControl("saleType", caption, vl.add(txtHVoucherType));

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHFromBillNo();
        addControl("fromBillNo", "From Bill No.", vl.add(txtHFromBillNo));

        iniTxtHToBillNo();
        addControl("toBillNo", "To Bill No.", vl.add(txtHToBillNo));

        iniTxtHBillNo();
        addControl("billNo", "Bill No.", vl.add(txtHBillNo));

        iniTxtHDepartment();
        addControl("departmentName", "Department", vl.add(txtHDepartment));

        iniTxtHConsultant();
        addControl("consultantName", "Consultant", vl.add(txtHConsultant));

        iniTxtHType();
        addControl("type", "Type", vl.add(txtHType));

        iniTxtHLMCode();
        addControl("phAccount", "Pharmacy A/c", vl.add(txtHLMCode));

        iniTxtHOperation();
        addControl("operation", "Operation", vl.add(txtHOperation));

        iniTxtHMedGroup();
        addControl("medicineGroup", "Medicine Group", vl.add(txtHMedGroup));

        iniTxtHMedCat();
        addControl("medicineCat", "Medicine Category", vl.add(txtHMedCat));

        iniTxtHSubCat();
        addControl("subCat", "Sub Category", vl.add(txtHSubCat));

        iniTxtHMedicineName();
        addControl("medicineName", "Medicine Name", vl.add(txtHMedicineName));

        iniTxtHRestrictGroup();
        addControl("restrictGroup", "Restricted Group", vl.add(txtHRestrictGroup));

        iniTxtHOrganization();
        addControl("organization", "Organization", vl.add(txtHOrganization));

        iniTxtHValueType();
        addControl("valueType", "Value Type", vl.add(txtHValueType));

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
