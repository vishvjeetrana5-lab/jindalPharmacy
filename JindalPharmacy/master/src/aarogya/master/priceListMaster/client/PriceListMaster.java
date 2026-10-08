package aarogya.master.priceListMaster.client;

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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.*;
import aarogya.master.priceListMaster.shared.DefaultValue_Mdl;
import aarogya.master.priceListMaster.shared.Find_Mdl;
import aarogya.master.priceListMaster.shared.GrdItemDetail_Mdl;
import aarogya.master.priceListMaster.shared.PriceListMaster_Srv;
import aarogya.master.priceListMaster.shared.PriceListMaster_SrvAsync;
import aarogya.master.priceListMaster.shared.PriceList_Mdl;
import aarogya.master.priceListMaster.shared.SearchValue_Mdl;
import aarogya.selfToolkit.iplApplicableWithOption.client.IPLApplicableWithOption;
import aarogya.selfToolkit.iplApplicableWithOption.shared.Applicable_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;

public final class PriceListMaster extends PriceListMaster_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item>,
        KeyUpHandler, KeyDownHandler, DmRootEvents {

    private final ServiceInvoker<PriceListMaster_SrvAsync> srvAsync;
    private final ItemGridEdit<GrdItemDetail_Mdl, GrdServices_PrpAcs> itemGridEdit = new ItemGridEdit(grdItemDetail.getDmGrid(), "Item Detail");
    private final IPLApplicableWithOption priceListPopup = new IPLApplicableWithOption();
    private final ApplyDiscountPopup applyDiscPopup = new ApplyDiscountPopup();
    private final ImportPopup importPopup = new ImportPopup();
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private String searchDbFieldName = "name", searchPrvValue = "";
    private DefaultValue_Mdl defaultMdl;
    private boolean allowSavePrice = false, isCreateNewRateList = false, deletePrice = false;
    private final CreatePriceListPopup createPriceList = new CreatePriceListPopup();

    private void textHelpEvents() {
        priceListPopup.addCallBack(new IPLApplicableWithOption.CallBack() {

            @Override
            public void onOk(Applicable_Mdl applicableMdl) {
                if (applicableMdl != null) {
                    txtApplicableDate.setText(DmShared.dNull(applicableMdl.getDateApplicable()));
                    reQuery(0);
                }
            }

            @Override
            public void onDelete() {
                //topControlMoveRec(DmShared.xNull(txtHPriceList.getTag()));
            }
        });

        applyDiscPopup.addCallBack(new ApplyDiscountPopup.CallBack() {

            @Override
            public void refresh(boolean callReQuery) {
                if (callReQuery) {
                    // reQuery(grdItemDetail.getHelper().getRowsStartFrom());
                    SearchValue_Mdl param = new SearchValue_Mdl();
                    param.setRateListCode(DmShared.xNull(txtHPriceList.getTag()));
                    grdItemDetail.reQuery();
                }
            }

        });
        importPopup.addCallBack(new ImportPopup.CallBack() {
            @Override
            public void getNewPriceList(String newApplicableDate) {
                txtApplicableDate.setText(DmShared.dNull(newApplicableDate));
                reQuery(0);
            }
        });

        txtHPriceList.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    txtSearchValue.setFocus(true);
                }
            }
        });
        txtHPriceList.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<PriceList_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPriceList(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<PriceList_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPriceList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<PriceList_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPriceList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPriceList).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, PriceList_Mdl tRow) {
                if (tRow != null && blnStatus) {
                    txtPriceListType.setTag(DmShared.xNull(tRow.getPriceListTypeTag()));
                    txtPriceListType.setText(DmShared.xNull(tRow.getPriceListTypeName()));
                    txtApplicableOn.setTag(DmShared.xNull(tRow.getApplicableOn()));
                    txtApplicableOn.setText(DmShared.xNull(tRow.getApplicableOnName()));
                    reQuery(0);
                }
            }

        });
        createPriceList.addCallBack(new CreatePriceListPopup.CallBack() {

            @Override
            public void onOk(PriceList_Mdl mrTemp) {
                if (mrTemp != null) {
                    if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() > 0) {
                        txtHPriceList.setTag(mrTemp.getSharedKeyFieldCode());
                        txtHPriceList.setText(mrTemp.getSharedKeyFieldName());
                        txtPriceListType.setTag(DmShared.xNull(mrTemp.getPriceListTypeTag()));
                        txtPriceListType.setText(DmShared.xNull(mrTemp.getPriceListTypeName()));
                      
                        SearchValue_Mdl param = new SearchValue_Mdl();
                        param.setRateListCode(mrTemp.getSharedKeyFieldCode());
                        // topControlMoveRec(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        grdItemDetail.reQuery();
                        reQuery(0);
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                txtHPriceList.setFocus(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void onClose() {
                txtHPriceList.setFocus(true);
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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
                    txtHPriceList.setText(DmShared.xNull(tRow.getPriceListName()));
                    txtHPriceList.setTag(DmShared.xNull(tRow.getSharedKeyFieldCode()));
                    txtPriceListType.setText(DmShared.xNull(tRow.getPriceListType()));
                    txtPriceListType.setTag(DmShared.xNull(tRow.getPriceListTypeTag()));
                    txtApplicableDate.setText(DmShared.dNull(DmClient.getStartDate()));
                    grdItemDetail.reQuery();
                }
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        itemGridEdit.addCallBack(new ItemGridEdit.CallBack() {
            @Override
            public void refresh() {
                reQuery(grdItemDetail.getHelper().getRowsStartFrom());
                grdItemDetail.reQuery();
            }
        });
        
        grdItemDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridServiceEdit(event.getRowIndex());
            }
        });
        
        grdItemDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridServiceEdit(grdItemDetail.getDmGrid().getGrid().getStore().indexOf(grdItemDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        
        grdItemDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getItemDetailGrid(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdItemDetail_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPriceList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdItemDetail_Mdl>> result) {
                        if (result.isStatus() ) {
                         grdItemDetail.setData(result.getData(), result.getPaging());
                        

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPriceList).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
            }
        });
        grdItemDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
                DmColumnConfig col = ((DmColumnConfig) grdItemDetail.getDmGrid().getGrid().getColumnModel().getColumn(event.getColumnIndex()));
                searchDbFieldName = col.getDbFieldName();
                searchDbFieldDataType = col.getDbFieldDataType();

            }
        });
    }

    protected SearchValue_Mdl setParam() {
        SearchValue_Mdl svm = new SearchValue_Mdl();
        if (DmShared.dNull(txtApplicableDate.getText()).equalsIgnoreCase("")) {
            svm.setApplicableDate(DmShared.dNull(DmClient.getStartDate()));
            txtApplicableDate.setText(DmShared.dNull(DmClient.getStartDate()));
        } else {
            svm.setApplicableDate(DmShared.dNull(txtApplicableDate.getText()));
        }
        svm.setRateListCode(DmShared.xNull(txtHPriceList.getTag()));
        svm.setApplicableOn(DmShared.xNull(txtApplicableOn.getTag()));
        return svm;
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdItemDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            columnIndex = 0;
        } else if (columnIndex < 0) {
            columnIndex = grdItemDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            columnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdItemDetail.getDmGrid().getGrid().getColumnModel().getColumn(columnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdItemDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdItemDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdItemDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }

        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdItemDetail.getDmGrid().getGrid().getView().refresh(true);

        txtSearchValue.setFocus(true);
    }

    private void reQuery(int rowsStartFrom) {
        grdItemDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdItemDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdItemDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
       // grdItemDetail.reQuery(rowsStartFrom, setParam());
         grdItemDetail.reQuery();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(priceListBtn)) {
            if (DmShared.iNull(txtHPriceList.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select a Record.", priceListBtn).show();
                return;
            } else {
                priceListPopup.show(priceListBtn, true, true, PriceListEntryFrom.PRICELIST_MASTER, "", DmShared.xNull(txtHPriceList.getTag()), DmShared.xNull(defaultMdl.getSiteCode()));
            }
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnFind)) {
            onFind();
        } else if (event.getSource().equals(btnOther)) {
            onOther();
        } else if (event.getSource().equals(lblPriceList)) {
            showCreateRateList();
        }
    }

    private void showCreateRateList() {
        if (isCreateNewRateList) {
            createPriceList.show(this);
        }
    }

    private void gridServiceEdit(Integer currentRowIndex) {
        itemGridEdit.show(currentRowIndex, allowSavePrice, false, deletePrice, this);
    }

    public PriceListMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(PriceListMaster_Srv.class), PriceListMaster.class.getName());
        grdItemDetail.getHelper().addFilter("Name", DmDataType.STRING, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        this.asWidget().setTitle(widgetName);
        for (String ctrlPermission : menuPermission.getControlPermission()) {
            if (ctrlPermission.equalsIgnoreCase(savePriceBlog)) {
                allowSavePrice = true;
            } else if (ctrlPermission.equalsIgnoreCase(newRateListBlog)) {
                isCreateNewRateList = true;
            } else if (ctrlPermission.equalsIgnoreCase(deletePriceBlog)) {
                deletePrice = true;
            }
        }
        manageEvents();
        textHelpEvents();
        findEvents();
        gridEvents();
        manageDisplay(true);
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHPriceList).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    swi.setWidget(PriceListMaster.this);
                    defaultMdl = result.getData();
                    txtHPriceList.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHPriceList).show();
                }
            }
        });
    }

    private void manageEvents() {
        txtApplicableDate.addBlurHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        mnuApplyDiscount.addSelectionHandler(this);
        mnuImportApplicableRate.addSelectionHandler(this);
        flwForm.addAttachHandler(this);
        priceListBtn.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnFind.addClickHandler(this);
        btnOther.addClickHandler(this);
        lblPriceList.addClickHandler(this);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PriceListMaster.this);
                }
            }
        }).show();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            itemGridEdit.hide();
            priceListPopup.hide();
            applyDiscPopup.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onFind();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_O && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOther();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F2) {
            showCreateRateList();
        }

    }

    private void onFind() {
        flwForm.remove(vrtInnerForm);
        flwForm.add(dmFind);
    }

    private void onOther() {
        menu.show(btnOther);
    }

    private void manageDisplay(boolean flag) {

        txtPriceListType.setEnabled(false);
        txtApplicableDate.setEnabled(false);
        txtSearchValue.setEnabled(true);
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuApplyDiscount)) {
            applyDiscPopup.show(this);
        } else if (event.getSource().equals(mnuImportApplicableRate)) {
            importPopup.show(DmShared.xNull(txtHPriceList.getTag()), txtApplicableDate.getText());
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                ((CellSelectionModel) grdItemDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 5);
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
                reQuery(0);
            }
            searchPrvValue = txtSearchValue.getText();
        }
    }
}
