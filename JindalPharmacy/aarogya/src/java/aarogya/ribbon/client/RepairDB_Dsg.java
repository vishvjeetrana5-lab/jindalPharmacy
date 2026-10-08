package aarogya.ribbon.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class RepairDB_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwChkRepairDB = new FlowPanel();
    final DmCheckBox chkRepairDB = new DmCheckBox();
    final FlowPanel flwWarning = new FlowPanel();
    final HTML htmlWarning = new HTML("<u><b>Warning:</u></b></br> "
            + "Exhaustive repair ! May take some additional time. It is highly recommended to please check logs after repair.", true);
    final HTML htmlMessage = new HTML("", true);

    public RepairDB_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("382px");
        popupPanel.setHeight("150px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(vl.add(btnClose));
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
//        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "110px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Are you sure ?");
//        lbl.getElement().getStyle().setMarginLeft(70, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("You want to Repair");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Transaction");
//        lbl.getElement().setAttribute("style", "font-weight:bold");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//        
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("database.");

        flwTd.add(htmlMessage);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "90px", "left", null, DmPosition.NONE, true);

        flwTr.add(bottomArea());
        flwTable.add(flwTr);
        return flwTable;

    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "58px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        flwChkRepairDB.getElement().setAttribute("style", "min-width:141px;padding:4px;margin-top:3px;");
        chkRepairDB.getElement().setAttribute("style", "margin-right:6px;margin-top:-1px");
        flwChkRepairDB.add(chkRepairDB);
        lbl = new DmLabel("Exhaustive Repair");
        flwChkRepairDB.add(lbl);

        flwTd.add(flwChkRepairDB);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        htmlWarning.getElement().setAttribute("style", "color:red;");
        flwWarning.add(htmlWarning);
        flwTd.add(flwWarning);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;

    }

}
