package dataman.dmToolkit.overHead.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class OverHeadGridEdit<T1, T2> extends OverHeadGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(OverHead_Mdl grdTdsEntry);

        public void onDelete();
    }

    private final OverHeadGridMAEdit<OverHeadMA_Mdl, OverHeadMA_PrpAcs> overHeadGridMAEdit = new OverHeadGridMAEdit(gridMA);
    private OverHead overHead;
    private CallBack callBack;
    private OverHead_Mdl parentRow;

    private void gridEvents() {

        overHeadGridMAEdit.addCallBack(new OverHeadGridMAEdit.CallBack() {
            @Override
            public void onOk(OverHeadMA_Mdl overHeadMA_Mdl) {
                gridMA.commitChanges();
            }

            @Override
            public void onDelete() {
                gridMA.commitChanges();
            }
        });

        gridMA.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showVoucherEntryGridEdit(event.getRowIndex());
            }
        });

        gridMA.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showVoucherEntryGridEdit(gridMA.getGrid().getStore().indexOf(gridMA.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void textHelpEvents() {

        txtAtRate.addBlurHandler(this);

        txtAmount.addBlurHandler(this);
    }

    private void setAmountToDefaultAc() {
        List<OverHeadMA_Mdl> overHeadMAList;
        int index = -1;

        if (!DmShared.xNull(parentRow.getDrCr()).trim().equalsIgnoreCase("")) {
            overHeadMAList = gridMA.getGrid().getStore().getAll();
            if (overHeadMAList != null) {
                for (int i = 0; i < overHeadMAList.size(); i++) {
                    if (!DmShared.xNull(overHeadMAList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                        if (index >= 0) {
                            index = -1;
                            break;
                        }
                        index = i;
                    }
                }
            }

            if (index >= 0) {
                gridMA.getGrid().getStore().get(index).setAmount(txtAmount.getText());
                gridMA.commitChanges();
            }
        }

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 0, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtAtRate) || event.getSource().equals(txtAmount)) {
            parentRow.setAtRate(DmClient.vNull(txtAtRate.getText(), "0.00"));
            parentRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));

            overHead.calculateSingleOH(parentRow, overHead.grossAmount);

            txtAtRate.setText(DmClient.vNull(parentRow.getAtRate(), "0.00"));
            txtAmount.setText(DmClient.vNull(parentRow.getAmount(), "0.00"));

            setAmountToDefaultAc();
        }
    }

    @Override
    public void displayRow(T1 row) {
        OverHead_Mdl ourRow = (OverHead_Mdl) row;
        OverHeadMA_Mdl overHeadMA_Mdl;
        final String drCR = ourRow != null ? ourRow.getDrCr() : "";
        parentRow = new OverHead_Mdl();

        if (ourRow != null) {
            parentRow = ourRow.clone();
        }

        txtOverHead.setEnabled(false);
        txtOverHead.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        if (DmShared.vNull(ourRow.getOverHeadAliasEditable()).doubleValue() == 1) {
            txtOverHeadAlias.setEnabled(true);
        } else {
            txtOverHeadAlias.setEnabled(false);
        }
        txtOverHeadAlias.setText(ourRow.getOverHeadAlias());

        if (DmShared.vNull(ourRow.getAtRateMandatory()).doubleValue() == 1) {
            txtAtRate.setMandatory(true);
        } else {
            txtAtRate.setMandatory(false);
        }

        if (DmShared.vNull(ourRow.getAtRateEditable()).doubleValue() == 0) {
            txtAtRate.setEnabled(false);
        } else {
            txtAtRate.setEnabled(true);
        }
        txtAtRate.setText(ourRow.getAtRate());

        if (DmShared.vNull(ourRow.getAmountMandatory()).doubleValue() == 1) {
            txtAmount.setMandatory(true);
        } else {
            txtAmount.setMandatory(false);
        }
        if (DmShared.vNull(ourRow.getAmountEditable()).doubleValue() == 0) {
            txtAmount.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
        }
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        SafeHtml headerText = new SafeHtml() {

            @Override
            public String asString() {
                return DmShared.xNull(drCR).trim().equalsIgnoreCase("D") ? "Debit A/c" : "Credit A/c";
            }
        };

        gridMA.getGrid().getView().getHeader().setHeader(0, headerText);
        gridMA.getGrid().getStore().clear();
        if (ourRow.getOverHeadMA() != null) {
            for (int i = 0; i < ourRow.getOverHeadMA().size(); i++) {
                overHeadMA_Mdl = new OverHeadMA_Mdl();
                overHeadMA_Mdl.setSharedKeyFieldCode(ourRow.getOverHeadMA().get(i).getSharedKeyFieldCode());
                overHeadMA_Mdl.setSharedKeyFieldName(ourRow.getOverHeadMA().get(i).getSharedKeyFieldName());
                overHeadMA_Mdl.setAmount(ourRow.getOverHeadMA().get(i).getAmount());
                gridMA.getGrid().getStore().add(overHeadMA_Mdl);
            }
        }

        if (gridMA.getGrid().getStore() != null) {
            if (gridMA.getGrid().getStore().size() <= 0) {
                gridMA.getGrid().getStore().add(new OverHeadMA_Mdl());
            }
        }

        if (txtAtRate.isEnabled()) {
            txtAtRate.setFocus(true);
        } else if (txtAmount.isEnabled()) {
            txtAmount.setFocus(true);
        } else {
            ((CellSelectionModel) gridMA.getGrid().getSelectionModel()).selectCell(0, 1);
        }

        gridMA.commitChanges();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new OverHead_Mdl();
    }

    @Override
    public void onOk() {
        if (!chkGrid()) {
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        OverHead_Mdl ourRow = (OverHead_Mdl) row;
        OverHeadMA_Mdl overHeadMA_Mdl;

        ourRow.setOverHeadAlias(txtOverHeadAlias.getText());
        ourRow.setAtRate(txtAtRate.getText());
        ourRow.setAmount(txtAmount.getText());

        ourRow.setOverHeadMA(new ArrayList<OverHeadMA_Mdl>());
        for (int i = 0; i < gridMA.getGrid().getStore().size(); i++) {
            overHeadMA_Mdl = new OverHeadMA_Mdl();
            overHeadMA_Mdl.setSharedKeyFieldCode(gridMA.getGrid().getStore().get(i).getSharedKeyFieldCode());
            overHeadMA_Mdl.setSharedKeyFieldName(gridMA.getGrid().getStore().get(i).getSharedKeyFieldName());
            overHeadMA_Mdl.setAmount(gridMA.getGrid().getStore().get(i).getAmount());
            ourRow.getOverHeadMA().add(overHeadMA_Mdl);
        }

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    private boolean chkGrid() {
        List<OverHeadMA_Mdl> gridData;
        boolean rtn = true;
        double amount = 0;

        gridData = gridMA.getGrid().getStore().getAll();
        setAmountToDefaultAc();
        for (int i = 0; i < gridData.size(); i++) {
            if (!DmShared.xNull(gridData.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("") || DmShared.vNull(gridData.get(i).getAmount()) != 0) {
                amount += DmShared.vNull(gridData.get(i).getAmount());
                if (DmShared.xNull(gridData.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    ((CellSelectionModel) gridMA.getGrid().getSelectionModel()).selectCell(i, 2);
                    new DmAlertMessageBox("Please fill A/c Name.", (DmTextBox) null).show();
                    gridMA.getGrid().getView().focus();
                    rtn = false;
                    break;
                }

//                if (DmShared.vNull(gridData.get(i).getAmount()) == 0) {
//                    ((CellSelectionModel) gridMA.getGrid().getSelectionModel()).selectCell(i, 3);
//                    new DmAlertMessageBox("Please fill amount.", (DmTextBox) null).show();
//                    gridMA.getGrid().getView().focus();
//                    rtn = false;
//                    break;
//                }
                for (int j = i + 1; j < gridData.size(); j++) {
                    if (DmShared.xNull(gridData.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(gridData.get(j).getSharedKeyFieldCode()).trim())) {
                        ((CellSelectionModel) gridMA.getGrid().getSelectionModel()).selectCell(i, 2);
                        new DmAlertMessageBox("Duplicate A/c Name.", (DmTextBox) null).show();
                        gridMA.getGrid().getView().focus();
                        rtn = false;
                        break;
                    }
                }
            }

            if (!rtn) {
                break;
            }
        }

        if (rtn) {
            if (!DmShared.xNull(parentRow.getDrCr()).trim().equalsIgnoreCase("")) {
                BigDecimal d, c;
                d = new BigDecimal(amount);
                c = new BigDecimal(DmShared.vNull(txtAmount.getText()));

                if (d.setScale(2, RoundingMode.HALF_UP).doubleValue() != c.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
//                if (amount != DmShared.vNull(txtAmount.getText())) {
                    new DmAlertMessageBox("Please bifurcate full amount", txtAmount).show();
                    rtn = false;
                }
            }

        }

        return rtn;
    }

    public OverHeadGridEdit(DmGrid<T1, T2> grid, OverHead overHead) {
        super(grid, "Overhead", "335px", "195px");
        textHelpEvents();
        gridEvents();
        this.overHead = overHead;
    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {
        if (!isOkEnable()) {
            overHeadGridMAEdit.show(currentRowIndex, false, false, false);
        } else {
            if (DmShared.iNull(parentRow.getAcEditable()).longValue() == 0) {
                overHeadGridMAEdit.show(currentRowIndex, false, false, false);
            } else {
                overHeadGridMAEdit.show(currentRowIndex, true, true, true);
            }
        }
    }
}
