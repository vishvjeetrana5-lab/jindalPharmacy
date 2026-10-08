package aarogya.nabh.roomHelp.client;

import aarogya.nabh.roomHelp.shared.GrdRoomHelp_Mdl;
import aarogya.nabh.roomHelp.shared.PatientHistorySearch_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class RoomHelp_Dsg extends IsWidgetExtended {

    public String widgetNameDisplay = "";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtPatient = new DmTextBox(50, false);
    final DmTextBox txtGender = new DmTextBox(50, false);
    final DmTextBox txtGuardian = new DmTextBox(50, false);
    final DmTextBox txtUHIDNo = new DmTextBox(20, false);
    final DmTextBox txtAddress = new DmTextBox(200, false);
    final DmTextBox txtAge = new DmTextBox(15, false);
    final DmTextBox txtMobile = new DmTextBox(35, false);
    final DmTextBox txtRegConsultant = new DmTextBox(50, false);
    final DmTextBox txtOrganization = new DmTextBox(50, false);
    final DmTextBox txtCase = new DmTextBox(50, false);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    final DmTextBox txtIpdNo = new DmTextBox(20, true);
    final DmTextBox txtDaysOPD = new DmTextBox(150, false);
    final DmTextBoxTime txtAdmittingTime = new DmTextBoxTime(false);
    final DmTextBox txtDtOPD = new DmTextBox(11, false);

    final DmTextBox txtAdmittingDate = new DmTextBox(11, true);
    final DmTextBox txtRoomNo = new DmTextBox(150, true);
    final Button btnPatientSearch = new Button("...");
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwPatientSearch = new FlowPanel();

    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwNew = new FlowPanel();
    final FlowPanel flwTr = new FlowPanel();
    final Button btnClose = new Button("");
    final Button btnNew = new Button("");
    final DmLabel cancelBy = new DmLabel("");

    public RoomHelp_Dsg() {
        ini();
        iniToolContorls();
        iniGrdGUIEntry();
        iniTxtHGUI();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("498px");
        vrtInnerForm.add(toolContainer);

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("New");
        label.setWidth("35px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("green");
        btnNew.getElement().appendChild(aBtn.getElement());
        btnNew.getElement().appendChild(label.getElement());
        btnNew.setWidth("60px");
        flwNew.add(btnNew);

        label = new InlineLabel("Close");
        label.setWidth("35px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "460px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(topSection());
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        // UHID Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, true);
        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; width 98%; height: 35px;");
        cPnl.add(uhidSection());
        flwTr.add(cPnl);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        // Registration Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", "10px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftBottom());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31.5%", "10px", "left", null, DmPosition.NONE, true);
        flwTd.add(middleBottom());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", "10px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightBottom());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "360px", "left", null, DmPosition.NONE, true);
        flwTr.add(SearchHistoryTabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1st Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room No.");
        lbl.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtRoomNo));
        hrtPnl.setCellWidth(txtRoomNo, "100%");
        DmClient.setStyle(flwPatientSearch, "100%", "20px", "left", null, DmPosition.NONE, true);
        btnPatientSearch.setWidth("20px");
        btnPatientSearch.setHeight("20px");
        btnPatientSearch.getElement().setAttribute("style", btnPatientSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPatientSearch.setTitle("Room Search");
        flwPatientSearch.add(btnPatientSearch);
        hrtPnl.add(flwPatientSearch);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IPD No.");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtIpdNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Admitting Date");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdmittingDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdmittingTime));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((cancelBy));
        cancelBy.getElement().getStyle().setColor("red");
        cancelBy.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        cancelBy.getElement().getStyle().setMarginLeft(51, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel uhidSection() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "48px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1st Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "48px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftUhid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(middleUhid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightUhid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatient));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian Name");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGuardian));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel middleUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAge));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUHIDNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftBottom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        ////////////////////////////// 1st Row / ////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "61%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel middleBottom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        ////////////////// 1st Row ////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrganization));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightBottom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        //////////////////////////////////////// 1st Row ////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Case");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "69.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCase));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "270px", null, null, DmPosition.NONE, true);
        tabPanel.add(SearchHistoryTabArea(), "Search History");

        tabPanel.selectTab(0);

        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel SearchHistoryTabArea() {
        FlowPanel flwTable, flwTr, flwTd;

        HorizontalPanel hrtPanel;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "336px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setBackgroundColor("lightgray");
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchValue));
        txtSearchValue.setHeight("21px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "right", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("lightgray");
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTd.add(hrtPanel);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "336px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdGuiEntry);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="txtHGUI">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHGUI = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHGUI() {
        txtHGUI.getDmGrid().addTextColumn(txtHGUI.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGUI.getDmGrid().initializeGrid(txtHGUI.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHGUI.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="grdGuiEntry">
    interface GrdGUIEntry_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRoomHelp_Mdl> {

        @Editor.Path("Date")
        ValueProvider<GrdRoomHelp_Mdl, String> date();

        @Editor.Path("Time")
        ValueProvider<GrdRoomHelp_Mdl, String> time();

        @Editor.Path("TemplateName")
        ValueProvider<GrdRoomHelp_Mdl, String> templateName();

        @Editor.Path("LastModifiedBy")
        ValueProvider<GrdRoomHelp_Mdl, String> lastModifiedBy();

        @Editor.Path("GuiName")
        ValueProvider<GrdRoomHelp_Mdl, String> guiName();

        @Editor.Path("U_Name")
        ValueProvider<GrdRoomHelp_Mdl, String> u_Name();

        @Editor.Path("PreparedBy")
        ValueProvider<GrdRoomHelp_Mdl, String> preparedBy();

        @Editor.Path("RefRecId")
        ValueProvider<GrdRoomHelp_Mdl, String> refRecId();
    }
    final DmGridPaging<GrdRoomHelp_Mdl, GrdGUIEntry_PrpAcs, PatientHistorySearch_Mdl> grdGuiEntry = new DmGridPaging(GWT.create(GrdGUIEntry_PrpAcs.class), 13, true);

    private void iniGrdGUIEntry() {
        grdGuiEntry.getDmGrid().addRowNumberColumn();
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().date(), "Date", 80, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().time(), "Time", 45, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().refRecId(), "Reference Name", 170, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().templateName(), "Template Name", 165, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().guiName(), "GUI Name", 170, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().lastModifiedBy(), "Modified Date/Time", 115, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 80, "");
        grdGuiEntry.getDmGrid().addTextColumn(grdGuiEntry.getDmGrid().getPropertyAccess().u_Name(), "Modified By", 75, "");

        grdGuiEntry.getDmGrid().initializeGrid(grdGuiEntry.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 336, 5);

        grdGuiEntry.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdRoomHelp_Mdl>() {

            @Override
            public String getColStyle(GrdRoomHelp_Mdl model, ValueProvider<? super GrdRoomHelp_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getColorStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRoomHelp_Mdl model, int rowIndex) {
                return "";
            }
        });
    }
    //</editor-fold>

}
