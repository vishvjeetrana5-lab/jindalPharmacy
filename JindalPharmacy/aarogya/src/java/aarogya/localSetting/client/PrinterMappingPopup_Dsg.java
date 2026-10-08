package aarogya.localSetting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PrinterMappingPopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnSave = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwFillBack = new FlowPanel();
    public String popupTitle = "";
    final Button btnCancel = new Button();

    final Button btnPrnt1 = new Button("...");
    final Button btnPrnt2 = new Button("...");
    final Button btnPrnt3 = new Button("...");
    final Button btnPrnt4 = new Button("...");
    final Button btnPrnt5 = new Button("...");

    final DmCheckBox chkBoxPrntr1 = new DmCheckBox();
    final DmCheckBox chkBoxPrntr2 = new DmCheckBox();
    final DmCheckBox chkBoxPrntr3 = new DmCheckBox();
    final DmCheckBox chkBoxPrntr4 = new DmCheckBox();
    final DmCheckBox chkBoxPrntr5 = new DmCheckBox();

    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter1">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrinter1 = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHBarcode() {
        txtHPrinter1.getDmGrid().addTextColumn(txtHPrinter1.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrinter1.getDmGrid().initializeGrid(txtHPrinter1.getDmGrid().getPropertyAccess().code(), 345, 0, 1);

        txtHPrinter1.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter2">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrinter2 = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHReceipt() {
        txtHPrinter2.getDmGrid().addTextColumn(txtHPrinter2.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrinter2.getDmGrid().initializeGrid(txtHPrinter2.getDmGrid().getPropertyAccess().code(), 345, 0, 1);

        txtHPrinter2.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter3">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrinter3 = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHPrescription() {
        txtHPrinter3.getDmGrid().addTextColumn(txtHPrinter3.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrinter3.getDmGrid().initializeGrid(txtHPrinter3.getDmGrid().getPropertyAccess().code(), 345, 0, 1);

        txtHPrinter3.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter4">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrinter4 = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHPrinter4() {
        txtHPrinter4.getDmGrid().addTextColumn(txtHPrinter4.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrinter4.getDmGrid().initializeGrid(txtHPrinter4.getDmGrid().getPropertyAccess().code(), 345, 0, 1);

        txtHPrinter4.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter5">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrinter5 = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHPrinter5() {
        txtHPrinter5.getDmGrid().addTextColumn(txtHPrinter5.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrinter5.getDmGrid().initializeGrid(txtHPrinter5.getDmGrid().getPropertyAccess().code(), 345, 0, 1);

        txtHPrinter4.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PrinterMappingPopup_Dsg() {
        iniTxtHBarcode();
        iniTxtHPrescription();
        iniTxtHReceipt();
        iniTxtHPrinter4();
        iniTxtHPrinter5();
        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("170px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

    }

    private DmToolContainer iniToolControls() {

        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.setTitle("Save");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");
        flwOk.add(vl.add(btnSave));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);

        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "115px", null, null, DmEnum.DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Printer-1");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Byte");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(chkBoxPrntr1);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrinter1));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);

        DmClient.setStyle(btnPrnt1, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnPrnt1.setWidth("20px");
        btnPrnt1.setHeight("20px");
        btnPrnt1.getElement().setAttribute("style", btnPrnt1.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrnt1.addStyleName("dm-button-grad");
        btnPrnt1.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);

        flwTd.add(btnPrnt1);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Printer-2");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Byte");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(chkBoxPrntr2);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrinter2));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        DmClient.setStyle(btnPrnt2, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnPrnt2.setWidth("20px");
        btnPrnt2.setHeight("20px");
        btnPrnt2.getElement().setAttribute("style", btnPrnt2.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrnt2.addStyleName("dm-button-grad");
        btnPrnt2.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(btnPrnt2);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Printer-3");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Byte");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(chkBoxPrntr3);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrinter3));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        DmClient.setStyle(btnPrnt3, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnPrnt3.setWidth("20px");
        btnPrnt3.setHeight("20px");
        btnPrnt3.getElement().setAttribute("style", btnPrnt3.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrnt3.addStyleName("dm-button-grad");
        btnPrnt3.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(btnPrnt3);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Printer-4");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Byte");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(chkBoxPrntr4);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrinter4));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        DmClient.setStyle(btnPrnt4, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnPrnt4.setWidth("20px");
        btnPrnt4.setHeight("20px");
        btnPrnt4.getElement().setAttribute("style", btnPrnt4.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrnt4.addStyleName("dm-button-grad");
        btnPrnt4.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(btnPrnt4);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Printer-5");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Byte");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(chkBoxPrntr5);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrinter5));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        DmClient.setStyle(btnPrnt5, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnPrnt5.setWidth("20px");
        btnPrnt5.setHeight("20px");
        btnPrnt5.getElement().setAttribute("style", btnPrnt5.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrnt5.addStyleName("dm-button-grad");
        btnPrnt5.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd.add(btnPrnt5);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
