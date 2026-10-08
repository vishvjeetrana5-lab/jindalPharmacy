package aarogya.dashBoard.favorite.client;

import aarogya.dashBoard.favorite.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class Manage<T1, T2> extends Manage_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onClose();
    }

    CallBack callBack;
    ServiceInvoker<Favorite_SrvAsync> SrvAsync;
    private Favorite_Mdl dashboard_mdl = new Favorite_Mdl();
    final SetPosition setPoitionPopup = new SetPosition();
    private String moduleName = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        fillGrd();
    }

    private void fillGrd() {
        grdAllMenu.getGrid().getStore().clear();
        grdFavouriteMenu.getGrid().getStore().clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                grdAllMenu.getGrid().getColumnModel().getColumn(1).setHeader("...");
                grdAllMenu.getGrid().getStore().commitChanges();
                grdAllMenu.getGrid().getView().refresh(true);
                grdFavouriteMenu.getGrid().getColumnModel().getColumn(1).setHeader("...");
                grdFavouriteMenu.getGrid().getStore().commitChanges();
                grdFavouriteMenu.getGrid().getView().refresh(true);
            }
        });
        fillGrdAllModule();
    }

    private void grdEvent() {
        grdAllModule.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                dashboard_mdl.setAllModuleName(grdAllModule.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                dashboard_mdl.setFavModuleName(grdAllModule.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        grdAllMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(dashboard_mdl.getAllModuleName()));
                        grdAllMenu.getGrid().getStore().commitChanges();
                        grdAllMenu.getGrid().getView().refresh(true);
                    }
                });
                fillGrdAllMenu(dashboard_mdl.getAllModuleName());
                moduleName = dashboard_mdl.getAllModuleName();
            }
        });
        grdFavouriteModule.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                dashboard_mdl.setFavModuleName(grdFavouriteModule.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        grdFavouriteMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(dashboard_mdl.getFavModuleName()));
                        grdFavouriteMenu.getGrid().getStore().commitChanges();
                        grdFavouriteMenu.getGrid().getView().refresh(true);
                    }
                });
                fillGrdFavMenu(dashboard_mdl.getFavModuleName());
                moduleName = dashboard_mdl.getFavModuleName();
            }
        });
        grdAllMenu.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                dashboard_mdl.setAllMenuName(grdAllMenu.getGrid().getStore().get(event.getRowIndex()).getMenuName());
                dashboard_mdl.setAllMenuText(grdAllMenu.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                moduleName = dashboard_mdl.getAllModuleName();
            }
        });
        grdFavouriteMenu.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                dashboard_mdl.setFavMenuName(grdFavouriteMenu.getGrid().getStore().get(event.getRowIndex()).getMenuName());
                dashboard_mdl.setFavMenuText(grdFavouriteMenu.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                moduleName = dashboard_mdl.getFavModuleName();
            }
        });
    }

    private void fillGrdAllModule() {
        try {
            SrvAsync.invokeRPC().getModuleList(Favorite_Mdl.allModule, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                    if (result.isStatus()) {
                        grdAllModule.getGrid().getStore().clear();
                        grdAllModule.getGrid().getStore().addAll(result.getData());
                        grdAllModule.commitChanges();
                        fillGrdFavModule();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            throw new DmException(100, "");
        }
    }

    private void fillGrdAllMenu(String moduleName) {
        try {
            SrvAsync.invokeRPC().getMenuList(Favorite_Mdl.allMenu, moduleName, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
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

    private void fillGrdFavModule() {
        try {
            SrvAsync.invokeRPC().getModuleList(Favorite_Mdl.favModule, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                    if (result.isStatus()) {
                        grdFavouriteModule.getGrid().getStore().clear();
                        grdFavouriteModule.getGrid().getStore().addAll(result.getData());
                        grdAllModule.commitChanges();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            throw new DmException(100, "");
        }
    }

    private void fillGrdFavMenu(String moduleName) {
        try {
            SrvAsync.invokeRPC().getMenuList(Favorite_Mdl.favMenu, moduleName, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                    if (result.isStatus()) {
                        grdFavouriteMenu.getGrid().getStore().clear();
                        grdFavouriteMenu.getGrid().getStore().addAll(result.getData());
                        grdFavouriteMenu.commitChanges();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            throw new DmException(100, "");
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            clear();
            callBack.onClose();
        } else if (event.getSource() == btnRight) {
            insertRecord();
        } else if (event.getSource() == btnLeft) {
            deleteRecord();
        } else if (event.getSource() == setPositionMenu) {
            if (dashboard_mdl.getFavModuleName() == null || dashboard_mdl.getFavModuleName().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select module to open menu.", btnCancel).show();
                return;
            }
            setPoitionPopup.show(true, dashboard_mdl, dashboard_mdl.grdMenu, moduleName);
        } else if (event.getSource() == setPositionModule) {

            setPoitionPopup.show(true, dashboard_mdl, dashboard_mdl.grdModule, moduleName);
        }
    }

    private void insertRecord() {
        SrvAsync.invokeRPC().insertRecord(dashboard_mdl, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnRight).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    fillGrdFavModule();
                    fillGrdFavMenu(dashboard_mdl.getAllModuleName());
                    grdFavouriteMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(dashboard_mdl.getAllModuleName()));
                    grdFavouriteMenu.getGrid().getStore().commitChanges();
                    grdFavouriteMenu.getGrid().getView().refresh(true);
                    dashboard_mdl.setAllMenuText("");
                } else {
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }
            }
        });
    }

    private void deleteRecord() {
        SrvAsync.invokeRPC().deleteRecord(dashboard_mdl, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnRight).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    fillGrdFavModule();
                    fillGrdFavMenu(dashboard_mdl.getFavModuleName());
                    grdFavouriteMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(dashboard_mdl.getFavModuleName()));
                    grdFavouriteMenu.getGrid().getStore().commitChanges();
                    grdFavouriteMenu.getGrid().getView().refresh(true);
                    dashboard_mdl.setFavMenuText("");
                } else {
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }
            }
        });
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, btnRight).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, btnRight).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, btnRight).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, btnRight).show();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            setPoitionPopup.hide();
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public Manage() {
        super();
        SrvAsync = new ServiceInvoker<Favorite_SrvAsync>(GWT.create(Favorite_Srv.class), Favorite.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Manage Favorite");
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        grdEvent();
        manageEvent();
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnRight.addClickHandler(this);
        btnLeft.addClickHandler(this);
        setPositionMenu.addClickHandler(this);
        setPositionModule.addClickHandler(this);

        setPoitionPopup.addCallBack(new SetPosition.CallBack() {

            @Override
            public void onOk() {
                fillGrdFavModule();
                if (moduleName != "") {
                    fillGrdFavMenu(moduleName);
                }

            }
        });
    }

    private void clear() {
        dashboard_mdl.setAllMenuName("");
        dashboard_mdl.setAllMenuText("");
        dashboard_mdl.setAllModuleName("");
        dashboard_mdl.setFavMenuName("");
        dashboard_mdl.setFavMenuText("");
        dashboard_mdl.setFavModuleName("");
        dashboard_mdl.setGrdType("");
        moduleName = "";
    }
}
