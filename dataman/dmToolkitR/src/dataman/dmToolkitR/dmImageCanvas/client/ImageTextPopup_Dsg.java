package dataman.dmToolkitR.dmImageCanvas.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.dmTextBoxNumeric.client.DmTextBoxNumeric;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.base.button.AbstractIconButton;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

abstract class ImageTextPopup_Dsg {

    final Modal modal = new Modal();
    final ModalHeader modalHeader = new ModalHeader();
    final Column colFont = new Column(ColumnSize.MD_4);
    final Column colText = new Column(ColumnSize.MD_8);
    final DmTextBoxNumeric txtFontSize = new DmTextBoxNumeric(1, false, 2, true);
    final DmTextBox txtText = new DmTextBox();
    final DmLabel lblFontSize = new DmLabel("Font Size");
    final DmLabel lblText = new DmLabel("Text");
    final AbstractIconButton btnok = new AbstractIconButton() {
        @Override
        protected Element createElement() {
            return Document.get().createPushButtonElement().cast();
        }
    };

    public ImageTextPopup_Dsg() {
        ini();
    }

    private void ini() {
        modal.setSize(ModalSize.SMALL);
        modal.addStyleName("prescription-modal");
        modal.setDataBackdrop(ModalBackdrop.STATIC);
        btnok.addStyleName("close");
        btnok.addStyleName("modalBtn");
        btnok.setIcon(IconType.CHECK);
//btnok.getElement().setInnerHTML("&#10004;");
        modalHeader.add(btnok);
        modal.add(modalHeader);
        modal.add(workingArea());
    }

    private ModalBody workingArea() {
        ModalBody modalBody = new ModalBody();
        Container container = new Container();
        container.setFluid(true);
         Row row = new Row();
        colFont.add(lblFontSize);
        colFont.add(txtFontSize);
        colText.add(lblText);
        colText.add(txtText);
        row.add(colFont);
        row.add(colText);
        container.add(row);
        modalBody.add(container);
        return modalBody;
    }

}
