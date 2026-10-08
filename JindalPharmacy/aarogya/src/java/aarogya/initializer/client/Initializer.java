package aarogya.initializer.client;

import aarogya.initializer.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import java.util.List;

public class Initializer extends Initializer_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<Initializer_SrvAsync> srvAsync;

    public void show() {
        popupPanel.show();
        popupPanel.setGlassEnabled(true);
        popupPanel.center();
        getInitializer();
    }

    private void gridEvents() {
        grdInitializer.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == 3) {
                    grdInitializer.lockScroll();
                    GrdInitializer_Mdl grdMdl = grdInitializer.getGrid().getStore().get(event.getRowIndex());
                    grdInitializer.releaseScroll();
                }
            }
        });
        grdInitializer.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    grdInitializer.lockScroll();
                    try {
                        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE && !event.getNativeEvent().getCtrlKey()) {
                            int colIndex = ((CellSelectionModel) grdInitializer.getGrid().getSelectionModel()).getSelectCell().getCell();
                            int rowIndex = grdInitializer.getGrid().getStore().indexOf(grdInitializer.getGrid().getSelectionModel().getSelectedItem());

                        }
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                    } finally {
                        grdInitializer.releaseScroll();
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void getInitializer() {
        srvAsync.invokeRPC(true).getInitializerClass(DmClient.getStartDate(), new AsyncCallback<DmResult<List<GrdInitializer_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdInitializer_Mdl>> result) {
                if (result.isStatus()) {
                    grdInitializer.getGrid().getStore().clear();
                    grdInitializer.getGrid().getStore().addAll(result.getData());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    public Initializer() {
        super();
        srvAsync = new ServiceInvoker<Initializer_SrvAsync>(GWT.create(Initializer_Srv.class), Initializer.class.getName());
        handlerEvents();
        gridEvents();
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            hide();
        } else if (event.getSource().equals(btnOk)) {
            new DmConfirmMessageBox("Are you sure? You want to run Initializer.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        execute(0);
                    }
                }
            }).show();
        }
    }

    private void execute(final int index) {
        GrdInitializer_Mdl node = null;
        if (grdInitializer.getGrid().getStore().size() > index) {
            node = grdInitializer.getGrid().getStore().get(index);
            grdInitializer.getGrid().getView().focusCell(index, 3, true);
            grdInitializer.lockScroll();
        }
        if (node != null) {
            srvAsync.invokeRPC().execute(node, new AsyncCallback<DmResult<GrdInitializer_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnOk).show();
                }

                @Override
                public void onSuccess(DmResult<GrdInitializer_Mdl> result) {
                    GrdInitializer_Mdl localNode = null, resultNode = null;
                    if (result.isStatus()) {
                        resultNode = result.getData();
                        localNode = grdInitializer.getGrid().getStore().get(index);
                        localNode.setLogFilePath(resultNode.getLogFilePath());
                        localNode.setImportStatus(resultNode.getImportStatus());
                        grdInitializer.getGrid().getStore().update(localNode);
                        grdInitializer.commitChanges();
                        grdInitializer.getGrid().getView().refresh(true);
                        grdInitializer.releaseScroll();
                        if (resultNode.getImportStatus() != DmImportStatus.ERROR) {
                            execute(index + 1);
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), btnOk).show();
                    }
                }
            });
        } else {
            srvAsync.invokeRPC().getDefaultUHID(new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
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
            hide();
        }
    }

    private void hide() {
        this.popupPanel.hide();
    }
}
