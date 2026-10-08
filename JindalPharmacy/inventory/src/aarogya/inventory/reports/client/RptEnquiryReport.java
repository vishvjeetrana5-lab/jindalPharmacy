package aarogya.inventory.reports.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.reports.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptEnquiryReport extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmInventory().ENQUIRY_REGISTER.caption;
    ServiceInvoker<RptEnquiryReport_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHEnquiryType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHEnquiryType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHEnquiryType() {
        txtHEnquiryType.getDmGrid().addTextColumn(txtHEnquiryType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHEnquiryType.getDmGrid().initializeGrid(txtHEnquiryType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHEnquiryType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface Supplier_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    DmTextHelpMulti<KeyFieldExtended, Supplier_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(Supplier_PrpAcs.class), 5);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedModifiedBy(), "City", 100, "city");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHItemGroup() {
        txtHItemGroup.getDmGrid().addTextColumn(txtHItemGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        txtHItemGroup.getDmGrid().initializeGrid(txtHItemGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemGroup.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHItemCat() {
        txtHItemCat.getDmGrid().addTextColumn(txtHItemCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemCat.getDmGrid().initializeGrid(txtHItemCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemCat.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItem">
    interface GridItem_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("Unit")
        ValueProvider<TxtHItem_Mdl, String> unit();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();
    }
    DmTextHelpMulti<TxtHItem_Mdl, GridItem_PrpAcs> txtHItem = new DmTextHelpMulti(GWT.create(GridItem_PrpAcs.class), 10);

    private void iniTxtHItem() {
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "manualCode");
        txtHItem.getDmGrid().initializeGrid(txtHItem.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
        txtHItem.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIndent">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIndent = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHIndent() {
        txtHIndent.getDmGrid().addTextColumn(txtHIndent.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHIndent.getDmGrid().initializeGrid(txtHIndent.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHIndent.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHEnquiryNo">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHEnquiryNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHEnquiryNo() {
        txtHEnquiryNo.getDmGrid().addTextColumn(txtHEnquiryNo.getDmGrid().getPropertyAccess().name(), "Enquiry No.", 0, "recid");
        txtHEnquiryNo.getDmGrid().initializeGrid(txtHEnquiryNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHEnquiryNo.getHelper().addSort("recid", DmEnum.DmSortType.ASC);
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

    private void txtHelpEvents() {

        txtHEnquiryType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEnquiryType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEnquiryType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEnquiryType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEnquiryType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVendor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHItemGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItemCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHIndent.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndent(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndent).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndent.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndent).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItem.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItem_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
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

        txtHEnquiryNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEnquiryNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEnquiryNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEnquiryNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEnquiryNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });

    }

    public RptEnquiryReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptEnquiryReport_Srv.class), RptEnquiryReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefault();
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

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHEnquiryType();
        addControl("enquiryType", "Enquiry Type", vl.add(txtHEnquiryType));

        iniTxtHEnquiryNo();
        addControl("enquiryno", "Enquiry No.", vl.add(txtHEnquiryNo));

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));

        iniTxtHIndent();
        addControl("indent", "Indent", vl.add(txtHIndent));

        iniTxtHItemGroup();
        addControl("itemGroup", "Item Group", vl.add(txtHItemGroup));

        iniTxtHItemCat();
        addControl("itemCat", "Item Category", vl.add(txtHItemCat));

        iniTxtHItem();
        addControl("item", "Item", vl.add(txtHItem));

        swi.setWidget(this);

    }

    private void setDefault() {
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
