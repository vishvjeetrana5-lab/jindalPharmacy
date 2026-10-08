package dataman.utility.menuCodification.client;

import dataman.utility.menuCodification.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import java.util.HashMap;
import java.util.List;

public class MenuCodification extends MenuCodification_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<MenuCodification_SrvAsync> SrvAsync;
    private final MenuCodificationGridEdit<GrdDescription_Mdl, GrdAllMenu_PrpAcs> gridEdit = new MenuCodificationGridEdit(grdAllMenu);
    private String moduleName = "";

    public MenuCodification(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(MenuCodification_Srv.class), MenuCodification.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        btnClose.addClickHandler(this);
        btnExport.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        radioMenu.addClickHandler(this);
        radioMenuCode.addClickHandler(this);
        flwForm.addAttachHandler(this);
        gridEvents();
        onLoad(swi);
    }

    private void fillGrdAllModule(final SetWidgetInvoker swi) {
        try {
            SrvAsync.invokeRPC().getModuleList("", new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                    if (result.isStatus()) {
                        grdAllModule.getGrid().getStore().clear();
                        grdAllModule.getGrid().getStore().addAll(result.getData());
                        grdAllModule.commitChanges();
                        swi.setWidget(MenuCodification.this);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            throw new DmException(100, "");
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MenuCodification.this);
                }
            }
        }).show();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void gridEvents() {
        grdAllModule.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                moduleName = (grdAllModule.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        grdAllMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(moduleName));
                        grdAllMenu.getGrid().getStore().commitChanges();
                        grdAllMenu.getGrid().getView().refresh(true);
                    }
                });
                if (radioMenu.getValue().equals(true)) {
                    fillGrdAllMenu("M", moduleName);
                } else if (radioMenuCode.getValue().equals(true)) {
                    fillGrdAllMenu("C", moduleName);
                }

            }
        });
        grdAllModule.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    moduleName = grdAllMenu.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldName();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            grdAllMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(moduleName));
                            grdAllMenu.getGrid().getStore().commitChanges();
                            grdAllMenu.getGrid().getView().refresh(true);
                        }
                    });
                    if (radioMenu.getValue().equals(true)) {
                        fillGrdAllMenu("M", moduleName);
                    } else if (radioMenuCode.getValue().equals(true)) {
                        fillGrdAllMenu("C", moduleName);
                    }
                }
            }
        }, KeyDownEvent.getType());
        grdAllMenu.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGrid(event.getRowIndex());

            }
        });
        grdAllMenu.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int row = 0;

                    row = grdAllMenu.getGrid().getStore().indexOf(grdAllMenu.getGrid().getSelectionModel().getSelectedItem());
                    showGrid(row);
                }
            }
        }, KeyDownEvent.getType());
        grdAllMenu.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                //  gridEduEdit(event.getRowIndex());
            }
        });
        gridEdit.addCallBack(new MenuCodificationGridEdit.CallBack() {

            @Override
            public void onOk(GrdDescription_Mdl eduquali) {
                grdAllMenu.commitChanges();
//                topControlMoveRec("");
            }

            @Override
            public void onDelete() {
                grdAllMenu.commitChanges();
            }
        });
    }

    private void fillGrdAllMenu(String sorting, String moduleName) {
        try {
            SrvAsync.invokeRPC().getMenuList(sorting, moduleName, new AsyncCallback<DmResult<List<GrdDescription_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdDescription_Mdl>> result) {
                    if (result.isStatus()) {
                        grdAllMenu.getGrid().getStore().clear();
                        grdAllMenu.getGrid().getStore().addAll(result.getData());
                        grdAllMenu.commitChanges();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            throw new DmException(100, "");
        }
    }

    private void showGrid(int index) {
        gridEdit.show(index, true, true, true, moduleName);
    }

    private void onLoad(final SetWidgetInvoker swi) {
        moduleName = "";
        radioMenu.setValue(true);
        fillGrdAllModule(swi);
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnClose) {
                onClose();
            } else if (event.getSource() == btnRefresh) {
                if (radioMenu.getValue().equals(true)) {
                    fillGrdAllMenu("M", moduleName);
                } else if (radioMenuCode.getValue().equals(true)) {
                    fillGrdAllMenu("C", moduleName);
                }
            } else if (event.getSource() == radioMenu) {
                fillGrdAllMenu("M", moduleName);
            } else if (event.getSource() == radioMenuCode) {
                fillGrdAllMenu("C", moduleName);
            } else if (event.getSource().equals(btnExport)) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportCSV();
                        }
                    }
                }).show();
            }
        } catch (Exception e) {
        }
    }

    private void exportCSV() {
        SrvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                }
            }
        });
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
}
