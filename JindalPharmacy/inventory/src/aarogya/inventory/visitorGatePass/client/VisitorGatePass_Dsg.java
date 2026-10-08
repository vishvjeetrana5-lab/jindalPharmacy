package aarogya.inventory.visitorGatePass.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmSnapshot.client.DmSnapshot;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.inventory.visitorGatePass.shared.*;
import aarogya.projectMenu.self.shared.pmInventory;

abstract public class VisitorGatePass_Dsg extends IsWidgetExtended {

    public final static String moduleName = new pmInventory().moduleName;
    public final static String widgetName = new pmInventory().VISITOR_GATE_PASS.caption;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String dateTimeEditableBlog = "Date Time Editable";

    public final static String gateOutByBlog = "Gate Out By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtEntryNo = new DmTextBox(20, false);
    final DmTextBox txtDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtAddress = new DmRemarkTextBox(250, true);
    final DmRemarkTextBox txtPurpose = new DmRemarkTextBox(100, true);
    final DmTextBoxTime txtTime = new DmTextBoxTime(true);
    final DmTextBox txtVisitorName = new DmTextBox(100, true);
    final DmTextBox txtOrganizationName = new DmTextBox(100, false);
    final DmTextBox txtVehicleNo = new DmTextBox(30, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmTextBox txtPersonToMeet = new DmTextBox(100, true);
    final DmTextBox txtGateOutDate = new DmTextBox(11, true);
    final DmTextBox txtSlipNo = new DmTextBox(11, false);
    final DmTextBoxTime txtGateOutTime = new DmTextBoxTime(true);
    final DmTextBox txtGateOutDateView = new DmTextBox(11, false);
    final DmTextBoxTime txtGateOutTimeView = new DmTextBoxTime(false);
    final FlowPanel flwGateOutContainer = new FlowPanel();
    final FlowPanel flwGateOutEntry = new FlowPanel();
    final FlowPanel flwGateOutDetail = new FlowPanel();
    final FlowPanel flwSearchByMobile = new FlowPanel();
    protected final FlowPanel flwSearchGatePass = new FlowPanel();
    protected final Button btnSearchGatePass = new Button();
    protected final FlowPanel flwSearchTime = new FlowPanel();
    protected final FlowPanel flwInDateTime = new FlowPanel();
    protected final Button btnGateDateTime = new Button();
    protected final Button btnGateInDateTime = new Button();
    protected final Button btnSearchByMobile = new Button();
    
    final Button btnGateOutBy = new Button();
    final FlowPanel flwInDateTimeContainer = new FlowPanel();
    DmFileRuleType fileRuleType;
    DmSnapshot snapshot;

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
   
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHGateNo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGateNo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHGateNo() {
        txtHGateNo.setAppendable(true);
        txtHGateNo.getDmGrid().addTextColumn(txtHGateNo.getDmGrid().getPropertyAccess().name(), "Gate No.", 0, "gateNo");
        txtHGateNo.getDmGrid().initializeGrid(txtHGateNo.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHGateNo.getHelper().addSort("gateNo", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface txtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();
    }

    final DmTextHelp<TxtHCity_Mdl, txtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(txtHCity_PrpAcs.class), 5, true);

    public void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 100, "cityname");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "statename");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHCity.getHelper().addSort("cityname", DmEnum.DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="iniTxtHVehicleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVehicleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    public void iniTxtHVehicleType() {
        txtHVehicleType.setAppendable(true);
        txtHVehicleType.getDmGrid().addTextColumn(txtHVehicleType.getDmGrid().getPropertyAccess().name(), "Vehicle Type", 0, "vehicleType");
        txtHVehicleType.getDmGrid().initializeGrid(txtHVehicleType.getDmGrid().getPropertyAccess().code(), 165, 0, 1);
        txtHVehicleType.getHelper().addSort("vehicleType", DmEnum.DmSortType.ASC);

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("InDate")
        ValueProvider<Find_Mdl, String> inDate();

        @Editor.Path("InTime")
        ValueProvider<Find_Mdl, String> inTime();

        @Editor.Path("GateOutDate")
        ValueProvider<Find_Mdl, String> gateOutDate();

        @Editor.Path("GateOutTime")
        ValueProvider<Find_Mdl, String> gateOutTime();

        @Editor.Path("VisitorName")
        ValueProvider<Find_Mdl, String> visitorName();

        @Editor.Path("Address")
        ValueProvider<Find_Mdl, String> address();

        @Editor.Path("CityName")
        ValueProvider<Find_Mdl, String> cityName();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

        @Editor.Path("VehicleType")
        ValueProvider<Find_Mdl, String> vehicleType();

        @Editor.Path("VehicleNo")
        ValueProvider<Find_Mdl, String> vehicleNo();

        @Editor.Path("PersonToMeet")
        ValueProvider<Find_Mdl, String> personToMeet();

        @Editor.Path("Purpose")
        ValueProvider<Find_Mdl, String> purpose();
        
        @Editor.Path("Organization")
        ValueProvider<Find_Mdl, String> organization();
        
        @Editor.Path("GateNo")
        ValueProvider<Find_Mdl, String> gateNo();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Gate Pass No.", 85, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().inDate(), "In Date", 75, "replace(convert(varchar, v_Date, 106), ' ','-')", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().inTime(), "In Time", 55, "v_Time", DmEnum.DmDataType.DOUBLE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gateOutDate(), "Out Date", 75, "replace(convert(varchar, gateOutDate, 106), ' ','-')", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gateOutTime(), "Out Time", 55, "gateOutTime", DmEnum.DmDataType.DOUBLE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().visitorName(), "Visitor Name", 130, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().organization(), "Organization", 130, "organization");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gateNo(), "Gate No.", 85, "gateNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address(), "Address", 200, "address");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vehicleType(), "Vehicle Type ", 100, "vehicleType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vehicleNo(), "Vehicle No.", 90, "vehicleNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().personToMeet(), "Person To Meet", 130, "personToMeet");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().purpose(), "Purpose", 150, "purpose");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1550, 183, 9);
        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("v_Time", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public VisitorGatePass_Dsg() {
        iniFind();
        ini();
        iniTxtHVoucherType();
        iniTxtHGateNo();

    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        iniTxtHCity();
        iniTxtHVehicleType();
        
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }
    
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "220px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", "220px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(leftWorkingArea());
        flwTable.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "160px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(rightSideWorkingArea());
        flwTable.add(flwTd);
        return flwTable;
    }

    private FlowPanel leftWorkingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62%", "220px", null, null, DmEnum.DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Gate Pass Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //===============================< ******* >===============================//
        //===============================< 2nd Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date/Time");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "0.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTime));
        flwTr.add(flwTd);

        DmClient.setStyle(flwInDateTimeContainer, "3.1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwInDateTimeContainer.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        aBtn = new Anchor();
//        aBtn.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        aBtn.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        aBtn.getElement().getStyle().setPaddingLeft(1, Style.Unit.PX);
        aBtn.addStyleName("fa fa-clock-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        flwInDateTime.setTitle("Get Date Time");
        btnGateInDateTime.getElement().appendChild(aBtn.getElement());
        btnGateInDateTime.setWidth("26px");
        btnGateInDateTime.setHeight("20px");
        btnGateInDateTime.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        flwInDateTime.add((btnGateInDateTime));
        flwInDateTimeContainer.add(flwInDateTime);
        flwTr.add(flwInDateTimeContainer);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Gate Pass No.");
        lbl.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
         //===============================< ******* >===============================//
         //===============================< Search By Mobile >===============================//
         flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        
        DmClient.setStyle(flwSearchByMobile, "3%", null, "left", null, DmEnum.DmPosition.NONE, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSearchByMobile.setTitle("Search visitor gate pass");
        btnSearchByMobile.getElement().appendChild(aBtn.getElement());
        btnSearchByMobile.setWidth("27px");
        btnSearchByMobile.setHeight("20px");
        btnSearchByMobile.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        flwSearchByMobile.add((btnSearchByMobile));
        flwTr.add(flwSearchByMobile);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Gate No.");
        lbl.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGateNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
         
        //===============================< ******* >===============================//
        
        //===============================< organization >===============================//        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Visitor Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVisitorName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        
        //===============================< ******* >===============================//
        
       
        //===============================< 3rd Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Organization Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrganizationName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 4rth >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 5th Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Mobile");
//        lbl.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtMobile));
//        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //===============================< 6 Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Vehicle Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVehicleType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Vehicle No.");
        lbl.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVehicleNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 7th Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Person To Meet");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPersonToMeet));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 8th Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Purpose");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurpose));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 9th Row >===============================//        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(gateOutEntryArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //===============================< ******* >===============================//
        return flwTable;
    }

    private FlowPanel gateOutEntryArea() {
        FlowPanel flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;
        InlineLabel label;

        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Slip No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSlipNo));
        flwTr.add(flwTd);

        DmClient.setStyle(flwSearchGatePass, "3%", null, "left", null, DmEnum.DmPosition.NONE, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSearchGatePass.setTitle("Search visitor gate pass");
        btnSearchGatePass.getElement().appendChild(aBtn.getElement());
        btnSearchGatePass.setWidth("27px");
        btnSearchGatePass.setHeight("20px");
        btnSearchGatePass.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        flwSearchGatePass.add((btnSearchGatePass));
        flwTr.add(flwSearchGatePass);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Gate Out Date/Time");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtGateOutDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "0.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtGateOutTime));
        flwTr.add(flwTd);

        DmClient.setStyle(flwSearchTime, "3.1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwSearchTime.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        aBtn = new Anchor();
//        aBtn.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        aBtn.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        aBtn.getElement().getStyle().setPaddingLeft(1, Style.Unit.PX);
        aBtn.addStyleName("fa fa-clock-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        flwSearchTime.setTitle("Search Date Time");
        btnGateDateTime.getElement().appendChild(aBtn.getElement());
        btnGateDateTime.setWidth("26px");
        btnGateDateTime.setHeight("20px");
        btnGateDateTime.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        flwSearchTime.add((btnGateDateTime));

        flwTr.add(flwSearchTime);

        //===============================< ******* >===============================//
        flwGateOutEntry.add(flwTr);
        flwGateOutContainer.add(flwGateOutEntry);
        gateOutDetailArea();

        return flwGateOutContainer;
    }

    private void gateOutDetailArea() {
        FlowPanel flwTr, flwTd;
        DmLabel lbl;

        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Out Date/Time");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtGateOutDateView));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "0.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtGateOutTimeView));
        flwTr.add(flwTd);

        //===============================< ******* >===============================//
        flwGateOutDetail.add(flwTr);
    }

    private DmUserControl createBottom() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(gateOutByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-sign-out");
        aBtn.getElement().getStyle().setColor("green");
        btnGateOutBy.getElement().appendChild(aBtn.getElement());
        btnGateOutBy.setWidth("30px");
        userControl.addControlBlog(gateOutByBlog, btnGateOutBy);
        return userControl;
    }
    
    private FlowPanel imageArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "150px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, null, "150px", "right", null, DmEnum.DmPosition.NONE, false);
        fileRuleType = new DmFileRuleType();
        fileRuleType.setRuleCode("VGP");
        fileRuleType.setFileTypeName("Snapshot");
        fileRuleType.setFileSizeLimit("5120");
        fileRuleType.setFilters("jpg,bmp,gif,jpeg");
        snapshot = new DmSnapshot(150, 140, fileRuleType);
        flwTr.add(snapshot);
        flwTable.add(flwTr);

        return flwTable;
    }
    
    private FlowPanel rightSideWorkingArea(){
     FlowPanel flwTable, flwTr, flwTd;
     DmLabel lbl;
      
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "20%", "160PX", null, null, DmEnum.DmPosition.CENTER, true);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
     
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "44%", "150px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(imageArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);
    
         return flwTable;
    
    }

}
