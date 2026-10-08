package aarogya.ribbon.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;

public class RepairDB<T1, T2> extends RepairDB_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk(String repairDB, boolean isExhaustiveRepair);

        public void onClose();
    }

    CallBack callBack;
    String repairDB = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String repairDB) {
        this.repairDB = repairDB;
        htmlMessage.setHTML("<center>Are you sure?</br>You want to repair <u><b>" + repairDB + "</b></u> database.</center>");
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        chkRepairDB.setValue(Boolean.FALSE);
        flwWarning.clear();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            callBack.onOk(repairDB, chkRepairDB.getValue());
            hide();
        } else if (event.getSource() == btnClose) {
            callBack.onClose();
            hide();
        }

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onClose();
            this.hide();
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

    private void check() {
        chkRepairDB.addCallBack(new DmCheckBox.CallBack() {
            @Override
            public void valueChanged() {
                if (chkRepairDB.getValue() == true) {
                    flwWarning.add(htmlWarning);
                } else {
                    flwWarning.clear();
                }
            }
        });

    }

    public RepairDB() {
        super();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Repair Database");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        btnOk.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwWarning.clear();
        check();
    }

}
