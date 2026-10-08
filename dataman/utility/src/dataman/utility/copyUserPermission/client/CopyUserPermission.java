package dataman.utility.copyUserPermission.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.copyUserPermission.shared.*;
import java.util.ArrayList;
import java.util.List;

public class CopyUserPermission extends CopyUserPermission_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyDownHandler {

    ServiceInvoker<CopyUserPermission_SrvAsync> SrvAsync;
    List<TxtHCompany_Mdl> txtHPLK_Mdl = new ArrayList<TxtHCompany_Mdl>();
    protected DefaultValue_Mdl dvm;
    Boolean checkAllFrom = false, checkAllTo = false;

    private void textHelpEvents() {
        txtHFromCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, DmShared.xNull(txtHToCompany.getTag()), new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {

                        new DmAlertMessageBox(caught.getMessage(), txtHFromCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHFromCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHFromCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCompany_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtFinanceYearFrom.setText(DmShared.xNull(tRow.getFinanceYear()));
                    txtPrefixFrom.setText(DmShared.xNull(tRow.getPrefix()));
                    fillFromGrdRecords(tRow.getSharedKeyFieldCode());
                } else {
                    txtFinanceYearFrom.setText("");
                    txtPrefixFrom.setText("");
                    grdFromCompany.getGrid().getStore().clear();
                }
            }
        });

        txtHToCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, DmShared.xNull(txtHFromCompany.getTag()), new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {

                        new DmAlertMessageBox(caught.getMessage(), txtHToCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHToCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHToCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCompany_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtFinanceYearTo.setText(DmShared.xNull(tRow.getFinanceYear()));
                    txtPrefixTo.setText(DmShared.xNull(tRow.getPrefix()));
                    fillToGrdRecords(tRow.getSharedKeyFieldCode());
                } else {
                    txtFinanceYearTo.setText("");
                    txtPrefixTo.setText("");
                    grdToCompany.getGrid().getStore().clear();
                }
            }
        });

    }

    private void gridEvents() {

        grdToCompany.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {
                        int val = DmShared.vNull(grdToCompany.getGrid().getStore().get(event.getRowIndex()).getIsCheckedOrDelete()).intValue();
                        if (val == 0) {
                            grdToCompany.getGrid().getStore().get(event.getRowIndex()).setIsCheckedOrDelete("1");
                        } else if (val == 1) {
                            grdToCompany.getGrid().getStore().get(event.getRowIndex()).setIsCheckedOrDelete("0");
                        }
                        grdToCompany.commitChanges();
                        setCheckUnchek(false);
                        ((CellSelectionModel) grdToCompany.getGrid().getSelectionModel()).selectCell(event.getRowIndex(), 1);
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        grdFromCompany.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {
                        int val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(event.getRowIndex()).getIsCheckedOrDelete()).intValue();
                        if (val == 0) {
                            grdFromCompany.getGrid().getStore().get(event.getRowIndex()).setIsCheckedOrDelete("1");
                        } else if (val == 1) {
                            grdFromCompany.getGrid().getStore().get(event.getRowIndex()).setIsCheckedOrDelete("0");
                        }
                        grdFromCompany.commitChanges();
                        ((CellSelectionModel) grdFromCompany.getGrid().getSelectionModel()).selectCell(event.getRowIndex(), 1);

                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

    }

    private void setCheckUnchek(boolean flag) {
        if (flag) {
            for (int i = 0; i < grdFromCompany.getGrid().getStore().size(); i++) {
                GrdCopyPermissionFromTo_Mdl grd_Mdl = grdFromCompany.getGrid().getStore().get(i);
                if (DmShared.xNull(grd_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("0") || DmShared.xNull(grd_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("")) {
                    checkAllFrom = false;
                    chkAllFromGrd.setStyleName("fa fa-square-o");
                    break;
                } else {
                    checkAllFrom = true;
                    chkAllFromGrd.setStyleName("fa fa-check-square-o");
                }

            }

        } else {
            for (int i = 0; i < grdToCompany.getGrid().getStore().size(); i++) {
                GrdCopyPermissionFromTo_Mdl grd_Mdl = grdToCompany.getGrid().getStore().get(i);
                if (DmShared.xNull(grd_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("0") || DmShared.xNull(grd_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("")) {
                    checkAllTo = false;
                    chkAllToGrd.setStyleName("fa fa-square-o");
                    chkAllToGrd.getElement().getStyle().setColor("black");
                    break;
                } else {
                    checkAllTo = true;
                    chkAllToGrd.setStyleName("fa fa-times");
                    chkAllToGrd.getElement().getStyle().setColor("red");
                }

            }
        }

    }

    private void fillFromGrdRecords(String compCode) {
        SrvAsync.invokeRPC().fillGrid(compCode, new AsyncCallback<DmResult<List<GrdCopyPermissionFromTo_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCopyPermissionFromTo_Mdl>> result) {
                if (result.isStatus()) {
                    grdFromCompany.getGrid().getStore().clear();
                    grdFromCompany.getGrid().getStore().addAll(result.getData());
                    grdFromCompany.commitChanges();
                    setCheckUnchek(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHFromCompany).show();
                }
            }
        });

    }

    private void fillToGrdRecords(String compCode) {
        SrvAsync.invokeRPC().fillGrid(compCode, new AsyncCallback<DmResult<List<GrdCopyPermissionFromTo_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCopyPermissionFromTo_Mdl>> result) {
                if (result.isStatus()) {
                    grdToCompany.getGrid().getStore().clear();
                    grdToCompany.getGrid().getStore().addAll(result.getData());
                    for (int i = 0; i < grdToCompany.getGrid().getStore().getAll().size(); i++) {
                        grdToCompany.getGrid().getStore().getAll().get(i).setToCompanyCode(DmShared.xNull(txtHToCompany.getTag()));
                        grdToCompany.getGrid().getStore().getAll().get(i).setToCompanyName(DmShared.xNull(txtHToCompany.getText()));
                        grdToCompany.getGrid().getStore().getAll().get(i).setToCompVrPrefix(DmShared.xNull(txtPrefixTo.getText()));
                    }
                    grdToCompany.commitChanges();
                    setCheckUnchek(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHFromCompany).show();
                }
            }
        });

    }

    public CopyUserPermission(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<CopyUserPermission_SrvAsync>(GWT.create(CopyUserPermission_Srv.class), CopyUserPermission.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        textHelpEvents();
        handlerEvents();
        gridEvents();
        flwForm.addAttachHandler(this);
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    txtHToCompany.setTag(DmShared.xNull(dvm.getCompCode()));
                    txtHToCompany.setText(DmShared.xNull(dvm.getCompName()));
                    txtPrefixTo.setText(DmShared.xNull(dvm.getPrefix()));
                    txtFinanceYearTo.setText(DmShared.xNull(dvm.getFinanceYear()));
                    fillToGrdRecords(DmShared.xNull(txtHToCompany.getTag()));
                    swi.setWidget(CopyUserPermission.this);
                    txtHFromCompany.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void saveData() {
        List<GrdCopyPermissionFromTo_Mdl> lstCopyPermission_Mdl = new ArrayList<>();
        try {
            lstCopyPermission_Mdl.addAll(grdToCompany.getGrid().getStore().getAll());
            int cnt = 0;
            for (int i = 0; i < lstCopyPermission_Mdl.size(); i++) {
                if ((DmShared.xNull(lstCopyPermission_Mdl.get(i).getIsCheckedOrDelete()).equalsIgnoreCase("1"))
                        || (DmShared.xNull(lstCopyPermission_Mdl.get(i).getIsCopy()).equalsIgnoreCase("1"))) {
                    ++cnt;
                }
            }
            if (cnt > 0) {
                SrvAsync.invokeRPC().saveRecord(lstCopyPermission_Mdl, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            fillToGrdRecords(DmShared.xNull(txtHToCompany.getTag()));
                            txtHFromCompany.setFocus(true);
                        } else {
                            saveDataError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                        }
                    }
                });
            } else {
                new DmAlertMessageBox("Please select user to copy or delete permission.", txtHFromCompany).show();
                return;

            }

        } catch (DmException e) {
            saveDataError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveDataError(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, txtHFromCompany).show();
                break;
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }

        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            new DmConfirmMessageBox("Are you sure? You want to copy permission.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        saveData();
                    }
                }
            }).show();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && event.getNativeKeyCode() == KeyCodes.KEY_S) {
            new DmConfirmMessageBox("Are you sure? You want to copy permission.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        saveData();
                    }
                }
            }).show();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CopyUserPermission.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnClose) {
                onClose();
            } else if (event.getSource() == btnSave) {

                new DmConfirmMessageBox("Are you sure? You want to copy permission.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            saveData();
                        }
                    }
                }).show();

            } else if (event.getSource() == chkAllFromGrd) {
                selectAllFromGrd();
            } else if (event.getSource() == chkAllToGrd) {
                selectAllToGrd();
            } else if (event.getSource() == btnCopy) {
                if (!DmShared.xNull(txtHFromCompany.getText()).equalsIgnoreCase("")) {
                    copyGrdData();
                } else {
                    new DmAlertMessageBox("Please select from company to copy permission.", txtHFromCompany).show();
                    return;
                }

            }
        } catch (Exception e) {
        }
    }

    private void copyGrdData() {
        String userName = "";
        for (int i = 0; i < grdFromCompany.getGrid().getStore().getAll().size(); i++) {
            GrdCopyPermissionFromTo_Mdl copyPermission = grdFromCompany.getGrid().getStore().getAll().get(i);
            if ((grdToCompany.getGrid().getStore().getAll().contains(copyPermission)) && (copyPermission.getIsCheckedOrDelete().equalsIgnoreCase("1"))) {
                int index = grdToCompany.getGrid().getStore().getAll().indexOf(copyPermission);
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompanyCode(DmShared.xNull(txtHFromCompany.getTag()));
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompanyName(DmShared.xNull(txtHFromCompany.getText()));
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompVrPrefix(DmShared.xNull(txtPrefixFrom.getText()));
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompanyCode(DmShared.xNull(txtHToCompany.getTag()));
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompanyName(DmShared.xNull(txtHToCompany.getText()));
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompVrPrefix(DmShared.xNull(txtPrefixTo.getText()));
                grdToCompany.getGrid().getStore().getAll().get(index).setIsCopy(DmShared.xNull("1"));
            } else if ((grdToCompany.getGrid().getStore().getAll().contains(copyPermission)) && (copyPermission.getIsCheckedOrDelete().equalsIgnoreCase("0"))) {
                int index = grdToCompany.getGrid().getStore().getAll().indexOf(copyPermission);
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompanyCode("");
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompanyName("");
                grdToCompany.getGrid().getStore().getAll().get(index).setFromCompVrPrefix("");
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompanyCode(DmShared.xNull(txtHToCompany.getTag()));
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompanyName(DmShared.xNull(txtHToCompany.getText()));
//                grdToCompany.getGrid().getStore().getAll().get(index).setToCompVrPrefix(DmShared.xNull(txtPrefixTo.getText()));
                grdToCompany.getGrid().getStore().getAll().get(index).setIsCopy(DmShared.xNull("0"));
            } else if ((!grdToCompany.getGrid().getStore().getAll().contains(copyPermission)) && (copyPermission.getIsCheckedOrDelete().equalsIgnoreCase("1"))) {
                userName += DmShared.xNull(copyPermission.getSharedKeyFieldName()) + ", ";
            }
        }
        grdToCompany.commitChanges();
        grdToCompany.getGrid().getStore().commitChanges();
        if (!DmShared.xNull(userName).equalsIgnoreCase("")) {
            userName = userName.substring(0, userName.length() - 2);
            new DmAlertMessageBox(userName + " user not found to copy permission.", btnSave).show();
        }

    }

    private void selectAllFromGrd() {
        if (checkAllFrom == false) {
            checkAllFrom = true;
            chkAllFromGrd.setStyleName("fa fa-check-square-o");
        } else {
            checkAllFrom = false;
            chkAllFromGrd.setStyleName("fa fa-square-o");
        }
        for (int i = 0; i < grdFromCompany.getGrid().getStore().size(); i++) {

            if (checkAllFrom == false) {
                grdFromCompany.getGrid().getStore().get(i).setIsCheckedOrDelete("0");
            } else if (checkAllFrom == true) {
                grdFromCompany.getGrid().getStore().get(i).setIsCheckedOrDelete("1");
            }

        }
        grdFromCompany.commitChanges();
    }

    private void selectAllToGrd() {
        if (checkAllTo == false) {
            checkAllTo = true;
            chkAllToGrd.setStyleName("fa fa-times");
            chkAllToGrd.getElement().getStyle().setColor("red");

        } else {
            checkAllTo = false;
            chkAllToGrd.setStyleName("fa fa-square-o");
            chkAllToGrd.getElement().getStyle().setColor("black");
        }
        for (int i = 0; i < grdToCompany.getGrid().getStore().size(); i++) {

            if (checkAllTo == false) {
                grdToCompany.getGrid().getStore().get(i).setIsCheckedOrDelete("0");
            } else if (checkAllTo == true) {
                grdToCompany.getGrid().getStore().get(i).setIsCheckedOrDelete("1");
            }

        }
        grdToCompany.commitChanges();
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        chkAllFromGrd.addClickHandler(this);
        btnCopy.addClickHandler(this);
        chkAllToGrd.addClickHandler(this);
        txtFinanceYearFrom.setEnabled(false);
        txtFinanceYearTo.setEnabled(false);
        txtPrefixFrom.setEnabled(false);
        txtPrefixTo.setEnabled(false);
    }
}
