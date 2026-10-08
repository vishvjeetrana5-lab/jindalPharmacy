package redirect.changePassword.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ChangePassword_Dsg extends IsWidgetExtended {

    public final static String widgetName = "Change Password";
    final DmUserControl userControl = new DmUserControl();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtUserName = new DmTextBox();
    final PasswordTextBox txtNewPassword = new PasswordTextBox();
    final PasswordTextBox txtConfirmPassword = new PasswordTextBox();

    final Button btnSave = new Button();
    final Button btnClose = new Button();

    public ChangePassword_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("150px");

        vrtInnerForm.add(vl.add(toolContainer));
        vrtInnerForm.setCellWidth(toolContainer, "20%");

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(section1());
        vrtInnerForm.add(flw);

        txtNewPassword.setMaxLength(15);
        txtConfirmPassword.setMaxLength(15);

        txtUserName.setEnabled(false);

        txtNewPassword.getElement().getStyle().setBorderColor("lightgray");
        txtNewPassword.getElement().setAttribute("style", txtNewPassword.getElement().getAttribute("style") + "; border-left: 1px solid red;");

        txtConfirmPassword.getElement().getStyle().setBorderColor("lightgray");
        txtConfirmPassword.getElement().setAttribute("style", txtConfirmPassword.getElement().getAttribute("style") + "; border-left: 1px solid red;");

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
        createToolControl();

    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o ");
        aBtn.getElement().getStyle().setColor("black");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "40%", "60px", null, null, DmPosition.CENTER, true);

        ///////////////////////////TR-1/////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtUserName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        ///////////////////////////TR-2/////////////////////////////////////////////////////////       
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("New Password");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtNewPassword));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        ///////////////////////////TR-3/////////////////////////////////////////////////////////       
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtConfirmPassword));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    }
}
