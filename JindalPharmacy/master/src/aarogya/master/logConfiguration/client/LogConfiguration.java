package aarogya.master.logConfiguration.client;

import aarogya.master.logConfiguration.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import java.util.List;

public class LogConfiguration extends LogConfiguration_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<LogConfiguration_SrvAsync> SrvAsync;
    private String moduleName = "";

    public LogConfiguration(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(LogConfiguration_Srv.class), LogConfiguration.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        textHelpEvents();
        handlerEvent();
        gridEvents();
        onLoad();
        swi.setWidget(this);
//        txtHModule.setFocus(true);
        this.getViaList();
    }

    private void textHelpEvents() {

        txtHEntry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEntry(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEntry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEntry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEntry).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

                if (blnStatus && t1Row != null) {
                    moduleName = DmShared.xNull(t1Row.getSharedModifiedBy());
                    getFields(moduleName, t1Row.getSharedKeyFieldName());

                }
            }
        });

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LogConfiguration.this);
                }
            }
        }).show();
    }

    private LogConfiguration_Mdl topControlSaveInitialize() {
        LogConfiguration_Mdl mrTemp = new LogConfiguration_Mdl();
        mrTemp.setModuleName(DmShared.xNull(moduleName));
        mrTemp.setMenuName(DmShared.xNull(txtHEntry.getText()));
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        grdLogDetail.commitChanges();
        mrTemp.getGrdLogDetailMdl().addAll(grdLogDetail.getGrid().getStore().getAll());
        return mrTemp;

    }

    private void topControlSave() {
        LogConfiguration_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            LogConfiguration_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEntry).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        getFields(result.getData(), txtHEntry.getText());
                        new DmAlertMessageBox("Record Saved Successfully...", (DmTextBox) null).show();
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
                new DmAlertMessageBox(errorMessage, txtHEntry).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEntry).show();
        }

    }

    private void getFields(final String module, final String entry) {
        SrvAsync.invokeRPC().getFields(module, entry, new AsyncCallback<DmResult<LogConfiguration_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LogConfiguration_Mdl> result) {
                topControlMoveRecSuccess(module, entry, result);
            }
        });
    }

    private void topControlMoveRecSuccess(String moduleName, String menuName, DmResult<LogConfiguration_Mdl> result) {
        LogConfiguration_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            txtEmail.setText(result.getData().getEmail());
            grdLogDetail.getGrid().getStore().clear();
            grdLogDetail.getGrid().getStore().addAll(result.getData().getGrdLogDetailMdl());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        txtHEntry.setFocus(true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void gridEvents() {
        grdLogDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 3) {
                        int var = DmShared.iNull(grdLogDetail.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                        if (var == 1) {
                            grdLogDetail.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                        } else if (var == 0) {
                            grdLogDetail.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                        }
                        grdLogDetail.commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onLoad() {
        grdLogDetail.getGrid().getStore().clear();

    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnClose) {
                onClose();
            } else if (event.getSource() == btnSave) {
                topControlSave();
//            } else if (event.getSource() == btnReset) {
//                topControlDelete();
//            } else if (event.getSource().equals(btnExport)) {

            }
        } catch (Exception e) {
        }
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

    private void handlerEvent() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
//        btnReset.addClickHandler(this);
//        btnExport.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

}
