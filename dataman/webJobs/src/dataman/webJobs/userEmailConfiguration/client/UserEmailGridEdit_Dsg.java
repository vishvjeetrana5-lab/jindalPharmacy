package dataman.webJobs.userEmailConfiguration.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmEnum.*;

public abstract class UserEmailGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtProductLTo = new DmTextBox(50, false);
    final DmTextBox txtEmail = new DmTextBox(100, true);
    final DmTextBox txtCc = new DmTextBox(250, false);
    final DmTextBox txtBcc = new DmTextBox(250, false);
    final PasswordTextBox txtPassword = new PasswordTextBox();
    final PasswordTextBox txtConfirmPassword = new PasswordTextBox();
    final Button btnCc = new Button();
    final Button btnBcc = new Button();

    public UserEmailGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());

        txtPassword.setMaxLength(30);
        txtConfirmPassword.setMaxLength(30);

        txtPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        txtPassword.getElement().getStyle().setBorderColor("lightgray");
        txtPassword.getElement().setAttribute("style", txtPassword.getElement().getAttribute("style") + "; border-left: 1px solid red;");

        txtConfirmPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        txtConfirmPassword.getElement().getStyle().setBorderColor("lightgray");

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;
        DmLabel lbl;
        HorizontalPanel hrtPnl1, hrtPnl2;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "128px", null, null, DmPosition.CENTER, true);

        //////////////////////////Tr-1//////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Product License To");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtProductLTo));
        txtProductLTo.setEnabled(false);
        flwTr.add(flwTD);
        flwTable.add(flwTr);
        //////////////////////////Tr-2//////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEmail));
        flwTr.add(flwTD);
        flwTable.add(flwTr);
        /////////////////////tr-3//////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("CC");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        hrtPnl1 = new HorizontalPanel();
        DmClient.setStyle(hrtPnl1, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl1.add(vl.add(txtCc));
        hrtPnl1.setCellWidth(txtCc, "100%");
        txtCc.setEnabled(false);
        btnCc.setText("...");
        btnCc.setWidth("20px");
        btnCc.setHeight("20px");
        btnCc.getElement().setAttribute("style", btnCc.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnCc.setTitle("Allergy");
        hrtPnl1.add(btnCc);
        flwTD.add(hrtPnl1);

        flwTr.add(flwTD);
        flwTable.add(flwTr);
        ////////////////////////////tr-4///////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("BCC");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        hrtPnl2 = new HorizontalPanel();
        DmClient.setStyle(hrtPnl2, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl2.add(vl.add(txtBcc));
        hrtPnl2.setCellWidth(txtBcc, "100%");
        txtBcc.setEnabled(false);
        btnBcc.setText("...");
        btnBcc.setWidth("20px");
        btnBcc.setHeight("20px");
        btnBcc.getElement().setAttribute("style", btnBcc.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnBcc.setTitle("Allergy");
        hrtPnl2.add(btnBcc);
        flwTD.add(hrtPnl2);
        flwTr.add(flwTD);
        flwTable.add(flwTr);
        /////////////////////////tr-5///////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Password");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPassword));

        flwTr.add(flwTD);
        flwTable.add(flwTr);
        ////////////////////////tr-6////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "67%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtConfirmPassword));

        flwTr.add(flwTD);
        flwTable.add(flwTr);

        return flwTable;
    }

}
