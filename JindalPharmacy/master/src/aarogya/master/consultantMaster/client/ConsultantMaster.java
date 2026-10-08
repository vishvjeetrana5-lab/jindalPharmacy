package aarogya.master.consultantMaster.client;

import aarogya.master.consultantMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.selfToolkit.splApplicableFrom.client.SPLApplicableFrom;
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
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.manageSite.client.ManageSite;
import java.util.List;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmWeekdays;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.ArrayList;
import java.util.HashMap;

public final class ConsultantMaster extends ConsultantMaster_Dsg implements AttachEvent.Handler, KeyPressHandler, BlurHandler, ClickHandler {

    private final ServiceInvoker<ConsultantMaster_SrvAsync> srvAsync;
    private final ClinicDetailGridEdit<GrdClinicDetail_Mdl, GrdClinicDetail_PrpAcs> clinicGridEdit = new ClinicDetailGridEdit(grdClinicDetail);
    private final ServiceGridEdit<GrdService_Mdl, GrdServiceDetail_PrpAcs> serviceGridEdit = new ServiceGridEdit(grdServiceDetail);
    private final TimingGridEdit<GrdTiming_Mdl, GrdTiming_PrpAcs> timingGridEdit = new TimingGridEdit(grdTimingDetail);
    private DefaultValue_Mdl defaultValue;
    protected List<GrdRoomCategory_Mdl> roomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    protected List<GrdQueueSequence_Mdl> lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
    private final SPLApplicableFrom splApplicableFrom = new SPLApplicableFrom();
    private final ManageSite manageSite = new ManageSite();
    private final QueuePopup queuePopup = new QueuePopup();
    private final DmFileManager fileManager = new DmFileManager(ProjectRuleCode.CONSULTANT.getRuleCode());
    private boolean isAttachFiles = false, isDeleteFiles = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                txtHActive.setEnabled(true);
                txtConsultant.setFocus(true);
                txtApplicableFrom.setEnabled(false);
                grdClinicDetail.getGrid().getStore().add(new GrdClinicDetail_Mdl());
                grdServiceDetail.getGrid().getStore().add(new GrdService_Mdl());
                grdTimingDetail.getGrid().getStore().add(new GrdTiming_Mdl());
                grdBankDetail.getGrid().getStore().add(new GrdBankDetail_Mdl());
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

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ConsultantMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

                    @Override
                    public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                        srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                            }

                            @Override
                            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                                if (result.isStatus()) {
                                    txtHActive.setData(result.getData(), result.getPaging());
                                } else {
                                    new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                    }
                });
            }
        });
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSpecialization.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSpecialization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSpecialization).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSpecialization.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSpecialization).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtConsultant.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggetionConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtConsultant).show();
                        }
                    }
                });
            }
        });
        splApplicableFrom.addCallBack(new SPLApplicableFrom.CallBack() {

            @Override
            public void onOk(String newDtApplicable, String oldDtApplicable, String siteCode) {
                try {
//                    txtApplicableFrom.setTag2nd(DmShared.xNull(siteCode));
                    txtApplicableFrom.setTag(DmShared.dNull(oldDtApplicable));
                    txtApplicableFrom.setText(DmShared.dNull(newDtApplicable));
                    if (!DmShared.dNull(oldDtApplicable).equalsIgnoreCase("")) {
                        moveRecApplicable(siteCode);
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }

            @Override
            public void onDelete(String applicableFrom) {
                deleteApplicable(DmShared.dNull(applicableFrom));
            }
        });
        queuePopup.addCallBack(new QueuePopup.CallBack() {
            @Override
            public void onOk(List<GrdQueueSequence_Mdl> lstSequence) {
                if (lstSequence.size() > 0) {
                    lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
                    for (GrdQueueSequence_Mdl queue : lstSequence) {
                        lstQueue.add(queue.clone());
                    }
                }
            }

            @Override
            public void onClose() {
                txtRemark.setFocus(true);
            }
        });

        txtShortName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggetionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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
        txtHLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHInitial.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInitial(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInitial).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInitial.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInitial).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHConsultantNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultantNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultantNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultantNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultantNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHQueueAlgo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHQueueAlgoNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQueueAlgo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHQueueAlgo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQueueAlgo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    queuePopup.show(btnQueue, topControl.getMode(), lstQueue);
                }
            }
        });
        txtHTDSCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHTDSCategory.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHTDSCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHLinkDept.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLinkDept(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkDept).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkDept.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkDept).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    private void gridEvents() {
        clinicGridEdit.addCallBack(new ClinicDetailGridEdit.CallBack() {

            @Override
            public void onOk(GrdClinicDetail_Mdl grdClinic) {
                grdClinicDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdClinicDetail.commitChanges();
            }
        });
        grdClinicDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridClinicEdit(event.getRowIndex());
            }
        });
        grdClinicDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridClinicEdit(grdClinicDetail.getGrid().getStore().indexOf(grdClinicDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        timingGridEdit.addCallBack(new TimingGridEdit.CallBack() {

            @Override
            public void onOk(GrdTiming_Mdl grdTime) {
                grdTimingDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdTimingDetail.commitChanges();
            }
        });
        grdTimingDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridTimingEdit(event.getRowIndex());
            }
        });
        grdTimingDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridTimingEdit(grdTimingDetail.getGrid().getStore().indexOf(grdTimingDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        serviceGridEdit.addCallBack(new ServiceGridEdit.CallBack() {

            @Override
            public void onOk(GrdService_Mdl operation) {
                grdServiceDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdServiceDetail.commitChanges();
            }
        });
        grdServiceDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridServiceEdit(event.getRowIndex());
            }
        });
        grdServiceDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridServiceEdit(grdServiceDetail.getGrid().getStore().indexOf(grdServiceDetail.getGrid().getSelectionModel().getSelectedItem()));
                }

            }
        }, KeyDownEvent.getType());

        grdBankDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                String subCode = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldCode();
                String sNo = grdBankDetail.getGrid().getStore().get(r1).getSharedKeyFieldName();
                showBankDetail(DmShared.xNull(subCode), DmShared.xNull(sNo));

            }
        });

        grdSiteDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 2) {
                        boolean var = grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).isAllowForSite();
                        if (var) {
                            grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).setAllowForSite(false);
                        } else {
                            grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).setAllowForSite(true);
                        }
                        grdSiteDetail.commitChanges();
                    }
                    if (event.getCellIndex() == 3) {
                        boolean var = grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).isOPDConsultant();
                        if (var) {
                            grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).setIsOPDConsultant(false);
                        } else {
                            grdSiteDetail.getGrid().getStore().get(event.getRowIndex()).setIsOPDConsultant(true);
                        }
                        grdSiteDetail.commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            serviceGridEdit.hide();
            serviceGridEdit.gridEdit.hide();
            clinicGridEdit.hide();
            timingGridEdit.hide();
            splApplicableFrom.hide();
            manageSite.hide();

        }
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

        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() == 1) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            grdClinicDetail.getGrid().getView().refresh(true);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtDeviceCode)) {
            if ((charCode > 31) && (charCode < 48)) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 57 && charCode < 65) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 90 && charCode < 97) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 121) {
                txtDeviceCode.cancelKey();
            }
        } else if (event.getSource().equals(txtDeviceBlock)) {
            if ((charCode > 31) && (charCode < 48)) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 57 && charCode < 65) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 90 && charCode < 94) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 95 && charCode < 97) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 121) {
                txtDeviceBlock.cancelKey();
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtMobile)) {
            if (DmShared.xNull(txtMobile.getText()).equalsIgnoreCase("")) {
                txtMobile.setText(txtMobile.getText());
            }

        } else if (event.getSource().equals(txtDOA)) {
            if (!DmShared.dNull(DmClient.returnDate(txtDOA.getText())).equalsIgnoreCase("")) {
                if (DmShared.convertDate(DmClient.returnDate(txtDOA.getText())).getTime() <= DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                    txtDOA.setText(DmClient.returnDate(txtDOA.getText()));
                } else {
                    new DmAlertMessageBox("Anniv. Date can't be greater than current Date", txtDOA).show();
                }
            }

        } else if (event.getSource().equals(txtDOB)) {
            if (!DmShared.dNull(DmClient.returnDate(txtDOB.getText())).equalsIgnoreCase("")) {
                if (DmShared.convertDate(DmClient.returnDate(txtDOB.getText())).getTime() <= DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                    txtDOB.setText(DmClient.returnDate(txtDOB.getText()));
                } else {
                    new DmAlertMessageBox("Date of Birth can't be greater than current Date", txtDOB).show();
                }
            }

        } else if (event.getSource().equals(txtApplicableFrom)) {
            txtApplicableFrom.setText(DmClient.returnDate(txtApplicableFrom.getText()));
        } else if (event.getSource().equals(txtConsultant)) {
            if (!DmShared.xNull(txtConsultant.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                    txtDisplayName.setText(txtConsultant.getText());
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnApplicable)) {
            splApplicableFrom.show(btnApplicable, true, true, PriceListEntryFrom.CONSULTANT_MASTER, DmShared.iNull(topControl.getDocId()).toString(), DmShared.iNull(defaultValue.getOrganization()).toString(), DmShared.iNull(defaultValue.getSiteCode()).toString());
        } else if (event.getSource().equals(btnQueue)) {
            queuePopup.show(btnQueue, topControl.getMode(), lstQueue);
        }
    }

    private void handlerEvents(DmTextBox[] txtBox) {
        flwForm.addAttachHandler(this);
        for (int i = 0; i < txtBox.length; i++) {
            txtBox[i].addBlurHandler(this);
        }
        txtMobile.addKeyPressHandler(this);
        txtMobile.addBlurHandler(this);
        btnApplicable.addClickHandler(this);
        btnQueue.addClickHandler(this);
        txtConsultant.addBlurHandler(this);
        txtDeviceBlock.addKeyPressHandler(this);
        txtDeviceCode.addKeyPressHandler(this);
    }

    public ConsultantMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(ConsultantMaster_Srv.class), ConsultantMaster.class.getName());
        this.asWidget().setTitle(widgetName);
//        handlerEvents(new DmTextBox[]{txtDOA, txtDOB, txtApplicableFrom});
        handlerEvents(new DmTextBox[]{txtDOB, txtApplicableFrom});
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        iniDefaultValue(swi);
        manageDisplay(false);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void gridClinicEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            clinicGridEdit.show(currentRowIndex, false, false, false);
        } else {
            clinicGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void gridTimingEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            timingGridEdit.show(currentRowIndex, false, false, false);
        } else {
            timingGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void gridServiceEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            serviceGridEdit.show(this, currentRowIndex, false, false, false);
        } else {
            serviceGridEdit.show(this, currentRowIndex, true, true, true);
        }
    }

    private ConsultantMaster_Mdl topControlSaveInitialize() {
        ConsultantMaster_Mdl mrTemp = new ConsultantMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtConsultant.getText()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));

        mrTemp.setDoa(DmShared.dNull(txtDOA.getText()));
        mrTemp.setNoOfVisit(DmShared.xNull(txtNoOfVisit.getText()));
        mrTemp.setDob(DmShared.dNull(txtDOB.getText()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentText(DmShared.xNull(txtHDepartment.getText()));

        mrTemp.seteMail(DmShared.xNull(txtEmail.getText()));
        mrTemp.getGrdClinic().addAll(grdClinicDetail.getGrid().getStore().getAll());
        mrTemp.getGrdService().addAll(grdServiceDetail.getGrid().getStore().getAll());
        mrTemp.getGrdTime().addAll(grdTimingDetail.getGrid().getStore().getAll());
        mrTemp.getLstQueue().addAll(lstQueue);
        mrTemp.setMciReg(DmShared.xNull(txtMCIReg.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));

        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setSpecilizationTag(DmShared.xNull(txtHSpecialization.getTag()));
        mrTemp.setSpecilizationText(DmShared.xNull(txtHSpecialization.getText()));
        mrTemp.setValidUpTo(DmClient.vNull(txtValidUpTo.getText(), "0"));
        mrTemp.setLocationTag(DmShared.xNull(txtHLocation.getTag()));
        mrTemp.setLocationText(DmShared.xNull(txtHLocation.getText()));
        mrTemp.setQueueTag(DmShared.xNull(txtHQueueAlgo.getTag()));
        mrTemp.setDeviceBlock(DmShared.xNull(txtDeviceBlock.getText()));
        mrTemp.setDeviceCode(DmShared.xNull(txtDeviceCode.getText()));
        mrTemp.setPan(DmShared.xNull(txtPAN.getText()));

        if (!DmShared.xNull(txtApplicableFrom.getText()).trim().equalsIgnoreCase("")) {
            mrTemp.setApplicableFrom(DmShared.dNull(txtApplicableFrom.getText()));
        } else {
            mrTemp.setApplicableFrom(DmShared.dNull(DmClient.getStartDate()));
        }

        mrTemp.setOrganizaton(DmShared.xNull(defaultValue.getOrganization()));
        mrTemp.setService(DmShared.xNull(defaultValue.getService()));
        mrTemp.setInitial(DmShared.xNull(txtHInitial.getTag()));
        mrTemp.setRemark(txtRemark.getText());
        mrTemp.setConsultantNatureTag(DmShared.xNull(txtHConsultantNature.getTag()));
        mrTemp.setTdsCategory(DmShared.xNull(txtHTDSCategory.getTag()));
        mrTemp.setRegionalDescription(DmShared.xNull(txtRegionalDescription.getText()));
        mrTemp.setLinkDepartmentCode(DmShared.xNull(txtHLinkDept.getTag()));
        mrTemp.setLinkDepartmentName(DmShared.xNull(txtHLinkDept.getText()));
        mrTemp.setRateListCode(DmShared.xNull(defaultValue.getRateListCode()));
        mrTemp.getGrdSitedetail().addAll(grdSiteDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        ConsultantMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ConsultantMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtConsultant).show();
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
            case 200:
                new DmAlertMessageBox(errorMessage, txtConsultant).show();
                break;
            case 250:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHSpecialization).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 550:
                new DmAlertMessageBox(errorMessage, txtHConsultantNature).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtDOA).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtApplicableFrom).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtPAN).show();

            default:
                new DmAlertMessageBox(errorMessage, txtConsultant).show();
                break;
        }

    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtConsultant).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtConsultant).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ConsultantMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ConsultantMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ConsultantMaster_Mdl> result) {
        ConsultantMaster_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            defaultValue.setOrganization(data.getOrganizaton());
            defaultValue.setService(data.getService());
            manageDisplay(false);
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtDOA.setText(data.getDoa());
            txtDOB.setText(data.getDob());
            txtEmail.setText(data.geteMail());
            txtHActive.setTag(data.getActiveTag());
            txtHActive.setText(data.getActiveText());

            txtHInitial.setTag(data.getInitial());
            txtHInitial.setText(data.getInitial());
            txtConsultant.setText(data.getSharedKeyFieldName());
            txtNoOfVisit.setText(data.getNoOfVisit());
            txtHDepartment.setTag(data.getDepartmentTag());
            txtHDepartment.setText(data.getDepartmentText());
            txtDisplayName.setText(data.getDisplayName());
            txtHSpecialization.setTag(data.getSpecilizationTag());
            txtHSpecialization.setText(data.getSpecilizationText());
            txtHLocation.setTag(data.getLocationTag());
            txtHLocation.setText(data.getLocationText());
            txtHQueueAlgo.setTag(data.getQueueTag());
            txtHQueueAlgo.setText(data.getQueueText());
            txtDeviceBlock.setText(data.getDeviceBlock());
            txtDeviceCode.setText(data.getDeviceCode());
            txtPAN.setText(data.getPan());

            txtMCIReg.setText(data.getMciReg());

            txtMobile.setText(data.getMobile());
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            txtShortName.setText(data.getShortName());
            txtValidUpTo.setText(DmClient.vNull(data.getValidUpTo(), "0"));
            txtApplicableFrom.setText(DmShared.dNull(data.getApplicableFrom()));

            txtRegionalDescription.setText(DmShared.xNull(data.getRegionalDescription()));

            txtHConsultantNature.setTag(data.getConsultantNatureTag());
            txtHConsultantNature.setText(data.getConsultantNatureText());

            txtHTDSCategory.setText(data.getTdsCategoryName());
            txtHTDSCategory.setTag(data.getTdsCategory());

            txtHLinkDept.setTag(data.getLinkDepartmentCode());
            txtHLinkDept.setText(data.getLinkDepartmentName());

            grdClinicDetail.getGrid().getStore().clear();
            grdClinicDetail.getGrid().getStore().addAll(data.getGrdClinic());

            grdTimingDetail.getGrid().getStore().clear();
            grdTimingDetail.getGrid().getStore().addAll(data.getGrdTime());

            grdServiceDetail.getGrid().getStore().clear();
            grdServiceDetail.getGrid().getStore().addAll(data.getGrdService());

            grdBankDetail.getGrid().getStore().clear();
            grdBankDetail.getGrid().getStore().addAll(data.getGrdBankDetail());

            grdSiteDetail.getGrid().getStore().clear();
            grdSiteDetail.getGrid().getStore().addAll(data.getGrdSitedetail());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
            lstQueue = data.getLstQueue();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void moveRecApplicable(String siteCode) {
        srvAsync.invokeRPC().getApplicableRate(DmShared.xNull(topControl.getDocId()), DmShared.dNull(txtApplicableFrom.getTag()), DmShared.xNull(siteCode), new AsyncCallback<DmResult<ConsultantMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();

            }

            @Override
            public void onSuccess(DmResult<ConsultantMaster_Mdl> result) {
                applicableSuccess(result);
            }
        });
    }

    private void applicableSuccess(DmResult<ConsultantMaster_Mdl> result) {
        ConsultantMaster_Mdl data;
        if (result.isStatus()) {
            data = result.getData();

            grdTimingDetail.getGrid().getStore().clear();
            grdTimingDetail.getGrid().getStore().addAll(data.getGrdTime());

            grdServiceDetail.getGrid().getStore().clear();
            grdServiceDetail.getGrid().getStore().addAll(data.getGrdService());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void deleteApplicable(String applicableDate) {
        srvAsync.invokeRPC().deleteApplicableRecord(DmShared.xNull(topControl.getDocId()), DmShared.dNull(applicableDate), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnApplicable).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlMoveRec(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnApplicable).show();
                }

            }
        });
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
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        manageDisplay(true);
        txtApplicableFrom.setEnabled(false);
        txtConsultant.setFocus(true);

        txtHActive.setTag("1");
        txtHActive.setText("Yes");

        txtHInitial.setTag("Dr.");
        txtHInitial.setText("Dr.");
        txtHConsultantNature.setTag("1");
        txtHConsultantNature.setText("Consultant");
        lstQueue = new ArrayList<GrdQueueSequence_Mdl>();

        tabPanel.selectTab(0);
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");

        grdClinicDetail.getGrid().getStore().clear();
        grdClinicDetail.getGrid().getStore().add(new GrdClinicDetail_Mdl());

        grdServiceDetail.getGrid().getStore().clear();
        grdServiceDetail.getGrid().getStore().add(new GrdService_Mdl());

        grdBankDetail.getGrid().getStore().clear();

        grdTimingDetail.getGrid().getStore().clear();
        GrdTiming_Mdl newGrdTiming_Mdl = new GrdTiming_Mdl();
        newGrdTiming_Mdl.setSharedKeyFieldCode(DmShared.iNull(DmWeekdays.ALLDAY.getWeekDay()).toString());
        newGrdTiming_Mdl.setSharedKeyFieldName(DmWeekdays.ALLDAY.getWeekDayName());
        newGrdTiming_Mdl.setFromTime("00.00");
        newGrdTiming_Mdl.setToTime("23.59");

        grdTimingDetail.getGrid().getStore().add(newGrdTiming_Mdl);

        grdSiteDetail.getGrid().getStore().clear();
        manageGrid();
        getTempKeyCode();
    }

    private void manageGrid() {
        srvAsync.invokeRPC().getSites(new AsyncCallback<DmResult<List<GrdSiteDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdSiteDetail_Mdl>> result) {
                if (result.isStatus()) {
                    grdSiteDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtDOA).show();
                }
            }
        });
    }

    private void getTempKeyCode() {
        srvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtConsultant).show();
            }

            @Override
            public void onSuccess(String result) {
                fileManager.setTempKeyCode(result);
            }
        });
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
                    roomCategory.clear();
                    roomCategory.addAll(defaultValue.getCategory());
                    swi.setWidget(ConsultantMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);

    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(manageSiteMenu.getText())) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                manageSite.show("subGroupSiteList", "subCode", topControl.getDocId(), "site_code");
            } else {
                new DmAlertMessageBox("Can be used in browse mode only and select one record", (DmTextBox) null).show();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(manageFileMenu.getText())) {

            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        }
    }

    private void showBankDetail(String subCode, String sNo) {
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("grdAccountCode", subCode);
        hm.put("grdsNo", sNo);
        hm.put("accountCode", topControl.getDocId());
        hm.put("accountName", txtConsultant.getText());
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().BANK_ACCOUNT_DETAIL.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("BANK_DETAIL", hm, new DmCallBack() {
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
