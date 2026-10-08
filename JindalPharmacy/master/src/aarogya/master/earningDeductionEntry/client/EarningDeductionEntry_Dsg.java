package aarogya.master.earningDeductionEntry.client;

import aarogya.master.earningDeductionEntry.shared.CopyFrom_Mdl;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntryGridEdit_Mdl;
import aarogya.master.earningDeductionEntry.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class EarningDeductionEntry_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().EARNING_DEDUCTION_ENTRY.caption;
    public final static String moduleName = new pmMaster().moduleName;

    public final static String preparedByBlog = "Prepared By ";
    public final static String modifiedByBlog = "Modified By ";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwCopyFromTr = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSalaryPeriod = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHPoolGroupName() {
        txtHSalaryPeriod.getDmGrid().addTextColumn(txtHSalaryPeriod.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSalaryPeriod.getDmGrid().initializeGrid(txtHSalaryPeriod.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHSalaryPeriod.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCopy">
    interface TxtHCopyPriceList_PrpAcs extends KeyField_T_PrpAcs<CopyFrom_Mdl> {

        @Editor.Path("EmpName")
        ValueProvider<CopyFrom_Mdl, String> empName();

        @Editor.Path("SalName")
        ValueProvider<CopyFrom_Mdl, String> salName();
    }

    final DmTextHelp<CopyFrom_Mdl, TxtHCopyPriceList_PrpAcs> txtCopyFrom = new DmTextHelp(GWT.create(TxtHCopyPriceList_PrpAcs.class), 6, false);

    private void iniTxtCopyFrom() {
        txtCopyFrom.getDmGrid().addTextColumn(txtCopyFrom.getDmGrid().getPropertyAccess().salName(), "Name", 195, "salaryName");
        txtCopyFrom.getDmGrid().addTextColumn(txtCopyFrom.getDmGrid().getPropertyAccess().empName(), "Employee Name", 195, "empName");
        txtCopyFrom.getDmGrid().initializeGrid(txtCopyFrom.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtCopyFrom.getHelper().addSort("salaryName", DmEnum.DmSortType.DESC);

    }
    //</editor-fold>

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmployeeName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniTxtHEmployeetName() {
        txtHEmployeeName.getDmGrid().addTextColumn(txtHEmployeeName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHEmployeeName.getDmGrid().initializeGrid(txtHEmployeeName.getDmGrid().getPropertyAccess().code(), 380, 0, 1);
        txtHEmployeeName.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("SalName")
        ValueProvider<Find_Mdl, String> salName();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().salName(), "Salary Period", 500, "salaryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 300, "employeeName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0);
        dmFind.getHelper().addSort("salaryName", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("employeeName", DmEnum.DmSortType.DESC);

    }
    //</editor-fold>

    public EarningDeductionEntry_Dsg() {
        ini();
        iniFind();
        iniTxtHPoolGroupName();
        iniTxtHEmployeetName();
        iniTxtCopyFrom();

    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
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
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);

        return userControl;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="workingArea">
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "524px", "left", null, DmEnum.DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTable.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", "67px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51%", "85px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "50%", "451px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(18, Style.Unit.PCT);
        flwTd.add(vl.add(iniGrdGateDetail()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
    //<editor-fold defaultstate="collapsed" desc="topRightArea">

    //<editor-fold defaultstate="collapsed" desc="GrdGate">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<EarningDeductionEntryGridEdit_Mdl> {

        @Editor.Path("NatureName")
        ValueProvider<EarningDeductionEntryGridEdit_Mdl, String> natureName();

        @Editor.Path("DespName")
        ValueProvider<EarningDeductionEntryGridEdit_Mdl, String> despName();

        @Editor.Path("Amount")
        ValueProvider<EarningDeductionEntryGridEdit_Mdl, String> amount();

        @Editor.Path("TypeName")
        ValueProvider<EarningDeductionEntryGridEdit_Mdl, String> typeName();

    }
    final DmGrid<EarningDeductionEntryGridEdit_Mdl, Grd_PrpAcs> grdEarningDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid iniGrdGateDetail() {
        grdEarningDetail.addRowNumberColumn();
        grdEarningDetail.addTextColumn(grdEarningDetail.getPropertyAccess().natureName(), "Type", 100, "");
        grdEarningDetail.addTextColumn(grdEarningDetail.getPropertyAccess().despName(), "Description", 260, "");
        grdEarningDetail.addTextColumn(grdEarningDetail.getPropertyAccess().amount(), "Amount", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdEarningDetail.addTextColumn(grdEarningDetail.getPropertyAccess().typeName(), "Nature", 150, "");

        grdEarningDetail.initializeGrid(grdEarningDetail.getPropertyAccess().fieldHashCode(), 645, 451);
        return grdEarningDetail.getGrid();
    }
    //</editor-fold>

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
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Salary Period");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalaryPeriod));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Employee Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmployeeName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwCopyFromTr.getElement().getStyle().setDisplay(Style.Display.NONE);
        DmClient.setStyle(flwCopyFromTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Copy From");
        flwTd.add(lbl);
        flwCopyFromTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCopyFrom));
        flwCopyFromTr.add(flwTd);
        flwTable.add(flwCopyFromTr);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Employee Name");
////        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
//        flwTd.add(vl.add(txtHEmployeeName));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
        //===============================< ******* >===============================//
        return flwTable;
    }

}
