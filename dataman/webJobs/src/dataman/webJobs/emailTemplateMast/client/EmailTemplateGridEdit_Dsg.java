package dataman.webJobs.emailTemplateMast.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class EmailTemplateGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtProcName = new DmTextBox(100, true);
    final DmTextBox txtRptName = new DmTextBox(100, true);
    final DmTextBox txtFileName = new DmTextBox(100, true);
    final Button btnProcedure = new Button();
    final Button btnRpt = new Button();
    final DmTextArea txtProcedure = new DmTextArea();
    final DmTextArea txtRpt = new DmTextArea();
    final PopupPanel pnlGrd2 = new PopupPanel(true);
    final PopupPanel pnlGrd3 = new PopupPanel(true);

    public EmailTemplateGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        pnlGrd2.setWidth("475px");
        pnlGrd2.setHeight("185px");
        pnlGrd2.hide();

        pnlGrd3.setWidth("325px");
        pnlGrd3.setHeight("100px");
        pnlGrd3.hide();

        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;
        HorizontalPanel hrtPnl1 = new HorizontalPanel();
        HorizontalPanel hrtPnl2 = new HorizontalPanel();

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "60px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////////////////////////1st///////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Procedure Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(hrtPnl1, "100%", "100%", null, null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-question-circle");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnProcedure.setTitle("Procedure");
        btnProcedure.addStyleName("dm-button-grad");
        btnProcedure.getElement().appendChild(aBtn.getElement());
        btnProcedure.getElement().getStyle().setWidth(24, Style.Unit.PX);
        btnProcedure.getElement().getStyle().setHeight(20, Style.Unit.PX);
        hrtPnl1.add(btnProcedure);
        hrtPnl1.add(vl.add(txtProcName));
        hrtPnl1.setCellWidth(txtProcName, "100%");
        flwTd.add(hrtPnl1);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ////////////////////////////////////////////////////2nd///////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("RPT Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(hrtPnl2, "100%", "100%", null, null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-question-circle");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRpt.setTitle("RPT Name");
        btnRpt.addStyleName("dm-button-grad");
        btnRpt.getElement().appendChild(aBtn.getElement());
        btnRpt.getElement().getStyle().setWidth(24, Style.Unit.PX);
        btnRpt.getElement().getStyle().setHeight(20, Style.Unit.PX);
        hrtPnl2.add(btnRpt);
        hrtPnl2.add(vl.add(txtRptName));
        hrtPnl2.setCellWidth(txtRptName, "100%");
        flwTd.add(hrtPnl2);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        ////////////////////////////////////////////////////3rd///////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("File Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "69%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFileName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
