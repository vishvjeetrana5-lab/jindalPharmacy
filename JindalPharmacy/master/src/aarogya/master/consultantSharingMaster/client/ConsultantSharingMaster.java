package aarogya.master.consultantSharingMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantSharingMaster.shared.*;
import aarogya.master.reports.client.RptSharingMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.LinkedHashMap;
import java.util.List;

public final class ConsultantSharingMaster extends ConsultantSharingMaster_Dsg implements ClickHandler, BlurHandler {

    private final ServiceInvoker<ConsultantSharingMaster_SrvAsync> SrvAsync;
    private final SharingDetailGridEdit<GrdSharingDetail_Mdl, GridSharingDetail_PrpAcs> sharingDetailGridEdit = new SharingDetailGridEdit(gridSharingDetail);
    private final SharingApplicableFrom splApplicableFrom = new SharingApplicableFrom();
    private boolean setCopyDate = false, isSearch = false;
    final CopySharingPopup copySharingPopup = new CopySharingPopup();
    private final SearchPopup searchPopup = new SearchPopup();
    private SearchValue_Mdl search_Mdl = new SearchValue_Mdl();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                txtHConsultant.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                flwApplicable.clear();
                manageDisplay(false);
                gridSharingDetail.getGrid().getStore().add(new GrdSharingDetail_Mdl());
                ((CellSelectionModel) gridSharingDetail.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId(), "");
                        }
                    }
                }).show();
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
                topControlMoveRec(strFetchCode, "");
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

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {

                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ConsultantSharingMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultantName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        splApplicableFrom.addCallBack(new SharingApplicableFrom.CallBack() {

            @Override
            public void onOk(String newDtApplicable, String oldDtApplicable) {
                txtApplicableFrom.setTag(DmShared.dNull(oldDtApplicable));
                txtApplicableFrom.setText(DmShared.dNull(newDtApplicable));
                if (!DmShared.dNull(oldDtApplicable).equalsIgnoreCase("")) {
                    setCopyDate = true;
                    topControlMoveRec(DmShared.iNull(txtHConsultant.getTag()).toString(), DmShared.dNull(txtApplicableFrom.getTag()));
                }
            }

            @Override
            public void onDelete(String applicableFrom) {
                topControlDelete(topControl.getDocId(), DmShared.dNull(applicableFrom));
            }
        });
        copySharingPopup.addCallBack(new CopySharingPopup.CallBack() {
            @Override
            public void onOk(String consultantCode, String applicableDate) {
                if (DmShared.iNull(consultantCode).longValue() != 0) {
                    getSharingDetail(consultantCode, applicableDate);
                }
                ((CellSelectionModel) gridSharingDetail.getGrid().getSelectionModel()).selectCell(0, 1);
            }

            @Override
            public void onCancel() {
                ((CellSelectionModel) gridSharingDetail.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });
        searchPopup.addCallBack(new SearchPopup.CallBack() {

            @Override
            public void onOk(SearchValue_Mdl mdl) {
                search_Mdl = mdl;
                if (search_Mdl != null) {
                    isSearch = true;
                    topControlMoveRec(topControl.getDocId(), DmShared.dNull(txtApplicableFrom.getText()));
                }
            }

            @Override
            public void onCancel() {
            }

        });
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
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode(), "");
                }
            }
        });
    }

    private void prnRecord(boolean flag) {
        RptSharingMaster sharingMast = new RptSharingMaster();
        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
        FieldUDT_Mdl filter;

        filter = new FieldUDT_Mdl();

        filter.setText(DmShared.xNull(txtHConsultant.getText()));
        filter.setTag(DmShared.xNull(topControl.getDocId()));
        hashMapFilter.put("consultantCode", filter);

        filter = new FieldUDT_Mdl();
        filter.setText(DmShared.xNull(txtApplicableFrom.getText()));
        filter.setTag(DmShared.xNull(txtApplicableFrom.getText()));

        hashMapFilter.put("applicableFrom", filter);

        sharingMast.setDefault(hashMapFilter, flag, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void gridEvents() {
        sharingDetailGridEdit.addCallBack(new SharingDetailGridEdit.CallBack() {

            @Override
            public void onOk(GrdSharingDetail_Mdl grdSharingDetail_Mdl) {
                gridSharingDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                gridSharingDetail.commitChanges();
            }

        });
        gridSharingDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showSharingDetailGridEdit(event.getRowIndex());
            }
        });
        gridSharingDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showSharingDetailGridEdit(gridSharingDetail.getGrid().getStore().indexOf(gridSharingDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showSharingDetailGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            sharingDetailGridEdit.show(currentRowIndex, false, false, false);
        } else {
            sharingDetailGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    public ConsultantSharingMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(ConsultantSharingMaster_Srv.class), ConsultantSharingMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        manageDisplay(false);
        gridEvents();
        flwApplicable.clear();
        flwApplicable.add(btnPriceList);
        btnPriceList.addClickHandler(this);
        swi.setWidget(this);
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key, "");
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnPriceList)) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                splApplicableFrom.show(btnPriceList, true, true, DmShared.iNull(txtHConsultant.getTag()).toString());
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtApplicableFrom)) {
            txtApplicableFrom.setText(DmClient.returnDate(txtApplicableFrom.getText()));
        }
    }

    private void topControlSave() {
        ConsultantSharingMaster_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            ConsultantSharingMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId(), DmShared.dNull(txtApplicableFrom.getText()));
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

    private ConsultantSharingMaster_Mdl topControlSaveInitialize() {
        ConsultantSharingMaster_Mdl mrTemp = new ConsultantSharingMaster_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHConsultant.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHConsultant.getText()));

        if (DmShared.dNull(txtApplicableFrom.getText()).equalsIgnoreCase("")) {
            mrTemp.setApplicableFrom(DmShared.dNull(DmClient.getStartDate()));
        } else {
            mrTemp.setApplicableFrom(DmShared.dNull(txtApplicableFrom.getText()));
        }
        mrTemp.getListSharingDetail().addAll(gridSharingDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHConsultant).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHConsultant).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void topControlMoveRec(String strKey, String applicableFrom) {
        SrvAsync.invokeRPC().getMoveRec(strKey, DmShared.dNull(applicableFrom), search_Mdl, new AsyncCallback<DmResult<ConsultantSharingMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ConsultantSharingMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ConsultantSharingMaster_Mdl> result) {
        ConsultantSharingMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            flwApplicable.clear();
            flwApplicable.add(btnPriceList);

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHConsultant.setTag(data.getSharedKeyFieldCode());
            txtHConsultant.setText(data.getSharedKeyFieldName());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            if (!setCopyDate) {
                txtApplicableFrom.setText(DmShared.dNull(data.getApplicableFrom()));
            }
            setCopyDate = false;

            gridSharingDetail.getGrid().getStore().clear();
            gridSharingDetail.getGrid().getStore().addAll(data.getListSharingDetail());
            if (isSearch) {
                for (int i = 0; i < gridSharingDetail.getGrid().getStore().size(); i++) {
                    GrdSharingDetail_Mdl share_mdl = gridSharingDetail.getGrid().getStore().get(i);
                    if (DmShared.iNull(share_mdl.getSearchStatus()).intValue() == 1) {
                        focusToGrd(i);
                        break;

                    }

                }

            }
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);

    }

    private void topControlDelete(String strKey, String applicableFrom) {
        SrvAsync.invokeRPC().deleteRecord(strKey, DmShared.dNull(applicableFrom), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtHConsultant.setEnabled(flag);
        txtApplicableFrom.setEnabled(false);
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(miCopySharing.getText())) {
            if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                copySharingPopup.show(DmShared.xNull(txtHConsultant.getTag()));
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(searching.getText())) {
            searchPopup.show();
        }
    }

    private void getSharingDetail(String consultantCode, String applicableDate) {
        SrvAsync.invokeRPC().getMoveRec(consultantCode, applicableDate, null, new AsyncCallback<DmResult<ConsultantSharingMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ConsultantSharingMaster_Mdl> result) {
                if (result.isStatus()) {
                    gridSharingDetail.getGrid().getStore().clear();
                    gridSharingDetail.getGrid().getStore().addAll(result.getData().getListSharingDetail());
                    gridSharingDetail.getGrid().getStore().add(new GrdSharingDetail_Mdl());
                    ((CellSelectionModel) gridSharingDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void focusToGrd(int i) {
        final int row = i;
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ((CellSelectionModel) gridSharingDetail.getGrid().getSelectionModel()).selectCell(row, 1);
            }
        });

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        flwApplicable.clear();
        gridSharingDetail.getGrid().getStore().add(new GrdSharingDetail_Mdl());
    }

    private void clear() {
        gridSharingDetail.getGrid().getStore().clear();
        userControl.setTextBlog(modifiedByBlog, "");
    }

}
