package dataman.dmToolkitR.dmLabel.client;

import org.gwtbootstrap3.client.ui.Label;

class DmLabel_Dsg extends Label {

    public DmLabel_Dsg(String strCaption) {
        super(strCaption);
        this.getElement().setAttribute("style", "width: 100% ! important; background-color: transparent; display: inline-block; padding: 0px; line-height: normal; white-space: initial; color: #444;");
    }
}
