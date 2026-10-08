package dataman.dmToolkit.overHead.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.ServiceInvoker; 
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHead.shared.OverHeadExclude_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_SrvAsync;
import java.util.ArrayList;
import java.util.List;

public class OverHeadExcludePopup extends OverHeadExcludePopup_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void ok();

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    protected ServiceInvoker<OverHead_SrvAsync> srvAsync;
    protected OverHead overHead = null;

    public OverHeadExcludePopup() {
        super();
        manageEvents();
    }

    public void show(OverHead overHead) {
        this.overHead = overHead;

        flwOk.clear();
        if (!DmShared.xNull(overHead.topcontrol.getMode()).trim().equalsIgnoreCase("Browse")) {
            flwOk.add(btnOk);
        }

        fillGrid();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    private void fillGrid() {
        List<OverHeadExclude_Mdl> lstExcludeMdl = new ArrayList<OverHeadExclude_Mdl>();
        OverHeadExclude_Mdl excludeMdl = null;
        String excludeFormula = "";

        for (OverHead_Mdl ohMdl : overHead.getOverHeadList()) {
            if (ohMdl != null) {
                if (DmShared.xNull(ohMdl.getCalLValOn()).trim().equals("1")) {
                    excludeFormula = ohMdl.getExcludeFormula();
                    break;
                }
            }
        }

        for (OverHead_Mdl ohMdl : overHead.getOverHeadList()) {
            if (ohMdl != null) {
                if (!DmShared.xNull(ohMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if (DmShared.xNull(ohMdl.getNature()).trim().equalsIgnoreCase("A")) {

                        excludeMdl = new OverHeadExclude_Mdl();
                        excludeMdl.setSharedKeyFieldCode(ohMdl.getSharedKeyFieldCode());
                        excludeMdl.setSharedKeyFieldName(ohMdl.getSharedKeyFieldName());
                        excludeMdl.setAtRate(ohMdl.getAtRate());
                        excludeMdl.setAmount(ohMdl.getAmount());
                        excludeMdl.setFormula(ohMdl.getFormula());
                        excludeMdl.setManualCode(ohMdl.getOhManualCode());
                        excludeMdl.setIsSelected("0");

                        if (DmShared.xNull(excludeFormula).trim().contains("[" + DmShared.xNull(excludeMdl.getManualCode()).trim() + "]")) {
                            excludeMdl.setIsSelected("1");
                        }

                        lstExcludeMdl.add(excludeMdl);
                    }
                }
            }
        }

        grid.getGrid().getStore().clear();
        grid.getGrid().getStore().addAll(lstExcludeMdl);
        focusGrid();
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCallback) {
        popupPanel.hide();
        if (withCallback) {
            callBack.close();
        }
    }

    private void onOk() {
        hide();
        callBack.ok();
    }

    private void focusGrid() {
        ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void manageEvents() {
        gridEvents();
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void gridEvents() {

        grid.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == selectCheckBoxColIndex) {
                    setTick(event.getRowIndex());
                }
            }
        });

        grid.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    if (grid.getSelectedCell().getCell() == selectCheckBoxColIndex) {
                        setTick(grid.getSelectedCell().getRow());
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setTick(int rowIndex) {
        if (!DmShared.xNull(overHead.topcontrol.getMode()).trim().equalsIgnoreCase("Browse")) {
            OverHeadExclude_Mdl selectedRow = grid.getGrid().getStore().get(rowIndex);
            int val = DmShared.vNull(selectedRow.getIsSelected()).intValue();

            if (val == 0) {
                selectedRow.setIsSelected("1");
            } else if (val == 1) {
                selectedRow.setIsSelected("0");
            }

            grid.commitChanges();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnOk) {
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            onOk();
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
