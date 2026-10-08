package aarogya.selfToolkit.appointmentSearch.client;

import aarogya.selfToolkit.appointmentSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class AppointmentSearch_Dsg {

    public final static String widgetName = "Appointment Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmTextBox txtFromDate = new DmTextBox(11, false);
    final DmTextBox txtToDate = new DmTextBox(11, false);

    public AppointmentSearch_Dsg() {
        ini();
        iniGrdAppoinmentDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("900px");
        popupPanel.setHeight("345px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-gears");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.setTitle("Setting");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");
        flwSetting.add(btnSetting);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "315px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "290px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridAppoinmentDetail));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "63%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Token No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtFromDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtToDate);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Appoinment Detail">
    interface GrdPaging_PrpAcs extends KeyField_T_PrpAcs<GrdAppointmentDetails_Mdl> {

        @Editor.Path("DateTime")
        ValueProvider<GrdAppointmentDetails_Mdl, String> dateTime();

        @Editor.Path("PatientName")
        ValueProvider<GrdAppointmentDetails_Mdl, String> patientName();

        @Editor.Path("PriorityType")
        ValueProvider<GrdAppointmentDetails_Mdl, String> priorityType();

        @Editor.Path("Mobile")
        ValueProvider<GrdAppointmentDetails_Mdl, String> mobile();

        @Editor.Path("UhidNo")
        ValueProvider<GrdAppointmentDetails_Mdl, String> uhidNo();

        @Editor.Path("AppoinmentNo")
        ValueProvider<GrdAppointmentDetails_Mdl, String> appoinmentNo();

        @Editor.Path("RegNo")
        ValueProvider<GrdAppointmentDetails_Mdl, String> regNo();

        @Editor.Path("Status")
        ValueProvider<GrdAppointmentDetails_Mdl, String> status();

        @Editor.Path("ConsultantName")
        ValueProvider<GrdAppointmentDetails_Mdl, String> consultantName();

    }
    final DmGridPaging<GrdAppointmentDetails_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridAppoinmentDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 12, false);

    private void iniGrdAppoinmentDetail() {
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().dateTime(), "Date-Time", 110, "dTime");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 1, "patientName");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().mobile(), "Mobile", 75, "mobile");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().priorityType(), "Priority", 45, "priorityType");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().status(), "Status", 65, "");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().consultantName(), "Consultant", 100, "consultantName");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().uhidNo(), "UHID No.", 100, "uhidNo");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().appoinmentNo(), "Appoinment No.", 100, "appoinmentNo");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().regNo(), "Reg No.", 100, "RegNo");
        gridAppoinmentDetail.getDmGrid().addTextColumn(gridAppoinmentDetail.getDmGrid().getPropertyAccess().name(), "Token No.", 60, "tokenNo");
        gridAppoinmentDetail.getDmGrid().initializeGrid(gridAppoinmentDetail.getDmGrid().getPropertyAccess().code(), 880, 1, 2);

        gridAppoinmentDetail.getHelper().addSort("v_Date", DmSortType.ASC, "v_Date");
        gridAppoinmentDetail.getHelper().addSort("timeFrom", DmSortType.ASC, "timeFrom");
        gridAppoinmentDetail.getHelper().addSort("tokenNo", DmSortType.ASC, "tokenNo");
    }
    //</editor-fold>

}
