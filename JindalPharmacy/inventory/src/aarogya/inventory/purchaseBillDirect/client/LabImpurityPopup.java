package aarogya.inventory.purchaseBillDirect.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import aarogya.inventory.purchaseBillDirect.shared.GrdLabEntry_Mdl;

public class LabImpurityPopup extends LabImpurityPopup_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(List<GrdLabEntry_Mdl> lstLabEntry);

        public void onClose();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    PurchaseBillDirectGridEdit purchaseBillDirectGridEdit;

    final LabEntryGridEdit<GrdLabEntry_Mdl, Grd_PrpAcs> labEntryGridEdit = new LabEntryGridEdit(dmGrdLab);
    String formulaValue = "";

    public LabImpurityPopup() {
        super();
        gridEvents();
        manageEvents();
    }

    private void manageEvents() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void show(PurchaseBillDirectGridEdit purchaseBillDirectGridEdit) {
        this.purchaseBillDirectGridEdit = purchaseBillDirectGridEdit;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        dmGrdLab.getGrid().getStore().clear();
        if (purchaseBillDirectGridEdit.lstLabEntry.size() > 0) {
            dmGrdLab.getGrid().getStore().clear();
            dmGrdLab.getGrid().getStore().addAll(purchaseBillDirectGridEdit.lstLabEntry);
        } else {
            dmGrdLab.getGrid().getStore().clear();
            dmGrdLab.getGrid().getStore().add(new GrdLabEntry_Mdl());
        }

        ((CellSelectionModel) dmGrdLab.getGrid().getSelectionModel()).selectCell(0, 1);
        flwOk.clear();
        if (!DmShared.xNull(purchaseBillDirectGridEdit.billDirectEntry.topControl.getMode()).equalsIgnoreCase("Browse")) {
            flwOk.add(btnOk);
        }
    }

    private void gridEvents() {
        dmGrdLab.getGrid().getStore().clear();
        dmGrdLab.getGrid().getStore().add(new GrdLabEntry_Mdl());

        labEntryGridEdit.addCallBack(new LabEntryGridEdit.CallBack() {
            @Override
            public void onOk(GrdLabEntry_Mdl impurityMdl) {
                dmGrdLab.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrdLab.commitChanges();

            }
        });

        dmGrdLab.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showLabEntryGridEdit(event.getRowIndex());
            }
        });

        dmGrdLab.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showLabEntryGridEdit(dmGrdLab.getGrid().getStore().indexOf(dmGrdLab.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showLabEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(purchaseBillDirectGridEdit.billDirectEntry.topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
            labEntryGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            labEntryGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
                && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOk();
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
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void onOk() {
        hide();
        if (callBack != null) {
            callBack.onOk(dmGrdLab.getGrid().getStore().getAll());
        }
    }

    private void onClose() {
        hide();
        if (callBack != null) {
            callBack.onClose();
        }
    }

}
