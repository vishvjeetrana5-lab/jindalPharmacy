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
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public class SetPosition<T1, T2> extends SetPosition_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk();
    }

    CallBack callBack;
    ServiceInvoker<Favorite_SrvAsync> SrvAsync;
    private Favorite_Mdl dashboard_mdl = new Favorite_Mdl();
    private List<GrdModuleMenu_Mdl> listNode = new ArrayList<GrdModuleMenu_Mdl>();
    private String moduleName;
    private int rowIndex = 0;
    boolean isDown = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void grdEvent() {
        grdMenu.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                dashboard_mdl.setAllModuleName(grdMenu.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
            }
        });
    }

    public void show(boolean editable, Favorite_Mdl dash_Mdl, String grdType, String moduleType) {
        clear();
        this.moduleName = moduleType;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdMenu.getGrid().getStore().clear();
        if (grdType.equalsIgnoreCase(dash_Mdl.grdModule)) {
            fillGrdAllModule();
            dashboard_mdl.setGrdType(dashboard_mdl.favModule);
        } else if (grdType.equalsIgnoreCase(dash_Mdl.grdMenu)) {
            fillGrdMenu();
            dashboard_mdl.setFavModuleName(moduleType);
            dashboard_mdl.setGrdType(dashboard_mdl.favMenu);
        }
    }

    private void fillGrdMenu() {
        SrvAsync.invokeRPC().getMenuList(Favorite_Mdl.favMenu, moduleName, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                if (result.isStatus()) {
                    grdMenu.getGrid().getStore().clear();
                    grdMenu.getGrid().getStore().addAll(result.getData());
                    if (isDown) {
                        grdMenu.getGrid().setSelectionModel(new CellSelectionModel<GrdModuleMenu_Mdl>());
                        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(rowIndex + 1, 0);
                    } else {
                        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(rowIndex - 1, 0);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnUp).show();
                }
            }
        });
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                grdMenu.getGrid().getColumnModel().getColumn(1).setHeader(DmShared.xNull(dashboard_mdl.getFavModuleName()));
                grdMenu.getGrid().getStore().commitChanges();
                grdMenu.getGrid().getView().refresh(true);
            }
        });
    }

    private void fillGrdAllModule() {
        SrvAsync.invokeRPC().getModuleList(Favorite_Mdl.favModule, new AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdModuleMenu_Mdl>> result) {
                if (result.isStatus()) {
                    grdMenu.getGrid().getStore().clear();
                    grdMenu.getGrid().getStore().addAll(result.getData());
                    grdMenu.getGrid().getColumnModel().getColumn(1).setHeader("Module");
                    grdMenu.commitChanges();
                    if (isDown) {
                        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(rowIndex + 1, 0);
                    } else {
                        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(rowIndex - 1, 0);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                grdMenu.getGrid().getColumnModel().getColumn(1).setHeader("Module");
                grdMenu.getGrid().getStore().commitChanges();
                grdMenu.getGrid().getView().refresh(true);
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            saveGrdData();
            popupPanel.hide();
        } else if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnUp) {
            sorting(true);
        } else if (event.getSource() == btnDown) {
            sorting(false);
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
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public SetPosition() {
        super();
        SrvAsync = new ServiceInvoker<Favorite_SrvAsync>(GWT.create(Favorite_Srv.class), Favorite.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Set Position");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        grdEvent();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnUp.addClickHandler(this);
        btnDown.addClickHandler(this);
    }

    private void sorting(boolean isUp) {
        List<GrdModuleMenu_Mdl> listTemp = new ArrayList<GrdModuleMenu_Mdl>();

        isDown = !isUp;
        rowIndex = grdMenu.getGrid().getStore().indexOf(grdMenu.getGrid().getSelectionModel().getSelectedItem());

        int temp = 0;

        if (isUp) {
            temp = rowIndex - 1;
        } else {
            temp = rowIndex + 1;
        }

        for (int i = 0; i < grdMenu.getGrid().getStore().getAll().size(); i++) {
            GrdModuleMenu_Mdl mrTemp = grdMenu.getGrid().getStore().getAll().get(i);
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                if (i == rowIndex) {
                    listTemp.add(grdMenu.getGrid().getStore().getAll().get(temp));
                } else if (i == temp) {
                    listTemp.add(grdMenu.getGrid().getStore().getAll().get(rowIndex));
                } else {
                    listTemp.add(mrTemp);
                }
            }
        }
        listNode.clear();
        listNode.addAll(listTemp);
        grdMenu.getGrid().getStore().clear();
        grdMenu.getGrid().getStore().addAll(listTemp);
        grdMenu.commitChanges();
        ((CellSelectionModel) grdMenu.getGrid().getSelectionModel()).selectCell(temp, 1);
    }

    private void saveGrdData() {
        SrvAsync.invokeRPC().setPosition(listNode, dashboard_mdl, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnUp).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    if (result.getValue1().equalsIgnoreCase(Favorite_Mdl.favMenu)) {
                        fillGrdMenu();
                    } else if (result.getValue1().equalsIgnoreCase(Favorite_Mdl.favModule)) {
                        fillGrdAllModule();
                    }
                    callBack.onOk();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnUp).show();
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
    }
}
