package dataman.dmToolkit.dmTextBoxNumeric.client;

import com.google.gwt.user.client.ui.TextBox;

class DmTextBoxNumeric_Dsg extends TextBox {

    public DmTextBoxNumeric_Dsg() {
        super();
        this.setWidth("100%");
        this.getElement().setAttribute("onpaste", "return false;");
        this.getElement().setAttribute("ondrop", "return false;");
    }
}
