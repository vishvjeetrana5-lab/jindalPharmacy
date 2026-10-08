package aarogya.inventory.reports.client;

import aarogya.inventory.reports.shared.TxtHDate_Mdl;
import aarogya.inventory.reports.shared.TxtHItemCompany_Mdl;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.inventory.reports.shared.TxtHManufacturer_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;
import aarogya.inventory.reports.shared.RptPhysicalStockBeforePosting_Srv;
import aarogya.inventory.reports.shared.RptPhysicalStockBeforePosting_SrvAsync;

public class RptPhysicalStockBeforePosting extends DmReportLayout implements IsWidget {

    public final String widgetName = new pmInventory().PHYSICAL_STOCK_BEFORE_POSTING_REPROT.caption;
    private final ServiceInvoker<RptPhysicalStockBeforePosting_SrvAsync> SrvAsync;
    private DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtDepartment = new DmTextBox();
    final DmTextBox txtFilterType = new DmTextBox();
    final DmTextBox txtFilterValue = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherDate">
    interface TxtHDateOn_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHDate_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHDate_Mdl, String> departmentName();

        @Editor.Path("FilterTypeName")
        ValueProvider<TxtHDate_Mdl, String> filterTypeName();

        @Editor.Path("FilterValueName")
        ValueProvider<TxtHDate_Mdl, String> filterValueName();
    }

    final DmTextHelp<TxtHDate_Mdl, TxtHDateOn_PrpAcs> txtHPostingDate = new DmTextHelp(GWT.create(TxtHDateOn_PrpAcs.class), 6, true);

    private void iniTxtHPostingDate() {
        txtHPostingDate.getDmGrid().addTextColumn(txtHPostingDate.getDmGrid().getPropertyAccess().name(), "Date", 100, "REPLACE(CONVERT(varchar, date,106), ' ', '-')");
        txtHPostingDate.getDmGrid().addTextColumn(txtHPostingDate.getDmGrid().getPropertyAccess().departmentName(), "Department", 180, "departmentName");
        txtHPostingDate.getDmGrid().addTextColumn(txtHPostingDate.getDmGrid().getPropertyAccess().filterTypeName(), "Filter Type", 140, "filterTypeName");
        txtHPostingDate.getDmGrid().addTextColumn(txtHPostingDate.getDmGrid().getPropertyAccess().filterValueName(), "Filter Value", 180, "filterValueName");
        txtHPostingDate.getDmGrid().initializeGrid(txtHPostingDate.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 2);
        txtHPostingDate.getHelper().addSort("date", DmEnum.DmSortType.DESC);
        txtHPostingDate.getHelper().addSort("departmentName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface GridItem_PrpAcs extends KeyField_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("Unit")
        ValueProvider<TxtHItem_Mdl, String> unit();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();
    }
    DmTextHelpMulti<TxtHItem_Mdl, GridItem_PrpAcs> txtHItemName = new DmTextHelpMulti(GWT.create(GridItem_PrpAcs.class), 5);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 0, "manualCode");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHItemName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemCategory">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHItemCategory() {
        txtHItemCategory.getDmGrid().addTextColumn(txtHItemCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemCategory.getDmGrid().initializeGrid(txtHItemCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemCategory.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHItemGroup() {
        txtHItemGroup.getDmGrid().addTextColumn(txtHItemGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHItemGroup.getDmGrid().initializeGrid(txtHItemGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHItemGroup.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHManufacturer">
    interface TxtHManufacturer_PrpAcs extends KeyField_T_PrpAcs<TxtHManufacturer_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHManufacturer_Mdl, String> city();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHManufacturer_Mdl, String> manualCode();

    }
    final DmTextHelpMulti<TxtHManufacturer_Mdl, TxtHManufacturer_PrpAcs> txtHManufacturer = new DmTextHelpMulti(GWT.create(TxtHManufacturer_PrpAcs.class), 6, false);

    private void iniTxtHManufacturer() {
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().city(), "City", 100, "");
        txtHManufacturer.getDmGrid().initializeGrid(txtHManufacturer.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHManufacturer.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemMake">
    interface TxtHItemCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHItemCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHItemCompany_Mdl, String> shortName();
    }
    final DmTextHelpMulti<TxtHItemCompany_Mdl, TxtHItemCompany_PrpAcs> txtHItemCompany = new DmTextHelpMulti(GWT.create(TxtHItemCompany_PrpAcs.class), 6, false);

    private void iniTxtHItemCompany() {
        txtHItemCompany.getDmGrid().addTextColumn(txtHItemCompany.getDmGrid().getPropertyAccess().name(), "Name", 100, "companyName");
        txtHItemCompany.getDmGrid().addTextColumn(txtHItemCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "shortName");
        txtHItemCompany.getDmGrid().initializeGrid(txtHItemCompany.getDmGrid().getPropertyAccess().code(), 400, 0, 2);

        txtHItemCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
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

    private void txtHelpEvents() {

        txtHPostingDate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDate_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDate(DmShared.xNull(txtHReportType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDate_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPostingDate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDate_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPostingDate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPostingDate).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDate_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtDepartment.setTag(t1Row.getDepartmentCode());
                    txtDepartment.setText(t1Row.getDepartmentName());
                    txtFilterType.setTag(t1Row.getFilterTypeCode());
                    txtFilterType.setText(t1Row.getFilterTypeName());
                    txtFilterValue.setTag(t1Row.getFilterValueCode());
                    txtFilterValue.setText(t1Row.getFilterValueName());
                } else {
                    txtDepartment.setTag("");
                    txtDepartment.setText("");
                    txtFilterType.setTag("");
                    txtFilterType.setText("");
                    txtFilterValue.setTag("");
                    txtFilterValue.setText("");
                }
            }
        });

        txtHItemName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItem_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });

        txtHItemCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemCategory).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemCategory).show();
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHItemCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItemCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });
        txtHManufacturer.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHManufacturer_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHManufacturer(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHManufacturer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHManufacturer_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHManufacturer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHManufacturer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });

    }

    public RptPhysicalStockBeforePosting(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPhysicalStockBeforePosting_Srv.class), RptPhysicalStockBeforePosting.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtHelpEvents();
        setDefault();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        iniTxtHReportType();
        addControl("reportType", "Report Type ", vl.add(txtHReportType));

        iniTxtHPostingDate();
        addControl("postingDate", "Posting Date", vl.add(txtHPostingDate));

        addControl("departmentCode", "Department", vl.add(txtDepartment));
        addControl("filterType", "Filter Type", vl.add(txtFilterType));
        addControl("filterValue", "Filter Value", vl.add(txtFilterValue));

        iniTxtHItemGroup();
        addControl("itemGroup", "Item Group", vl.add(txtHItemGroup));

        iniTxtHItemCategory();
        addControl("itemCategory", "Item Category", vl.add(txtHItemCategory));

        iniTxtHManufacturer();
        addControl("manufacturer", "Manufacturer", vl.add(txtHManufacturer));

        iniTxtHItemCompany();
        addControl("itemCompany", "Item Make", vl.add(txtHItemCompany));

        iniTxtHItemName();
        addControl("itemName", "Item Name", vl.add(txtHItemName));

        swi.setWidget(this);

    }

    private void setDefault() {
        txtDepartment.setEnabled(false);
        txtFilterType.setEnabled(false);
        txtFilterValue.setEnabled(false);
        txtHReportType.setText("Physical Stock");
        txtHReportType.setTag("PS");
    }

}
