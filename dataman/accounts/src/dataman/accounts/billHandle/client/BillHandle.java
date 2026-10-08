package dataman.accounts.billHandle.client;

import dataman.accounts.billHandle.shared.DefaultValue_Mdl;
import dataman.accounts.billHandle.shared.GrdBillDetail_Mdl;
import dataman.accounts.billHandle.shared.Find_Mdl;
import dataman.accounts.billHandle.shared.BillHandle_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.AttachEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.billHandle.shared.BillHandle_Srv;
import dataman.accounts.billHandle.shared.BillHandle_SrvAsync;
import dataman.accounts.billHandle.shared.BillHandle_Vld;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.HashMap;

public final class BillHandle extends BillHandle_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler {
    //<editor-fold defaultstate="collapsed" desc="Search Column [Enum]">

    private enum SearchColumn {

        check(1), type(2), vNo(3), date(4), pBillNo(5), pBillDate(6), partyName(7),
        netAmt(8), paymntDueDt(9), lastStatusDate(10), remark(11);
        int colIndex;

        SearchColumn(int p) {
            colIndex = p;
        }

        public int getIntValue() {
            return this.colIndex;
        }
    }
    //</editor-fold>
    ServiceInvoker<BillHandle_SrvAsync> SrvAsync;

    protected DefaultValue_Mdl defaultValue;
    final BillHandleGridEdit<GrdBillDetail_Mdl, GrdMedicineDetail_PrpAcs> gridEdit = new BillHandleGridEdit(grdBillDetail);
    WalkThrough walkThrough;
    String vNo = "", recIdPrefix = "", recIdValue = "";
    private String moduleName = "Pharmacy";
    private boolean isApprove = false;
    private boolean isCheckPermission = false;
    private SearchColumn searchColumn;
    private final List<GrdBillDetail_Mdl> lstData = new ArrayList<>();

    public BillHandle(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(BillHandle_Srv.class), BillHandle.class.getName());
        this.asWidget().setTitle(widgetName);
        walkThrough = new WalkThrough(SrvAsync);
        menuPermission.setUserPermission(menuPermission.getUserPermission().replace("D", ""));
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        blurHandler();
        setPermission(menuPermission);
        defaultValues(swi);
    }

    private void blurHandler() {
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
        txtVDate.addBlurHandler(this);
        txtFromBilldt.addBlurHandler(this);
        txtToBilldt.addBlurHandler(this);
        txtSearchText.addBlurHandler(this);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            }
        }
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().BILL_HANDLE})) {
            @Override
            public String getVoucherType() {
                return txtHBillHandleType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                txtHBillHandleType.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
//                    grdBillDetail.getGrid().getStore().add(new GrdBillDetail_Mdl());
                    txtBillNo.setEnabled(false);
                    txtModule.setEnabled(false);
                    txtHBillHandleType.setEnabled(false);

                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

                prnRecord(false);
            }

        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BillHandle.this);
            }
        });

        topControl.addRefershHandeller(new DmTopControl.eRefersh() {
            @Override
            public void refreshClicked() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        topControl.setDisplay(true);
    }

    private void setPermission(UserMenu menuPermission) {
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(isCheckPermissionBlog)) {
                isCheckPermission = true;
            }
        }
    }

    private void textHelpEvents() {

        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        txtHFromLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHFromLocation(helper, blnToEndOfFile, DmShared.xNull(txtHToLocation.getTag()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFromLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFromLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFromLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                txtModule.setText(tRow.getSharedPreparedBy());
                getPBillGrd();
            }
        });
        txtHToLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHToLocation(helper, blnToEndOfFile, DmShared.xNull(txtHFromLocation.getTag()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHToLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHToLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHToLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                getPBillGrd();
            }
        });

        txtHBillHandleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitiontype(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillHandleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillHandleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillHandleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        if (this.searchColumn == null) {
            this.searchColumn = SearchColumn.vNo;
        } else if (this.searchColumn == SearchColumn.check || this.searchColumn == SearchColumn.remark) {
            return;
        }
        DmColumnConfig col = ((DmColumnConfig) grdBillDetail.getGrid().getColumnModel().getColumn(searchColumn.getIntValue()));
        for (ColumnConfig col1 : grdBillDetail.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdBillDetail.getGrid().getView().refresh(true);
        txtSearchText.setText("");
        txtSearchText.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
        txtSearchText.setFocus(true);
    }

    private void fillFilteredData() {
        String searchText = DmShared.xNull(txtSearchText.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<GrdBillDetail_Mdl> lstFilteredData = new ArrayList<>();
            for (GrdBillDetail_Mdl gMdl : lstData) {
                switch (searchColumn) {

                    case type:
                        matchWith = gMdl.getType();
                        break;
                    case vNo:
                        matchWith = gMdl.getVoucherNo();
                        break;
                    case date:
                        matchWith = gMdl.getDate();
                        break;
                    case pBillNo:
                        matchWith = gMdl.getPartyBillNo();
                        break;
                    case pBillDate:
                        matchWith = gMdl.getPartyDate();
                        break;
                    case partyName:
                        matchWith = gMdl.getPartyName();
                        break;
                    case netAmt:
                        matchWith = gMdl.getNetAmt();
                        break;
                    case paymntDueDt:
                        matchWith = gMdl.getPaymentDueDt();
                        break;
                    case lastStatusDate:
                        matchWith = gMdl.getLastStatusDt();
                        break;

                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(lstFilteredData);
        } else {
            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(lstData);
        }

    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.vNo);

        txtSearchText.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) grdBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });
        gridEdit.addCallBack(new BillHandleGridEdit.CallBack() {
            @Override
            public void onOk(GrdBillDetail_Mdl grdVoucherEntry) {
                grdBillDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdBillDetail.commitChanges();
            }
        });
        grdBillDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == ColCheckPBill) {
                    grdCheckedValue(event.getRowIndex());
                } else {
                    showRequisitionGridEdit(event.getRowIndex());
                }

            }
        });
        grdBillDetail.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                for (SearchColumn column : SearchColumn.values()) {
                    if (column.getIntValue() == event.getColumnIndex()) {
                        setSearchColumn(column);
                        break;
                    }
                }
            }
        });
        grdBillDetail.getGrid().addDomHandler(new KeyDownHandler() {
            CellSelectionModel<GrdBillDetail_Mdl> cellModel;

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    cellModel = (CellSelectionModel<GrdBillDetail_Mdl>) grdBillDetail.getGrid().getSelectionModel();
                    if (cellModel.getSelectCell().getCell() == ColCheckPBill) {
                        grdCheckedValue(cellModel.getSelectCell().getRow());

                    } else {
                        showRequisitionGridEdit(grdBillDetail.getGrid().getStore().indexOf(grdBillDetail.getGrid().getSelectionModel().getSelectedItem()));
                    }

                }
            }
        }, KeyDownEvent.getType());
    }

    private void grdCheckedValue(int currentRowIndex) {
        GrdBillDetail_Mdl gbdm;
        int scrollPos;
        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("BROWSE")
                || DmShared.xNull(topControl.getMode()).equalsIgnoreCase("EDIT")) {
            return;
        }
        if (!isCheckPermission) {
            new DmAlertMessageBox("You don't have permission for this action.", (DmTextBox) null).show();
            return;
        }

        gbdm = grdBillDetail.getGrid().getStore().get(currentRowIndex);

        grdBillDetail.getGrid().getStore().get(currentRowIndex).setCheck(gbdm.isCheck() ? false : true);

        Element scroller = grdBillDetail.getGrid().getView().getEditorParent();
        scrollPos = scroller.getScrollTop();
        grdBillDetail.commitChanges();
        grdBillDetail.getGrid().getView().getEditorParent().setScrollTop(scrollPos);
    }

    private void showRequisitionGridEdit(int currentRowIndex) {

        if (DmShared.xNull(txtHFromLocation.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select From Location", txtHFromLocation).show();
            return;
        }
        if (DmShared.xNull(txtHToLocation.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select To Location", txtHToLocation).show();
            return;
        }
        if (DmShared.xNull(txtFromBilldt.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select From Bill Date", txtFromBilldt).show();
            return;
        }
        if (DmShared.xNull(txtToBilldt.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select To Bill Date", txtToBilldt).show();
            return;
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gridEdit.show(currentRowIndex, true, false, false, this);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                  //  setApproved(topControl.getDocId(), false);
                   new DmAlertMessageBox("Record has been approved. You can not modify this record.", txtToBilldt).show();
                }

            }
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            topControlMoveRec(result.getData());
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
        } else {
//            gridEdit.hide();
        }
    }

    public void defaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(BillHandle.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<BillHandle_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<BillHandle_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<BillHandle_Mdl> result) {
        BillHandle_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHFromLocation.setTag(data.getFromLocationCode());
            txtHFromLocation.setText(data.getFromLocationName());
            txtHToLocation.setTag(data.getToLocationCode());
            txtHToLocation.setText(data.getToLocationName());
            txtHBillHandleType.setTag(data.getBillTypeCode());
            txtHBillHandleType.setText(data.getBillTypeName());
            txtFromBilldt.setText(data.getFromBilldate());
            txtToBilldt.setText(data.getToBilldate());
            txtModule.setText(data.getModule());
            txtBillNo.setText(data.getRecid().getRecIdValue());
            txtVDate.setText(data.getDate());
            txtWTACode.setText(data.getWtaCode());
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            userControl.setTextBlog(approvedByBlog, DmShared.xNull(data.getApprovedBy()));

            vNo = DmShared.iNull(data.getRecid().getCounter()).toString();
            recIdValue = DmShared.xNull(data.getRecid().getRecIdValue());
            recIdPrefix = DmShared.xNull(data.getRecid().getPrefix());

//            grdBillDetail.getGrid().getStore().clear();
//            grdBillDetail.getGrid().getStore().addAll(data.getGrdData());
            fillGrid(data.getGrdData());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        BillHandle_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();
            BillHandle_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtVDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHBillHandleType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVDate).show();
                break;

            case 300:
                new DmAlertMessageBox(errorMessage, txtHFromLocation).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHToLocation).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdBillDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHBillHandleType).show();
                break;
        }
    }

    private BillHandle_Mdl topControlSaveInitialize() {
        BillHandle_Mdl mrTemp = new BillHandle_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecid().setCounter(DmShared.iNull(vNo).longValue());
            mrTemp.getRecid().setPrefix(DmShared.xNull(recIdPrefix));
            mrTemp.getRecid().setRecIdValue(DmShared.xNull(recIdValue));
        }
        grdBillDetail.getGrid().getStore().commitChanges();
        mrTemp.setFromLocationCode(DmShared.xNull(txtHFromLocation.getTag()));
        mrTemp.setToLocationCode(DmShared.xNull(txtHToLocation.getTag()));
        mrTemp.setBillTypeCode(DmShared.xNull(txtHBillHandleType.getTag()));
        mrTemp.setBillTypeName(DmShared.xNull(txtHBillHandleType.getText()));
        mrTemp.setModule(DmShared.xNull(txtModule.getText()));
        mrTemp.setDate(DmShared.xNull(txtVDate.getText()));
        mrTemp.setFromBilldate(DmShared.xNull(txtFromBilldt.getText()));
        mrTemp.setToBilldate(DmShared.xNull(txtToBilldt.getText()));
        mrTemp.setWtaCode(DmShared.xNull(txtWTACode.getText()));
//        List<GrdBillDetail_Mdl> OBJList = new ArrayList<>();
//        OBJList.addAll(grdBillDetail.getGrid().getStore().getAll());
        mrTemp.getGrdData().addAll(grdBillDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtVDate)) {
            txtVDate.setText(DmClient.returnDate(txtVDate.getText()));

        }
        if (event.getSource().equals(txtFromBilldt)) {
            txtFromBilldt.setText(DmClient.returnDate(txtFromBilldt.getText()));
            getPBillGrd();

        }
        if (event.getSource().equals(txtToBilldt)) {
            txtToBilldt.setText(DmClient.returnDate(txtToBilldt.getText()));
            getPBillGrd();

        }

    }

    private void clear() {
        grdBillDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    private void onAdd() {
        clear();
        txtHBillHandleType.setFocus(true);
        txtVDate.setText(DmClient.getLoginDate());
        txtBillNo.setEnabled(false);
        txtModule.setEnabled(false);

        txtHBillHandleType.setTag(DmShared.xNull(defaultValue.getBillHandleTypeCode()));
        txtHBillHandleType.setText(DmShared.xNull(defaultValue.getBillHandleTypeName()));

    }

    private void getPBillGrd() {
        if (DmShared.dNull(txtFromBilldt.getText()).equalsIgnoreCase("")
                || DmShared.dNull(txtToBilldt.getText()).equalsIgnoreCase("")) {
            return;
        }
        if (DmShared.convertDate(txtFromBilldt.getText()).getTime() > DmShared.convertDate(txtToBilldt.getText()).getTime()) {
            return;
        }
        if (DmShared.iNull(txtHFromLocation.getTag()).longValue() == 0
                || DmShared.iNull(txtHToLocation.getTag()).longValue() == 0
                || DmShared.xNull(txtModule.getText()).equalsIgnoreCase("")) {
            return;
        }
        getPBillByModuleLocation();
    }

    private void getPBillByModuleLocation() {

        SrvAsync.invokeRPC().getPBillByModuleLocation(DmShared.xNull(txtModule.getText()),
                DmShared.xNull(txtFromBilldt.getText()),
                DmShared.xNull(txtToBilldt.getText()),
                DmShared.xNull(txtHFromLocation.getTag()),
                new AsyncCallback<DmResult<List<GrdBillDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdBillDetail_Mdl>> result) {
                if (result.isStatus()) {
//                    grdBillDetail.getGrid().getStore().clear();
//                    grdBillDetail.getGrid().getStore().addAll(result.getData());
                    fillGrid(result.getData());
                    if (grdBillDetail.getGrid().getStore().getAll().size() <= 0) {
                        new DmAlertMessageBox("No Bill to Transfer from location " + txtHFromLocation.getText(), txtHToLocation).show();
                        return;
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHToLocation).show();
                }
            }
        }
        );

    }

    private void fillGrid(List<GrdBillDetail_Mdl> grdLst) {
        lstData.clear();
        lstData.addAll(grdLst);
        txtSearchText.setText("");
        fillFilteredData();
    }

    private void prnRecord(boolean printToPrinter) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(txtBillNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHBillHandleType.getTag()), "", "Bill Handle", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }
}
