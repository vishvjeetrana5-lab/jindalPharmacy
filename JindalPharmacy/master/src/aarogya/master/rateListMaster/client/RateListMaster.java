package aarogya.master.rateListMaster.client;

import aarogya.master.rateListMaster.shared.*;
import aarogya.selfToolkit.splApplicableWithOption.client.SPLApplicableWithOption;
import aarogya.selfToolkit.splApplicableWithOption.shared.Applicable_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import java.util.*;

public final class RateListMaster extends RateListMaster_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item>,
        KeyUpHandler, KeyDownHandler, DmRootEvents {

    private final ServiceInvoker<RateListMaster_SrvAsync> srvAsync;
    private final ServicesGridEdit<GrdServices_Mdl, GrdServices_PrpAcs> serviceGridEdit = new ServicesGridEdit(grdService.getDmGrid(), "Service Detail");
    protected List<GrdRoomCategory_Mdl> lstRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    private final SPLApplicableWithOption priceListPopup = new SPLApplicableWithOption();
    private final ApplyDiscountPopup applyDiscPopup = new ApplyDiscountPopup();
    private final ImportPopup importPopup = new ImportPopup();
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private String searchDbFieldName = "name", searchPrvValue = "";
    private DefaultValue_Mdl defaultMdl;
    private final PercentPopup percentPopup = new PercentPopup();
    private RateListMaster_Mdl mrTemp = null;
    private boolean allowSavePrice = false, isCreateNewRateList = false, deletePrice = false;
    private final CreateRatelListPopup createRateList = new CreateRatelListPopup();

    private void setPercentagePopup(RateListMaster_Mdl mrTemp) {
        this.mrTemp.setMedicineSaleOn(mrTemp.getMedicineSaleOn());
        this.mrTemp.setPharmacyDiscountPer(mrTemp.getPharmacyDiscountPer());
        this.mrTemp.setAllowPharmacyDiscount(mrTemp.isAllowPharmacyDiscount());
    }

    private void textHelpEvents() {
        priceListPopup.addCallBack(new SPLApplicableWithOption.CallBack() {

            @Override
            public void onOk(Applicable_Mdl applicableMdl) {
                if (applicableMdl != null) {
                    txtApplicableDate.setText(DmShared.dNull(applicableMdl.getDateApplicable()));
                    reQuery(0);
                }
            }

            @Override
            public void onDelete() {
                topControlMoveRec(DmShared.xNull(txtHRateList.getTag()));
            }
        });
        percentPopup.addCallBack(new PercentPopup.CallBack() {
            @Override
            public void onSave(RateListMaster_Mdl mrTemp) {
                if (mrTemp != null) {
//                    txtFeesDiscount.setText(mrTemp.getFeesDiscountPer());
//                    txtInvestigationDiscount.setText(mrTemp.getTestDiscountPer());
                    txtMedicineSaleOn.setText(mrTemp.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : mrTemp.getMedicineSaleOn().getValue());
                    txtPharmacyDiscount.setText(mrTemp.getPharmacyDiscountPer());
                    txtPharmacyDiscountAllow.setText(mrTemp.isAllowPharmacyDiscount() ? "Yes" : "No");
                    setPercentagePopup(mrTemp);
//                    txtServiceDiscount.setText(mrTemp.getServiceDiscountPer());
                }
            }

            @Override
            public void onClose() {
                txtSearchValue.setFocus(true);
            }
        });
        applyDiscPopup.addCallBack(new ApplyDiscountPopup.CallBack() {

            @Override
            public void refresh(boolean callReQuery) {
                if (callReQuery) {
                    reQuery(grdService.getHelper().getRowsStartFrom());
                }
            }

        });
        importPopup.addCallBack(new ImportPopup.CallBack() {
            @Override
            public void getNewPriceList(RateListMaster_Mdl organizationMdl, String newApplicableDate) {
                txtApplicableDate.setText(DmShared.dNull(newApplicableDate));
                reQuery(0);
            }
        });
        txtHServiceNature.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getServiceNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {
                    reQuery(0);
                }
            }
        });
        txtHRateList.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    txtSearchValue.setFocus(true);
                }
            }
        });
        txtHRateList.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<RateList_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRateList(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<RateList_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<RateList_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRateList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, RateList_Mdl tRow) {
                if (tRow != null && blnStatus) {
                    txtRateListType.setText(tRow.getRateListTypeEnum().getText());
                    txtRateListType.setTag(DmShared.iNull(tRow.getRateListTypeEnum().getId()).toString());
                    topControlMoveRec(DmShared.xNull(tRow.getSharedKeyFieldCode()));
                }
//                reQuery(0);
            }
        });
        createRateList.addCallBack(new CreateRatelListPopup.CallBack() {

            @Override
            public void onOk(RateList_Mdl mrTemp) {
                if (mrTemp != null) {
                    if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() > 0) {
                        txtHRateList.setTag(mrTemp.getSharedKeyFieldCode());
                        txtHRateList.setText(mrTemp.getSharedKeyFieldName());
                        txtRateListType.setTag(DmShared.iNull(mrTemp.getRateListTypeEnum().getId()).toString());
                        txtRateListType.setText(DmShared.xNull(mrTemp.getRateListTypeEnum().getText()));
                        topControlMoveRec(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        reQuery(0);
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                txtHRateList.setFocus(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void onClose() {
                txtHRateList.setFocus(true);
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
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        serviceGridEdit.addCallBack(new ServicesGridEdit.CallBack() {
            @Override
            public void refresh() {
                reQuery(grdService.getHelper().getRowsStartFrom());
            }
        });
        grdService.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridServiceEdit(event.getRowIndex());
            }
        });
        grdService.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridServiceEdit(grdService.getDmGrid().getGrid().getStore().indexOf(grdService.getDmGrid().getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        grdService.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getServiceAndInvestigation(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdServices_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdServices_Mdl>> result) {
                        if (result.isStatus()) {
                            grdService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
            }
        });
        grdService.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
                DmColumnConfig col = ((DmColumnConfig) grdService.getDmGrid().getGrid().getColumnModel().getColumn(event.getColumnIndex()));
                searchDbFieldName = col.getDbFieldName();
                searchDbFieldDataType = col.getDbFieldDataType();

            }
        });
    }

    protected SearchValue_Mdl setParam() {
        SearchValue_Mdl svm = new SearchValue_Mdl();
        if (DmShared.dNull(txtApplicableDate.getText()).equalsIgnoreCase("")) {
            svm.setApplicableDate(DmShared.dNull(DmClient.getStartDate()));
        } else {
            svm.setApplicableDate(DmShared.dNull(txtApplicableDate.getText()));
        }
        svm.setRateListCode(DmShared.xNull(txtHRateList.getTag()));
        svm.setServiceNature(DmShared.xNull(txtHServiceNature.getTag()));
        return svm;
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdService.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            columnIndex = 0;
        } else if (columnIndex < 0) {
            columnIndex = grdService.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            columnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdService.getDmGrid().getGrid().getColumnModel().getColumn(columnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdService.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdService.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdService.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }

        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdService.getDmGrid().getGrid().getView().refresh(true);

        txtSearchValue.setFocus(true);
    }

    private void reQuery(int rowsStartFrom) {
        grdService.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdService.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdService.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        grdService.reQuery(rowsStartFrom, setParam());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(priceListBtn)) {
            if (DmShared.iNull(txtHRateList.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select a Record.", priceListBtn).show();
                return;
            } else {
                priceListPopup.show(priceListBtn, true, true, PriceListEntryFrom.RATE_LIST_MASTER, DmShared.xNull(txtHRateList.getTag()), DmShared.xNull(txtHRateList.getTag()), DmShared.xNull(defaultMdl.getSiteCode()));
            }
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnFind)) {
            onFind();
        } else if (event.getSource().equals(btnOther)) {
            onOther();
        } else if (event.getSource().equals(btnDiscountPercent)) {
            if (mrTemp != null) {

                percentPopup.show(mrTemp);
            }
        } else if (event.getSource().equals(lblName)) {
            showCreateRateList();
        }
    }

    private void showCreateRateList() {
        if (isCreateNewRateList) {
            createRateList.show(this);
        }
    }

    private void gridServiceEdit(Integer currentRowIndex) {
        serviceGridEdit.show(currentRowIndex, allowSavePrice, false, deletePrice, this);
    }

    public RateListMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(RateListMaster_Srv.class), RateListMaster.class.getName());
        grdService.getHelper().addFilter("Name", DmDataType.STRING, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        this.asWidget().setTitle(widgetName);
        flwPercent.clear();
        for (String ctrlPermission : menuPermission.getControlPermission()) {
            if (ctrlPermission.equalsIgnoreCase(percentBlog)) {
                flwPercent.add(btnDiscountPercent);
            } else if (ctrlPermission.equalsIgnoreCase(savePriceBlog)) {
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
                new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    swi.setWidget(RateListMaster.this);
                    defaultMdl = result.getData();
                    txtHRateList.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
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
        btnDiscountPercent.addClickHandler(this);
        lblName.addClickHandler(this);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RateListMaster.this);
                }
            }
        }).show();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlMoveRec(String strKey) {
        topControlMoveRec(strKey, null);
    }

    private void topControlMoveRec(String strKey, final DmCallBack callBack) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<RateListMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                mrTemp = null;
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<RateListMaster_Mdl> result) {
                mrTemp = null;
                if (result.isStatus()) {
                    topControlMoveRecSuccess(result.getData());
                    mrTemp = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
                if (callBack != null && result.isStatus()) {
                    DmResult<Object> dmrTemp = new DmResult<>();
                    dmrTemp.setStatus(true);
                    callBack.result(dmrTemp);
                } else {
                    reQuery(0);
                }
            }
        });
    }

    private void topControlMoveRecSuccess(RateListMaster_Mdl data) {
//        txtName.setTag2nd(DmShared.xNull(data.getSiteCode()));
        txtHRateList.setTag(DmShared.xNull(data.getRateListCode()));
        txtHRateList.setText(DmShared.xNull(data.getRateListName()));
        txtRateListType.setTag(DmShared.xNull(data.getRateListTypeCode()));
        txtRateListType.setText(DmShared.xNull(data.getRateLisTypeName()));
//        txtShortName.setText(data.getShortName());
//        txtFeesDiscount.setText(data.getFeesDiscountPer());
//        txtInvestigationDiscount.setText(data.getTestDiscountPer());
        txtMedicineSaleOn.setText(data.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : data.getMedicineSaleOn().getValue());
        txtPharmacyDiscount.setText(data.getPharmacyDiscountPer());
        txtPharmacyDiscountAllow.setText(data.isAllowPharmacyDiscount() ? "Yes" : "No");
//        txtServiceDiscount.setText(data.getServiceDiscountPer());
//        txtActive.setText(data.isActive() ? "Yes" : "No");
//        txtNature.setText(data.getOrgnizationType());
        txtApplicableDate.setText(DmShared.dNull(data.getApplicableDate()).equalsIgnoreCase("") ? DmClient.getStartDate() : data.getApplicableDate());
        manageDisplay(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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
            serviceGridEdit.hide();
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
        txtRateListType.setEnabled(false);
        txtApplicableDate.setEnabled(false);
//        txtActive.setEnabled(false);
//        txtFeesDiscount.setEnabled(false);
//        txtInvestigationDiscount.setEnabled(false);
        txtMedicineSaleOn.setEnabled(false);
//        txtNature.setEnabled(false);
        txtPharmacyDiscount.setEnabled(false);
        txtPharmacyDiscountAllow.setEnabled(false);
//        txtServiceDiscount.setEnabled(false);
        txtHServiceNature.setEnabled(true);
        txtSearchValue.setEnabled(true);
    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        HashMap<String, String> hmRateListCode = ((HashMap<String, String>) object);
        try {
            if (hmRateListCode != null) {
                switch (nature) {
                    case "OPEN_ORGANIZATION":
                        topControlMoveRec(((String) hmRateListCode.get("organizationCode")), callBack);
                        break;
                    case "OPEN_RATELIST":
                        topControlMoveRec(((String) hmRateListCode.get("rateListCode")), callBack);
                        break;
                    case "ADD_INVESTIGATION_SERVICE":
                        txtSearchValue.setText(DmShared.xNull((String) hmRateListCode.get("serviceCode")));
                        reQuery(0);
                        break;
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuApplyDiscount)) {
            applyDiscPopup.show(this);
        } else if (event.getSource().equals(mnuImportApplicableRate)) {
            importPopup.show(DmShared.xNull(txtHRateList.getTag()), txtApplicableDate.getText());
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                ((CellSelectionModel) grdService.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 5);
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
