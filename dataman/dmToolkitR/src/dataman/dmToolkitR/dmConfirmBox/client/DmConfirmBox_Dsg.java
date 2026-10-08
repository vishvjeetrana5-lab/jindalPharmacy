package dataman.dmToolkitR.dmConfirmBox.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

abstract public class DmConfirmBox_Dsg {

    final Modal modal = new Modal();
    final ModalHeader modalHeader = new ModalHeader();
    final ModalBody modalBody = new ModalBody();
    final ModalFooter modalFooter = new ModalFooter();
    public final static String widgetName = "Message Popup";
    DmLabel lblMessage;

    final Button btnYes = new Button("YES");
    final Button btnNo = new Button("NO");

    public DmConfirmBox_Dsg() {
        modal.addStyleName("dm-message-popup");
        modal.setDataBackdrop(ModalBackdrop.STATIC);
        modal.setSize(ModalSize.SMALL);
        modalBody.add(ini());
        modalFooter.add(getControls());
        modal.add(modalBody);
        modal.add(modalFooter);
    }

    private FlowPanel ini() {
        FlowPanel flwBody, flwContent;
        flwBody = new FlowPanel();
        flwBody.getElement().setAttribute("Style", "width:100%; display:inline-block;");
        flwContent = new FlowPanel();
        flwContent.getElement().setAttribute("style", "width:100%; float:left; padding: 6px 0px;");

        Label lblIcon = new Label();
        lblIcon.setStyleName("fa fa-question-circle");
        lblIcon.getElement().setAttribute("style", "width: 100%; color: rgb(165, 214, 167); text-align: center; font-size: 4rem;");
        flwContent.add(lblIcon);

        lblMessage = new DmLabel("");
        lblMessage.addStyleName("dm-lbl-message");
        flwContent.add(lblMessage);
        flwBody.add(flwContent);

        return flwBody;

    }

    private FlowPanel getControls() {
        FlowPanel flwFooter;

        flwFooter = new FlowPanel();
        flwFooter.getElement().setAttribute("Style", "width:100%; display:inline-block;text-align:center;");

        btnYes.getElement().setAttribute("Style", "background-color: rgb(53, 56, 129); color: rgb(255, 255, 255); padding: 4px 32px; margin-right: 10px; box-shadow: 0px 0px 3px rgb(68, 68, 68); border: 1px solid rgb(53, 56, 129);");
        flwFooter.add(btnYes);

        btnNo.getElement().setAttribute("Style", "background-color: rgb(255, 255, 255); color: rgb(68, 68, 68); padding: 4px 32px; box-shadow: 0px 0px 3px rgb(68, 68, 68); border: 1px solid rgb(204, 204, 204);");
        flwFooter.add(btnNo);

        return flwFooter;

    }
}
