package aarogya.localSetting.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import java.util.HashMap;

public final class LocalSetting extends LocalSetting_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    PrinterMappingPopup prntMapPopup;
    MachineInterfacePopup miPopup;
    PosSettingPopup posSettingPopup;

    @Override
    public void onClick(ClickEvent event) {

        try {
            if (event.getSource().equals(btnDwnSetup)) {

                menu.show(btnDwnSetup);

            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnPrinter)) {
                if (prntMapPopup != null) {
                    prntMapPopup.hide();
                    prntMapPopup = null;
                }
                prntMapPopup = new PrinterMappingPopup();
                prntMapPopup.show();

            } else if (event.getSource().equals(btnMachineInterface)) {
                if (miPopup != null) {
                    miPopup.hide();
                    miPopup = null;
                }
                miPopup = new MachineInterfacePopup();
                miPopup.show();

            } else if (event.getSource().equals(btnPosSetting)) {
                if (posSettingPopup != null) {
                    posSettingPopup.hide();
                    posSettingPopup = null;
            }
                posSettingPopup = new PosSettingPopup();
                posSettingPopup.show();
            }
        } catch (DmException e) {
        }

    }

    public LocalSetting(final SetWidgetInvoker swi) {
        super();

        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());

        toolContainer.addWidget(btnDwnSetup);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);

        btnDwnSetup.addClickHandler(this);

        btnPrinter.addClickHandler(this);
        btnMachineInterface.addClickHandler(this);
        btnPosSetting.addClickHandler(this);

        flwForm.addAttachHandler(this);

        swi.setWidget(LocalSetting.this);

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            //  prntrstgs.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LocalSetting.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSource() == dwnPackSetup) {
            DmBaseURL pageUrl = DmClient.getHostPageBaseURL();
            HashMap<String, String> params = new HashMap<>();
            params.put("cmd", "downloadInstaller");
            params.put("cPath", pageUrl.getProtocol() + "://" + pageUrl.getUrl());
            Window.open(DmClient.concatURL("dataman/dmToolkit/self/server/DmPACSetup_Srvlt", params), "", "");
        }
        if (event.getSource() == dwnJDK64) {
            HashMap<String, String> params = new HashMap<>();
            params.put("cmd", "jdk8x64");
            Window.open(DmClient.concatURL("dataman/dmToolkit/self/server/DmPACSetup_Srvlt", params), "", "");
        }
        if (event.getSource() == dwnJDK32) {
            HashMap<String, String> params = new HashMap<>();
            params.put("cmd", "jdk8x32");
            Window.open(DmClient.concatURL("dataman/dmToolkit/self/server/DmPACSetup_Srvlt", params), "", "");
        }
    }

}
