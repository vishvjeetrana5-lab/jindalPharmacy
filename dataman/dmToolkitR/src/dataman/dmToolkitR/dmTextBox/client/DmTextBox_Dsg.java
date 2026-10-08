package dataman.dmToolkitR.dmTextBox.client;

import org.gwtbootstrap3.client.ui.Input;

class DmTextBox_Dsg extends Input {

    public DmTextBox_Dsg() {
        super();
        this.setWidth("100%");
        this.setStyleName("form-control");
        this.getElement().setAttribute("autofocus", "true");
    }
}
