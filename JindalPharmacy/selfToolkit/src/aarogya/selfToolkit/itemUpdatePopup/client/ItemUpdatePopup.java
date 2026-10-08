package aarogya.selfToolkit.itemUpdatePopup.client;

import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.shared.SchemePopup_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class ItemUpdatePopup extends ItemUpdatePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    public interface CallBack {

        public void ok(List<ItemUpdateField_Mdl> listItemUpdateFields);

        public void onClose();

    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private final SchemePopup schemePopup = new SchemePopup();
    private SchemePopup_Mdl schemePopupMdl = new SchemePopup_Mdl();
    private String unitDecimalFormat;

    public ItemUpdatePopup() {
        super();
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        manageEvents();
    }

    public void show(List<ItemUpdateField_Mdl> lstTemp, String purchaseUnitDecimalFormat, CallBack callBack) {
        this.unitDecimalFormat = purchaseUnitDecimalFormat;
        this.callBack = callBack;
        grid.getGrid().getStore().clear();
        if (lstTemp != null && lstTemp.size() > 0) {
            for (ItemUpdateField_Mdl lstNode : lstTemp) {
                if (lstNode.getField() != null && lstNode.getField() == ItemUpdateField_Mdl.Field.SCHEME) {
                    if (isSchemeChanged(lstNode)) {
                        grid.getGrid().getStore().add(lstNode);
                    }
                } else {
                    if (DmShared.vNull(lstNode.getMasterValue()).doubleValue() != DmShared.vNull(lstNode.getCurrentValue()).doubleValue()) {
                        grid.getGrid().getStore().add(lstNode);
                    }
                }
            }
        }
        if (grid.getGrid().getStore().getAll().size() > 0) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(0, 0);
        } else {
            onOk();
        }
    }

    private boolean isSchemeChanged(ItemUpdateField_Mdl iufMdl) {
        boolean returnValue = false;
        String[] masterScheme = DmShared.xNull(iufMdl.getMasterValue()).split("[+]");
        String[] currentScheme = DmShared.xNull(iufMdl.getCurrentValue()).split("[+]");
        String[] updatedScheme = DmShared.xNull(iufMdl.getFinalValue()).split("[+]");

        schemePopupMdl.setCurrentQty(DmClient.vNull(DmShared.xNull(currentScheme[0]).trim(), unitDecimalFormat));
        schemePopupMdl.setCurrentFreeQty(DmClient.vNull(DmShared.xNull(currentScheme[1]).trim(), unitDecimalFormat));
        schemePopupMdl.setCurrentSchemeOnQty(DmClient.vNull(DmShared.xNull(updatedScheme[0]).trim(), unitDecimalFormat));
        schemePopupMdl.setCurrentSchemeFreeQty(DmClient.vNull(DmShared.xNull(updatedScheme[1]).trim(), unitDecimalFormat));
        schemePopupMdl.setMasterSchemeOnQty(DmClient.vNull(DmShared.xNull(masterScheme[0]).trim(), unitDecimalFormat));
        schemePopupMdl.setMasterSchemeFreeQty(DmClient.vNull(DmShared.xNull(masterScheme[1]).trim(), unitDecimalFormat));

        if (DmShared.vNull(schemePopupMdl.getCurrentFreeQty()).doubleValue() > 0) {
            String masteRatio = DmClient.vNull((DmShared.vNull(schemePopupMdl.getMasterSchemeOnQty())) / (DmShared.vNull(schemePopupMdl.getMasterSchemeFreeQty()) != 0 ? DmShared.vNull(schemePopupMdl.getMasterSchemeFreeQty()) : 1), unitDecimalFormat);
            String currentRatio = DmClient.vNull((DmShared.vNull(schemePopupMdl.getCurrentQty())) / (DmShared.vNull(schemePopupMdl.getCurrentFreeQty()) != 0 ? DmShared.vNull(schemePopupMdl.getCurrentFreeQty()) : 1), unitDecimalFormat);
            returnValue = DmShared.vNull(masteRatio).doubleValue() != DmShared.vNull(currentRatio).doubleValue();
        }
        return returnValue;
    }

    public void hide() {
        this.popupPanel.hide();
        if (callBack != null) {
            callBack.onClose();
        }
    }

    private void manageEvents() {
        gridEvents();
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void gridEvents() {
        schemePopup.addCallBack(new SchemePopup.CallBack() {
            @Override
            public void ok(SchemePopup_Mdl spMdl) {
                int rowIndex = 0;
                for (ItemUpdateField_Mdl iufMdl : grid.getGrid().getStore().getAll()) {
                    if (iufMdl.getField() != null && iufMdl.getField() == ItemUpdateField_Mdl.Field.SCHEME) {
                        iufMdl.setFinalValue(DmClient.vNull(spMdl.getCurrentSchemeOnQty(), unitDecimalFormat) + " + " + DmClient.vNull(spMdl.getCurrentSchemeFreeQty(), unitDecimalFormat));
                        rowIndex = grid.getGrid().getStore().getAll().indexOf(iufMdl);
                        break;
                    }
                }
                grid.commitChanges();
                schemePopupMdl = spMdl;
                ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(rowIndex, 3);
            }

            @Override
            public void onClose() {
                int rowIndex = 0;
                for (ItemUpdateField_Mdl iufMdl : grid.getGrid().getStore().getAll()) {
                    if (iufMdl.getField() != null && iufMdl.getField() == ItemUpdateField_Mdl.Field.SCHEME) {
                        iufMdl.setSaveToItem("0");
                        rowIndex = grid.getGrid().getStore().getAll().indexOf(iufMdl);
                        break;
                    }
                }
                grid.commitChanges();
                ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(rowIndex, 3);
            }
        });

        grid.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                setTick(event.getRowIndex(), event.getCellIndex());
            }
        });

        grid.getGrid().addDomHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                event.stopPropagation();
                CellSelection<ItemUpdateField_Mdl> csm;
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    csm = ((CellSelectionModel<ItemUpdateField_Mdl>) grid.getGrid().getSelectionModel()).getSelectCell();
                    setTick(csm.getRow(), csm.getCell());
                }
            }
        }, KeyUpEvent.getType());
    }

    private void setTick(int row, int cell) {
        ItemUpdateField_Mdl.Field field = grid.getGrid().getStore().get(row).getField();
        if (cell == 4) {
            if (field != null && field == ItemUpdateField_Mdl.Field.PURCHASE_DISCOUNT_PERCENTAGE) {
                if (DmShared.iNull(grid.getGrid().getStore().get(row).getSaveToCompany()).intValue() == 0) {
                    grid.getGrid().getStore().get(row).setSaveToCompany("1");
                    grid.getGrid().getStore().get(row).setSaveToItem("0");
                } else {
                    grid.getGrid().getStore().get(row).setSaveToCompany("0");
                }
            }
        } else {
            if (DmShared.iNull(grid.getGrid().getStore().get(row).getSaveToItem()).intValue() == 0) {
                grid.getGrid().getStore().get(row).setSaveToItem("1");
                grid.getGrid().getStore().get(row).setSaveToCompany("0");
                if (field != null && field == ItemUpdateField_Mdl.Field.SCHEME) {
                    schemePopup.show(schemePopupMdl, unitDecimalFormat);
                }
            } else {
                grid.getGrid().getStore().get(row).setSaveToItem("0");
            }
        }
        grid.commitChanges();
    }

    private void onOk() {
        if (callBack != null) {
            callBack.ok(grid.getGrid().getStore().getAll());
        }
        hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            onOk();
        } else if (event.getSource().equals(btnClose)) {
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
            schemePopup.hide(false);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }
}
