package dataman.dmToolkit.walkThrough.client;

import dataman.dmToolkit.walkThrough.shared.*;
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
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class WalkThrough extends WalkThrough_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onClose();
    }

    CallBack callBack;
    ServiceInvoker<? extends IWalkThrough_SrvAsync> srvWalkThroughAsync;
    ServiceInvoker<WalkThrough_SrvAsync> srvAsync;
    private String wtaCode = "";
    private WalkThroughEdit walkThroughPopupEdit = new WalkThroughEdit();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String wtaCode) {
        this.wtaCode = wtaCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdWalkthrough.getGrid().getStore().clear();
        fillWalkThrough();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {

        walkThroughPopupEdit.addCallBack(new WalkThroughEdit.CallBack() {
            @Override
            public void onOk(WalkThrough_Mdl grdWalkThrough_Mdl) {
                if (grdWalkThrough_Mdl != null) {
                    saveRecord(grdWalkThrough_Mdl);
                }
            }

            @Override
            public void onClose() {
                ((CellSelectionModel) grdWalkthrough.getGrid().getSelectionModel()).selectCell(0, 0);
            }
        });

    }

    private void gridEvents() {

        grdWalkthrough.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 5) {
                    WalkThrough_Mdl mrTemp = grdWalkthrough.getGrid().getStore().get(event.getRowIndex());
                    if (DmShared.iNull(mrTemp.getStatus()).intValue() == 0) {
                        mrTemp.setAccept(true);
                        saveRecord(mrTemp);
                    }
                } else {
                    showWalkthroughGridEdit(event.getRowIndex());
                }
            }
        });

        grdWalkthrough.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showWalkthroughGridEdit(grdWalkthrough.getGrid().getStore().indexOf(grdWalkthrough.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showWalkthroughGridEdit(int rowIndex) {
        WalkThrough_Mdl walkThroughDetail_Mdl;

        if (rowIndex == 0) {
            walkThroughDetail_Mdl = grdWalkthrough.getGrid().getStore().getAll().get(rowIndex);
            walkThroughPopupEdit.show(walkThroughDetail_Mdl);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.onClose();
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

    public WalkThrough(ServiceInvoker<? extends IWalkThrough_SrvAsync> srvWalkThroughAsync) {
        super();
        this.srvWalkThroughAsync = srvWalkThroughAsync;
        srvAsync = new ServiceInvoker<WalkThrough_SrvAsync>(GWT.create(WalkThrough_Srv.class), WalkThrough.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        gridEvents();
        textHelpEvents();
    }

    private void fillWalkThrough() {
        srvAsync.invokeRPC().getWalkThroughDetail(wtaCode, new AsyncCallback<DmResult<List<WalkThrough_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<WalkThrough_Mdl>> result) {
                if (result.isStatus()) {
                    grdWalkthrough.getGrid().getStore().clear();
                    grdWalkthrough.getGrid().getStore().addAll(result.getData());
                    ((CellSelectionModel) grdWalkthrough.getGrid().getSelectionModel()).selectCell(0, 0);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void saveRecord(WalkThrough_Mdl walkThrough) {
        srvWalkThroughAsync.invokeRPC().walkThrough(walkThrough, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    fillWalkThrough();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
