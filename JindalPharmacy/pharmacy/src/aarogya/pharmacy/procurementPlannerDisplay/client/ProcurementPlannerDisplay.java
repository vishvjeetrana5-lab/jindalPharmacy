package aarogya.pharmacy.procurementPlannerDisplay.client;

import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.Find_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.Mdl_Main;
import aarogya.pharmacy.procurementPlannerDisplay.shared.SearchValue_Mdl;
import aarogya.selfToolkit.stStatic.shared.MultiRequisition_Model;

import aarogya.selfToolkit.stStatic.shared.RequisitionItemDetail_Model;
import aarogya.pharmacy.self.shared.TrigeredAction_Model;

import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmEnum;
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
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Srv;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_SrvAsync;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;

import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;

public class ProcurementPlannerDisplay extends ProcurementPlannerDisplay_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    final MedicineDetail<ProcurementPlannerDisplay_Mdl, GridRequisition_PrpAcs> medicineDetailGridEdit = new MedicineDetail(gridProcurement);
    private final ServiceInvoker<ProcurementPlannerDisplay_SrvAsync> SrvAsync;
    private SearchValue_Mdl svm = new SearchValue_Mdl();
    private String searchDbFieldName, searchPrvValue = "", planDocID = "", PlanNo = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private final SettingPopup settingsPopup = new SettingPopup();
    private final ColumsLegends columsLegends = new ColumsLegends();
    private boolean flagOpenAsPopup = false;
    public DmReportProperty_Mdl reportProperty = new DmReportProperty_Mdl();
    private List<ProcurementPlannerDisplay_Mdl> lstData = new ArrayList<>();
    protected String lblMedicineItemBrand = "";
    String callingModuleName;

    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();

    public ProcurementPlannerDisplay(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<ProcurementPlannerDisplay_SrvAsync>(GWT.create(ProcurementPlannerDisplay_Srv.class), ProcurementPlannerDisplay.class.getName());
        gridEvents();
        flwForm.addAttachHandler(this);
        handlerEvents();
        textHelpEvents();
        manageDisplay();
        callingModuleName = DmShared.xNull((String) paramHashMap.get("module"));
        iniDefaultValue(swi, DmShared.xNull((String) paramHashMap.get("module")));
        findEvents();
    }

    private void handlerEvents() {
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnFind.addClickHandler(this);
        btnPrint.addClickHandler(this);
        cbCheck.addClickHandler(this);
    }

    private void textHelpEvents() {
        settingsPopup.addCallBack(new SettingPopup.CallBack() {

            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                keyFields.clear();
                svm = searchValue;
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
//        setSearchColumn(SearchColumn.FROM_DEPARTMENT);
        medicineDetailGridEdit.addCallBack(new MedicineDetail.CallBack() {
            @Override
            public void onOk(ProcurementPlannerDisplay_Mdl opdService) {
                gridProcurement.commitChanges();
            }

        });

//        gridProcurement.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
//            @Override
//            public void onHeaderClick(HeaderClickEvent event) {
////                setHeaderValue(event.getColumnIndex());
//                for (SearchColumn column : SearchColumn.values()) {
//                    if (column.ordinal() == event.getColumnIndex()) {
//                        setSearchColumn(column);
//                    }
//                }
//                setAllChkBox(event.getColumnIndex());
//            }
//        });
//        gridProcurement.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
//
//            @Override
//            public void onCellClick(CellClickEvent event) {
//                try {
//                    if (event.getCellIndex() == 0) {
////                        setTick(event.getRowIndex());
//                    }
//                } catch (Exception e) {
//                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
//                }
//            }
//        });
        gridProcurement.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() != 0 && event.getColumnIndex() != 1) {
                    if (!DmShared.xNull(planNoDate.getTitle()).trim().equalsIgnoreCase("")) {
                        medicineDetailGridEdit.show(event.getRowIndex(), false, false, false, ProcurementPlannerDisplay.this);
                    } else {
                        medicineDetailGridEdit.show(event.getRowIndex(), true, false, false, ProcurementPlannerDisplay.this);
                    }
                }
            }
        });
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
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProcurementPlannerDisplay.this);
                    }
                }
            }).show();
        } else if (event.getSource() == btnSetting) {
            if (callingModuleName.equalsIgnoreCase(new pmPharmacy().moduleName)) {
                settingsPopup.lblCompanyName.setText("Medicine Company");
                settingsPopup.lblMedicineItemGroup.setText("Medicine Group");
                settingsPopup.lblMedicineItemCategpory.setText("Medicine Category");
                settingsPopup.lblMedicineItem.setText("Medicine");
            } else {
                settingsPopup.lblCompanyName.setText("Item Brand");
                settingsPopup.lblMedicineItemGroup.setText("Item Group");
                settingsPopup.lblMedicineItemCategpory.setText("Item Category");
                settingsPopup.lblMedicineItem.setText("Item");
            }
            settingsPopup.show(svm, (DmShared.xNull(planNoDate.getTitle()).trim().equalsIgnoreCase("") ? false : true));
        } else if (event.getSource() == btnRefresh) {
            clear();
            fillgrid(svm);
        } else if (event.getSource() == btnFind) {
            flwForm.remove(vrtInnerForm);
            flwForm.add(dmFind);
        } else if (event.getSource() == btnPrint) {
            printDocument();
        } else if (event.getSource() instanceof InlineLabel) {
            if (!((InlineLabel) event.getSource()).getText().trim().equalsIgnoreCase("Colums Legends")) {
                getItemCollection();
                if (keyFields.size() == 0) {
                    new DmAlertMessageBox("No entry found for process.", (DmTextBox) null).show();
                    return;
                }
                if (!DmShared.xNull(txtIndentDocID.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Indent against this entry has been already created.", btnFind).show();
                    return;
                } else if (!DmShared.xNull(planNoDate.getTitle()).trim().equalsIgnoreCase("")) {
                    createIndent();
                } else {
                    savePlanner();
                }
            } else {
                columsLegends.show();
            }
//            try {
//                savePlanner();
//            } catch (DmException e) {
//            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            if (!DmShared.xNull(svm.getDepartmentCode()).trim().equals("")) {
//                fillgrid(svm);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource().equals(txtSearchValue)) {
//            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
//        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
//        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
////            fillgrid();
//            fillFilteredData();
//        }
//        searchPrvValue = txtSearchValue.getText();
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
//        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return value_Mdl;
    }

    private void focusToSearchValue() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
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
            List<ProcurementPlannerDisplay_Mdl> lst = gridProcurement.getGrid().getStore().getAll();
            KeyFieldExtended field;
            if (headerValue == 0) {
                headerValue = 1;
                keyFields.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    keyFields.clear();
                }
            }
            gridProcurement.commitChanges();
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
        gridProcurement.getGrid().getView().getHeader().refresh();
    }

    private void fillgrid(SearchValue_Mdl svm) {
        SrvAsync.invokeRPC().getFillRecords(svm, new AsyncCallback<DmResult<List<ProcurementPlannerDisplay_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<ProcurementPlannerDisplay_Mdl>> result) {
                if (result.isStatus()) {
                    gridProcurement.getGrid().getStore().clear();
                    gridProcurement.getGrid().getStore().addAll(result.getData());
                    gridProcurement.commitChanges();

                    lstData.clear();
                    lstData.addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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

        for (ProcurementPlannerDisplay_Mdl mdl : gridProcurement.getGrid().getStore().getAll()) {
            if (DmShared.vNull(mdl.getFinalIndentedQty()).doubleValue() > 0) {
                node = new MultiRequisition_Model();
                node.setItemCode(DmShared.xNull(mdl.getItemCode()));
                node.setItemName(DmShared.xNull(mdl.getItemName()));
                node.setSharedKeyFieldCode(DmShared.xNull(mdl.getDepartmentCode()));
                node.setSharedKeyFieldName(DmShared.xNull(mdl.getDepartmentName()));
                node.setRequiredDate(DmShared.dNull(mdl.getRequiredDate()));
                node.setRemark(DmShared.xNull(mdl.getRemark()));
                nodeLine = new RequisitionItemDetail_Model();

                if (lst.contains(node)) {
                    node = lst.get(lst.indexOf(node));
                } else {
                    lst.add(node);
                }
                nodeLine.setSharedKeyFieldCode(DmShared.xNull(mdl.getSharedKeyFieldCode()));
                nodeLine.setSkuName(DmShared.xNull(mdl.getSkuName()));
                nodeLine.setSkuCode(DmShared.xNull(mdl.getSkuCode()));
                nodeLine.setQuantity(DmShared.xNull(mdl.getFinalIndentedQty()));
                nodeLine.setSharedPreparedBy(DmShared.xNull(planDocID));
                nodeLine.setSharedModifiedBy(DmShared.xNull(PlanNo));
                node.getGrdItemDetailList().add(nodeLine);
            }
        }

        return lst;
    }

    private void iniDefaultValue(final SetWidgetInvoker swi, String moduleName) {
        SrvAsync.invokeRPC(true).getDefaultValues(moduleName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    txtHDepartmentBy.setTag(result.getData().getSharedKeyFieldCode());
                    txtHDepartmentBy.setText(result.getData().getSharedKeyFieldName());
                    swi.setWidget(ProcurementPlannerDisplay.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

//    private void setSearchColumn(SearchColumn searchColumn) {
//        this.searchColumn = searchColumn;
//        if (searchColumn.PENDING_REQ_TO != searchColumn && searchColumn.CURR_STK != searchColumn) {
//            DmColumnConfig col = ((DmColumnConfig) gridProcurement.getGrid().getColumnModel().getColumn(searchColumn.getSearchColIndex()));
//            for (ColumnConfig col1 : gridProcurement.getGrid().getColumnModel().getColumns()) {
//                col1.setColumnHeaderClassName("dm-Garbage");
//            }
//            col.setColumnHeaderClassName("dm-ColActiveForFilter");
//            gridProcurement.getGrid().getView().refresh(true);
//        }
//    }
    private void fillFilteredData() {

    }

    private void getItemCollection() {
        List<ProcurementPlannerDisplay_Mdl> lst = gridProcurement.getGrid().getStore().getAll();
        KeyFieldExtended field;
        keyFields.clear();
        for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
            if (DmShared.vNull(gridProcurement.getGrid().getStore().get(rowIndex).getFinalIndentedQty()).doubleValue() > 0) {
                field = new KeyFieldExtended();
                field.setSharedKeyFieldCode(DmShared.iNull(gridProcurement.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                keyFields.add(field);
            }
        }

        gridProcurement.commitChanges();
//        setHeaderStyle();
    }

    private void savePlanner() {
        planDocID = "";
        List<ProcurementPlannerDisplay_Mdl> lst = new ArrayList<ProcurementPlannerDisplay_Mdl>();
        for (int i = 0; i < gridProcurement.getGrid().getStore().getAll().size(); i++) {
            if (DmShared.vNull(gridProcurement.getGrid().getStore().get(i).getFinalIndentedQty()) > 0) {
                lst.add(gridProcurement.getGrid().getStore().get(i));
            }
        }
        if (lst.size() == 0) {
            new DmAlertMessageBox("No record found to update.", dmFind.getTextBox()).show();
            return;
        }
        SrvAsync.invokeRPC().saveRecord(svm, lst, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    planDocID = result.getValue1();
                    PlanNo = result.getValue2();
                }
                createIndent();
            }
        });
    }

    private void createIndent() {
        TrigeredAction_Model saveAction;
        saveAction = new TrigeredAction_Model();
        String moduleName = "", mnuName = "";

        if (callingModuleName.equalsIgnoreCase(new pmInventory().moduleName)) {
            moduleName = new pmInventory().moduleName;
            mnuName = new pmInventory().INDENT.caption;
        } else {
            moduleName = new pmPharmacy().moduleName;
            mnuName = new pmPharmacy().INDENT.caption;
        }
        saveAction.setStartupAction("I");
        saveAction.setPerformedAction("I");
        invokeWidget(moduleName, mnuName, fillGridDetailForIndentRequisition(), "ADD_REQUISITION_WITH_DETAIL", "", false, false, null, true);
    }

    private void findEvents() {

        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                        flwForm.remove(dmFind);
                        flwForm.add(vrtInnerForm);
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Mdl_Main>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Mdl_Main> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Mdl_Main> result) {
        Mdl_Main data;
        svm = new SearchValue_Mdl();
        planNoDate.setTitle("");
        txtIndentDocID.setTag("");
        txtIndentDocID.setText("");
        if (result.isStatus()) {
            data = result.getData();
            svm = data.getFilterMdl();
            planNoDate.setText("Plan No. : " + DmShared.xNull(svm.getSharedKeyFieldName()).trim() + "   Date : " + DmShared.dNull(svm.getVoucherDate()));
            planNoDate.setTitle(DmShared.xNull(svm.getSharedKeyFieldCode()).trim());
            planDocID = DmShared.xNull(svm.getSharedKeyFieldCode()).trim();
            PlanNo = DmShared.xNull(svm.getSharedKeyFieldName()).trim();
            txtIndentDocID.setTag(DmShared.xNull(result.getData().getIndentDocID()).trim());
            txtIndentDocID.setText(DmShared.xNull(result.getData().getIndentRecID()).trim());
            gridProcurement.getGrid().getStore().clear();
            gridProcurement.getGrid().getStore().addAll(result.getData().getGrdItemDetails());
            gridProcurement.commitChanges();
            lstData.clear();
            lstData.addAll(result.getData().getGrdItemDetails());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }

    private void printDocument() {
        SrvAsync.invokeRPC().execute(svm, lstData, reportProperty, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    RptProcurementPlannerDisplay prnReceipt = new RptProcurementPlannerDisplay(svm, lstData, false);
                    prnReceipt.getReport(result.getData(), true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
