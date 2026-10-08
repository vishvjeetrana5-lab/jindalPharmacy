package aarogya.master.eod.client;

import aarogya.master.eod.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class LabConsumption extends LabConsumption_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onClose();
    }
    private CallBack callBack;
    private ServiceInvoker<EOD_SrvAsync> srvAsync;
    private String forDate = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void gridEvents() {
        grdPendingDate.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPendingAP(DmShared.xNull(grdPendingDate.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName()).trim());
            }
        });
        grdPendingDate.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPendingAP(DmShared.xNull(grdPendingDate.getGrid().getStore().get(grdPendingDate.getGrid().getStore().indexOf(grdPendingDate.getGrid().getSelectionModel().getSelectedItem())).getSharedKeyFieldName()).trim());
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showPendingAP(String forDate) {
        this.forDate = forDate;
        txtForDate.setText(DmShared.dNull(forDate));
        getPostingItemDetail();
    }

    public void show(String forDate, boolean isDelete) {
        this.forDate = forDate;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdLCPostingDetail.getGrid().getStore().clear();
        grdLCDeleteDetail.getGrid().getStore().clear();
        grdPendingDate.getGrid().getStore().clear();
        txtForDate.setText(DmShared.dNull(forDate));

        flwPosting.clear();
        flwDelete.clear();
        flwWorkingArea.clear();
        if (isDelete) {
            grdLCDeleteDetail.getGrid().setHeight("350px");
            flwWorkingArea.add(deleteArea());
            flwPosting.add(vl.add(btnDelete));
            getDeleteItemDetail();
        } else {
            grdLCDeleteDetail.getGrid().setHeight("130px");
            flwWorkingArea.add(postingArea());
            flwPosting.add(vl.add(btnPosting));
            getPendingDates();
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean isCallbackNeeded) {
        this.popupPanel.hide();
        if (isCallbackNeeded) {
            callBack.onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnPosting) {
            new DmConfirmMessageBox("Are you sure? You want to post.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        save();
                    }
                    txtForDate.setFocus(true);
                }
            }).show();
        } else if (event.getSource() == btnDelete) {
            new DmConfirmMessageBox("Are you sure? You want to delete.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        delete();
                    }
                    txtForDate.setFocus(true);
                }
            }).show();
        } else if (event.getSource() == btnClose) {
            hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onClose();
            hide();
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
        return this.popupPanel;
    }

    public LabConsumption() {
        super();
        toolContainer.setTitle(widgetName);
        srvAsync = new ServiceInvoker<>(GWT.create(EOD_Srv.class), EOD.class.getName());
        handleEvent();
        gridEvents();
    }

    private void handleEvent() {
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnPosting.addClickHandler(this);
        btnDelete.addClickHandler(this);
    }

    private void getPendingDates() {
        try {
            srvAsync.invokeRPC().getLCPendingDates(forDate, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                    grdPendingDate.getGrid().getStore().clear();
                    if (result.isStatus()) {
                        grdPendingDate.getGrid().getStore().addAll(result.getData());
                        getPostingItemDetail();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getPostingItemDetail() {
        try {
            srvAsync.invokeRPC().getLCPostingDetails(forDate, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<LabConsumptionPosting_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<LabConsumptionPosting_Mdl> result) {
                    grdLCPostingDetail.getGrid().getStore().clear();
                    grdLCDeleteDetail.getGrid().getStore().clear();
                    if (result.isStatus()) {
                        grdLCPostingDetail.getGrid().getStore().addAll(result.getData().getListAcPostingDetail());
                        grdLCDeleteDetail.getGrid().getStore().addAll(result.getData().getListAcPostedDetail());
                        btnPosting.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getDeleteItemDetail() {
        try {
            srvAsync.invokeRPC().getLCDeleteDetails(forDate, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<List<GrdLabConsumptionDetail_Mdl>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdLabConsumptionDetail_Mdl>> result) {
                    grdLCDeleteDetail.getGrid().getStore().clear();
                    if (result.isStatus()) {
                        grdLCDeleteDetail.getGrid().getStore().addAll(result.getData());
                        btnDelete.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private AcPosting_Mdl saveInitialize() {
        AcPosting_Mdl mrTemp = new AcPosting_Mdl();

        mrTemp.setForDate(DmShared.dNull(txtForDate.getText()));

        return mrTemp;
    }

    private void save() {
        AcPosting_Mdl mrTemp;

        try {
            mrTemp = saveInitialize();
            AcPosting_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveLCRecord(forDate, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdLCPostingDetail.getGrid().getStore().clear();
                        getPendingDates();
                    } else {
                        error(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void delete() {
        try {
            srvAsync.invokeRPC().deleteLC(forDate, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdLCDeleteDetail.getGrid().getStore().clear();
                        hide();
                    } else {
                        error(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void error(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, txtForDate).show();
        }
    }

}
