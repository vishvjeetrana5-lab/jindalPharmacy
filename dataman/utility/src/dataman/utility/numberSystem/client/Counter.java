package dataman.utility.numberSystem.client;

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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.utility.numberSystem.shared.GrdCounter_Mdl;
import dataman.utility.numberSystem.shared.NumberSystem_Srv;
import dataman.utility.numberSystem.shared.NumberSystem_SrvAsync;
import java.util.List;

public class Counter extends Counter_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private CallBack callBack;
    private String modeforpopup;
    private ServiceInvoker<NumberSystem_SrvAsync> SrvAsync;

    private NumberSystem objNumberSystem;
    private String nsGroup = "";

    private final CounterGridEdit<GrdCounter_Mdl, Grd_CounterEntry> grdpopup = new CounterGridEdit(CounterGrd);

    public interface CallBack {

        public void onOk(List<GrdCounter_Mdl> keyFields);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String strKey, NumberSystem objNumberSys) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        modeforpopup = objNumberSys.topControl.getMode();
        objNumberSystem = objNumberSys;
        nsGroup = strKey;
        filldata(strKey);
    }

    public void filldata(String strKey) {
        SrvAsync.invokeRPC().getCountList(strKey, new AsyncCallback<DmResult<List<GrdCounter_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCounter_Mdl>> result) {
                CounterGrd.getGrid().getStore().clear();
                CounterGrd.getGrid().getStore().addAll(result.getData());
                if (!modeforpopup.equalsIgnoreCase("Browse")) {
                    CounterGrd.getGrid().getStore().add(new GrdCounter_Mdl());
                    int last = CounterGrd.getGrid().getStore().getAll().size();
                    ((CellSelectionModel) CounterGrd.getGrid().getSelectionModel()).selectCell(last - 1, 1);
                }
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
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

    public Counter() {
        super();
        SrvAsync = new ServiceInvoker<NumberSystem_SrvAsync>(GWT.create(NumberSystem_Srv.class), NumberSystem.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        gridEventsCounter();
        vrtInnerForm.addAttachHandler(this);
    }

    private void gridEventsCounter() {
        grdpopup.addCallBack(new CounterGridEdit.CallBack() {
            @Override
            public void onDelete(GrdCounter_Mdl linkedUserGrid) {
                CounterGrd.commitChanges();
                SrvAsync.invokeRPC().deleteRecordPopUp(linkedUserGrid, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        return;
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            filldata(nsGroup);
                        }
                    }
                });

            }

            @Override
            public void onOk(GrdCounter_Mdl linkedUserGrid) {
                CounterGrd.commitChanges();

                SrvAsync.invokeRPC().InsertRecordPopUp(linkedUserGrid, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            filldata(nsGroup);
                        }
                    }
                });
            }
        });

        CounterGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEditComposition(event.getRowIndex());
            }
        });

        CounterGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGridEditComposition(CounterGrd.getGrid().getStore().indexOf(CounterGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGridEditComposition(int currentRowIndex) {
        if (modeforpopup.equalsIgnoreCase("Browse")) {
            grdpopup.show(currentRowIndex, false, false, false, objNumberSystem);
        } else {
            grdpopup.show(currentRowIndex, true, true, true, objNumberSystem);
        }
    }
}
