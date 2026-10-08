package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.TxtHLinkedAccount_Mdl;
import dataman.utility.userPermission.shared.TxtHUsers_Mdl;
import dataman.utility.userPermission.shared.UserPermission_Mdl;
import dataman.utility.userPermission.shared.GrdCostCenter_Mdl;
import dataman.utility.userPermission.shared.GrdVoucher_Mdl;
import dataman.utility.userPermission.shared.GrdMenu_Mdl;
import dataman.utility.userPermission.shared.GrdSite_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.userPermission.shared.GrdMenu_Mdl.EventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;
import dataman.utility.userPermission.shared.UserPermission_SrvAsync;

public final class UserPermission extends UserPermission_Dsg implements ClickHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<UserPermission_SrvAsync> SrvAsync;
    private final VoucherGridEdit<GrdVoucher_Mdl, GetVouchersData_PrpAcs> grdVouchers = new VoucherGridEdit(grdVouchersDetail);
    private final SiteGridEdit<GrdSite_Mdl, GetSiteData> grdSiteEdit = new SiteGridEdit(super.grdSite);
    private final CostCenterGridEdit<GrdCostCenter_Mdl, GetCostCenterData> grdCostCenter = new CostCenterGridEdit(GrdCostCenter);
    private final LinkedAccountEdit linkedAccountEdit = new LinkedAccountEdit();
    private final CopyPermissionEdit copyPermissionEdit = new CopyPermissionEdit();
    private final RestrictionPopup restrictionPopup = new RestrictionPopup();

    private void textHelpEvents() {
        restrictionPopup.addCallBack(new RestrictionPopup.CallBack() {
            @Override
            public void onSave(GrdMenu_Mdl menuMdl) {
                if (menuMdl != null) {
                    saveMenuPermission(EventType.VIEW_DAYS, menuMdl);
                }
            }
        });

        copyPermissionEdit.addCallBack(new CopyPermissionEdit.CallBack() {

            @Override
            public void onOk(TxtHUsers_Mdl users_Mdl) {
                if (users_Mdl != null) {
                    txtCopyPermission.setTag(DmShared.xNull(users_Mdl.getSharedKeyFieldCode()));
                    txtCopyPermission.setText(DmShared.xNull(users_Mdl.getSharedKeyFieldName()));
                    fillUserRecord(DmShared.xNull(txtHUser.getText()), DmShared.xNull(txtHUser.getTag()));
                }
            }
        });
        linkedAccountEdit.addCallBack(new LinkedAccountEdit.CallBack() {
            @Override
            public void onOk(TxtHLinkedAccount_Mdl txtHLinkedAccount) {
                if (txtHLinkedAccount != null) {
                    txtLinkedAccount.setText(txtHLinkedAccount.getSharedKeyFieldName());
                    txtLinkedAccount.setTag(txtHLinkedAccount.getSharedKeyFieldCode());
                }
            }
        });
        txtHUser.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHUsers_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHUsers_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHUsers_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHUsers_Mdl tRow) {
                clear();
                if (blnStatus && tRow != null) {
                    fillUserRecord(tRow.getSharedKeyFieldName(), tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvent() {
        grdSiteEdit.addCallBack(new SiteGridEdit.CallBack() {
            @Override
            public void onOk(GrdSite_Mdl grdsite) {
                grdSite.commitChanges();
                saveSitePermission(getSiteList("|"), true);
            }

            @Override
            public void onDelete() {
                grdSite.commitChanges();
                saveSitePermission(getSiteList("|"), true);
            }
        });
        grdSite.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showSiteGridEdit(event.getRowIndex());
            }
        });
        grdSite.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showSiteGridEdit(grdSite.getGrid().getStore().indexOf(grdSite.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdCostCenter.addCallBack(new CostCenterGridEdit.CallBack() {
            @Override
            public void onOk(GrdCostCenter_Mdl grdCostCenter) {
                GrdCostCenter.commitChanges();
                saveSitePermission(getCostCenterList("|"), false);
            }

            @Override
            public void onDelete() {
                GrdCostCenter.commitChanges();
                saveSitePermission(getCostCenterList("|"), false);
            }
        });
        GrdCostCenter.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showCostCenterGridEdit(event.getRowIndex());
            }
        });
        GrdCostCenter.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCostCenterGridEdit(GrdCostCenter.getGrid().getStore().indexOf(GrdCostCenter.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdVouchers.addCallBack(new VoucherGridEdit.CallBack() {
            @Override
            public void onOk(GrdVoucher_Mdl grdVouchers) {
                grdVouchersDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdVouchersDetail.commitChanges();
            }
        });
        grdVouchersDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showVoucherGridEdit(event.getRowIndex());

            }
        });
        grdVouchersDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showVoucherGridEdit(grdVouchersDetail.getGrid().getStore().indexOf(grdVouchersDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdMenu.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    manageMenuPermission(grdMenu.getGrid().getStore().get(event.getRowIndex()), event.getCellIndex());
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), txtHUser).show();
                }
            }
        });
        grdMenu.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int rowIndex = grdMenu.getGrid().getStore().indexOf(grdMenu.getGrid().getSelectionModel().getSelectedItem());
                int columnIndex = ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).getSelectCell().getCell();
                manageMenuPermission(grdMenu.getGrid().getStore().get(rowIndex), columnIndex);
            }
        }, KeyDownEvent.getType());

        grdNavigation.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                manageNavigation(grdNavigation.getGrid().getStore().get(event.getRowIndex()), event.getRowIndex());
            }
        });
        grdNavigation.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    int rowIndex = grdNavigation.getGrid().getStore().indexOf(grdNavigation.getGrid().getSelectionModel().getSelectedItem());
                    manageNavigation(grdNavigation.getGrid().getStore().get(rowIndex), rowIndex);
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showSiteGridEdit(int currentRowIndex) {
        grdSiteEdit.show(currentRowIndex, true, true, true);
    }

    private void showCostCenterGridEdit(int currentRowIndex) {
        grdCostCenter.show(currentRowIndex, true, true, true);
    }

    private void showVoucherGridEdit(int currentRowIndex) {
        grdVouchers.show(currentRowIndex, true, true, true, grdVouchersDetail.getGrid().getStore().get(currentRowIndex).getSharedKeyFieldCode(), DmShared.xNull(txtHUser.getText()), DmShared.xNull(txtRole.getTag()));
    }

    public UserPermission(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<UserPermission_SrvAsync>(GWT.create(UserPermission_Srv.class), UserPermission.class.getName());
        this.asWidget().setTitle(widgetName);
        textHelpEvents();
        gridEvent();
        manageEvents();
        manageDisplay();
        swi.setWidget(this);
    }

    private void manageDisplay() {
        txtHUser.setFocus(true);
        txtAdministratorYN.setEnabled(false);
        txtDescription.setEnabled(false);
        txtEmail.setEnabled(false);
        txtLinkedAccount.setEnabled(false);
        txtCopyPermission.setEnabled(false);
        txtRole.setEnabled(false);
        txtUserID.setEnabled(false);
    }

    private void manageEvents() {
        flwForm.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnCopyPermission.addClickHandler(this);
        btnLinkedAccount.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            grdVouchers.hide();
            grdSiteEdit.hide();
            grdCostCenter.hide();
            linkedAccountEdit.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();

        } else if (event.getSource() == btnCopyPermission) {
            if (!DmShared.xNull(txtHUser.getText()).equalsIgnoreCase("")) {
                showCopyPermissionPopUp();
            } else {
                new DmAlertMessageBox("Select User Name.", txtHUser).show();
            }

        } else if (event.getSource() == btnLinkedAccount) {
            if (!DmShared.xNull(txtHUser.getText()).equalsIgnoreCase("")) {
                showLinkedAccountPopUp();
            } else {
                new DmAlertMessageBox("Select User Name.", txtHUser).show();
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_USER_PERMISSION":
                    openWithAddUserPermission((HashMap<String, String>) object, callback);
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void openWithAddUserPermission(HashMap<String, String> param, DmCallBack callback) {
        if (param != null) {
            fillUserRecord(DmShared.xNull(param.get("userName")), DmShared.xNull(param.get("userId")));
            if (callback != null) {
                DmResult<Object> dmrTemp = new DmResult<>();
                dmrTemp.setStatus(true);
                callback.result(dmrTemp);
            }
        }
    }

    private void bindMenu(final GrdMenu_Mdl node, final boolean isRoot, final boolean isMenuGridClick) {

        grdMenu.lockScroll();

        SrvAsync.invokeRPC().getMenuRecord(DmShared.xNull(txtHUser.getText()).trim(), DmShared.xNull(node.getMnuModule()), DmShared.xNull(node.getSharedKeyFieldCode()), DmShared.xNull(node.getMnuLevel()), isRoot, new AsyncCallback<DmResult<List<GrdMenu_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMenu_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.getData().size() > 0) {
                        grdMenu.getGrid().getStore().clear();
                        grdMenu.getGrid().getStore().addAll(result.getData());
                        grdMenu.releaseScroll();

//                        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(0, 1);
                        if (isMenuGridClick) {
                            grdNavigation.getGrid().getStore().add(node);
                        }
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                }
            }
        });
    }

    private void showCopyPermissionPopUp() {
        copyPermissionEdit.show(btnCopyPermission, DmShared.xNull(txtHUser.getText()));
    }

    private void showLinkedAccountPopUp() {
        TxtHLinkedAccount_Mdl mdl = new TxtHLinkedAccount_Mdl();
        mdl.setSharedKeyFieldCode(DmShared.xNull(txtLinkedAccount.getTag()));
        mdl.setSharedKeyFieldName(DmShared.xNull(txtLinkedAccount.getText()));
        linkedAccountEdit.show(mdl, true, btnLinkedAccount, DmShared.xNull(txtHUser.getText()), DmShared.xNull(txtRole.getTag()));
    }

    private void manageMenuPermission(GrdMenu_Mdl node, int colIndex) {
        switch (colIndex) {
            case 1:
                bindMenu(node, false, true);
                break;
            case 2:
                if (DmShared.iNull(node.getBuMnuCount()).intValue() == 0) {
                    new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                    return;
                }
                if (DmShared.iNull(node.getMnuLevel()).intValue() == 4) {
                    new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                    return;
                }
                saveMenuPermission(EventType.VIEW, node);
                break;
            case 3:
                if (chkViewPermission(node)) {
                    if (DmShared.iNull(node.getBuIsAdd()).intValue() == 0) {
                        new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                        return;
                    }
                    saveMenuPermission(EventType.ADD, node);
                }
                break;
            case 4:
                if (chkViewPermission(node)) {
                    if (DmShared.iNull(node.getBuIsEdit()).intValue() == 0) {
                        new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                        return;
                    }
                    saveMenuPermission(EventType.EDIT, node);
                }
                break;
            case 5:
                if (chkViewPermission(node)) {
                    if (DmShared.iNull(node.getBuIsDelete()).intValue() == 0) {
                        new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                        return;
                    }
                    saveMenuPermission(EventType.DELETE, node);
                }
                break;
            case 6:
                if (chkViewPermission(node)) {
                    if (DmShared.iNull(node.getBuIsPrint()).intValue() == 0) {
                        new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                        return;
                    }
                    saveMenuPermission(EventType.PRINT, node);
                }
                break;
            case 7:
                if (DmShared.iNull(node.getMnuLevel()).intValue() != 4) {
                    if (DmShared.iNull(node.getCuMnuCount()).intValue() == 0) {
                        new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                        return;
                    }
                }
                if (DmShared.iNull(node.getBuIsEnable()).intValue() == 0) {
                    new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
                    return;
                }
                saveMenuPermission(EventType.ENABLE, node);
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                if (chkViewPermission(node)) {
                    restrictionPopup.show(node);
                }
                break;
            default:
                break;
        }
    }

    private boolean chkViewPermission(GrdMenu_Mdl node) {
        if (DmShared.iNull(node.getCuMnuCount()).intValue() == 0) {
            new DmAlertMessageBox("You don't have permission.", (DmTextBox) null).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveMenuPermission(EventType eventType, GrdMenu_Mdl node) {
        SrvAsync.invokeRPC(true).saveMenuPermission(eventType, node, DmShared.xNull(txtHUser.getText()), DmShared.xNull(txtRole.getTag()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    GrdMenu_Mdl menuMdl = grdNavigation.getGrid().getStore().get(grdNavigation.getGrid().getStore().size() - 1);
                    boolean isRoot = false;
                    if (DmShared.xNull(menuMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("Root")) {
                        isRoot = true;
                    }
                    bindMenu(menuMdl, isRoot, false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                }
            }
        });
    }

    public void fillUserRecord(String userName, String userId) {
        txtHUser.setText(userName);
        txtHUser.setTag(userId);
        SrvAsync.invokeRPC().getUserRecord(DmShared.xNull(userName), DmShared.xNull(userId), new AsyncCallback<DmResult<UserPermission_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHUser).show();
            }

            @Override
            public void onSuccess(DmResult<UserPermission_Mdl> result) {
                UserPermission_Mdl data;
                if (result.isStatus()) {
                    data = result.getData();
                    txtUserID.setText(data.getUserId());
                    txtRole.setTag(data.getRoleCode());
                    txtRole.setText(data.getRoleName());
                    txtDescription.setText(data.getDescription());
                    txtAdministratorYN.setText(data.getIsAdministrator());
                    txtLinkedAccount.setTag(data.getLinkedAccountCode());
                    txtLinkedAccount.setText(data.getLinkedAccountName());
                    txtEmail.setText(data.getEmail());

                    grdVouchersDetail.getGrid().getStore().clear();
                    grdVouchersDetail.getGrid().getStore().addAll(data.getListVoucher());
                    grdVouchersDetail.getGrid().getStore().add(new GrdVoucher_Mdl());

                    grdMenu.getGrid().getStore().clear();
                    grdMenu.getGrid().getStore().addAll(data.getListMenuDetail());

                    grdSite.getGrid().getStore().clear();
                    grdSite.getGrid().getStore().addAll(data.getListSite());
                    grdSite.getGrid().getStore().add(new GrdSite_Mdl());
                    ((CellSelectionModel) grdSite.getGrid().getSelectionModel()).selectCell(0, 1);

                    GrdCostCenter.getGrid().getStore().clear();
                    GrdCostCenter.getGrid().getStore().addAll(data.getListCostCenter());
                    GrdCostCenter.getGrid().getStore().add(new GrdCostCenter_Mdl());

                    addNavigation();

                    if (DmShared.xNull(txtRole.getText()).equalsIgnoreCase("")) {
                        flwCopyPermission.add(btnCopyPermission);
                        tabPanel.setVisible(true);
                    } else {
                        tabPanel.setVisible(false);
                        flwCopyPermission.clear();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                }
            }
        });
    }

    private String getSiteList(String seprator) {
        String val = "";
        List<GrdSite_Mdl> grd = super.grdSite.getGrid().getStore().getAll();
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

    protected void saveSitePermission(String name, boolean isSite) {
        SrvAsync.invokeRPC().saveSitePermission(name, DmShared.xNull(txtHUser.getText()), isSite, DmShared.xNull(txtRole.getTag()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {

                } else {

                }
            }
        });
    }

    private String getCostCenterList(String seprator) {
        String val = "";
        List<GrdCostCenter_Mdl> grd = GrdCostCenter.getGrid().getStore().getAll();
        for (int i = 0; i < grd.size(); i++) {
            if (!DmShared.xNull(grd.get(i).getShortName()).equalsIgnoreCase("")) {
                if (val.equalsIgnoreCase("")) {
                    val = val + seprator + DmShared.xNull(grd.get(i).getShortName()) + seprator;
                } else {
                    val = val + "," + seprator + DmShared.xNull(grd.get(i).getShortName()) + seprator;
                }
            }
        }
        return val;
    }

    private void manageNavigation(GrdMenu_Mdl node, int rowIndex) {
        if (node != null) {
            List<GrdMenu_Mdl> lstNavigation = new ArrayList<GrdMenu_Mdl>();
            boolean isRoot = false;
            for (int i = 0; i <= rowIndex; i++) {
                lstNavigation.add(grdNavigation.getGrid().getStore().get(i));
            }

            grdNavigation.getGrid().getStore().clear();
            grdNavigation.getGrid().getStore().addAll(lstNavigation);
            if (DmShared.xNull(node.getSharedKeyFieldCode()).trim().equalsIgnoreCase("Root")) {
                isRoot = true;
            }
            bindMenu(node, isRoot, false);
        }
    }

    private void addNavigation() {
        GrdMenu_Mdl node = new GrdMenu_Mdl();
        node.setSharedKeyFieldCode("Root");
        node.setSharedKeyFieldName("Root");
        grdNavigation.getGrid().getStore().clear();
        grdNavigation.getGrid().getStore().add(node);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(UserPermission.this);
                }
            }
        }).show();
    }

    private void clear() {
        txtUserID.setText("");
        txtRole.setText("");
        txtRole.setTag("");
        txtDescription.setText("");
        txtEmail.setText("");
        txtLinkedAccount.setText("");
        txtLinkedAccount.setTag("");
        grdVouchersDetail.getGrid().getStore().clear();
        grdMenu.getGrid().getStore().clear();
        grdSite.getGrid().getStore().clear();
        GrdCostCenter.getGrid().getStore().clear();
    }
}
