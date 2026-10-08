package dataman.dmToolkitR.dmTextBoxNumeric.client;

import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

class DmTextBoxNumeric_Dsg extends Input {

    public DmTextBoxNumeric_Dsg() {
        super();
        this.setWidth("100%");
        this.getElement().setAttribute("onpaste", "return false;");
        this.getElement().setAttribute("ondrop", "return false;");
        this.getElement().setAttribute("autofocus", "true");
        this.setType(InputType.NUMBER);
    }
}
