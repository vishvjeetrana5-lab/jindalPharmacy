package aarogya.dashBoard.requisitionInventory.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.requisitionInventory.shared.*;
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
import com.google.gwt.user.client.Event;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.List;

public final class RequisitionInventory extends RequisitionInventory_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<RequisitionInventory_SrvAsync> SrvAsync;
    private RequisitionInventoryDetail_Mdl requisitionDetail;
    private String searchDbFieldName, searchPrvValue = "";
    private int searchColumnIndex;
    private DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    public RequisitionInventory(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<RequisitionInventory_SrvAsync>(GWT.create(RequisitionInventory_Srv.class), RequisitionInventory.class.getName());
        gridRequisition.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
        flwForm.addAttachHandler(this);
        handlerEvents();
        manageDisplay();
        textHelpEvents();
        reQuery();
        swi.setWidget(this);
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addDomHandler(this, KeyDownEvent.getType());
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
                reQuery();
            }
        });
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return value_Mdl;
    }

    private void gridEvents() {
        setHeaderValue(0);
        gridRequisition.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridRequisition.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(blnToEndOfFile).getFillRecords(txtHDepartmentBy.getTag(), searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdRequisitionInventory_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRequisitionInventory_Mdl>> result) {
                        if (result.isStatus()) {
                            gridRequisition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });
        gridRequisition.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                String docId = gridRequisition.getDmGrid().getGrid().getStore().get(r1).getSharedKeyFieldCode();
                FillRequisitionDetail(docId);
            }
        });
        gridRequisitionDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        List<GrdRequisitionInventoryDetail_Mdl> grdRequisitionDetailToStore;
                        grdRequisitionDetailToStore = gridRequisitionDetail.getGrid().getStore().getAll();

                        if ((DmShared.xNull(grdRequisitionDetailToStore.get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0"))
                                || (DmShared.xNull(grdRequisitionDetailToStore.get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("1"))
                                || (DmShared.xNull(grdRequisitionDetailToStore.get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("2"))) {

                            int val = DmShared.iNull(grdRequisitionDetailToStore.get(event.getRowIndex()).getCheck()).intValue();
                            if (val == 0) {
                                grdRequisitionDetailToStore.get(event.getRowIndex()).setCheck("1");
                            } else if (val == 1) {
                                grdRequisitionDetailToStore.get(event.getRowIndex()).setCheck("0");
                            }

                            gridRequisitionDetail.commitChanges();
                        }
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        gridRequisition.getDmGrid()
                .getGrid().addDomHandler(new KeyDownHandler() {
                    @Override
                    public void onKeyDown(KeyDownEvent event
                    ) {
                        if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && (!event.getNativeEvent().getCtrlKey())) {
                            int code = gridRequisition.getDmGrid().getGrid().getStore().indexOf(gridRequisition.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                            String docId = gridRequisition.getDmGrid().getGrid().getStore().get(code).getSharedKeyFieldCode();
                            FillRequisitionDetail(docId);
                        }
                    }
                }, KeyDownEvent
                        .getType()
                );

    }

    private void setHeaderValue(int columnIndex) {
        if ((gridRequisition.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridRequisition.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridRequisition.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridRequisition.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridRequisition.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridRequisition.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridRequisition.getDmGrid().getGrid().getView().refresh(true);
        focusToSearchValue();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    private void FillRequisitionDetail(String docid) {
        SrvAsync.invokeRPC().getRequisitionDetail(docid, new AsyncCallback<DmResult<RequisitionInventoryDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<RequisitionInventoryDetail_Mdl> result) {
                if (result.isStatus()) {
                    requisitionDetail = result.getData();
                    txtDepartment.setText(DmShared.xNull(requisitionDetail.getDepartmentName()));
                    txtRequistionBy.setText(DmShared.xNull(requisitionDetail.getRequisitionByName()));
                    txtRequisitionNo.setText(DmShared.xNull(requisitionDetail.getSharedKeyFieldName()));
                    txtRequisitionNo.setTag(DmShared.xNull(requisitionDetail.getSharedKeyFieldCode()));
                    txtDate.setText(requisitionDetail.getDate());
                    gridRequisitionDetail.getGrid().getStore().clear();
                    gridRequisitionDetail.getGrid().getStore().addAll(requisitionDetail.getObjRequistionDetail());
                    focusToSearchValue();
//                    flMain.clear();
//                    flMain.add(flMainItem);

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnRefresh) {
            reQuery();
            txtDate.setText("");
            txtDepartment.setText("");
            txtRequisitionNo.setText("");
            txtRequistionBy.setText("");
            gridRequisitionDetail.getGrid().getStore().clear();
        } else if (event.getSource() instanceof InlineLabel) {
            String moduleName = "", mnuName = "";
            String[] title;
            title = ((InlineLabel) event.getSource()).getTitle().split("\\|");
            if (title.length == 2) {
                moduleName = DmShared.xNull(title[0]).trim();
                mnuName = DmShared.xNull(title[1]).trim();
            }
            if (!(moduleName.equalsIgnoreCase("") && mnuName.equalsIgnoreCase(""))) {
                if (moduleName.equalsIgnoreCase(new pmInventory().moduleName) && mnuName.equalsIgnoreCase(new pmInventory().ITEM_ISSUE.caption)) {
                    showItemIssue();
                }
            }
        }
    }

    private void showItemIssue() {
        if (DmShared.iNull(txtRequisitionNo.getTag()).longValue() > 0) {

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().ITEM_ISSUE.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    ((IsWidgetExtended) wgtObject).action("add_Requisition", DmShared.iNull(txtRequisitionNo.getTag()).toString(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).action("focus_On_Add", null, null);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void manageDisplay() {
        txtDate.setEnabled(false);
        txtDepartment.setEnabled(false);
        txtRequisitionNo.setEnabled(false);
        txtRequistionBy.setEnabled(false);
    }

    private void reQuery() {
        gridRequisition.getHelper().setRowsStartFrom(0);
        gridRequisition.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridRequisition.reQuery();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RequisitionInventory.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            focusToSearchValue();
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtSearchValue) {
            txtKeyDown(event);
        }
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            ((CellSelectionModel) gridRequisition.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            int code = gridRequisition.getDmGrid().getGrid().getStore().indexOf(gridRequisition.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
            String docId = gridRequisition.getDmGrid().getGrid().getStore().get(code).getSharedKeyFieldCode();
            FillRequisitionDetail(docId);

        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (gridRequisition.getDmGrid().getGrid().getStore().size() > 0) {
                ((CellSelectionModel) gridRequisition.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "REFRESH":
                    reQuery();
                    focusToSearchValue();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void focusToSearchValue() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

}
