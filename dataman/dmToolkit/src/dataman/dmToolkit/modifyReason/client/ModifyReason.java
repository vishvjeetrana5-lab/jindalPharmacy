package dataman.dmToolkit.modifyReason.client;

import dataman.dmToolkit.modifyReason.shared.ModifyReason_SrvAsync;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Srv;
import dataman.dmToolkit.modifyReason.shared.GrdCancelReason_Mdl;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class ModifyReason extends ModifyReason_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(ModifyReason_Mdl modifyReason);
    }
    private String mode;
    private CallBack callBack;
    private ServiceInvoker<ModifyReason_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String mode, String primaryId) {
        this.mode = mode;
        showPopup(mode, primaryId, "", 0);
    }

    public void show(String mode, String primaryId, String tableName) {
        this.mode = mode;
        showPopup(mode, primaryId, tableName, 0);
    }

    public void show(String mode, String primaryId, long secondaryId) {
        this.mode = mode;
        showPopup(mode, primaryId, "", secondaryId);
    }

    public void show(String mode, String primaryId, String tableName, long secondaryId) {
        this.mode = mode;
        showPopup(mode, primaryId, tableName, secondaryId);
    }

    private void showPopup(String mode, String primaryId, String tableName, long secondaryId) {
        if (!DmShared.xNull(mode).trim().equalsIgnoreCase("New")) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            txtHReason.setTag("");
            txtHReason.setText("");
            txtRemark.setText("");
            txtRemark.setTag("");
            txtHReason.setFocus(true);
            fillGrid(primaryId, tableName, secondaryId);
        }
    }

    public void showNew(String mode, String keyId_1, String keyId_2, String tableName) {
        this.mode = mode;
        if (!DmShared.xNull(mode).trim().equalsIgnoreCase("New")) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            txtHReason.setTag("");
            txtHReason.setText("");
            txtRemark.setText("");
            txtRemark.setTag("");
            txtHReason.setFocus(true);
            getReasonDetails(keyId_1, keyId_2, tableName);
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {
        txtHReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(this.mode).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Mode is mandatory.", txtHReason).show();
                return;
            }
            if (DmShared.xNull(txtHReason.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Reason is mandatory.", txtHReason).show();
                return;
            }
            if (DmShared.removeSpace(DmShared.xNull(txtRemark.getText())).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Remark is mandatory.", txtRemark).show();
                return;
            }

            ModifyReason_Mdl mrTemp = new ModifyReason_Mdl();

            mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHReason.getTag()));
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(DmShared.xNull(txtHReason.getText())));
            mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
            mrTemp.setMode(DmShared.xNull(this.mode).trim().equalsIgnoreCase("Browse") ? "Delete" : this.mode);
            callBack.onOk(mrTemp);
            hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
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

    public ModifyReason() {
        super();
        srvAsync = new ServiceInvoker<ModifyReason_SrvAsync>(GWT.create(ModifyReason_Srv.class), ModifyReason.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void fillGrid(String docId, String tableName, long sNo) {
        srvAsync.invokeRPC().getEditResion(docId, tableName, sNo, new AsyncCallback<DmResult<List<GrdCancelReason_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox("Reason Not Found.", txtHReason).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCancelReason_Mdl>> result) {
                if (result.isStatus()) {
                    grid.getGrid().getStore().clear();
                    grid.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHReason).show();
                }
            }
        });
    }

    private void getReasonDetails(String keyId_1, String keyId_2, String tableName) {
        srvAsync.invokeRPC().getReasonDetails(keyId_1, keyId_2, tableName, new AsyncCallback<DmResult<List<GrdCancelReason_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox("Reason Not Found.", txtHReason).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCancelReason_Mdl>> result) {
                if (result.isStatus()) {
                    grid.getGrid().getStore().clear();
                    grid.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHReason).show();
                }
            }
        });
    }
}
