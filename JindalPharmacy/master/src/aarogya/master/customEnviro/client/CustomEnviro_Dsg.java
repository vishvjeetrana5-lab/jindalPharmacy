package aarogya.master.customEnviro.client;

import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;

abstract class CustomEnviro_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CUSTOM_ENVIRO.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSave = new Button();

    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmTextBoxNumeric txtUnApproveUptoDays = new DmTextBoxNumeric(1, false, 3, false);
    final DmTextBoxNumeric txtEditUptoDays = new DmTextBoxNumeric(1, false, 3, false);

    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHIsPbillMrpLessthenPo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsPbillMrpLessthenPo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHIsPbillMrpLessthenPo() {
        txtHIsPbillMrpLessthenPo.getDmGrid().addTextColumn(txtHIsPbillMrpLessthenPo.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsPbillMrpLessthenPo.getDmGrid().initializeGrid(txtHIsPbillMrpLessthenPo.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsPbillMrpLessthenPo.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>   

    public CustomEnviro_Dsg() {
        iniTxtHIsPbillMrpLessthenPo();

        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("500px");
        vrtInnerForm.add(toolContainer);
        createToolControl();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "550px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "552px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Export");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        // aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");

    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "460px", null, null, DmPosition.NONE, true);

        tabPanel.add(vl.add(General()), "General");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel General() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "321px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unapprove Purchase Order Upto Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnApproveUptoDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2 th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Order Edit Upto Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEditUptoDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 3 th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PBill Mrp Less Then PO");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsPbillMrpLessthenPo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
