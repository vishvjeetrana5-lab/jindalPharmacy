package dataman.dmToolkit.dmTextHelpMulti.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

class DmTextHelpMulti_Dsg {

    final FlowPanel flwform = new FlowPanel();
    final TextBox txtHelp = new TextBox();
    final Button btnHelp = new Button();
    final FlowPanel flwbtn = new FlowPanel();

    public DmTextHelpMulti_Dsg() {
        HorizontalPanel hrtPanel = new HorizontalPanel();
        Anchor aBtn;

        flwform.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: hidden;");
        hrtPanel.setHeight("100%");

        txtHelp.getElement().setAttribute("style", "border: 0px none; width: 100%;");
//        txtHelp.getElement().setAttribute("readOnly", "true");
        hrtPanel.add(txtHelp);
        hrtPanel.setCellWidth(txtHelp, "100%");
        btnHelp.getElement().setAttribute("style", "width: 20px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-down");
        btnHelp.getElement().appendChild(aBtn.getElement());
        flwbtn.add(btnHelp);
        hrtPanel.add(flwbtn);
        hrtPanel.setCellWidth(flwbtn, "1px");
        flwform.add(hrtPanel);
    }
}
