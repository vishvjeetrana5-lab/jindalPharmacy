package dataman.payroll.employeeExpenceEntry.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.payroll.employeeExpenceEntry.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;

import com.google.gwt.editor.client.Editor;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;

import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmControlBlog;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.employeeExpenceEntry.shared.GridEmployeeExpenceEntry_Mdl;
import dataman.payroll.employeeExpenceEntry.shared.SalaryMethod_Mdl;

abstract class EmployeeExpenceEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().PERKS_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    DmControlBlog addServiceApplTo;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtDate = new DmTextBox(11, true);
    final DmLabel totalVal = new DmLabel("0.00");
  final DmLabel lblPrint = new DmLabel("");
     
 

    //<editor-fold defaultstate="collapsed" desc="txtEmployee>
    interface TxtHEmployee_PrpAcs extends KeyField_T_PrpAcs<Employee_Mdl> {

        @Editor.Path("Employee")
        ValueProvider<Employee_Mdl, String> employee();

      

        @Editor.Path("FatherName")
        ValueProvider<Employee_Mdl, String> fatherName();

        @Editor.Path("Department")
        ValueProvider<Employee_Mdl, String> department();

        @Editor.Path("DOJ")
        ValueProvider<Employee_Mdl, String> DOJ();
    }
    final DmTextHelp<Employee_Mdl, TxtHEmployee_PrpAcs> txtHEmployee = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHEmployee_PrpAcs.class), 10, true);

    private void iniTxtHEmployee() {
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().employee(), "Employee", 150, "Employee");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().fatherName(), "Father Name", 100, "fatherName");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().DOJ(), "DOJ", 100, "doj");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 500, 300);
        txtHEmployee.getHelper().addSort("Employee", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtSalaryMethod">
    interface TxtHSalaryMethod_PrpAcs extends KeyField_T_PrpAcs<SalaryMethod_Mdl> {

        @Editor.Path("SalaryMethodName")
        ValueProvider<SalaryMethod_Mdl, String> salaryMethodName();
    }

    final DmTextHelp<SalaryMethod_Mdl, TxtHSalaryMethod_PrpAcs> txtHSalaryMethod = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHSalaryMethod_PrpAcs.class), 5, true);

    private void iniTxtHSalaryMethod() {
        txtHSalaryMethod.getDmGrid().addTextColumn(txtHSalaryMethod.getDmGrid().getPropertyAccess().salaryMethodName(), "Salary Method", 180, "name");
        txtHSalaryMethod.getDmGrid().initializeGrid(txtHSalaryMethod.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHSalaryMethod.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdLogDetail">
    interface GrdTabIndex_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridEmployeeExpenceEntry_Mdl> {

        @Editor.Path("Nature")
        ValueProvider<GridEmployeeExpenceEntry_Mdl, String> nature();

        @Editor.Path("Value")
        ValueProvider<GridEmployeeExpenceEntry_Mdl, String> value();

    }
    final DmGrid<GridEmployeeExpenceEntry_Mdl, GrdTabIndex_PrpAcs> grdLogDetail = new DmGrid(GWT.create(GrdTabIndex_PrpAcs.class));

    private Grid iniGrdLogDetail() {
        grdLogDetail.addRowNumberColumn();

        grdLogDetail.addTextColumn(grdLogDetail.getPropertyAccess().name(), "Over Head Name", 150, "");
        grdLogDetail.addTextColumn(grdLogDetail.getPropertyAccess().nature(), "Nature", 100, "");
        grdLogDetail.addTextColumn(grdLogDetail.getPropertyAccess().value(), "Value", 80, "");
        grdLogDetail.initializeGrid(grdLogDetail.getPropertyAccess().fieldHashCode(), 510, 300, 2);

        grdLogDetail.getGrid().getView().setViewConfig(new GridViewConfig<GridEmployeeExpenceEntry_Mdl>() {
            @Override
            public String getColStyle(GridEmployeeExpenceEntry_Mdl model, ValueProvider<? super GridEmployeeExpenceEntry_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
//                if (!model.isDiscountAllow() && (DmShared.iNull(model.getSharedKeyFieldCode()).longValue() > 0)) {
//                    return "NDSColor";
//                }
                return "";
            }

            @Override
            public String getRowStyle(GridEmployeeExpenceEntry_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdLogDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EntryDate")
        ValueProvider<Find_Mdl, String> entryDate();

        @Editor.Path("Employee")
        ValueProvider<Find_Mdl, String> employee();

        @Editor.Path("SalaryPeriod")
        ValueProvider<Find_Mdl, String> salaryPeriod();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 120, "EntDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employee(), "Employee", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().salaryPeriod(), "Salary Period", 120, "sp");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 2);

        dmFind.getHelper().addSort("EntDate", DmSortType.DESC);
        //dmFind.getHelper().addSort("printSNo", DmSortType.ASC);
    }
    //</editor-fold>

    public EmployeeExpenceEntry_Dsg() {
        ini();

        iniFind();
        iniTxtHEmployee();
        iniTxtHSalaryMethod();


    }

    private void ini() {

        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        
        

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "476px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Date");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDate));
        flwTR.add(flwTD);
        flwTable.add(flwTR);


        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "45%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEmployee));//removed v1.add
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Salary Period");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "45%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSalaryMethod));//removed v1.add
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //==================Grid Design Adding=========
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "300px", "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginLeft(-22, Style.Unit.PCT);
        
        

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "55%", "250px", null, null, DmPosition.CENTER, false);
        flwTD.add(vl.add(iniGrdLogDetail()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTR.getElement().getStyle().setMarginTop(25, Style.Unit.PX);
        flwTR.getElement().getStyle().setMarginLeft(-141, Style.Unit.PX);
        flwTR.add(displayAmountArea());
        flwTable.add(flwTR);

        return flwTable;
    }
        private FlowPanel displayAmountArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmLabel lbl;

        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);
        

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "80%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setMarginLeft(143, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total :");
        lbl.setHeight("18px");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        totalVal.setHeight("18px");
        totalVal.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        totalVal.getElement().getStyle().setMarginRight(-125, Style.Unit.PX);
        totalVal.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(totalVal);
        flwTr.add(flwTd);

   

        flwTable.add(flwTr);

        return flwTable;
    }

}
