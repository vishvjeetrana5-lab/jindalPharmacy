package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.*;
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
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class EnvironmentService extends EnvironmentService_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyDownHandler {

    private final ServiceInvoker<EnvironmentService_SrvAsync> srvAsync;
    private String destSiteName = "", destSiteCode = "", isCopy = "";
    private final GeneralTabGridEdit<GrdServiceCategory_Mdl, EnvironmentService_Dsg.GrdGeneralTwo_PrpAcs> grdGeneralEdit = new GeneralTabGridEdit(grdServiceCategory);
    private final GeneralTabGridEdit<GrdServiceCategory_Mdl, EnvironmentService_Dsg.GrdGeneralTwo_PrpAcs> grdDiagnosticEdit = new GeneralTabGridEdit(grdDiagnosticCategory);
    private final BloodComponentTabGridEdit<GrdBloodComponent_Mdl, EnvironmentService_Dsg.GrdGeneralTwo_PrpAcs> grdBloodComponentEdit = new BloodComponentTabGridEdit(grdBloodComponent);
    private final GrdServicePopup grdServicePop = new GrdServicePopup();

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(DmShared.xNull(txtHSite.getTag()));
                        destSiteCode = txtHSite.getTag();
                        destSiteName = txtHSite.getText();
                    }
                }
            }
        });
        txtHSiteCopy.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        isCopy = t1Row.getSharedKeyFieldCode();
                        topControlMoveRec(t1Row.getSharedKeyFieldCode());
                        txtHSite.setText(DmShared.xNull(destSiteName));
                        txtHSite.setTag(DmShared.iNull(destSiteCode).toString());
                    }
                }
            }
        });
    }

    private void gridEvents() {
        grdServicePop.addCallBack(new GrdServicePopup.CallBack() {
            @Override
            public void onClose() {
            }

            @Override
            public void onOk(int rowIndex, boolean grdFlag, List<GrdEnviroSerive_Mdl> lstService) {
                if (grdFlag == true) {
                    grdServiceCategory.getGrid().getStore().get(rowIndex).getLstEnviroService().clear();
                    grdServiceCategory.getGrid().getStore().get(rowIndex).getLstEnviroService().addAll(lstService);
                    grdServiceCategory.getGrid().getStore().get(rowIndex).setHaveServices(lstService.size() > 0);
                    grdServiceCategory.commitChanges();
                    grdServiceCategory.releaseScroll();
                } else if (grdFlag == false) {
                    grdDiagnosticCategory.getGrid().getStore().get(rowIndex).getLstEnviroService().clear();
                    grdDiagnosticCategory.getGrid().getStore().get(rowIndex).getLstEnviroService().addAll(lstService);
                    grdDiagnosticCategory.getGrid().getStore().get(rowIndex).setHaveServices(lstService.size() > 0);
                    grdDiagnosticCategory.commitChanges();
                    grdDiagnosticCategory.releaseScroll();
                }

            }
        });

        grdGeneralEdit.addCallBack(new GeneralTabGridEdit.CallBack() {
            @Override
            public void onOk(GrdServiceCategory_Mdl grdTdsEntry) {
                grdServiceCategory.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });
        grdServiceCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 5) {
                    grdServiceCategory.lockScroll();
                    GrdServiceCategory_Mdl grdMdl = grdServiceCategory.getGrid().getStore().get(event.getRowIndex());
                    grdServicePop.show(event.getRowIndex(), grdMdl.getSharedKeyFieldCode(), grdMdl.getSharedKeyFieldName(), txtHSite.getTag(), true, grdMdl.getLstEnviroService());
                } else if (event.getColumnIndex() != 5) {
                    grdGeneralEdit(event.getRowIndex());
                }

            }
        });
        grdServiceCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    grdGeneralEdit(grdServiceCategory.getGrid().getStore().indexOf(grdServiceCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdDiagnosticEdit.addCallBack(new GeneralTabGridEdit.CallBack() {
            @Override
            public void onOk(GrdServiceCategory_Mdl grdTdsEntry) {
                grdDiagnosticCategory.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });
        grdDiagnosticCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 5) {
                    grdDiagnosticCategory.lockScroll();
                    GrdServiceCategory_Mdl grdMdl = grdDiagnosticCategory.getGrid().getStore().get(event.getRowIndex());
                    grdServicePop.show(event.getRowIndex(), grdMdl.getSharedKeyFieldCode(), grdMdl.getSharedKeyFieldName(), txtHSite.getTag(), false, grdMdl.getLstEnviroService());
                } else if (event.getColumnIndex() != 5) {
                    grdDiagnosticEdit(event.getRowIndex());
                }

            }
        });
        grdDiagnosticCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    grdDiagnosticEdit(grdDiagnosticCategory.getGrid().getStore().indexOf(grdDiagnosticCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdBloodComponentEdit.addCallBack(new BloodComponentTabGridEdit.CallBack() {
            @Override
            public void onOk(GrdBloodComponent_Mdl grdTdsEntry) {
                grdBloodComponent.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });
        grdBloodComponent.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                grdBloodComponentEdit(event.getRowIndex());
            }
        });
        grdBloodComponent.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    grdBloodComponentEdit(grdBloodComponent.getGrid().getStore().indexOf(grdBloodComponent.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void grdGeneralEdit(Integer currentRowIndex) {
        grdGeneralEdit.show(currentRowIndex, true, false, false, txtHSite.getTag());
    }

    private void grdDiagnosticEdit(int currentRowIndex) {
        grdDiagnosticEdit.show(currentRowIndex, true, false, false, txtHSite.getTag());
    }

    private void grdBloodComponentEdit(int currentRowIndex) {
        grdBloodComponentEdit.show(currentRowIndex, true, false, false, txtHSite.getTag());
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnCopy)) {
                txtHSiteCopy.show(btnCopy, "");
                txtHSiteCopy.reQuery("");
            }
        } catch (DmException e) {
        }

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (!txtHSite.getTag().equalsIgnoreCase("")) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && event.getNativeKeyCode() == KeyCodes.KEY_S) {
                topControlSave();
            }
        }
    }

    public EnvironmentService(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        tabPanel.clear();
        managePermission(menuPermission);
        srvAsync = new ServiceInvoker<>(GWT.create(EnvironmentService_Srv.class), EnvironmentService.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());

        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        btnDelete.addClickHandler(this);
        textHelpEvents();
        flwForm.addAttachHandler(this);
        gridEvents();
        grdBloodComponent.getGrid().getStore().add(new GrdBloodComponent_Mdl());
        defaultValues(swi);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EnvironmentService_Mdl topControlSaveInitialize() {
        EnvironmentService_Mdl mrTemp = new EnvironmentService_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));

        mrTemp.setDestiSiteCode(destSiteCode);
        mrTemp.getGrdGeneral().addAll(grdServiceCategory.getGrid().getStore().getAll());
        mrTemp.getGrdDiagnostic().addAll(grdDiagnosticCategory.getGrid().getStore().getAll());
        mrTemp.getGrdBloodComponent().addAll(grdBloodComponent.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        EnvironmentService_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EnvironmentService_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                        new DmAlertMessageBox("Record saved successfully", txtHSite).show();
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
                new DmAlertMessageBox(errorMessage, txtHSite).show();
                break;

            default:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtHSite).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EnvironmentService_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnvironmentService_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EnvironmentService_Mdl> result) {
        EnvironmentService_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            grdServiceCategory.getGrid().getStore().clear();
            grdServiceCategory.getGrid().getStore().addAll(data.getGrdGeneral());

            grdDiagnosticCategory.getGrid().getStore().clear();
            grdDiagnosticCategory.getGrid().getStore().addAll(data.getGrdDiagnostic());

            grdBloodComponent.getGrid().getStore().clear();
            grdBloodComponent.getGrid().getStore().addAll(data.getGrdBloodComponent());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            grdGeneralEdit.hide();
            grdDiagnosticEdit.hide();
            grdBloodComponentEdit.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public void defaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    txtHSite.setTag(DmShared.xNull(result.getData().getSiteCode()));
                    txtHSite.setText(DmShared.xNull(result.getData().getSiteName()));
                    destSiteCode = DmShared.xNull(result.getData().getSiteCode());
                    destSiteName = DmShared.xNull(result.getData().getSiteName());
                    swi.setWidget(EnvironmentService.this);
                    topControlMoveRec(txtHSite.getTag());
                    txtHSite.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnvironmentService.this);
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
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    private void managePermission(UserMenu menuPermission) {
        boolean flag = false;
        tabPanel.clear();
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (controlPermission.equalsIgnoreCase(tabServiceCategory)) {
                tabPanel.add(flwGeneral, tabServiceCategory);
                flag = true;
            }
            if (controlPermission.equalsIgnoreCase(tabDiagnosticCategory)) {
                tabPanel.add(flwDiagnosticCategory, tabDiagnosticCategory);
                flag = true;
            }
            if (controlPermission.equalsIgnoreCase(tabBloodComponent)) {
                tabPanel.add(flwBloodComponent, tabBloodComponent);
                flag = true;
            }
        }
        if (flag) {
            tabPanel.selectTab(0);
            tabPanel.ensureDebugId("cwTabPanel");
        }

    }

}
