package dataman.dmToolkit.accountAdjustment.client;

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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.List;

public class HistoryPopup extends HistoryPopup_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void select(List<GrdAdjustment_Mdl> selections);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<AccountAdjustment_SrvAsync> srvAsync;
    private String adjDocId = "", adjV_SNo = "";

    public HistoryPopup() {
        super();
        srvAsync = new ServiceInvoker<AccountAdjustment_SrvAsync>(GWT.create(AccountAdjustment_Srv.class), AccountAdjustment.class.getName());
        manageEvents();
    }

    public void show(String adjDocId, String adjV_SNo) {
        this.adjDocId = adjDocId;
        this.adjV_SNo = adjV_SNo;
        if (!DmShared.xNull(adjDocId).trim().equalsIgnoreCase("") && !DmShared.xNull(adjV_SNo).trim().equalsIgnoreCase("")) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            fillGrid();
        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCallback) {
        popupPanel.hide();
        if (withCallback) {
            callBack.close();
        }
    }

    private void fillGrid() {
        srvAsync.invokeRPC(true).getHistory(adjDocId, adjV_SNo, new AsyncCallback<DmResult<List<GrdHistory_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnClose).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdHistory_Mdl>> result) {
                if (result.isStatus()) {
                    grdSearch.getGrid().getStore().clear();
                    grdSearch.getGrid().getStore().addAll(result.getData());
                    focusGrid();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnClose).show();
                }
            }
        });
    }

    private void focusGrid() {
        ((CellSelectionModel) grdSearch.getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void manageEvents() {
        gridEvents();
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
    }

    private void gridEvents() {
        grdSearch.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                showEntryPoint();
            }
        });

        grdSearch.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showEntryPoint();
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showEntryPoint() {
        new DmConfirmMessageBox("Are you sure? You want to view entry.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    GrdHistory_Mdl ghMdl = grdSearch.getGrid().getSelectionModel().getSelectedItem();
                    String docId = "", mnuText = "", mnuAttachedInModule = "";

                    if (ghMdl != null) {
                        if (!DmShared.xNull(ghMdl.getAdj_DocId()).trim().equalsIgnoreCase("")) {
                            docId = DmShared.xNull(ghMdl.getAdj_DocId());
                            mnuText = DmShared.xNull(ghMdl.getAdj_MnuText());
                            mnuAttachedInModule = DmShared.xNull(ghMdl.getAdj_MnuAttachedInModule());
                        } else {
                            docId = DmShared.xNull(ghMdl.getMain_DocId());
                            mnuText = DmShared.xNull(ghMdl.getMain_MnuText());
                            mnuAttachedInModule = DmShared.xNull(ghMdl.getMain_MnuAttachedInModule());
                        }

                        final String searchCode = docId;
                        if (!DmShared.xNull(mnuText).trim().equalsIgnoreCase("") && !DmShared.xNull(mnuAttachedInModule).trim().equalsIgnoreCase("")) {
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(mnuAttachedInModule, mnuText, null, null, new SetWidgetInvokerImpl() {
                                @Override
                                public void setWidget(final IsWidget wgtObject) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).searchRecord(searchCode);
                                }
                            });
                        }
                    }
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
