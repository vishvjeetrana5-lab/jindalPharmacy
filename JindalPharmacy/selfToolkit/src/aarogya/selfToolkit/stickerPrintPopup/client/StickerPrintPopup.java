package aarogya.selfToolkit.stickerPrintPopup.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class StickerPrintPopup extends StickerPrintPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void ok(String noOfSticker);
    }

    private CallBack callBack;
    private String noOfStickerText;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String noOfsticker) {
        noOfStickerText = noOfsticker;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwOk.clear();
        flwOk.add(btnOk);
        txtNoOfStickers.setFocus(true);
        txtNoOfStickers.setText(DmShared.xNull(noOfStickerText));
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            ok();
        }
    }

    private void ok() {
        popupPanel.hide();
        callBack.ok(DmShared.xNull(txtNoOfStickers.getText()));
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
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public StickerPrintPopup() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

}
