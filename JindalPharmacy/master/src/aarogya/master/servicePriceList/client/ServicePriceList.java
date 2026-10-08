package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.*;
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
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.*;

public final class ServicePriceList extends ServicePriceList_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item>,
        KeyUpHandler, KeyDownHandler, DmRootEvents {

    private final ServiceInvoker<ServicePriceList_SrvAsync> srvAsync;
    private final ServicesGridEdit<GrdServices_Mdl, GrdServices_PrpAcs> serviceGridEdit = new ServicesGridEdit(grdService.getDmGrid(), "Service Detail");
    protected List<GrdRoomCategory_Mdl> lstRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    private final SPLApplicableWithOption priceListPopup = new SPLApplicableWithOption();
    private final ApplyDiscountPopup applyDiscPopup = new ApplyDiscountPopup();
    private final ImportPopup importPopup = new ImportPopup();
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private String searchDbFieldName = "name", searchPrvValue = "";
    private final PercentPopup percentPopup = new PercentPopup();
    private ServicePriceMaster_Mdl mrTemp = null;
    private boolean allowSavePrice = false, deletePrice = false;

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
                topControlMoveRec(DmShared.xNull(txtName.getTag()));
            }
        });
        percentPopup.addCallBack(new PercentPopup.CallBack() {
            @Override
            public void onSave(ServicePriceMaster_Mdl mrTemp) {
                if (mrTemp != null) {
                    txtFeesDiscount.setText(mrTemp.getFeesDiscountPer());
                    txtInvestigationDiscount.setText(mrTemp.getTestDiscountPer());
                    txtMedicineSaleOn.setText(mrTemp.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : mrTemp.getMedicineSaleOn().getValue());
                    txtPharmacyDiscount.setText(mrTemp.getPharmacyDiscountPer());
                    txtPharmacyDiscountAllow.setText(mrTemp.isAllowPharmacyDiscount() ? "Yes" : "No");
                    txtServiceDiscount.setText(mrTemp.getServiceDiscountPer());
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
            public void getNewPriceList(ServicePriceMaster_Mdl organizationMdl, String newApplicableDate) {
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
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox());
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
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdServices_Mdl>> result) {
                        if (result.isStatus()) {
                            grdService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
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
        svm.setApplicableDate(DmShared.dNull(txtApplicableDate.getText()));
        svm.setOrganizationCode(DmShared.xNull(txtName.getTag()));
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
            if (DmShared.iNull(txtName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please select a Record.", priceListBtn).show();
                return;
            } else {
                priceListPopup.show(priceListBtn, true, true, PriceListEntryFrom.ORGANIZATION_MASTER, DmShared.xNull(txtName.getTag()), DmShared.xNull(txtName.getTag()), DmShared.xNull(txtName.getTag2nd()));
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
        }
    }

    private void gridServiceEdit(Integer currentRowIndex) {
        serviceGridEdit.show(currentRowIndex, allowSavePrice, false, deletePrice, this);
    }

    public ServicePriceList(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(ServicePriceList_Srv.class), ServicePriceList.class.getName());
        grdService.getHelper().addFilter("Name", DmDataType.STRING, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        this.asWidget().setTitle(widgetName);
        flwPercent.clear();
        for (String ctrlPermission : menuPermission.getControlPermission()) {
            if (ctrlPermission.equalsIgnoreCase(percentBlog)) {
                flwPercent.add(btnDiscountPercent);
            }
            if (ctrlPermission.equalsIgnoreCase(savePriceBlog)) {
                allowSavePrice = true;
            }
            if (ctrlPermission.equalsIgnoreCase(deletePriceBlog)) {
                deletePrice = true;
            }
        }
        manageEvents();
        textHelpEvents();
        findEvents();
        gridEvents();
        manageDisplay(true);
        swi.setWidget(this);
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
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ServicePriceList.this);
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ServicePriceMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                mrTemp = null;
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ServicePriceMaster_Mdl> result) {
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

    private void topControlMoveRecSuccess(ServicePriceMaster_Mdl data) {
        txtName.setTag2nd(DmShared.xNull(data.getSiteCode()));
        txtName.setTag(DmShared.xNull(data.getOrganizationCode()));
        txtName.setText(DmShared.xNull(data.getOrganizationName()));
        txtShortName.setText(data.getShortName());
        txtFeesDiscount.setText(data.getFeesDiscountPer());
        txtInvestigationDiscount.setText(data.getTestDiscountPer());
        txtMedicineSaleOn.setText(data.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : data.getMedicineSaleOn().getValue());
        txtPharmacyDiscount.setText(data.getPharmacyDiscountPer());
        txtPharmacyDiscountAllow.setText(data.isAllowPharmacyDiscount() ? "Yes" : "No");
        txtServiceDiscount.setText(data.getServiceDiscountPer());
        txtActive.setText(data.isActive() ? "Yes" : "No");
        txtNature.setText(data.getOrgnizationType());
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
        txtShortName.setEnabled(false);
        txtName.setEnabled(false);
        txtApplicableDate.setEnabled(false);
        txtActive.setEnabled(false);
        txtFeesDiscount.setEnabled(false);
        txtInvestigationDiscount.setEnabled(false);
        txtMedicineSaleOn.setEnabled(false);
        txtNature.setEnabled(false);
        txtPharmacyDiscount.setEnabled(false);
        txtPharmacyDiscountAllow.setEnabled(false);
        txtServiceDiscount.setEnabled(false);
        txtHServiceNature.setEnabled(true);
        txtSearchValue.setEnabled(true);
    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        HashMap<String, String> hmOrganization = ((HashMap<String, String>) object);
        try {
            if (hmOrganization != null) {
                switch (nature) {
                    case "OPEN_ORGANIZATION":
                        topControlMoveRec(((String) hmOrganization.get("organizationCode")), callBack);
                        break;
                    case "ADD_INVESTIGATION_SERVICE":
                        txtSearchValue.setText(DmShared.xNull((String) hmOrganization.get("serviceCode")));
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
            importPopup.show(DmShared.xNull(txtName.getTag()), txtApplicableDate.getText());
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
