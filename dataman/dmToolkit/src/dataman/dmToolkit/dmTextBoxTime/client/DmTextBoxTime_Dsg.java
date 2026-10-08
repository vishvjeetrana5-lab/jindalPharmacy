package dataman.dmToolkit.dmTextBoxTime.client;

import com.google.gwt.user.client.ui.TextBox;

class DmTextBoxTime_Dsg extends TextBox {

    public DmTextBoxTime_Dsg() {
        super();
        this.setWidth("100%");
        this.getElement().setAttribute("onpaste", "return false;");
        this.getElement().setAttribute("ondrop", "return false;");
    }
}
