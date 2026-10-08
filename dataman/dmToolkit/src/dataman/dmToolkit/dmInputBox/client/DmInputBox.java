package dataman.dmToolkit.dmInputBox.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmInputBox extends DmInputBox_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String inputValue, String parentName);
    }

    private CallBack callBack;
    String parentName = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(UIObject uiObject, String parentName, String inputValue, int maxLength, String caption, String width) {
        if (!DmShared.xNull(width).trim().equalsIgnoreCase("")) {
            popupPanel.setWidth(width);
        } else {
            popupPanel.setWidth("300px");
        }
        this.parentName = parentName;
        popupPanel.showRelativeTo(uiObject);
        popupPanel.setGlassEnabled(true);
        txtInputBox.setFocus(true);
        txtInputBox.setText(inputValue);
        if (maxLength > 0) {
            txtInputBox.setMaxLength(maxLength);
        }

        if (!DmShared.xNull(caption).trim().equalsIgnoreCase("")) {
            lblInput.setText(caption);
        }

    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            callBack.onOk(txtInputBox.getText(), parentName);
            popupPanel.hide();
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
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public DmInputBox() {
        super();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Input Box");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        txtInputBox.setText("");
    }

}
