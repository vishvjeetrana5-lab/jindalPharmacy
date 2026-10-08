package dataman.dmToolkitR.dmImageCanvas.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import org.gwtbootstrap3.client.shared.event.ModalHiddenEvent;
import org.gwtbootstrap3.client.shared.event.ModalHiddenHandler;

public class ImageTextPopup extends ImageTextPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents{

    private void manageEvents() {

        btnok.addClickHandler(this);

        modal.addHiddenHandler(
                new ModalHiddenHandler() {
            @Override
            public void onHidden(ModalHiddenEvent evt) {
                 hide(true);
            }
        });
    }

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;

    public ImageTextPopup() {
        super();
        modalHeader.setTitle("Image Text");
        manageEvents();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void hide() {
        this.modal.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        callBack.hide(isCancel);
    }

    @Override
    public Widget asWidget() {
        return this.modal;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == this.btnok) {
            hide(false);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
        }
    }


    public void show() {
        RootPanel.get().addStyleName("prescription-open");  
        modal.show();
        if (DmShared.iNull(txtFontSize.getText()).longValue() <= 0) {
            txtFontSize.setText("12");
        }
        txtText.setFocus(true);
    }

    public String getText() {
        return DmShared.xNull(txtText.getText());
    }

    public Integer getFontSize() {
        return DmShared.vNull(txtFontSize.getText()).intValue();
    }

    

}
