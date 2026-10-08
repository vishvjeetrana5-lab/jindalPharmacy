package dataman.accounts.reports.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.reports.shared.RptPurchaseRegister_Srv;
import dataman.accounts.reports.shared.RptPurchaseRegister_SrvAsync;
import dataman.accounts.reports.shared.RptPurchaseRegister_Vld;
import dataman.accounts.reports.shared.TxtHItemMedCompany_Mdl;
import dataman.accounts.reports.shared.TxtHItemMedicine_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public class RptPurchaseRegister extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmAccounts().PURCHASE_REGISTER.caption;
    private final ServiceInvoker<RptPurchaseRegister_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPbill">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPbill = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHPBill() {
        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "name");
        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Ref. No.", 100, "partyBillNo");
        txtHPbill.getDmGrid().initializeGrid(txtHPbill.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHPbill.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBillType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBillType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 8);

    private void iniTxtHPurchaseType() {
        txtHBillType.getDmGrid().addTextColumn(txtHBillType.getDmGrid().getPropertyAccess().name(), "Type", 1, "name");
        txtHBillType.getDmGrid().initializeGrid(txtHBillType.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHBillType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsApproved">
    DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHIsApproved = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 5, true, false);

    private void initxtHIsApproved() {
        txtHIsApproved.getDmGrid().addTextColumn(txtHIsApproved.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHIsApproved.getDmGrid().initializeGrid(txtHIsApproved.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier"> 
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedModifiedBy(), "City", 100, "city");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPorder">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPorder = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHPorder() {
        txtHPorder.getDmGrid().addTextColumn(txtHPorder.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHPorder.getDmGrid().initializeGrid(txtHPorder.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHPorder.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItemMedGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHItemMedGroup() {
        txtHItemMedGroup.getDmGrid().addTextColumn(txtHItemMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemMedGroup.getDmGrid().initializeGrid(txtHItemMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemMedGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHItemMedCompany">
    interface TxtHItemMedCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHItemMedCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHItemMedCompany_Mdl, String> shortName();
    }

    final DmTextHelpMulti<TxtHItemMedCompany_Mdl, TxtHItemMedCompany_PrpAcs> txtHCompany = new DmTextHelpMulti(GWT.create(TxtHItemMedCompany_PrpAcs.class), 7);

    private void iniTxtHItemMedCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 2);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItemMedCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHItemMedCat() {
        txtHItemMedCat.getDmGrid().addTextColumn(txtHItemMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemMedCat.getDmGrid().initializeGrid(txtHItemMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemMedCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemMedicine">
    interface GridMed_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemMedicine_Mdl> {

        @Editor.Path("Unit")
        ValueProvider<TxtHItemMedicine_Mdl, String> unit();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemMedicine_Mdl, String> manualCode();
    }
    DmTextHelpMulti<TxtHItemMedicine_Mdl, GridMed_PrpAcs> txtHItemMedicine = new DmTextHelpMulti(GWT.create(GridMed_PrpAcs.class), 10);

    private void iniTxtHItemMedicine() {
        txtHItemMedicine.getDmGrid().addTextColumn(txtHItemMedicine.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemMedicine.getDmGrid().addTextColumn(txtHItemMedicine.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "manualCode");
        txtHItemMedicine.getDmGrid().initializeGrid(txtHItemMedicine.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
        txtHItemMedicine.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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

                    KeyFieldExtended kf = result.getData();
                    startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }

        }
        );
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
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

    private void txtHelpEvents() {

        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        
        txtHBillType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        
        txtHItemMedGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemMedicineGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemMedGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItemMedCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemMedicineCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemMedCat).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemMedCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemMedCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItemMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItemMedicine_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemMedicine).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHPorder.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPorder(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPorder).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPorder.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPorder).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHPbill.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPbill(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPbill).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPbill.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPbill).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItemMedCompany_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemMedCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemMedCompany_Mdl>> result) {
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
        
        txtHIsApproved.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIsActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsApproved).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsApproved.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsApproved).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    public RptPurchaseRegister(SetWidgetInvoker swi) {
        super();
        srvAsync= new ServiceInvoker<>(GWT.create(RptPurchaseRegister_Srv.class), RptPurchaseRegister.class.getName());

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

        iniTxtHModule();
        addControl("module", "Module", vl.add(txtHModule));
        txtHModule.setText("Purchase");
        txtHModule.setTag("PU");

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));
        
        iniTxtHPurchaseType();
        addControl("billType", "Bill Type", vl.add(txtHBillType));

        iniTxtHPorder();
        addControl("pOrder", "PO No.", vl.add(txtHPorder));

        iniTxtHPBill();
        addControl("pBill", "Voucher No.", vl.add(txtHPbill));
        
        iniTxtHItemMedGroup();
        addControl("itemMedGroup", "Medicine Group", vl.add(txtHItemMedGroup));

        iniTxtHItemMedCat();
        addControl("itemMedCat", "Medicine Category", vl.add(txtHItemMedCat));

        iniTxtHItemMedCompany();
        addControl("itemMedCom", "Item/Medicine Company", vl.add(txtHCompany));

        iniTxtHItemMedicine();
        addControl("itemMedicine", "Item/Medicine", vl.add(txtHItemMedicine));
        
        initxtHIsApproved();
        addControl("isApproved", "IsApprove", vl.add(txtHIsApproved));

        swi.setWidget(this);
    }

}
