package dataman.utility.numberSystem.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.numberSystem.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.utility.numberSystem.shared.GrdCounter_Mdl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NumberSystem extends NumberSystem_Dsg implements AttachEvent.Handler, ClickHandler {

    ServiceInvoker<NumberSystem_SrvAsync> SrvAsync;
    EntryGridEdit<GrdNumberFormat_Mdl, Grd_Entry> grdEntry = new EntryGridEdit(dmEntryGrd);
    VoucherGridEdit<KeyFieldExtended, KeyField_PrpAcs> grdVoucherEntry = new VoucherGridEdit(dmVoucherGrd);
    Counter counterPopup = new Counter();
    NumberSystem_Mdl counterList;

    private void topControlEvents(UserMenu menuPermission) {

        counterPopup.addCallBack(new Counter.CallBack() {
            @Override
            public void onOk(List<GrdCounter_Mdl> keyFields) {
                //System.out.println(keyFields);
            }
        });

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHNSVtype.setFocus(true);
                dmVoucherGrd.getGrid().getStore().clear();
                dmVoucherGrd.getGrid().getStore().add(new KeyFieldExtended());
                dmEntryGrd.getGrid().getStore().clear();
                dmEntryGrd.getGrid().getStore().add(new GrdNumberFormat_Mdl());
                counterPopUpButton.setVisible(false);

            }
        });
        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHNSVtype.setFocus(true);
                txtHNSVtype.setEnabled(false);
                dmVoucherGrd.getGrid().getStore().add(new KeyFieldExtended());
                dmEntryGrd.getGrid().getStore().add(new GrdNumberFormat_Mdl());

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
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
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(NumberSystem.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHNSVtype.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNSVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHNSVtype.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHNSVtype).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
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
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getNsGroup());
                }
            }
        });
    }

    private void gridEvent() {
        grdEntry.addCallBack(new EntryGridEdit.CallBack() {

            @Override
            public void onOk(GrdNumberFormat_Mdl linkedUserGrid) {
                dmEntryGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmEntryGrd.commitChanges();
            }
        });

        dmEntryGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showLinkedGridEdit(event.getRowIndex());
            }
        });
        dmEntryGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showLinkedGridEdit(dmEntryGrd.getGrid().getStore().indexOf(dmEntryGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showLinkedGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdEntry.show(currentRowIndex, false, false, false);
        } else {
            grdEntry.show(currentRowIndex, true, true, true);
        }
    }

    private void gridVoucherEvent() {
        grdVoucherEntry.addCallBack(new VoucherGridEdit.CallBack() {

            @Override
            public void onOk(KeyFieldExtended linkedUserGrid) {
                dmVoucherGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmVoucherGrd.commitChanges();
            }
        });

        dmVoucherGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showLinkedVoucherGridEdit(event.getRowIndex());
            }
        });
        dmVoucherGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showLinkedVoucherGridEdit(dmVoucherGrd.getGrid().getStore().indexOf(dmVoucherGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showLinkedVoucherGridEdit(int currentRowIndex) {

        if (DmShared.xNull(txtHNSVtype.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Select Voucher Type.", txtHNSVtype).show();
            return;
        }

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdVoucherEntry.show(currentRowIndex, false, false, false, DmShared.xNull(txtHNSVtype.getTag()));
        } else {
            grdVoucherEntry.show(currentRowIndex, true, true, true, DmShared.xNull(txtHNSVtype.getTag()));
        }
    }

    public NumberSystem(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<NumberSystem_SrvAsync>(GWT.create(NumberSystem_Srv.class), NumberSystem.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvent();
        gridVoucherEvent();
        flwForm.addAttachHandler(this);
        swi.setWidget(this);
        handlerEvents();

    }

    private void handlerEvents() {
        counterPopUpButton.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private NumberSystem_Mdl topControlSaveInitialize() {
        NumberSystem_Mdl mrTemp = new NumberSystem_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHNSVtype.getTag()));

        List<KeyFieldExtended> grd = new ArrayList<>();
        grd.addAll(dmVoucherGrd.getGrid().getStore().getAll());
        mrTemp.getGrdVType().addAll(grd);

        List<GrdNumberFormat_Mdl> entryGrd = new ArrayList<>();
        entryGrd.addAll(dmEntryGrd.getGrid().getStore().getAll());
        mrTemp.getGrdNumberFormat().addAll(entryGrd);

        return mrTemp;
    }

    private void topControlSave() {
        NumberSystem_Mdl mrEntryTemp;
        try {
            mrEntryTemp = topControlSaveInitialize();
            NumberSystem_Vld.chkSave(mrEntryTemp);

            SrvAsync.invokeRPC().saveRecord(mrEntryTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
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
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHNSVtype).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHNSVtype).show();
                break;
        }

    }

    private void topControlCard() {

        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<NumberSystem_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
            }

            @Override
            public void onSuccess(DmResult<NumberSystem_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<NumberSystem_Mdl> result) {
        NumberSystem_Mdl data;
        if (result.isStatus()) {
            counterPopUpButton.setVisible(true);
            data = result.getData();
            counterList = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtHNSVtype.setText(data.getSharedKeyFieldName());
            txtHNSVtype.setTag(data.getSharedKeyFieldCode());

            dmVoucherGrd.getGrid().getStore().clear();
            dmVoucherGrd.getGrid().getStore().addAll(data.getGrdVType());

            dmEntryGrd.getGrid().getStore().clear();
            dmEntryGrd.getGrid().getStore().addAll(data.getGrdNumberFormat());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), txtHNSVtype).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHNSVtype).show();
                }
            }
        });
    }

    private void exportToCSV() {
        SrvAsync.invokeRPC().exportDataToCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHNSVtype).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended kf = result.getData();
                    startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHNSVtype).show();
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            grdEntry.hide();
            counterPopup.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource().equals(counterPopUpButton)) {

            if ((topControl.getMode().equalsIgnoreCase("Browse") || topControl.getMode().equalsIgnoreCase("Edit"))) {
                counterPopup.show(topControl.getDocId(), this);
            }
        }
    }

    private void clear() {
        userControl.setTextBlog(modifiedByBlog, "");
        dmVoucherGrd.getGrid().getStore().clear();
        dmEntryGrd.getGrid().getStore().clear();
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportToCSV();
                        }
                    }
                }).show();

            }
        }
    }
}
