package dataman.utility.userMaster.client;

import dataman.dmBase.self.shared.UserMaster_Mdl;
import dataman.dmBase.self.shared.UserMaster_Vld;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.userMaster.shared.GrdLinkedUser_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
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
import dataman.utility.userMaster.shared.*;
import dataman.utility.userPermission.client.UserPermission;
import java.util.HashMap;
import java.util.List;

public class UserMaster extends UserMaster_Dsg implements AttachEvent.Handler {

    private final ServiceInvoker<UserMaster_SrvAsync> SrvAsync;
    private final ResetLink resetLink = new ResetLink();
    private final LinkedUserGridEdit<GrdLinkedUser_Mdl, UserMaster_Dsg.GridLinkedUserData> grdLinkedUser = new LinkedUserGridEdit(GrdLinkedUser);

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtUserName.setFocus(true);
                btnUserPermission.setVisible(false);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtDescription.setFocus(true);
                txtUserID.setEnabled(false);
                txtUserName.setEnabled(false);
                btnUserPermission.setVisible(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(DmShared.xNull(txtUserName.getText()));
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
                btnUserPermission.setVisible(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(UserMaster.this);
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
        btnUserPermission.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmUtility().moduleName, new lmUtility().USER_PERMISSION.caption, new SetWidgetInvoker() {

                    @Override
                    public void setWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
//                        ((UserPermission) wgtObject).fillUserRecord("", "");
                        ((UserPermission) wgtObject).fillUserRecord(DmShared.xNull(txtUserName.getText()), DmShared.xNull(txtUserID.getText()));
                    }

                    @Override
                    public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, swi);
                    }

                    @Override
                    public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, paramHashMap, userMenu, swi);
                    }

                    @Override
                    public void closeWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(wgtObject);
                    }
                });
            }
        });
        txtUserName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtUserName).show();
                        }
                    }
                });
            }
        });

        txtHRole.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRole(helper, blnToEndOfFile, false, DmShared.xNull(txtUserName.getText()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRole.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        flwBottomArea.clear();
                        bindLinkedAccount(txtHRole.getText());
                    } else {
                        flwBottomArea.clear();
                        flwBottomArea.add(flwLinkedUser);
                    }
                }
            }
        });
        txtHAdministratorYN.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAdministrator(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAdministratorYN.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {

                }
            }
        });
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {

                }
            }
        });
        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAdministrator(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {

                }
            }
        });
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvent() {
        grdLinkedUser.addCallBack(new LinkedUserGridEdit.CallBack() {
            @Override
            public void onOk(GrdLinkedUser_Mdl grdLinkedUser) {
                GrdLinkedUser.commitChanges();
            }

            @Override
            public void onDelete() {
                GrdLinkedUser.commitChanges();
            }
        });
        GrdLinkedUser.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showLinkedGridEdit(event.getRowIndex());
            }
        });
        GrdLinkedUser.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showLinkedGridEdit(GrdLinkedUser.getGrid().getStore().indexOf(GrdLinkedUser.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showLinkedGridEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdLinkedUser.show(currentRowIndex, false, false, false);
        } else {
            grdLinkedUser.show(currentRowIndex, true, true, true);
        }
    }

    public UserMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<UserMaster_SrvAsync>(GWT.create(UserMaster_Srv.class), UserMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvent();
        flwForm.addAttachHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void bindLinkedAccount(final String luName) {
        SrvAsync.invokeRPC().fillGridLinkedUser(luName, new AsyncCallback<DmResult<List<GrdLinkedUser_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdLinkedUser_Mdl>> result) {
                if (result.isStatus()) {
                    if (result != null) {
                        GrdLinkedUser.getGrid().getStore().clear();
                        for (int i = 0; i < result.getData().size(); i++) {
                            GrdLinkedUser.getGrid().getStore().add(i, result.getData().get(i));
                        }
                        GrdLinkedUser.getGrid().getStore().add(new GrdLinkedUser_Mdl());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private UserMaster_Mdl topControlSaveInitialize() {
        UserMaster_Mdl mrTemp = new UserMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtUserName.getText()));
        mrTemp.setUserId(DmShared.xNull(txtUserID.getText()));
        mrTemp.setRoleCode(DmShared.xNull(txtHRole.getTag()));
        mrTemp.setRoleName(DmShared.xNull(txtHRole.getText()));
        mrTemp.setDescription(DmShared.xNull(txtDescription.getText()));
        mrTemp.setLockTimeOut(DmShared.iNull(txtLockTimeOut.getText()).toString());
        mrTemp.setAdministratorYNCode(DmShared.xNull(txtHAdministratorYN.getTag()));
        mrTemp.setAdministratorYNName(DmShared.xNull(txtHAdministratorYN.getText()));
        mrTemp.setActiveYNCode(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setActiveYNName(DmShared.xNull(txtHActive.getText()));
        mrTemp.setNatureCode(DmShared.xNull(txtHNature.getTag()));

        mrTemp.setGridLinkedUser(DmShared.xNull(getLinkedUserList("|")));
//        mrTemp.setLinkedAccountCode(DmShared.xNull(txtHLinkedAccount.getTag()));
//        mrTemp.setLinkedAccountName(DmShared.xNull(txtHLinkedAccount.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        UserMaster_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            UserMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
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
                new DmAlertMessageBox(errorMessage, txtUserName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHAdministratorYN).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtUserName).show();
                break;
        }

    }

    private String getLinkedUserList(String seprator) {
        String val = "";
        List<GrdLinkedUser_Mdl> grd = GrdLinkedUser.getGrid().getStore().getAll();
        for (int i = 0; i < grd.size(); i++) {
            if (!DmShared.xNull(grd.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                if (val.equalsIgnoreCase("")) {
                    val = val + seprator + DmShared.xNull(grd.get(i).getSharedKeyFieldCode()) + seprator;
                } else {
                    val = val + "," + seprator + DmShared.xNull(grd.get(i).getSharedKeyFieldCode()) + seprator;
                }
            }
        }
        return val;
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtUserName).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<UserMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
            }

            @Override
            public void onSuccess(DmResult<UserMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<UserMaster_Mdl> result) {
        UserMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                btnUserPermission.setVisible(true);
            }
            topControl.setDocId(data.getSharedKeyFieldCode());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtUserName.setText(data.getSharedKeyFieldName());
            txtUserID.setText(data.getUserId());
            txtHRole.setTag(data.getRoleCode());
            txtHRole.setText(data.getRoleName());
            txtDescription.setText(data.getDescription());
            txtLockTimeOut.setText(data.getLockTimeOut());
            txtHAdministratorYN.setTag(data.getAdministratorYNCode());
            txtHAdministratorYN.setText(data.getAdministratorYNName());

            txtHActive.setTag(data.getActiveYNCode());
            txtHActive.setText(data.getActiveYNName());
            txtHNature.setTag(data.getNatureCode());
            txtHNature.setText(data.getNatureName());

            txtEmail.setText(data.getEmail());
            txtMobile.setText(data.getMobile());
            
            txtHDepartment.setTag(data.getDepartmentCode());
            txtHDepartment.setText(data.getDepartmentName());

            if (DmShared.xNull(data.getRoleName()).equalsIgnoreCase("")) {
                flwBottomArea.clear();
                flwBottomArea.add(flwLinkedUser);
            } else {
                flwBottomArea.clear();
            }
            bindLinkedAccount(txtUserName.getText());
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtUserName).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            grdLinkedUser.hide();
        }
    }

    private void clear() {
        flwBottomArea.clear();
        flwBottomArea.add(flwLinkedUser);
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtUserID.setEnabled(false);
        bindLinkedAccount("");
        txtHAdministratorYN.setText("No");
        txtHAdministratorYN.setTag("0");
        txtHActive.setText("Yes");
        txtHActive.setTag("1");
    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "OPEN_USER":
                    openWithAddUserPermission((String) object, callBack);
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHActive).show();
        }
    }

    private void openWithAddUserPermission(String userCode, final DmCallBack callBack) {
        if (DmShared.iNull(userCode).intValue() > 0) {
            topControlMoveRec(userCode);
        }
        if (callBack != null) {
            DmResult<Object> dmrTemp = new DmResult<>();
            dmrTemp.setStatus(true);
            callBack.result(dmrTemp);
        }
    }

    private void resetPassword(String userName) {
        try {
            SrvAsync.invokeRPC().resetPassword(DmShared.xNull(userName), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Password reset link sent successfully", txtUserName).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuResetPassword.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                resetLink.show(mnuResetPassword, txtUserName.getText());
                resetLink.addCallBack(new ResetLink.CallBack() {
                    @Override
                    public void resetLink(final String userName, final String eMail) {
                        new DmConfirmMessageBox("Are you sure? You want to send the password reset link to " + DmShared.xNull(eMail) + " e-mail id.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    resetPassword(userName);
                                }
                            }
                        }).show();
                    }

                    @Override
                    public void close() {
                        txtUserName.setFocus(true);
                    }
                });
            }
        }
    }
}
