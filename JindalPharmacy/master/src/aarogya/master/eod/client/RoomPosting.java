package aarogya.master.eod.client;

import aarogya.master.eod.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public class RoomPosting extends RoomPosting_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onClose();
    }

    private CallBack callBack;
    private ServiceInvoker<EOD_SrvAsync> srvAsync;
    private List<KeyFieldExtended> listRegistrationDetail = new ArrayList<KeyFieldExtended>();
    private String regDocId = "", fromDate = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String fromDate, String regDocId, boolean isDelete) {
        this.regDocId = regDocId;
        this.fromDate = fromDate;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdRoomPostingDetail.getGrid().getStore().clear();
        grdRoomPostingDelete.getGrid().getStore().clear();
        txtForDate.setText(DmShared.dNull(fromDate));

        flwPosting.clear();
        flwDelete.clear();
        flwWorkingArea.clear();
        if (isDelete) {
            flwWorkingArea.add(deleteArea());
            flwPosting.add(vl.add(btnDelete));
            getDeleteDetail(regDocId);
        } else {
            flwWorkingArea.add(postingArea());
            flwPosting.add(vl.add(btnPosting));
            getServiceDetail(regDocId);
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

    private void gridEvents() {
        grdRoomPostingDelete.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == 0) {
                    int val = DmShared.vNull(grdRoomPostingDelete.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                    if (val == 0) {
                        grdRoomPostingDelete.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                    } else if (val == 1) {
                        grdRoomPostingDelete.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                    }
                    grdRoomPostingDelete.commitChanges();
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide();
        } else if (event.getSource() == btnPosting) {
            new DmConfirmMessageBox("Are you sure? You want to post.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        save();
                    }
                }
            }).show();
        } else if (event.getSource() == btnDelete) {
            new DmConfirmMessageBox("Are you sure? You want to delete.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        onDelete();
                    }
                }
            }).show();
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

    public RoomPosting() {
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

    private void getServiceDetail(String regDocId) {
        try {
            srvAsync.invokeRPC().getRPRoomDetails(DmShared.dNull(txtForDate.getText()), regDocId, new AsyncCallback<DmResult<RoomPosting_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<RoomPosting_Mdl> result) {
                    if (result.isStatus()) {
                        listRegistrationDetail.clear();
                        listRegistrationDetail = result.getData().getListRegistrationDetail();
                        grdRoomPostingDetail.getGrid().getStore().clear();
                        grdRoomPostingDetail.getGrid().getStore().addAll(result.getData().getListServiceDetail());
                        btnPosting.setFocus(true);
                    } else {
                        listRegistrationDetail.clear();
                        grdRoomPostingDetail.getGrid().getStore().clear();
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getDeleteDetail(String regDocId) {
        try {
            srvAsync.invokeRPC().getRPDeleteDetails(DmShared.dNull(txtForDate.getText()), regDocId, new AsyncCallback<DmResult<List<GrdServiceDetail_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdServiceDetail_Mdl>> result) {
                    if (result.isStatus()) {
                        grdRoomPostingDelete.getGrid().getStore().clear();
                        grdRoomPostingDelete.getGrid().getStore().addAll(result.getData());
                        btnDelete.setFocus(true);
                    } else {
                        grdRoomPostingDelete.getGrid().getStore().clear();
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void save() {
        try {
            srvAsync.invokeRPC().saveRPRecord(fromDate, regDocId, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdRoomPostingDetail.getGrid().getStore().clear();
                        listRegistrationDetail.clear();
                        new DmAlertMessageBox("Record save successfully.", btnClose).show();
                    } else {
                        error(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void onDelete() {
        List<KeyFieldExtended> listTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended kf;

        for (GrdServiceDetail_Mdl detail_Mdl : grdRoomPostingDelete.getGrid().getStore().getAll()) {
            if ((DmShared.iNull(detail_Mdl.getServiceDocId()).longValue() != 0) && (DmShared.iNull(detail_Mdl.getServiceVSNo()).longValue() != 0)) {
                if (DmShared.iNull(detail_Mdl.getIsChecked()).intValue() == 1) {
                    kf = new KeyFieldExtended();
                    kf.setSharedKeyFieldCode(DmShared.xNull(detail_Mdl.getServiceDocId()));
                    kf.setSharedKeyFieldName(DmShared.xNull(detail_Mdl.getServiceVSNo()));
                    listTemp.add(kf);
                }
            }
        }

        if (listTemp.size() > 0) {
            delete(listTemp);
        } else {
            new DmAlertMessageBox("Select atleast one record.", txtForDate).show();
            return;
        }

    }

    private void delete(List<KeyFieldExtended> listRPDetail) {
        try {
            srvAsync.invokeRPC().deleteRPRecord(fromDate, listRPDetail, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdRoomPostingDelete.getGrid().getStore().clear();
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
            case 100:
                new DmAlertMessageBox(errorMessage, txtForDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtForDate).show();
        }
    }

}
