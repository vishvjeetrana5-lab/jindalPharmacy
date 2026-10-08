package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.GrdAdditionalUserPermission_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_Srv;
import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_SrvAsync;
import aarogya.master.additionalUserPermission.shared.SearchValue_Mdl;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.libraryMenu.self.shared.lmUtility;

public class AdditionalUserPermission extends AdditionalUserPermission_Dsg implements IsWidget, ClickHandler, DmRootEvents, KeyUpHandler, AttachEvent.Handler {

    private final ServiceInvoker<AdditionalUserPermission_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    protected boolean isCreateUserBlog = false, isAdditionalPermissionBlog = false, isUserPermissionBlog = false, isManageUserRoleBlog = false;
    private final AdditionalUserPermissionGridEdit griduserDepartment = new AdditionalUserPermissionGridEdit();
    private final ManageUserRoleGridEdit gridManageUserRole = new ManageUserRoleGridEdit();

    private void setPermission(UserMenu menuPermission) {
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(createUserBlog)) {
                isCreateUserBlog = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(additionalPermissionBlog)) {
                isAdditionalPermissionBlog = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(userPermissionBlog)) {
                isUserPermissionBlog = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(manageUserRoleBlog)) {
                isManageUserRoleBlog = true;
            }
        }
    }

    private void gridEvents() {
        setHeaderValue(1);

        griduserDepartment.addCallBack(new AdditionalUserPermissionGridEdit.CallBack() {
            @Override
            public void onOk() {
                reQuery();
            }

            @Override
            public void onClose() {
                txtSearchValue.setFocus(true);

            }

        });
        grdUserDepartment.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdUserDepartment.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(false).getFillGrid(helper, blnToEndOfFile, searchValue_Mdl, new AsyncCallback<DmResultPageing<List<GrdAdditionalUserPermission_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdAdditionalUserPermission_Mdl>> result) {
                        if (result.isStatus()) {
                            grdUserDepartment.getDmGrid().getGrid().getStore().clear();
                            grdUserDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
            }
        });
        grdUserDepartment.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                grdUserDepartment.getDmGrid().lockScroll();
                try {
                    showGridDetail(grdUserDepartment.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } finally {
                    grdUserDepartment.getDmGrid().releaseScroll();
                }
            }
        });
        grdUserDepartment.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                grdUserDepartment.getDmGrid().lockScroll();
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE && !event.getNativeEvent().getCtrlKey()) {
                        showGridDetail(grdUserDepartment.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } finally {
                    grdUserDepartment.getDmGrid().releaseScroll();
                }
            }

        }, KeyDownEvent.getType());
        gridManageUserRole.addCallBack(new ManageUserRoleGridEdit.CallBack() {
            @Override
            public void onOk() {
                reQuery();
                txtSearchValue.setFocus(true);
            }

            @Override
            public void onClose() {
                reQuery();
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdUserDepartment.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdUserDepartment.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdUserDepartment.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdUserDepartment.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdUserDepartment.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdUserDepartment.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdUserDepartment.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private SearchValue_Mdl setParam() {
        SearchValue_Mdl svm = new SearchValue_Mdl();
        svm.setFieldName(searchDbFieldName);
        svm.setFieldNameValue(txtSearchValue.getText());
        return svm;
    }

    private void reQuery() {
        grdUserDepartment.getHelper().setRowsStartFrom(0);
        grdUserDepartment.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdUserDepartment.reQuery();
    }

    private void showGridDetail(GrdAdditionalUserPermission_Mdl additionalUserPermission_Mdl) {
        griduserDepartment.show(this, additionalUserPermission_Mdl);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            onClose();

        } else if (event.getSource() == btnCreateUser) {
            openUserMast();

        } else if (event.getSource() == btnRefresh) {
            reQuery();
            txtSearchValue.setFocus(true);
        } else if (event.getSource() == btnManageUserRole) {
            gridManageUserRole.show();

        }
    }

    private void openUserMast() {

        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmUtility().moduleName, new lmUtility().USER_MASTER.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("OPEN_USER", null, new DmCallBack() {
                    @Override
                    public void result(DmResult result) {
                        if (result != null) {
                            if (result.isStatus()) {
                                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);

                            }
                        }
                    }
                });
            }
        });

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AdditionalUserPermission.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    public AdditionalUserPermission(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        this.asWidget().setTitle(widgetName);
        setPermission(menuPermission);
        toolContainer.setTitle(this.asWidget().getTitle());
        flwCreateUser.clear();
        flwManageUserRole.clear();
        if (isCreateUserBlog) {
            flwCreateUser.add(vl.add(btnCreateUser));
        }

        if (isManageUserRoleBlog) {
            flwManageUserRole.add(vl.add(btnManageUserRole));
        }
        toolContainer.addWidget(flwCreateUser);
        toolContainer.addWidget(flwManageUserRole);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        grdUserDepartment.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        gridEvents();
        manageEvents();
        reQuery();
        swi.setWidget(this);
        txtSearchValue.setFocus(true);
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        flwForm.addAttachHandler(this);
        btnCreateUser.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnManageUserRole.addClickHandler(this);

    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            reQuery();
        }
    }

}
