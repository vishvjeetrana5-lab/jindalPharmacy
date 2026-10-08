package aarogya.master.foreignCurrencyExchange.client;

import aarogya.master.foreignCurrencyExchange.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class GridEditForeignCurrencyExchange<T1, T2> extends GridEditForeignCurrencyExchange_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdForeignCurrencyExchange_Mdl foreignCurrencyExchange_Mdl);

    }
    private CallBack callBack;

    public GridEditForeignCurrencyExchange(DmGrid<T1, T2> grid) {
        super(grid, "Currency Exchange", "400px", "90px");
    }

    public void addCallBack(GridEditForeignCurrencyExchange.CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        txtCurrencyType.setEnabled(false);
        txtBase.setEnabled(false);
        txtRate.setFocus(true);
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.vNull(txtRate.getText()).doubleValue() <= 0) {
                new DmAlertMessageBox("Converstion Rate less than or equal to zero can not be saved.", txtRate).show();
                return;
            }

            if (DmShared.vNull(txtRate.getText()).doubleValue() > 100000) {
                new DmAlertMessageBox("Converstion Rate not greater than 100000", txtRate).show();
                return;
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtBase).show();
        }
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onDelete() {
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageOk(T1 row) {
        GrdForeignCurrencyExchange_Mdl ourRow = (GrdForeignCurrencyExchange_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtCurrencyType.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtCurrencyType.getText()));
        ourRow.setBase(DmShared.xNull(txtBase.getText()));
        ourRow.setRate(DmShared.xNull(txtRate.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdForeignCurrencyExchange_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdForeignCurrencyExchange_Mdl ourRow = (GrdForeignCurrencyExchange_Mdl) row;

        txtCurrencyType.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtCurrencyType.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtBase.setText(DmShared.xNull(ourRow.getBase()));
        txtRate.setText(DmShared.xNull(ourRow.getRate()));

        txtRate.setFocus(true);
    }

}
