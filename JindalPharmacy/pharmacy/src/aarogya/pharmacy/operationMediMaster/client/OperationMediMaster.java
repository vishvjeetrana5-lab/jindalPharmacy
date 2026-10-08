package aarogya.pharmacy.operationMediMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.operationMediMaster.shared.*;
import aarogya.pharmacy.reports.client.RptOperationMediMast;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class OperationMediMaster extends OperationMediMaster_Dsg implements IsWidget, AttachEvent.Handler {

    ServiceInvoker<OperationMediMaster_SrvAsync> SrvAsync;
    final OperationMediDetailsGridEdit<GrdOperationDetails_Mdl, Grd_PrpAcs> gridEdit = new OperationMediDetailsGridEdit(dmGrd);
    private String moduleName = new pmPharmacy().moduleName;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHService.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                dmGrd.getGrid().getStore().add(new GrdOperationDetails_Mdl());
                txtHService.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OperationMediMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        RptOperationMediMast operationMedMast = new RptOperationMediMast();
        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
        FieldUDT_Mdl filter;

        filter = new FieldUDT_Mdl();
        filter.setText(DmShared.xNull(txtHService.getText()));
        filter.setTag(DmShared.xNull(txtHService.getTag()));
        hashMapFilter.put("CODE", filter);

        operationMedMast.setDefault(hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    //  dmGrd.getGrid().getStore().add(new GrdOperationDetails_Mdl());
                    fillMedicineDetails(txtHService.getTag());

                } else {

                }

            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {

        gridEdit.addCallBack(new OperationMediDetailsGridEdit.CallBack() {
            @Override
            public void onOk(GrdOperationDetails_Mdl grdVoucherEntry) {
                dmGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrd.commitChanges();
            }
        });

        dmGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showOperationGridEdit(event.getRowIndex());
            }
        });

        dmGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showOperationGridEdit(dmGrd.getGrid().getStore().indexOf(dmGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
        } else {
            gridEdit.hide();
        }
    }

    private void showOperationGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEdit.show(currentRowIndex, false, false, false);
        } else {
            gridEdit.show(currentRowIndex, true, true, true);
        }
    }

    public OperationMediMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(OperationMediMaster_Srv.class), OperationMediMaster.class.getName());
        this.asWidget().setTitle("Operation Medicine Master");
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        swi.setWidget(this);

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<OperationMediMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OperationMediMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<OperationMediMaster_Mdl> result) {
        OperationMediMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHService.setTag(data.getSharedKeyFieldCode());
            txtHService.setText(data.getSharedKeyFieldName());

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdData());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        OperationMediMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            OperationMediMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHService).show();
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

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().add(new GrdOperationDetails_Mdl());
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHService).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHService).show();
                break;
        }
    }

    private OperationMediMaster_Mdl topControlSaveInitialize() {
        OperationMediMaster_Mdl mrTemp = new OperationMediMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        dmGrd.getGrid().getStore().commitChanges();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHService.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHService.getText()));
        List<GrdOperationDetails_Mdl> OBJList = new ArrayList<>();
        OBJList.addAll(dmGrd.getGrid().getStore().getAll());

        mrTemp.getGrdData().addAll(OBJList);
        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        dmGrd.getGrid().getStore().clear();
        dmGrd.getGrid().getStore().add(new GrdOperationDetails_Mdl());

    }

    private void fillMedicineDetails(String serviceCode) {
        SrvAsync.invokeRPC().getMoveRec(serviceCode, new AsyncCallback<DmResult<OperationMediMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OperationMediMaster_Mdl> result) {
                OperationMediMaster_Mdl data;
                data = result.getData();
                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(data.getGrdData());
                if (!DmShared.xNull(data.getGrdData().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    dmGrd.getGrid().getStore().add(new GrdOperationDetails_Mdl());
                }
            }
        });
    }

}
