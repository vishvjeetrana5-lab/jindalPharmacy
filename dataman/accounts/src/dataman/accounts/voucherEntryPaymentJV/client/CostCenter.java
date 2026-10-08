package dataman.accounts.voucherEntryPaymentJV.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class CostCenter extends CostCenter_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk(List<CostCenterGrid_Mdl> costCenterGridMdl);

        public void onClose();
    }

    CallBack callBack;
    CostCenterGridEdit<GrdVoucherEntryPaymentJV_Mdl, VoucherEntryPaymentJV_Dsg.Grd_PrpAcs> costCenterGridEdit = new CostCenterGridEdit(grid);
    boolean editable;

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            costCenterGridEdit.hide();

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

    private void gridEvents() {
        costCenterGridEdit.addCallBack(new CostCenterGridEdit.CallBack() {

            @Override
            public void onOk(CostCenterGrid_Mdl costCenterGrid) {
                calculate();
                grid.commitChanges();
            }

            @Override
            public void onDelete() {
                calculate();
                grid.commitChanges();
            }

        });

        grid.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showCostCenterGridEdit(event.getRowIndex());
            }
        });

        grid.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCostCenterGridEdit(grid.getGrid().getStore().indexOf(grid.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(List<CostCenterGrid_Mdl> costCenterGridMdl, String amount, boolean editable) {
        CostCenterGrid_Mdl data;
        List<CostCenterGrid_Mdl> dataList = new ArrayList<CostCenterGrid_Mdl>();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        this.editable = editable;
        lblAmount.setText(amount);
        dataList.addAll(costCenterGridMdl);

        grid.getGrid().getStore().clear();
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                if (!DmShared.xNull(dataList.get(i).getCode()).trim().equalsIgnoreCase("")) {
                    data = new CostCenterGrid_Mdl();
                    data.setCode(DmShared.xNull(dataList.get(i).getCode()));
                    data.setName(DmShared.xNull(dataList.get(i).getName()));
                    data.setAmount(dataList.get(i).getAmount());
                    grid.getGrid().getStore().add(data);
                }
            }
        }
        grid.getGrid().getStore().add(new CostCenterGrid_Mdl());

        setVisibleOk(editable);
        calculate();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

    }

    public void setVisibleOk(boolean visible) {
        flwOk.clear();
        flwOk.setVisible(false);

        if (visible) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
    }

    public CostCenter() {
        super();
        gridEvents();
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    private void showCostCenterGridEdit(int currentRowIndex) {
        if (editable) {
            costCenterGridEdit.show(currentRowIndex, true, true, true, lblPending.getText());
        }
    }

    public void calculate() {
        List<CostCenterGrid_Mdl> gridData;
        double amount = 0;

        gridData = grid.getGrid().getStore().getAll();
        for (int i = 0; i < gridData.size(); i++) {
            if (!DmShared.xNull(gridData.get(i).getCode()).trim().equalsIgnoreCase("")) {
                amount = amount + DmShared.vNull(gridData.get(i).getAmount());
            }
        }

        lblTotal.setText(DmClient.vNull(amount, "0.00"));
        lblPending.setText(DmClient.vNull(DmShared.vNull(lblAmount.getText()) - DmShared.vNull(lblTotal.getText()), "0.00"));
    }

    public void hide() {
        popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        CostCenterGrid_Mdl data;
        List<CostCenterGrid_Mdl> dataList;

        if (event.getSource() == btnCancel) {
            hide();
            callBack.onClose();
        } else if (event.getSource() == btnOk) {
            calculate();
            if (DmShared.vNull(lblPending.getText()).doubleValue() != 0 && DmShared.vNull(lblTotal.getText()).doubleValue() != 0) {
                new DmAlertMessageBox("Please bifurcate full value.", (DmTextBox) null).show();
                return;
            }

            dataList = new ArrayList<>();
            for (int i = 0; i < grid.getGrid().getStore().getAll().size(); i++) {
                data = new CostCenterGrid_Mdl();
                if (!DmShared.xNull(grid.getGrid().getStore().get(i).getCode()).equalsIgnoreCase("")) {
                    data.setCode(DmShared.xNull(grid.getGrid().getStore().get(i).getCode()));
                    data.setName(DmShared.xNull(grid.getGrid().getStore().get(i).getName()));
                    data.setAmount(DmClient.vNull(grid.getGrid().getStore().get(i).getAmount(), "0.00"));
                    dataList.add(data);
                }
            }

            callBack.onOk(dataList);
            hide();
        }
    }
}
