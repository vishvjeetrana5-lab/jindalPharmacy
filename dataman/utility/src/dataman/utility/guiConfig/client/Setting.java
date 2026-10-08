package dataman.utility.guiConfig.client;

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
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Setting<T> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk(String sqlQuery);
    }

    private CallBack callBack;
    private T config;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean editable, Widget wdg, T guiConfig, String query) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        popupPanel.showRelativeTo(wdg);
        this.config = guiConfig;

        txtQuery.setText(DmShared.xNull(query));
        txtQuery.setFocus(true);

        flwOk.clear();
        flwOk.setVisible(false);
        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
    }

    public void hide() {
        this.popupPanel.hide();
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
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            popupPanel.hide();
            callBack.onOk(txtQuery.getText());
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public Setting() {
        super();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

}
