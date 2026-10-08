package aarogya.master.referredMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.referredMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
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

public final class ReferredMaster extends ReferredMaster_Dsg implements AttachEvent.Handler, BlurHandler {

    ServiceInvoker<ReferredMaster_SrvAsync> srvAsync;
    final ClinicDetailGridEdit<GrdClinicDetail_Mdl, GrdClinicDetail_PrpAcs> clinicGridEdit = new ClinicDetailGridEdit(grdClinicDetail);

    final ManageSite manageSite = new ManageSite();
    final DmFileManager fileManager = new DmFileManager(ProjectRuleCode.CONSULTANT.getRuleCode());
    boolean isAttachFiles = false, isDeleteFiles = false;
    String strRemark;

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
                txtReferred.setFocus(true);

                grdClinicDetail.getGrid().getStore().add(new GrdClinicDetail_Mdl());

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ReferredMaster.this);
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
        txtHPrefixReferred.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInitial(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrefixReferred).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrefixReferred.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrefixReferred).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtReferred.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggetionConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtReferred).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtReferred.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtReferred).show();
                        }
                    }
                });
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

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            clinicGridEdit.hide();
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

        } else if (event.getSource().equals(txtReferred)) {
            if (!DmShared.xNull(txtReferred.getText()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                    txtDisplayName.setText(txtReferred.getText());
                }
            }
        }
    }

    private void handlerEvents(DmTextBox[] txtBox) {
        flwForm.addAttachHandler(this);
        for (int i = 0; i < txtBox.length; i++) {
            txtBox[i].addBlurHandler(this);
        }

        txtMobile.addBlurHandler(this);
        txtReferred.addBlurHandler(this);
    }

    public ReferredMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(ReferredMaster_Srv.class), ReferredMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents(new DmTextBox[]{txtDOA, txtDOB});
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(ReferredMaster.this);
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

    private void gridClinicEdit(Integer currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            clinicGridEdit.show(currentRowIndex, false, false, false);
        } else {
            clinicGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private ReferredMaster_Mdl topControlSaveInitialize() {
        ReferredMaster_Mdl mrTemp = new ReferredMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtReferred.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setDoa(DmShared.dNull(txtDOA.getText()));
        mrTemp.setDob(DmShared.dNull(txtDOB.getText()));
        mrTemp.setDeptTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDeptText(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.seteMail(DmShared.xNull(txtEmail.getText()));
        mrTemp.getGrdClinic().addAll(grdClinicDetail.getGrid().getStore().getAll());
        mrTemp.setMciReg(DmShared.xNull(txtMCIReg.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setRemark(DmShared.xNull(strRemark));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setSpeciTag(DmShared.xNull(txtHSpecialization.getTag()));
        mrTemp.setSpeciText(DmShared.xNull(txtHSpecialization.getText()));
        mrTemp.setInitial(DmShared.xNull(txtHPrefixReferred.getText()));
        mrTemp.setTdsCategory(DmShared.xNull(txtHTDSCategory.getTag()));

        return mrTemp;
    }

    private void topControlSave() {
        ReferredMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ReferredMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtReferred).show();
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
                new DmAlertMessageBox(errorMessage, txtReferred).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
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

            default:
                new DmAlertMessageBox(errorMessage, txtReferred).show();
                break;
        }

    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtReferred).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtReferred).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ReferredMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ReferredMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ReferredMaster_Mdl> result) {
        ReferredMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            manageDisplay(false);

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtDOA.setText(data.getDoa());
            txtDOB.setText(data.getDob());
            txtEmail.setText(data.geteMail());
            txtHActive.setTag(data.getActiveTag());
            txtHActive.setText(data.getActiveText());
            txtHPrefixReferred.setText(data.getInitial());
            txtReferred.setText(data.getSharedKeyFieldName());
            txtDisplayName.setText(data.getDisplayName());
            txtHDepartment.setTag(data.getDeptTag());
            txtHDepartment.setText(data.getDeptText());
            txtHSpecialization.setTag(data.getSpeciTag());
            txtHSpecialization.setText(data.getSpeciText());
            txtMCIReg.setText(data.getMciReg());
            txtMobile.setText(data.getMobile());
            strRemark = data.getRemark();
            txtRemark.setText(DmShared.xNull(strRemark));
            txtShortName.setText(data.getShortName());
            txtHTDSCategory.setText(data.getTdsCategoryName());
            txtHTDSCategory.setTag(data.getTdsCategory());

            grdClinicDetail.getGrid().getStore().clear();
            grdClinicDetail.getGrid().getStore().addAll(data.getGrdClinic());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

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
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        manageDisplay(true);
        txtReferred.setFocus(true);
        txtHActive.setTag("1");
        txtHActive.setText("Yes");
        txtHPrefixReferred.setTag("Dr.");
        txtHPrefixReferred.setText("Dr.");
        tabPanel.selectTab(0);
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdClinicDetail.getGrid().getStore().clear();
        grdClinicDetail.getGrid().getStore().add(new GrdClinicDetail_Mdl());

        getTempKeyCode();
    }

    private void getTempKeyCode() {

        srvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtReferred).show();
            }

            @Override
            public void onSuccess(String result) {
                fileManager.setTempKeyCode(result);
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);
        txtRemark.setEnabled(false);
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(manageSiteMenu.getText())) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                manageSite.show("subGroupSiteList", "subCode", topControl.getDocId(), "site_code");
            } else {
                new DmAlertMessageBox("Can be used in browse mode only.", (DmTextBox) null).show();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(manageFileMenu.getText())) {

            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        }
    }
}
