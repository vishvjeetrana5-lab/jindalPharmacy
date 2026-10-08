package dataman.dmToolkit.dmRemarkTextBox.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

class DmRemarkTextBox_Dsg {
    
    final FlowPanel flwform = new FlowPanel();
    final DmTextBox txtRemark = new DmTextBox();
    final Button btnRemark = new Button();
    final FlowPanel flwbtn = new FlowPanel();
    final DmRemarkPopup remarkPopup = new DmRemarkPopup();

    public DmRemarkTextBox_Dsg() {
        HorizontalPanel hrtPanel = new HorizontalPanel();

        flwform.getElement().setAttribute("style", "border: 1px solid lightgray; float:left; overflow: hidden; width:100%;");
        hrtPanel.setHeight("100%");
//        txtRemark.setReadOnly(true);
        txtRemark.getElement().setAttribute("style", "border: 0px none; width: 100%;");
        hrtPanel.add(txtRemark);
        hrtPanel.setCellWidth(txtRemark, "100%");
        btnRemark.getElement().setAttribute("style", "width: 20px; border:none; border-left:1px solid gray; float:left; padding-top: 0px; height:18px;");
        btnRemark.setText("...");
        flwbtn.add(btnRemark);
        hrtPanel.add(flwbtn);
        hrtPanel.setCellWidth(flwbtn, "1px");

        flwform.add(hrtPanel);
    }

}
