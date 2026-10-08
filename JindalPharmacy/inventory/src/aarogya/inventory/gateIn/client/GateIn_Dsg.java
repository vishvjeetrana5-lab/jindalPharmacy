package aarogya.inventory.gateIn.client;

import aarogya.inventory.gateIn.shared.*;
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract public class GateIn_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmInventory().GATE_PASS_IN.caption;
    public final static String moduleName = new pmMaster().moduleName;

    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
//    final DmRemarkTextBox txtDestination = new DmRemarkTextBox(100, false);
    final DmTextBox txtEntryNo = new DmTextBox(32, false);
    final DmTextBox txtPassInDate = new DmTextBox(5, true);
    final DmTextBox txtOutDate = new DmTextBox(11, true);
    final DmTextBoxTime txtPassInTime = new DmTextBoxTime(true);
    final DmTextBoxTime txtOutTime = new DmTextBoxTime(true);
    final DmTextBox txtEstdDate = new DmTextBox(11, false);
    public final static String gateOutByBlog = "Gate Out By";
    final DmTextBox txtGatePassOut = new DmTextBox(50, false);
    final DmTextBox txtSlipNo = new DmTextBox(32, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);
    final FlowPanel flwGateOutContainer = new FlowPanel();
    final FlowPanel flwGateOutDetail = new FlowPanel();
    final Button btnGateOutBy = new Button();
    final DmTextBox txtGateOutDateView = new DmTextBox(11, false);
    final DmTextBoxTime txtGateOutTimeView = new DmTextBoxTime(false);
    final FlowPanel flwGateOutEntry = new FlowPanel();
    protected final FlowPanel flwSearchGateEntry = new FlowPanel();
    protected final FlowPanel flwClockGateEntry = new FlowPanel();
    protected final Button btnSearchGateEntry = new Button();
    protected final Button btnClockGateEntry = new Button();
    public final static String dateTimeEditableBlog = "Date/Time Editable";
    final FlowPanel flwInDateTimeContainer = new FlowPanel();
    protected final FlowPanel flwInDateTime = new FlowPanel();
    protected final Button btnGateInDateTime = new Button();

    final DmTextBoxNumeric txtGrossWeight = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtTareWeight = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtNetWeight = new DmTextBoxNumeric(4, false, 12, false);

    final DmTextBox txtUnitGross = new DmTextBox(10, false);
    final DmTextBox txtUnitTare = new DmTextBox(10, false);
    final DmTextBox txtUnitNet = new DmTextBox(10, false);

    final Button btnGateAdvice = new Button("Gate Advice");
    final FlowPanel flwBtnAdvice = new FlowPanel();
    final FlowPanel flwRemarkText = new FlowPanel();
    final DmLabel lblDiscard = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtDestination">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDestination = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHDestination() {
        txtHDestination.setAppendable(true);
        txtHDestination.getDmGrid().addTextColumn(txtHDestination.getDmGrid().getPropertyAccess().name(), "Remark", 0, "destination");
        txtHDestination.getDmGrid().initializeGrid(txtHDestination.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDestination.getHelper().addSort("destination", DmEnum.DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVoucher_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucher_mdl> {

    }

    final DmTextHelp<TxtHVoucher_mdl, TxtHVoucher_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucher_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHMode() {
        txtHMode.setAppendable(true);
        txtHMode.getDmGrid().addTextColumn(txtHMode.getDmGrid().getPropertyAccess().name(), "Mode", 0, "mode");
        txtHMode.getDmGrid().initializeGrid(txtHMode.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHMode.getHelper().addSort("mode", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPurpose">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurpose = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHPurpose() {
        txtHPurpose.setAppendable(true);
        txtHPurpose.getDmGrid().addTextColumn(txtHPurpose.getDmGrid().getPropertyAccess().name(), "Purpose", 0, "purpose");
        txtHPurpose.getDmGrid().initializeGrid(txtHPurpose.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHPurpose.getHelper().addSort("purpose", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEmployeeName">
    interface TxtHPartyName_PrpAcs extends KeyField_T_PrpAcs<TxtHPartyName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHPartyName_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHPartyName_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHPartyName_Mdl, TxtHPartyName_PrpAcs> txtHEmployeeName = new DmTextHelp(GWT.create(TxtHPartyName_PrpAcs.class), 6, false);

    private void iniTxtHPartyName() {
        txtHEmployeeName.getDmGrid().addTextColumn(txtHEmployeeName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHEmployeeName.getDmGrid().addTextColumn(txtHEmployeeName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHEmployeeName.getDmGrid().addTextColumn(txtHEmployeeName.getDmGrid().getPropertyAccess().cityName(), "City", 80, "cityName");
        txtHEmployeeName.getDmGrid().initializeGrid(txtHEmployeeName.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHEmployeeName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHEmployeeName.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 0, "departmentName");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDepartment.getHelper().addSort("departmentName", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GrdGate">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GateInGridEdit_Mdl> {

        @Editor.Path("itemDescription")
        ValueProvider<GateInGridEdit_Mdl, String> itemDescription();

        @Editor.Path("Quantity")
        ValueProvider<GateInGridEdit_Mdl, String> Quantity();

        @Editor.Path("UnitName")
        ValueProvider<GateInGridEdit_Mdl, String> unitName();

        @Editor.Path("Specification")
        ValueProvider<GateInGridEdit_Mdl, String> specification();

        @Editor.Path("EstdValue")
        ValueProvider<GateInGridEdit_Mdl, String> estdValue();

        @Editor.Path("AssetName")
        ValueProvider<GateInGridEdit_Mdl, String> assetName();

        @Editor.Path("GateOutDate")
        ValueProvider<GateInGridEdit_Mdl, String> gateOutDate();

        @Editor.Path("GateOutTime")
        ValueProvider<GateInGridEdit_Mdl, String> gateOutTime();

    }
    final DmGrid<GateInGridEdit_Mdl, Grd_PrpAcs> grdGateDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid iniGrdGateDetail() {
        grdGateDetail.addRowNumberColumn();
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().itemDescription(), "Item Description", 250, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().Quantity(), "Quantity", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().unitName(), "Unit Name", 60, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().specification(), "Specification", 200, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().estdValue(), "Estm Value", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().assetName(), "Asset Name", 100, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().gateOutDate(), "Out Date", 80, "", DmEnum.DmDataType.DATE);
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().gateOutTime(), "Time", 50, "", DmEnum.DmDataType.TIMESTAMP);

        grdGateDetail.initializeGrid(grdGateDetail.getPropertyAccess().fieldHashCode(), 1260, 240, 2);
        return grdGateDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Entry_No")
        ValueProvider<Find_Mdl, String> entry_No();

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("Time")
        ValueProvider<Find_Mdl, String> time();

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("Party_Name")
        ValueProvider<Find_Mdl, String> party_Name();

        @Editor.Path("PassType")
        ValueProvider<Find_Mdl, String> passType();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

        @Editor.Path("Destination")
        ValueProvider<Find_Mdl, String> destination();

        @Editor.Path("Mode")
        ValueProvider<Find_Mdl, String> mode();

        @Editor.Path("Purpose")
        ValueProvider<Find_Mdl, String> purpose();

        @Editor.Path("EstdDate")
        ValueProvider<Find_Mdl, String> estdDate();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 16);

    private void iniFind() {
        ColumnConfig<Find_Mdl, String> discarded;
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry No", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().time(), "Time", 50, "v_time", DmEnum.DmDataType.DOUBLE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().passType(), "Type", 80, "passType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().party_Name(), "Employee Name", 220, "partyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department", 80, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().destination(), "To Destination", 150, "destination");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mode(), "Mode", 80, "mode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().purpose(), "Purpose", 100, "purpose");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().estdDate(), "Estd. Date", 80, "estdDate", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 120, "remark");
        discarded = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().status(), "Status", 80, "status");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0);

        discarded.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='color : red;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {
            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String rtnStyle = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getBillCancelled()).intValue() == 1) {
                    rtnStyle += "dm-GridCellDairyCancel ";
                }
                return rtnStyle;
            }

            @Override
            public String getRowStyle(Find_Mdl model, int rowIndex) {
                return "";
            }
        });

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("v_Time", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public GateIn_Dsg() {
        ini();
        iniFind();
        iniTxtHType();
        iniTxtHPartyName();
        iniTxtHVoucherType();
        iniTxtHDepartment();
        iniTxtHPurpose();
        iniTxtHDestination();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        iniTxtHMode();
        vrtInnerForm.add(vl.add(topControl));
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="addUserControl">
    private DmUserControl addUserControl() {
        userControl.addTextBlog(preparedByBlog, 200, false, true);

        userControl.addGeneralBlog(lblDiscard);
        lblDiscard.setHeight("15Px");
        lblDiscard.getElement().getStyle().setColor("red");
        lblDiscard.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDiscard.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblDiscard.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblDiscard.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, true, false);
        return userControl;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="workingArea">
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "405px", "left", null, DmEnum.DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", "70px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", "85px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", "85px", "right", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "245px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdGateDetail()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(bottomLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //</editor-fold>
    private FlowPanel bottomLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "108px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "0.5%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(gateOutEntryArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="topLeftArea">
    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //===============================< 1st Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Gate Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Entry No");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        //===============================< 2nd Row >===============================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Person/ Mode");
        lbl = new DmLabel("Type");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHMode));
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Purpose");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurpose));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //===============================< ******* >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Employee Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmployeeName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="topRightArea">
    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        //===============================< 1st Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Person/ Mode");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date/Time");
        lbl.getElement().getStyle().setMarginLeft(19, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassInDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8.5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassInTime));
        flwTr.add(flwTd);

        DmClient.setStyle(flwInDateTimeContainer, "3.1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwInDateTimeContainer.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        aBtn = new Anchor();
        aBtn.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        aBtn.addStyleName("fa fa-clock-o");
        aBtn.getElement().setAttribute("style", "font-weight: bold;color:#7d7d7d");
        flwInDateTime.setTitle("Get Date Time");
        btnGateInDateTime.getElement().appendChild(aBtn.getElement());
        btnGateInDateTime.getElement().setAttribute("style", "width: 26px; height: 20px; padding: 0px;");
        flwInDateTime.add((btnGateInDateTime));
        flwInDateTimeContainer.add(flwInDateTime);
        flwTr.add(flwInDateTimeContainer);
        flwTable.add(flwTr);

        //===============================< ******* >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Destination");
        lbl.getElement().getStyle().setMarginLeft(19, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDestination));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Estd. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEstdDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(19, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
