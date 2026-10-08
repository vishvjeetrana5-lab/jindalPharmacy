package dataman.dmToolkitR.dmGUI.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import dataman.dmToolkitR.dmTextArea.client.DmTextArea;
import dataman.dmToolkitR.isWidgetExtended.client.IsWidgetExtended;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.base.button.AbstractButton;
import org.gwtbootstrap3.client.ui.base.button.AbstractIconButton;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

abstract public class DmGUI_MultiLine_Dsg extends IsWidgetExtended {

    final Modal modal = new Modal();
    final ModalHeader modalHeader = new ModalHeader();
    final DmTextArea txtRemark = new DmTextArea();
    final ModalFooter modelFooter = new ModalFooter();
    final AbstractButton btnOk = new AbstractIconButton() {
        @Override
        protected Element createElement() {
            return Document.get().createPushButtonElement().cast();
        }
    };

    public DmGUI_MultiLine_Dsg() {
        ini();
    }

    private void ini() {
        modal.setSize(ModalSize.MEDIUM);
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
        txtRemark.setVisibleLines(5);
        formGroup.add(txtRemark);
        modalBody.add(formGroup);
        return modalBody;
    }

}
