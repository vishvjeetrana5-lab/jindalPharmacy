package dataman.dmToolkitR.dmColorPicker.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextArea.client.DmTextArea;
import dataman.dmToolkitR.isWidgetExtended.client.IsWidgetExtended;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.base.button.AbstractButton;
import org.gwtbootstrap3.client.ui.base.button.AbstractIconButton;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class ColorPicker extends IsWidgetExtended implements ClickHandler {

    final Modal modal = new Modal();
    final ModalHeader modalHeader = new ModalHeader();
    final ModalFooter modelFooter = new ModalFooter();
    private Input inputType = new Input(InputType.COLOR);
    final AbstractButton btnOk = new AbstractIconButton() {
        @Override
        protected Element createElement() {
            return Document.get().createPushButtonElement().cast();
        }
    };

    public interface CallBack {

        public void onOk(String brushColor);

    }

    public ColorPicker() {
        ini();
        btnOk.addClickHandler(this);
    }

    private void ini() {
        modal.setSize(ModalSize.SMALL);
        modal.setDataBackdrop(ModalBackdrop.STATIC);
        btnOk.addStyleName("close");
        btnOk.getElement().setInnerHTML("&#10004;");
        modalHeader.add(btnOk);
        modal.add(modalHeader);
        modal.add(workingArea());
    }

    private ModalBody workingArea() {
        ModalBody modalBody;
        FormGroup formGroup;
        modalBody = new ModalBody();
        formGroup = new FormGroup();
        formGroup.add(inputType);
        modalBody.add(formGroup);
        return modalBody;
    }

    @Override
    public Widget asWidget() {
       return modal;
    }

    @Override
    public void searchRecord(String key) {
        
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            hide(true);
        }
    }
    public CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(String color) {
        modal.setTitle("Color Picker");
        if (DmShared.xNull(color).equalsIgnoreCase("")) {
            inputType.setValue(color);
        } else {
            inputType.setValue("#000000");
        }
        modal.show();
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isOk) {
        if (isOk) {
            this.callBack.onOk(inputType.getValue());
        }

        this.modal.hide();
    }

}
