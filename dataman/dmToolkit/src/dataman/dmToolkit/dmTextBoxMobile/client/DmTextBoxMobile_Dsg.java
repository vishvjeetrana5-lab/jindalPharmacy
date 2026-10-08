package dataman.dmToolkit.dmTextBoxMobile.client;

import com.google.gwt.user.client.ui.TextBox;

class DmTextBoxMobile_Dsg extends TextBox {

    public DmTextBoxMobile_Dsg() {
        super();
        this.setWidth("100%");
        this.getElement().setAttribute("onpaste", "return false;");
        this.getElement().setAttribute("ondrop", "return false;");
    }
}
