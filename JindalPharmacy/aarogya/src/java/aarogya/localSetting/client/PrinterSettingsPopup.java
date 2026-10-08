package aarogya.localSetting.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.List;

public class PrinterSettingsPopup extends PrinterSettingsPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOk();
        }

    }

    public interface CallBack {

        public void ok(String title, List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap);

        public void close();
    }

    private CallBack callBack;
    private final GrdEditPopup<DmLocalSettingPrintMap_Mdl, GrdPrinterMapping_PrpAcs> gridEditPopup = new GrdEditPopup(grdPrinterMapping);
    private String title;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String title, List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap) {
        this.title = title;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        lblTitle.setText(title);
        grdPrinterMapping.getGrid().getStore().clear();
        grdPrinterMapping.getGrid().getStore().addAll(listSettingPrintMap);
        DmLocalSettingPrintMap_Mdl newRow = new DmLocalSettingPrintMap_Mdl();
        newRow.setFieldUniqueCode(String.valueOf(grdPrinterMapping.getGrid().getStore().size()));
        grdPrinterMapping.getGrid().getStore().add(newRow);
    }

    private void showPopUp(int currentRowIndex) {
        gridEditPopup.show(currentRowIndex, true, true, true, true);
    }

    private void gridEvents() {

        gridEditPopup.addCallBack(new GrdEditPopup.CallBack() {
            @Override
            public void onOk(DmLocalSettingPrintMap_Mdl node) {

                grdPrinterMapping.commitChanges();

            }

            @Override
            public void onDelete() {
                grdPrinterMapping.commitChanges();
            }
        });

        grdPrinterMapping.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPopUp(event.getRowIndex());
            }
        });

        grdPrinterMapping.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPopUp(grdPrinterMapping.getGrid().getStore().indexOf(grdPrinterMapping.getGrid().getSelectionModel().getSelectedItem()));
                }
            }

        }, KeyDownEvent.getType());

    }

    public PrinterSettingsPopup() {

        super();

        toolContainer.setTitle("Printer Settings");

        gridEvents();

        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.close();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
        if (event.getSource().equals(btnOk)) {
            onOk();
        } else if (event.getSource().equals(btnCancel)) {
            hide(true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    private void onOk() {
        List<DmLocalSettingPrintMap_Mdl> listTemp = new ArrayList<>();
        for (DmLocalSettingPrintMap_Mdl node : grdPrinterMapping.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(node.getModuleName()).trim().equalsIgnoreCase("")) {
                listTemp.add(node);
            }
        }
        hide(false);
        callBack.ok(title, listTemp);
    }

}
