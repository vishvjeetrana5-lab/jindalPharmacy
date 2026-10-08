package dataman.utility.guiConfig.client;

import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class RowGridEdit<T1, T2> extends RowGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GUIRow_Mdl row_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<GUIConfig_SrvAsync> srvAsync;
    private CallBack callBack;
    private GUIConfig config;
    private int hashCode, autoSNo = 0, prvRow;
    protected final ColumnGridEdit<GUIRow_Mdl, GrdColumn_PrpAcs> columnGridEdit = new ColumnGridEdit(grdColumn);

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void gridEvents() {
        columnGridEdit.addCallBack(new ColumnGridEdit.CallBack() {

            @Override
            public void onOk(GUIColumn_Mdl column_Mdl) {
                grdColumn.commitChanges();
            }

            @Override
            public void onDelete() {
                grdColumn.commitChanges();
            }
        });
        grdColumn.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showDescGrid(event.getRowIndex());
            }
        });
        grdColumn.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showDescGrid(grdColumn.getGrid().getStore().indexOf(grdColumn.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, GUIConfig gui, String prvRow) {
        this.prvRow = DmShared.vNull(prvRow).intValue();
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.config = gui;
    }

    @Override
    public void displayRow(T1 row) {
        GUIRow_Mdl ourRow = (GUIRow_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        if (prvRow > 0) {
            autoSNo = prvRow + 1;
        } else {
            autoSNo = 1;
        }

        if (DmShared.vNull(ourRow.getSrNo()).doubleValue() <= 0) {
            txtRowNo.setText(DmClient.vNull(autoSNo, "0"));
        } else {
            txtRowNo.setText(DmClient.vNull(ourRow.getSrNo(), "0"));
        }

        grdColumn.getGrid().getStore().clear();
        if (ourRow.getGuiCol() != null) {
            grdColumn.getGrid().getStore().addAll(ourRow.clone().getGuiCol());
        }
        grdColumn.getGrid().getStore().add(new GUIColumn_Mdl());
        txtRowNo.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GUIRow_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtRowNo.getText()).intValue() <= 0) {
            new DmAlertMessageBox("Row No. is mandatory.", txtRowNo).show();
            return;
        }
        for (GUIColumn_Mdl colMdl : grdColumn.getGrid().getStore().getAll()) {
            double totalwidth = 0;
            for (GUIColumn_Mdl col : grdColumn.getGrid().getStore().getAll()) {
                if (DmShared.vNull(col.getSharedKeyFieldHashCode()).doubleValue() != DmShared.vNull(colMdl.getSharedKeyFieldHashCode()).doubleValue()) {
                    if (DmShared.vNull(col.getSharedKeyFieldCode()).doubleValue() > 0) {
                        if (DmShared.vNull(col.getSharedKeyFieldCode()).doubleValue() == DmShared.vNull(colMdl.getSharedKeyFieldCode()).doubleValue()) {
                            new DmAlertMessageBox("Column No. could not be conflict.", txtRowNo).show();
                            return;
                        }
                        totalwidth += DmShared.vNull(col.getCaptionWidth()).doubleValue() + DmShared.vNull(col.getControlWidth()).doubleValue();
                        if (totalwidth > 100) {
                            new DmAlertMessageBox("Captions & Controls length is greater then 100.", (DmTextBox) null).show();
                            return;
                        }
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GUIRow_Mdl ourRow = (GUIRow_Mdl) row;
        String desc = "";
        ourRow.setSrNo(DmShared.xNull(txtRowNo.getText().trim()));
        if (grdColumn.getGrid().getStore().getAll() != null) {
            for (GUIColumn_Mdl col : grdColumn.getGrid().getStore().getAll()) {
                if (!DmShared.xNull(col.getFieldCaption()).equalsIgnoreCase("")) {
                    if (desc.equalsIgnoreCase("")) {
                        desc = col.getFieldCaption();
                    } else {
                        desc += ", " + col.getFieldCaption();
                    }
                } else if (!DmShared.xNull(col.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    if (desc.equalsIgnoreCase("")) {
                        desc = col.getSharedKeyFieldName();
                    } else {
                        desc += ", " + col.getSharedKeyFieldName();
                    }
                }
            }
        }
        ourRow.setDescription(desc);
        ourRow.getGuiCol().clear();
        ourRow.getGuiCol().addAll(grdColumn.getGrid().getStore().getAll());
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

    private void showDescGrid(int currentRowIndex) {
        String prvRow = "";
        if (currentRowIndex > 0) {
            prvRow = DmShared.xNull(grdColumn.getGrid().getStore().get(currentRowIndex - 1).getSharedKeyFieldCode());
        } else {
            prvRow = DmShared.xNull(grdColumn.getGrid().getStore().get(currentRowIndex).getSharedKeyFieldCode());
        }
        if (config.topControl.getMode().equalsIgnoreCase("Browse")) {
            columnGridEdit.show(currentRowIndex, false, false, false, config, prvRow);
        } else {
            columnGridEdit.show(currentRowIndex, true, true, true, config, prvRow);
        }
    }

    public RowGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "GUI Row", "1120px", "248px");
        srvAsync = new ServiceInvoker<>(GWT.create(GUIConfig_Srv.class), GUIConfig.class.getName());
        gridEvents();
    }
}
