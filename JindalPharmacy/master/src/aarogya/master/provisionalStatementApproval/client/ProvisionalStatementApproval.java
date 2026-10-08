package aarogya.master.provisionalStatementApproval.client;

import aarogya.master.provisionalStatementApproval.shared.*;
import com.google.gwt.core.shared.GWT;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class ProvisionalStatementApproval extends ProvisionalStatementApproval_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<ProvisionalStatementApproval_SrvAsync> srvAsync;
    private final Setting settingPopup = new Setting();
    private SharingFilter_Mdl defaultMdl;

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefault(new AsyncCallback<DmResult<SharingFilter_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {

                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SharingFilter_Mdl> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();
                    swi.setWidget(ProvisionalStatementApproval.this);
                    grdSharing.reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        settingPopup.addCallBack(new Setting.CallBack() {

            @Override
            public void onOk(SharingFilter_Mdl approvalEntry_Mdl) {
                defaultMdl = approvalEntry_Mdl;
                grdSharing.reQuery();
            }
        });

    }

    private void gridEvents() {
        grdSharing.addCallBack(new DmGridPaging.CallBack<SharingFilter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SharingFilter_Mdl param) {
                srvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdProvisionalStatementApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdProvisionalStatementApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            grdSharing.setData(result.getData(), result.getPaging());

                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }

            @Override
            public SharingFilter_Mdl getParam() {
                return defaultMdl;
            }
        });
        grdSharing.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    GrdProvisionalStatementApproval_Mdl model = grdSharing.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                    if (event.getCellIndex() == 3) {
                        if (model.isClosed()) {
                            getChecked(DmShared.xNull(model.getSharedKeyFieldCode()), 0);
                        } else if (!model.isClosed()) {
                            getChecked(DmShared.xNull(model.getSharedKeyFieldCode()), 1);
                        }
                        grdSharing.getDmGrid().commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void getChecked(String docId, int isClosed) {
        try {
            srvAsync.invokeRPC().getChecked(isClosed, docId, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdSharing.getDmGrid().commitChanges();
                        grdSharing.reQuery();
                    } else {
                        grdSharing.reQuery();
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProvisionalStatementApproval.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnSetting) {
            showSetting();
        } else if (event.getSource() == btnRefresh) {
            reQuery();
        }
    }

    private void reQuery() {
        grdSharing.reQuery();
        grdSharing.getDmGrid().getGrid().getStore().clear();
    }

    public ProvisionalStatementApproval(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ProvisionalStatementApproval_SrvAsync>(GWT.create(ProvisionalStatementApproval_Srv.class), ProvisionalStatementApproval.class.getName());
        this.asWidget().setTitle(widgetName);
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        gridEvents();
        iniDefaultValue(swi);
    }

    private void showSetting() {
        settingPopup.show(defaultMdl, true);
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
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;

        }
    }

    @Override
    public void searchRecord(String key) {

    }

}
