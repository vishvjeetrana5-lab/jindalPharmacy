package aarogya.inventory.purchaseOrderDisplay.client;

import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.SearchValue_Mdl;
import aarogya.selfToolkit.stStatic.shared.MultiRequisition_Model;
import aarogya.inventory.purchaseOrderDisplay.shared.*;
import aarogya.inventory.self.shared.TrigeredAction_Model;

import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Srv;
import java.util.Comparator;
import java.io.*;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_SrvAsync;
import aarogya.projectMenu.self.shared.pmInventory;

public class PurchaseOrderDisplay extends PurchaseOrderDisplay_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<PurchaseOrderDisplay_SrvAsync> SrvAsync;
    private SearchValue_Mdl svm = new SearchValue_Mdl();
    private String searchDbFieldName, searchPrvValue = "";
//    private final ApprovedVendors approvedVendors = new ApprovedVendors();
    private final ItemDetail medDetail = new ItemDetail(gridOrder, "Medicine History/Approved Vendors", "770px", "500px");
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private final SettingPopup settingsPopup = new SettingPopup();
    private boolean flagOpenAsPopup = false;
    private int searchColumnIndex;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private boolean checkAll = false;

    private enum SearchColumn {
        SR(0), FROM_DEPARTMENT(1), MEDICINE_NAME(2), PENDING_REQ_FROM(3), PENDING_REQ_TO(4), CURR_STK(5), PENDING_INDENT(6), REQUI_DATE(7);
        int searchColIndex;

        public int getSearchColIndex() {
            return searchColIndex;
        }

        SearchColumn(int searchColIndex) {
            this.searchColIndex = searchColIndex;
        }
    }

    private SearchColumn searchColumn;
    private final List<PurchaseOrderDisplay_Mdl> lstData = new ArrayList<>();

    public PurchaseOrderDisplay(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<PurchaseOrderDisplay_SrvAsync>(GWT.create(PurchaseOrderDisplay_Srv.class), PurchaseOrderDisplay.class.getName());
        gridEvents();
        flwForm.addAttachHandler(this);
        handlerEvents();
        textHelpEvents();
        manageDisplay();
        iniDefaultValue(swi);
//        fillgrid(svm);

    }

    private void handlerEvents() {
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnHelp.addClickHandler(this);
        btnShortOnSupplier.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addDomHandler(this, KeyDownEvent.getType());
        cbCheck.addClickHandler(this);
    }

    private void textHelpEvents() {
        settingsPopup.addCallBack(new SettingPopup.CallBack() {

            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                keyFields.clear();
                svm = searchValue;
//                manageGridColumns(DmShared.xNull(svm.getGroupOnName()));
                fillgrid(svm);
            }
        });

        txtHDepartmentBy.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });

        txtHDepartmentBy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentBy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                clear();
                fillgrid(svm);
            }
        });
    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.FROM_DEPARTMENT);

        txtSearchValue.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) gridOrder.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });

        gridOrder.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
//                setHeaderValue(event.getColumnIndex());
                for (SearchColumn column : SearchColumn.values()) {
                    if (column.ordinal() == event.getColumnIndex()) {
                        setSearchColumn(column);
                    }
                }
                setAllChkBox(event.getColumnIndex());
            }
        });

        gridOrder.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == intCheck) {
                        setTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        gridOrder.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                String itemCode = "";
                if (event.getColumnIndex() == intApprovedSupplier) {
                    itemCode = gridOrder.getGrid().getSelectionModel().getSelectedItem().getItemCode();
                    medDetail.show(itemCode, event.getRowIndex());
                }
            }
        });

        medDetail.addCallBack(new ItemDetail.CallBack() {
            @Override
            public void ok(ApprovedVendors_Mdl model, int gridRowIndex) {
                if (model != null) {
                    gridOrder.getGrid().getStore().get(gridRowIndex).setLastOrderSupplier(model.getSharedKeyFieldName());
                    gridOrder.getGrid().getStore().get(gridRowIndex).setLastOrderSupplierCode(model.getSharedKeyFieldCode());
                    gridOrder.getGrid().getStore().get(gridRowIndex).setLastOrderRate(model.getRate());
                    gridOrder.commitChanges();
                }
            }

            @Override
            public void close() {
            }
        });
    }

    private void setTick(final int rowIndex) {
        int val = DmShared.vNull(gridOrder.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        final KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(DmShared.iNull(gridOrder.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());

        if (val == 0) {
            new DmConfirmMessageBox("Do you want to check for same suplier? ", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    long currSupplierCode = 0;
                    currSupplierCode = DmShared.iNull(gridOrder.getGrid().getStore().get(rowIndex).getLastOrderSupplierCode()).longValue();
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        keyFields.clear();
                        for (int i = 0; i < gridOrder.getGrid().getStore().size(); i++) {
                            if (DmShared.iNull(gridOrder.getGrid().getStore().get(i).getLastOrderSupplierCode()).longValue() == currSupplierCode) {
                                gridOrder.getGrid().getStore().get(i).setIsChecked("1");
                                field.setSharedKeyFieldCode(DmShared.iNull(gridOrder.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                                keyFields.add(field);
                            } else {
                                gridOrder.getGrid().getStore().get(i).setIsChecked("0");
                            }
                            gridOrder.commitChanges();
                        }
//                        setHeaderStyle();
                    } else {
                        gridOrder.getGrid().getStore().get(rowIndex).setIsChecked("1");
                        if (!keyFields.contains(field)) {
                            keyFields.add(field);
                        }
                        gridOrder.commitChanges();
                    }
                }
            }).show();

        } else if (val == 1) {
            gridOrder.getGrid().getStore().get(rowIndex).setIsChecked("0");

            for (int i = 0; i < keyFields.size(); i++) {
                if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                    keyFields.remove(i);
                }
            }
        }
        gridOrder.commitChanges();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseOrderDisplay.this);
                    }
                }
            }).show();
        } else if (event.getSource() == btnSetting) {
            settingsPopup.show(svm);
        } else if (event.getSource().equals(btnHelp)) {
            pnlGrd1.clear();
            pnlGrd1.showRelativeTo(btnHelp);
            pnlGrd1.add(txtHelp);
            txtHelp.setText("");
            txtHelp.setReadOnly(true);
        } else if (event.getSource().equals(cbCheck)) {
            manageAllCheck();
        } else if (event.getSource() == btnRefresh) {
            clear();
            fillgrid(svm);
        } else if (event.getSource().equals(btnShortOnSupplier)) {

            if (!DmShared.xNull(svm.getAsOnDate()).trim().equals("")) {
                gridOrder.commitChanges();
                List<PurchaseOrderDisplay_Mdl> lst = new ArrayList<PurchaseOrderDisplay_Mdl>();
                for (PurchaseOrderDisplay_Mdl model : gridOrder.getGrid().getStore().getAll()) {
                    lst.add(model);
                }
                SrvAsync.invokeRPC().getShortOnSupplier(lst, new AsyncCallback<DmResult<List<PurchaseOrderDisplay_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<List<PurchaseOrderDisplay_Mdl>> result) {
                        if (result.isStatus()) {
                            gridOrder.getGrid().getStore().clear();
                            gridOrder.getGrid().getStore().addAll(result.getData());
                            gridOrder.commitChanges();

                            lstData.clear();
                            lstData.addAll(result.getData());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

        } else if (event.getSource() instanceof InlineLabel) {
            TrigeredAction_Model saveAction;

            if (keyFields.size() == 0) {
                new DmAlertMessageBox("No entry found for process.", txtSearchValue).show();
                return;
            }
            if (chkSameSupplier() == false) {
                new DmAlertMessageBox("Supplier must be same.", (DmTextBox) null).show();
                return;
            }
            //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
            //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
            try {

                saveAction = new TrigeredAction_Model();
                String moduleName = "", mnuName = "";
                String[] title;
                title = ((InlineLabel) event.getSource()).getTitle().split("\\|");
                if (title.length == 2) {
                    moduleName = DmShared.xNull(title[0]).trim();
                    mnuName = DmShared.xNull(title[1]).trim();
                }
                if (!(moduleName.equalsIgnoreCase("") && mnuName.equalsIgnoreCase(""))) {
                    if (moduleName.equalsIgnoreCase(new pmInventory().moduleName) && mnuName.equalsIgnoreCase(new pmInventory().PURCHASE_ORDER.caption)) {
                        saveAction.setStartupAction("I");
                        saveAction.setPerformedAction("I");
                        invokeWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_ORDER.caption, fillGridDetailForIndentRequisition(), "ADD_ORDER_WITH_PENDING_INDENT", "FOCUS_ON_ADD", true, false, null, true);
                    }
                }
            } catch (DmException e) {
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            fillgrid(svm);
        } else {
            pnlGrd1.hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
//            fillgrid();
            fillFilteredData();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void manageDisplay() {
        txtDate.setEnabled(false);
        txtRequisitionNo.setEnabled(false);
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return value_Mdl;
    }

//    private void setHeaderValue(int columnIndex) {
//        if ((gridOrder.getGrid().getColumnModel().getColumnCount()-1) < columnIndex) {
//            searchColumnIndex = 0;
//        } else if (columnIndex < 0) {
//            searchColumnIndex = gridOrder.getGrid().getColumnModel().getColumnCount() - 1;
//        } else {
//            searchColumnIndex = columnIndex;
//        }
//
//        DmColumnConfig col = ((DmColumnConfig) gridOrder.getGrid().getColumnModel().getColumn(searchColumnIndex));
//        searchDbFieldName = col.getDbFieldName();
//        searchDbFieldDataType = col.getDbFieldDataType();
//        txtSearchValue.setText("");
//        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
//
//for (SearchColumn column : SearchColumn.values()) {
//                    if (column.ordinal() == columnIndex-1) {
//                        setSearchColumn(column);
//                    }
//                }
//
//        for (ColumnConfig col1 : gridOrder.getGrid().getColumnModel().getColumns()) {
//            col1.setColumnHeaderClassName("dm-Garbage");
//        }
//        col.setColumnHeaderClassName("dm-ColActiveForFilter");
//        gridOrder.getGrid().getView().refresh(true);
//        focusToSearchValue();
//    }
    private void focusToSearchValue() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void clear() {
        txtDate.setText("");
        txtRequisitionNo.setTag("");
        txtRequisitionNo.setTag2nd("");
        txtRequisitionNo.setTag("");
        txtRequisitionNo.setText("");
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == 0) {
            List<PurchaseOrderDisplay_Mdl> lst = gridOrder.getGrid().getStore().getAll();
            KeyFieldExtended field;
            if (headerValue == 0) {
                headerValue = 1;
                keyFields.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    if (DmShared.vNull(gridOrder.getGrid().getStore().get(rowIndex).getIsChecked()).intValue() == 0) {
//                    gridOrder.getDmGrid().getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                        field = new KeyFieldExtended();
                        gridOrder.getGrid().getStore().get(rowIndex).setIsChecked("1");
                        field.setSharedKeyFieldCode(DmShared.iNull(gridOrder.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                        keyFields.add(field);
                    }
                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    gridOrder.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    keyFields.clear();
                }
            }
            gridOrder.commitChanges();
//            setHeaderStyle();
        }
    }

    private void setHeaderStyle() {
        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                if (headerValue == 1) {
                    headerStyle = "<span class = 'fa fa-check-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                }
                return headerStyle;
            }
        });
        gridOrder.getGrid().getView().getHeader().refresh();
    }

    private void manageAllCheck() {
        if (!checkAll) {
            checkAll = true;
            cbCheck.setStyleName("fa fa-check-square-o");
        } else {
            checkAll = false;
            cbCheck.setStyleName("fa fa-square-o");
        }
        setAllChkBox(0);
    }

    private void fillgrid(SearchValue_Mdl svm) {
        if (!DmShared.xNull(svm.getAsOnDate()).trim().equals("")) {
            SrvAsync.invokeRPC().getFillRecords(svm, new AsyncCallback<DmResult<List<PurchaseOrderDisplay_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<List<PurchaseOrderDisplay_Mdl>> result) {
                    if (result.isStatus()) {
                        gridOrder.getGrid().getStore().clear();
                        gridOrder.getGrid().getStore().addAll(result.getData());
                        gridOrder.commitChanges();

                        lstData.clear();
                        lstData.addAll(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        }
    }

    @Override
    public void searchRecord(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void invokeWidget(String moduleName, String menuName, final Object object, final String firstAction, final String secondAction, final boolean isCallBack,
            final boolean isHashMapInAction, final HashMap paramHashMap, final boolean isObject) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
                if (!isCallBack && !DmShared.xNull(firstAction).trim().equalsIgnoreCase("")) {
                    widget.action(firstAction, isObject ? (List<MultiRequisition_Model>) object : DmShared.iNull(object).toString());
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                } else if (!DmShared.xNull(firstAction).equalsIgnoreCase("") && !DmShared.xNull(secondAction).equalsIgnoreCase("")) {
                    widget.action(firstAction, isHashMapInAction ? paramHashMap : (isObject ? (List<MultiRequisition_Model>) object : DmShared.iNull(object).toString()),
                            new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).action(secondAction, null, null);
                                }
                            }
                        }
                    });
                } else {
                    widget.searchRecord((String) object);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            }
        });
    }

    private List<PurchaseOrderDisplay_Mdl> fillGridDetailForIndentRequisition() {
        List<PurchaseOrderDisplay_Mdl> lst = new ArrayList();
        int i = 0;
        for (PurchaseOrderDisplay_Mdl mdl : gridOrder.getGrid().getStore().getAll()) {
            if (DmShared.xNull(mdl.getIsChecked()).equalsIgnoreCase("1")) {
                lst.add(gridOrder.getGrid().getStore().get(i));
            }
            i++;
        }
        return lst;
    }

    private boolean chkSameSupplier() {
        boolean blnok = true;
        int i = 0;
        String prvSupplier = "";
        for (PurchaseOrderDisplay_Mdl mdl : gridOrder.getGrid().getStore().getAll()) {
            if (DmShared.xNull(mdl.getIsChecked()).equalsIgnoreCase("1")) {
                if (DmShared.xNull(prvSupplier).equalsIgnoreCase("")) {
                    prvSupplier = gridOrder.getGrid().getStore().get(i).getLastOrderSupplierCode();
                } else if (!DmShared.xNull(prvSupplier).equalsIgnoreCase("") && !DmShared.xNull(prvSupplier).equalsIgnoreCase(DmShared.xNull(gridOrder.getGrid().getStore().get(i).getLastOrderSupplierCode()))) {
                    blnok = false;
                    break;
                }
            }
            i++;
        }
        return blnok;
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    txtHDepartmentBy.setTag(result.getData().getSharedKeyFieldCode());
                    txtHDepartmentBy.setText(result.getData().getSharedKeyFieldName());
                    swi.setWidget(PurchaseOrderDisplay.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        if (searchColumn.PENDING_REQ_TO != searchColumn && searchColumn.CURR_STK != searchColumn) {
            DmColumnConfig col = ((DmColumnConfig) gridOrder.getGrid().getColumnModel().getColumn(searchColumn.getSearchColIndex()));
            for (ColumnConfig col1 : gridOrder.getGrid().getColumnModel().getColumns()) {
                col1.setColumnHeaderClassName("dm-Garbage");
            }
            col.setColumnHeaderClassName("dm-ColActiveForFilter");
            gridOrder.getGrid().getView().refresh(true);
            txtSearchValue.setText("");
            txtSearchValue.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
            txtSearchValue.setFocus(true);
        }

    }

    private void fillFilteredData() {
        String searchText = DmShared.xNull(txtSearchValue.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<PurchaseOrderDisplay_Mdl> lstFilteredData = new ArrayList<>();
            for (PurchaseOrderDisplay_Mdl gMdl : lstData) {
                switch (searchColumn) {
                    case FROM_DEPARTMENT:
                        matchWith = gMdl.getDepartmentName();
                        break;
                    case MEDICINE_NAME:
                        matchWith = gMdl.getItemName();
                        break;
                    case PENDING_REQ_FROM:
//                        matchWith = gMdl.getRequisitionFromDeptQty();
                        break;
                    case PENDING_REQ_TO:
//                        matchWith = gMdl.getRequisitionToDeptQty();
                        break;
                    case PENDING_INDENT:
//                        matchWith = gMdl.getPendingIndentQty();
                        break;
                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            gridOrder.getGrid().getStore().clear();
            gridOrder.getGrid().getStore().addAll(lstFilteredData);
        } else {
            gridOrder.getGrid().getStore().clear();
            gridOrder.getGrid().getStore().addAll(lstData);
        }

    }

//    
//    private void shortBySupplier (final List<MedicinePurchaseOrderDisplay_Mdl> shortLst){
//        Collections.sort(shortLst, new Comparator<MedicinePurchaseOrderDisplay_Mdl>() {
//            @Override
//            public int compare(MedicinePurchaseOrderDisplay_Mdl o1, MedicinePurchaseOrderDisplay_Mdl o2) {
//                        int rValue = -1;
//
//                String o1SerialString = DmShared.xNull(o1.getLastOrderSupplier()).trim();
//                String o2SerialString = DmShared.xNull(o2.getLastOrderSupplier()).trim();
//
//                if (o1SerialString.equals("")) {
//                    o1SerialString = String.valueOf(shortLst.size() + 1);
//                }
//
//                if (o2SerialString.equals("")) {
//                    o2SerialString = String.valueOf(shortLst.size() + 1);
//                }
//
//                int o1Serial = DmShared.iNull(o1SerialString).intValue();
//                int o2Serial = DmShared.iNull(o2SerialString).intValue();
//
//                if (o1Serial < o2Serial) {
//                    rValue = -1;
//                } else if (o1Serial == o2Serial) {
//                    rValue = 0;
//                } else {
//                    rValue = 1;
//                }
//                return rValue;
//            }
//        });
//        gridOrder.getGrid().getStore().clear();
//        gridOrder.getGrid().getStore().addAll(shortLst);
//        gridOrder.commitChanges();
//    }
    class ShortBySupplier implements Comparator<PurchaseOrderDisplay_Mdl>, Serializable {

        @Override
        public int compare(PurchaseOrderDisplay_Mdl o1, PurchaseOrderDisplay_Mdl o2) {
            return DmShared.xNull(o1.getLastOrderSupplier()).compareTo(DmShared.xNull(o2.getLastOrderSupplier()));
        }
    }

}
