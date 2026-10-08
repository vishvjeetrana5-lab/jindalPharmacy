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
import aarogya.inventory.purchaseBillDirect.shared.GrdImpurity_Mdl;

public class ImpurityPopup extends ImpurityPopup_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(List<GrdImpurity_Mdl> lstImpurity);

        public void onClose();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    PurchaseBillDirectGridEdit grnGridEdit;

    final ImpurityGridEdit<GrdImpurity_Mdl, GrdImpurity_PrpAcs> impurityGridEdit = new ImpurityGridEdit(dmGridImpurity);

    public ImpurityPopup() {
        super();
        gridEvents();
        manageEvents();
    }

    private void manageEvents() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void show(PurchaseBillDirectGridEdit grnGridEdit) {
        this.grnGridEdit = grnGridEdit;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        dmGridImpurity.getGrid().getStore().clear();
        if (grnGridEdit.lstImpurity.size() > 0) {
            dmGridImpurity.getGrid().getStore().addAll(grnGridEdit.lstImpurity);
        } else {
            dmGridImpurity.getGrid().getStore().add(new GrdImpurity_Mdl());
        }

        ((CellSelectionModel) dmGridImpurity.getGrid().getSelectionModel()).selectCell(0, 1);
        flwOk.clear();
        if (!DmShared.xNull(grnGridEdit.billDirectEntry.topControl.getMode()).equalsIgnoreCase("Browse")) {
            flwOk.add(btnOk);
        }
    }

    private void gridEvents() {
        dmGridImpurity.getGrid().getStore().clear();
        dmGridImpurity.getGrid().getStore().add(new GrdImpurity_Mdl());

        impurityGridEdit.addCallBack(new ImpurityGridEdit.CallBack() {
            @Override
            public void onOk(GrdImpurity_Mdl impurityMdl) {
                dmGridImpurity.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGridImpurity.commitChanges();
            }
        });

        dmGridImpurity.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showImpurityGridEdit(event.getRowIndex());
            }
        });

        dmGridImpurity.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showImpurityGridEdit(dmGridImpurity.getGrid().getStore().indexOf(dmGridImpurity.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showImpurityGridEdit(int currentRowIndex) {
        if (DmShared.xNull(grnGridEdit.billDirectEntry.topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
            impurityGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            impurityGridEdit.show(currentRowIndex, true, true, true, this);
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
            callBack.onOk(dmGridImpurity.getGrid().getStore().getAll());
        }
    }

    private void onClose() {
        hide();
        if (callBack != null) {
            callBack.onClose();
        }
    }

}
