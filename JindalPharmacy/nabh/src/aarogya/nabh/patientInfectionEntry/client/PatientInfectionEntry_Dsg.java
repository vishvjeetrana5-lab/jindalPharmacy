package aarogya.nabh.patientInfectionEntry.client;

import aarogya.nabh.patientInfectionEntry.shared.*;
import aarogya.projectMenu.self.shared.pmNABH;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PatientInfectionEntry_Dsg extends IsWidgetExtended {

    public static final String widgetName = new pmNABH().PATIENT_INFECTION_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtUHID = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(12, true);
    final DmTextBox txtPatientName = new DmTextBox(50, false);
    final DmTextBox txtGuardianName = new DmTextBox(50, false);
    final DmTextBox txtAddress = new DmTextBox(50, false);
    final DmTextBox txtMobile = new DmTextBox(35, false);
    final DmTextBox txtAge = new DmTextBox(11, false);
    final DmTextBoxTime txtVouchertTime = new DmTextBoxTime(true);
    final DmTextBox txtGender = new DmTextBox(15, false);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtRegNo = new DmTextBox(20, true);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final FlowPanel flwSearchRoom = new FlowPanel();
    final Button btnSearchRoom = new Button("...");
    final Button btnOPDSearch = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="txtHRegType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRegType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHOldRegType() {
        txtHRegType.getDmGrid().addTextColumn(txtHRegType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRegType.getDmGrid().initializeGrid(txtHRegType.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHRegType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHInfectionType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInfectionType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHInfectionType() {
        txtHInfectionType.getDmGrid().addTextColumn(txtHInfectionType.getDmGrid().getPropertyAccess().name(), "Infection Type", 615, "infectionType");
        txtHInfectionType.getDmGrid().initializeGrid(txtHInfectionType.getDmGrid().getPropertyAccess().code(), 630, 1);
        txtHInfectionType.getHelper().addSort("infectionType", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHServiceType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHServiceType() {
        txtHServiceType.getDmGrid().addTextColumn(txtHServiceType.getDmGrid().getPropertyAccess().name(), "Service Type", 0, "name");
        txtHServiceType.getDmGrid().initializeGrid(txtHServiceType.getDmGrid().getPropertyAccess().code(), 215, 0, 1);
        txtHServiceType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("RegNo")
        ValueProvider<Find_Mdl, String> regNo();

        @Editor.Path("PatientName")
        ValueProvider<Find_Mdl, String> patientName();

        @Editor.Path("RegDate")
        ValueProvider<Find_Mdl, String> regDate();

        @Editor.Path("InfectionType")
        ValueProvider<Find_Mdl, String> infectionType();

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("Time")
        ValueProvider<Find_Mdl, String> time();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 13);

    private void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().regNo(), "Reg No.", 120, "uhidRecId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().regDate(), "Reg Date", 90, "regDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 330, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().infectionType(), "Infection Type", 350, "infectionType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().time(), "Time", 80, "v_Time");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 250, "remark");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0, 4);
        dmFind.getHelper().addSort("uhidRecId", DmSortType.DESC);
        dmFind.getHelper().addSort("regDate", DmSortType.DESC);

    }
    //</editor-fold>

    public PatientInfectionEntry_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        iniFind();
        iniTxtHOldRegType();
        iniTxtHInfectionType();
        iniTxtHServiceType();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "260px", null, null, DmEnum.DmPosition.CENTER, true);

        /*========================1st row=========================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "135px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTR.add(topArea());

        flwTable.add(flwTR);

        /*=======================2nd row============================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "120px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTR.add(iniPatientInfectionEntry());

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel topArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "128px", null, null, DmPosition.CENTER, true);

        //=================================Row-1=============================================
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "border-bottom:solid 1px gray; height:25px;");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reg Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRegType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(txtRegNo);
        hrtPnl.setCellWidth(txtRegNo, "100%");
        DmClient.setStyle(flwSearchRoom, "100%", "20px", "left", null, DmPosition.NONE, true);

        btnSearchRoom.setWidth("20px");
        btnSearchRoom.setHeight("20px");

        btnSearchRoom.getElement().setAttribute("style", btnSearchRoom.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSearchRoom.addStyleName("dm-button-grad");
        btnSearchRoom.setTitle("Patient Search");

        flwSearchRoom.add(btnSearchRoom);

        btnOPDSearch.setWidth("20px");
        btnOPDSearch.setHeight("20px");
        btnOPDSearch.getElement().setAttribute("style", btnOPDSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnOPDSearch.addStyleName("dm-button-grad");
        btnOPDSearch.setTitle("Patient Search (OPD)");
        flwSearchRoom.add(btnOPDSearch);

        hrtPnl.add(flwSearchRoom);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date & Time");
        lbl.getElement().getStyle().setMarginLeft(334, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "right", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVouchertTime));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "right", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=================================Row-3=============================================   
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", " margin-top:5px; ");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatientName));
        flwTr.add(flwTd);

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian's Name");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGuardianName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUHID));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //=================================Row-5=============================================
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "border-bottom:solid 1px gray; height:23px;");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "53%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAge));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=================================Row-6=============================================
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "margin-top:5px;height:22px; ");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Infection Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "53%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInfectionType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceType));
        flwTr.add(flwTd);

        //=================================Row-7=============================================
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "88%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    //<editor-fold defaultstate="collapsed" desc="patientInfectionDetail Grid">
    interface GrdEmpDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPatientInfectionEntry_Mdl> {

        @Editor.Path("Date")
        ValueProvider<GrdPatientInfectionEntry_Mdl, String> date();

        @Editor.Path("Time")
        ValueProvider<GrdPatientInfectionEntry_Mdl, String> time();

        @Editor.Path("PatientInfectionReaction")
        ValueProvider<GrdPatientInfectionEntry_Mdl, String> patientInfectionReaction();

        @Editor.Path("Service")
        ValueProvider<GrdPatientInfectionEntry_Mdl, String> service();

    }

    final DmGrid<GrdPatientInfectionEntry_Mdl, GrdEmpDetail_PrpAcs> grdpatientInfectionDetail = new DmGrid(GWT.create(GrdEmpDetail_PrpAcs.class));

    private Grid iniPatientInfectionEntry() {
        grdpatientInfectionDetail.addRowNumberColumn();
        grdpatientInfectionDetail.addTextColumn(grdpatientInfectionDetail.getPropertyAccess().date(), "Date", 125, null);
        grdpatientInfectionDetail.addTextColumn(grdpatientInfectionDetail.getPropertyAccess().time(), "Time", 60, null);
        grdpatientInfectionDetail.addTextColumn(grdpatientInfectionDetail.getPropertyAccess().patientInfectionReaction(), "Infection Type", 640, null);
        grdpatientInfectionDetail.addTextColumn(grdpatientInfectionDetail.getPropertyAccess().service(), "Service", 430, null);
        grdpatientInfectionDetail.initializeGrid(grdpatientInfectionDetail.getPropertyAccess().fieldHashCode(), 1280, 133);
        return grdpatientInfectionDetail.getGrid();
    }
    //</editor-fold>

}
