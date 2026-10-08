package dataman.dmToolkitR.dmConfirmBox.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;

public class DmConfirmBox extends DmConfirmBox_Dsg implements IsWidget, ValueChangeHandler<String>, ClickHandler {

    public interface CallBack {

        public void onAction(DmEnum.DmYesNo dmYesNo);
    }
    private String message;
    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        lblMessage.getElement().setInnerHTML(message);
        modal.show();
    }

    public void hide() {
        modal.hide();
    }

    public DmConfirmBox(String messageHTML) {
        super();
        this.message = messageHTML;
        btnYes.addClickHandler(this);
        btnNo.addClickHandler(this);
        show();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnYes) {
            callBack.onAction(DmEnum.DmYesNo.YES);
            hide();
        } else if (event.getSource() == btnNo) {
            callBack.onAction(DmEnum.DmYesNo.NO);
            hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.modal;
    }
}
