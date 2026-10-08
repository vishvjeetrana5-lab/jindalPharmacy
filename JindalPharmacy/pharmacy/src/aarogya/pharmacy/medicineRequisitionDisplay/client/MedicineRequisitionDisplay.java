package aarogya.pharmacy.medicineRequisitionDisplay.client;

import aarogya.pharmacy.medicineRequisitionDisplay.shared.MedicineRequisitionDisplay_Srv;
import aarogya.pharmacy.medicineRequisitionDisplay.shared.MedicineRequisitionDisplay_SrvAsync;
import aarogya.pharmacy.medicineRequisitionDisplay.shared.MedicineRequisitionDisplay_Mdl;
import aarogya.selfToolkit.stStatic.shared.MultiRequisition_Model;

import aarogya.pharmacy.medicineRequisitionDisplay.shared.*;
import aarogya.selfToolkit.stStatic.shared.RequisitionItemDetail_Model;
import aarogya.pharmacy.self.shared.TrigeredAction_Model;

import aarogya.projectMenu.self.shared.pmPharmacy;
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

public class MedicineRequisitionDisplay extends MedicineRequisitionDisplay_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    final MedicineDetail<MedicineRequisitionDisplay_Mdl, GridRequisition_PrpAcs> medicineDetailGridEdit = new MedicineDetail(gridRequisition);
    private final ServiceInvoker<MedicineRequisitionDisplay_SrvAsync> SrvAsync;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private boolean flagOpenAsPopup = false;
    private int searchColumnIndex;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private boolean checkAll = false;

    private enum SearchColumn {
        SR(0), RECID(1), FROM_DEPARTMENT(2), MEDICINE_NAME(3), REQ_QTY(4), TOT_QTY(5), CURR_STK(6), REQI_BY(7), REQUI_DATE(8);
        int searchColIndex;

        public int getSearchColIndex() {
            return searchColIndex;
        }

        SearchColumn(int searchColIndex) {
            this.searchColIndex = searchColIndex;
        }
    }

    private SearchColumn searchColumn;
    private final List<MedicineRequisitionDisplay_Mdl> lstData = new ArrayList<>();

    public MedicineRequisitionDisplay(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<MedicineRequisitionDisplay_SrvAsync>(GWT.create(MedicineRequisitionDisplay_Srv.class), MedicineRequisitionDisplay.class.getName());
        gridEvents();
        flwForm.addAttachHandler(this);
        handlerEvents();
        textHelpEvents();
        manageDisplay();
        iniDefaultValue(swi);
        fillgrid();

    }

    private void handlerEvents() {
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addDomHandler(this, KeyDownEvent.getType());
        cbCheck.addClickHandler(this);
    }

    private void textHelpEvents() {
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
                fillgrid();
            }
        });
    }

    private void gridEvents() {
//        setHeaderValue(0);
        setSearchColumn(SearchColumn.RECID);

        txtSearchValue.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) gridRequisition.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });

        medicineDetailGridEdit.addCallBack(new MedicineDetail.CallBack() {
            @Override
            public void onOk(MedicineRequisitionDisplay_Mdl opdService) {
                gridRequisition.commitChanges();
            }

        });

        gridRequisition.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
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

        gridRequisition.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        setTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        gridRequisition.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() != 0 && event.getColumnIndex() != 1) {
                    medicineDetailGridEdit.show(event.getRowIndex(), true, false, false, MedicineRequisitionDisplay.this);
                } else if (event.getColumnIndex() == 1) {
                    HashMap paramHashMap = new HashMap();
                    paramHashMap.put("requisitiondocID", DmShared.xNull(gridRequisition.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    paramHashMap.put("requisitionNo", DmShared.xNull(gridRequisition.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName()));
                    paramHashMap.put("fromDeptName", DmShared.xNull(txtHDepartmentBy.getText()));
                    paramHashMap.put("fromDeptCode", DmShared.xNull(txtHDepartmentBy.getTag()));

                    String reqDocID = DmShared.xNull(gridRequisition.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
                    invokeWidget(new pmPharmacy().moduleName, new pmPharmacy().MEDICINE_ISSUE.caption, reqDocID, "ADD_REQUISITION", "FOCUS_ON_ADD", true, true, paramHashMap, false);
                }
            }
        });
    }

    private void setTick(int rowIndex) {
        int val = DmShared.vNull(gridRequisition.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(DmShared.iNull(gridRequisition.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());

        if (val == 0) {
            gridRequisition.getGrid().getStore().get(rowIndex).setIsChecked("1");
            if (!keyFields.contains(field)) {
                keyFields.add(field);
            }
        } else if (val == 1) {
            gridRequisition.getGrid().getStore().get(rowIndex).setIsChecked("0");

            for (int i = 0; i < keyFields.size(); i++) {
                if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                    keyFields.remove(i);
                }
            }
        }
        gridRequisition.commitChanges();
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
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineRequisitionDisplay.this);
                    }
                }
            }).show();
        } else if (event.getSource().equals(cbCheck)) {
            manageAllCheck();
        } else if (event.getSource() == btnRefresh) {
            clear();
            fillgrid();
        } else if (event.getSource() instanceof InlineLabel) {
            TrigeredAction_Model saveAction;

            if (keyFields.size() == 0) {
                new DmAlertMessageBox("No entry found for process.", txtDepartment).show();
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
                    if (moduleName.equalsIgnoreCase(new pmPharmacy().moduleName) && mnuName.equalsIgnoreCase(new pmPharmacy().INDENT.caption)) {
                        saveAction.setStartupAction("I");
                        saveAction.setPerformedAction("I");

                        invokeWidget(new pmPharmacy().moduleName, new pmPharmacy().INDENT.caption, fillGridDetailForIndentRequisition(), "ADD_REQUISITION_WITH_DETAIL", "", false, false, null, true);
                    } else if (moduleName.equalsIgnoreCase(new pmPharmacy().moduleName) && mnuName.equalsIgnoreCase(new pmPharmacy().REQUISITION.caption)) {
                        saveAction.setStartupAction("R");
                        saveAction.setPerformedAction("R");
                        invokeWidget(new pmPharmacy().moduleName, new pmPharmacy().REQUISITION.caption, fillGridDetailForIndentRequisition(), "ADD_REQUISITION_WITH_DETAIL", "", false, false, null, true);
                    }
                }
            } catch (DmException e) {

//                });
//            } catch (DmException e) {
//            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
            }

            //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
            //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            fillgrid();
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
        txtDepartment.setEnabled(false);
        txtRequisitionNo.setEnabled(false);
        txtRequistionBy.setEnabled(false);
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return value_Mdl;
    }

//    private void setHeaderValue(int columnIndex) {
//        if ((gridRequisition.getGrid().getColumnModel().getColumnCount()-1) < columnIndex) {
//            searchColumnIndex = 0;
//        } else if (columnIndex < 0) {
//            searchColumnIndex = gridRequisition.getGrid().getColumnModel().getColumnCount() - 1;
//        } else {
//            searchColumnIndex = columnIndex;
//        }
//
//        DmColumnConfig col = ((DmColumnConfig) gridRequisition.getGrid().getColumnModel().getColumn(searchColumnIndex));
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
//        for (ColumnConfig col1 : gridRequisition.getGrid().getColumnModel().getColumns()) {
//            col1.setColumnHeaderClassName("dm-Garbage");
//        }
//        col.setColumnHeaderClassName("dm-ColActiveForFilter");
//        gridRequisition.getGrid().getView().refresh(true);
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
        txtDepartment.setText("");
        txtRequisitionNo.setTag("");
        txtRequisitionNo.setTag2nd("");
        txtRequisitionNo.setTag("");
        txtRequisitionNo.setText("");
        txtRequistionBy.setText("");
//        gridRequisition.getGrid().getStore().clear();
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == 0) {
            List<MedicineRequisitionDisplay_Mdl> lst = gridRequisition.getGrid().getStore().getAll();
            KeyFieldExtended field;
            if (headerValue == 0) {
                headerValue = 1;
                keyFields.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    if (DmShared.vNull(gridRequisition.getGrid().getStore().get(rowIndex).getIsChecked()).intValue() == 0) {
//                    gridRequisition.getDmGrid().getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                        field = new KeyFieldExtended();
                        gridRequisition.getGrid().getStore().get(rowIndex).setIsChecked("1");
                        field.setSharedKeyFieldCode(DmShared.iNull(gridRequisition.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                        keyFields.add(field);
                    }
                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    gridRequisition.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    keyFields.clear();
                }
            }
            gridRequisition.commitChanges();
            setHeaderStyle();
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
        gridRequisition.getGrid().getView().getHeader().refresh();
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

    private void fillgrid() {
        SrvAsync.invokeRPC().getFillRecords(txtHDepartmentBy.getTag(), valueIntialize(), new AsyncCallback<DmResult<List<MedicineRequisitionDisplay_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<MedicineRequisitionDisplay_Mdl>> result) {
                if (result.isStatus()) {
                    gridRequisition.getGrid().getStore().clear();
                    gridRequisition.getGrid().getStore().addAll(result.getData());
                    gridRequisition.commitChanges();

                    lstData.clear();
                    lstData.addAll(result.getData());
//                    if (swi != null) {
//                        swi.setWidget(MedicineRequisitionDisplay.this);
//                    }
//                     gridRequisition.getGrid().getStore();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
//        fillFilteredData();
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

    private List<MultiRequisition_Model> fillGridDetailForIndentRequisition() {
        List<MultiRequisition_Model> lst = new ArrayList();
        MultiRequisition_Model node;
        RequisitionItemDetail_Model nodeLine;

        for (MedicineRequisitionDisplay_Mdl mdl : gridRequisition.getGrid().getStore().getAll()) {
            if (DmShared.xNull(mdl.getIsChecked()).equalsIgnoreCase("1")) {
                node = new MultiRequisition_Model();
                node.setItemCode(DmShared.xNull(mdl.getItemCode()));
                node.setItemName(DmShared.xNull(mdl.getItemName()));
                node.setSharedKeyFieldCode(DmShared.xNull(txtHDepartmentBy.getTag()));
                node.setSharedKeyFieldName(DmShared.xNull(txtHDepartmentBy.getText()));
                node.setDecimalPlaces(DmShared.xNull(mdl.getUnitDecimalPlaces()));
                nodeLine = new RequisitionItemDetail_Model();

                if (lst.contains(node)) {
                    node = lst.get(lst.indexOf(node));
                } else {
                    lst.add(node);
                }
                nodeLine.setSharedKeyFieldCode(DmShared.xNull(mdl.getSharedKeyFieldCode()));
                nodeLine.setSharedKeyFieldName(DmShared.xNull(mdl.getV_SNo()));
                nodeLine.setQuantity(DmClient.vNull(mdl.getItemQty(), DmShared.getNumberFormat(DmShared.iNull(mdl.getUnitDecimalPlaces()).intValue())));
                nodeLine.setSkuName(DmShared.xNull(mdl.getSkuName()));
                nodeLine.setSkuCode(DmShared.xNull(mdl.getSkuCode()));
                node.getGrdItemDetailList().add(nodeLine);
            }
        }

        return lst;
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
                    swi.setWidget(MedicineRequisitionDisplay.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        if (searchColumn.TOT_QTY != searchColumn && searchColumn.CURR_STK != searchColumn) {
            DmColumnConfig col = ((DmColumnConfig) gridRequisition.getGrid().getColumnModel().getColumn(searchColumn.getSearchColIndex()));
            for (ColumnConfig col1 : gridRequisition.getGrid().getColumnModel().getColumns()) {
                col1.setColumnHeaderClassName("dm-Garbage");
            }
            col.setColumnHeaderClassName("dm-ColActiveForFilter");
            gridRequisition.getGrid().getView().refresh(true);
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
            List<MedicineRequisitionDisplay_Mdl> lstFilteredData = new ArrayList<>();
            for (MedicineRequisitionDisplay_Mdl gMdl : lstData) {
                switch (searchColumn) {
                    case RECID:
                        matchWith = gMdl.getSharedKeyFieldName();
                        break;
                    case FROM_DEPARTMENT:
                        matchWith = gMdl.getDepartmentName();
                        break;
                    case MEDICINE_NAME:
                        matchWith = gMdl.getItemName();
                        break;
                    case REQ_QTY:
                        matchWith = gMdl.getItemQty();
                        break;
                    case REQI_BY:
                        matchWith = gMdl.getRequisitionByName();
                        break;
                    case REQUI_DATE:
                        matchWith = gMdl.getDate();
                        break;
                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            gridRequisition.getGrid().getStore().clear();
            gridRequisition.getGrid().getStore().addAll(lstFilteredData);
        } else {
            gridRequisition.getGrid().getStore().clear();
            gridRequisition.getGrid().getStore().addAll(lstData);
        }

    }
}
