package aarogya.master.patientScheduleMaster.client;

import aarogya.master.patientScheduleMaster.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public class PatientScheduleMaster extends PatientScheduleMaster_Dsg {

    private final ServiceInvoker<PatientScheduleMaster_SrvAsync> srvAsync;
    private final PatientScheduleMasterGridEdit<GrdPatntSchdulMster_Mdl, GridService_PrpAcs> grdEdtPatntSchdl = new PatientScheduleMasterGridEdit(grdService);

    public PatientScheduleMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<PatientScheduleMaster_SrvAsync>(GWT.create(PatientScheduleMaster_Srv.class), PatientScheduleMaster.class.getName());
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();

                txtName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                grdService.getGrid().getStore().add(new GrdPatntSchdulMster_Mdl());

                txtName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PatientScheduleMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void textHelpEvents() {

        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSugtnPatSchdlName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });

        txtShortName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtShortName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtShortName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtShortName).show();
                        }
                    }
                });
            }
        });

        txtHSchdlGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSchdlGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSchdlGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSchdlGroup.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSchdlGroup).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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
        grdEdtPatntSchdl.addCallBack(new PatientScheduleMasterGridEdit.CallBack() {

            @Override
            public void onOk(GrdPatntSchdulMster_Mdl grdPatntMstrMdl) {
                grdService.commitChanges();

            }

            @Override
            public void onDelete() {
                grdService.commitChanges();
            }
        });
        grdService.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPatientScheduleMasterGridEdit(event.getRowIndex());
            }
        });
        grdService.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPatientScheduleMasterGridEdit(grdService.getGrid().getStore().indexOf(grdService.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showPatientScheduleMasterGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            grdEdtPatntSchdl.show(currentRowIndex, false, false, false, this);
        } else {
            grdEdtPatntSchdl.show(currentRowIndex, true, true, true, this);
        }
    }

    private PatientScheduleMaster_Mdl topControlSaveInitialize() {
        PatientScheduleMaster_Mdl mrTemp = new PatientScheduleMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setScheduleGroup(DmShared.xNull(txtHSchdlGroup.getText()));
        mrTemp.setScheduleGroupCode(DmShared.iNull(txtHSchdlGroup.getTag()).toString());
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.getGrdPatientSchdulMsterList().addAll(new ArrayList<GrdPatntSchdulMster_Mdl>());
        mrTemp.getGrdPatientSchdulMsterList().addAll(grdService.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        PatientScheduleMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            PatientScheduleMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHSchdlGroup).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PatientScheduleMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PatientScheduleMaster_Mdl> result) {
                topControlMoveRecSuccess(result);

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PatientScheduleMaster_Mdl> result) {
        PatientScheduleMaster_Mdl data;

        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtHSchdlGroup.setText(DmShared.xNull(data.getScheduleGroup()));
            txtHSchdlGroup.setTag(DmShared.iNull(data.getScheduleGroupCode()).toString());
            txtShortName.setText(DmShared.xNull(data.getShortName()));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdService.getGrid().getStore().clear();
            grdService.getGrid().getStore().addAll(data.getGrdPatientSchdulMsterList());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {

        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().add(new GrdPatntSchdulMster_Mdl());

    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdService.getGrid().getStore().clear();

    }

}
