package aarogya.pharmacy.wholeSaleSettings.client;

import aarogya.pharmacy.wholeSaleSettings.shared.DefaultValue_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.Find_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.WholeSaleSetting_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.WholeSaleSetting_Srv;
import aarogya.pharmacy.wholeSaleSettings.shared.WholeSaleSetting_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.util.List;

public class WholeSaleSetting extends WholeSaleSetting_Dsg implements SelectionHandler<Item>, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private final ServiceInvoker<WholeSaleSetting_SrvAsync> srvAsync;
    private final ManageSite manageSite = new ManageSite();
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;

    private void textHelpEvents() {

        txtHCentralStore.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCentralStore(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCentralStore).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCentralStore.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCentralStore).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
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

    public WholeSaleSetting(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(WholeSaleSetting_Srv.class), WholeSaleSetting.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);

        textHelpEvents();
        findEvents();
        mnuManageSite.addSelectionHandler(this);
        mnuExportCard.addSelectionHandler(this);
        btnSave.addClickHandler(this);
        btnClose.addClickHandler(this);

        defaultValues(swi);

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


                    swi.setWidget(WholeSaleSetting.this);
                    topControlMoveRec(result.getData().getSharedKeyFieldCode());
                    txtHCentralStore.setFocus(true);

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

    private WholeSaleSetting_Mdl topControlSaveInitialize() {
        WholeSaleSetting_Mdl mrTemp = new WholeSaleSetting_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHCentralStore.getTag()));
        mrTemp.setPharmacyCentralStoreCode(DmShared.xNull(txtHCentralStore.getTag()));
        mrTemp.setPharmacyCentralStoreName(DmShared.xNull(txtHCentralStore.getText()));
        mrTemp.setEwayBillLimit(DmClient.vNull(txtEwayBillLimit.getText(), "0.00"));

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        return mrTemp;
    }

    private void topControlSave() {
        WholeSaleSetting_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
//            PropertyCategoryMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, "Update", new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {

                    new DmAlertMessageBox(caught.getMessage(), txtHCentralStore).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {

        }
    }

    private void topControlSaveError(int errorNo, String errorMessage, String errorSourceCode, String errorSource) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHCentralStore).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtHCentralStore).show();
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WholeSaleSetting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WholeSaleSetting_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WholeSaleSetting_Mdl> result) {
        WholeSaleSetting_Mdl data;

        clear();

        if (result.isStatus()) {

            data = result.getData();
            txtHCentralStore.setTag(DmShared.xNull(data.getPharmacyCentralStoreCode()));
            txtHCentralStore.setText(DmShared.xNull(data.getPharmacyCentralStoreName()));
            txtEwayBillLimit.setText(DmClient.vNull(data.getEwayBillLimit(), "0.00"));
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        modifyReasonMdl = null;
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            modifyReason.hide();
            manageSite.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        }
        if (event.getSource() == btnSave) {
            topControlSave();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WholeSaleSetting.this);
                }
            }
        }).show();
    }
}
