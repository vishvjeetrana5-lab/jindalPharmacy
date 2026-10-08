package dataman.payroll.salaryCreation.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.payroll.salaryCreation.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;

abstract class SalaryCreation_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().SALARY_CREATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwButtonFillDetails = new FlowPanel();
    final Button btnFillDetails = new Button("Fill Details");
    final DmTextBox txtFromToDate = new DmTextBox(11, false);

   // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("SalaryMethod")
        ValueProvider<Find_Mdl, String> salaryMethod();

        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();
     
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().salaryMethod(), "Salary Method", 100, "SalaryMethodName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department Name", 100, "DepartmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("SalaryMethodName", DmSortType.ASC);
    }
    //</editor-fold>
    // Constructor

    
    //<editor-fold defaultstate="collapsed" desc="txtHReceiptVoucher">
   interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSalaryCreation_Mdl> {

        @Editor.Path("Employee")
        ValueProvider<GrdSalaryCreation_Mdl, String> employee();
        
        @Editor.Path("EmployeeCode")
        ValueProvider<GrdSalaryCreation_Mdl, String> employeeCode();

        @Editor.Path("Father")
        ValueProvider<GrdSalaryCreation_Mdl, String> father();

        @Editor.Path("Basic")
        ValueProvider<GrdSalaryCreation_Mdl, String> basic();

        @Editor.Path("CalcBasic")
        ValueProvider<GrdSalaryCreation_Mdl, String> calcBasic();

        @Editor.Path("GrossSalary")
        ValueProvider<GrdSalaryCreation_Mdl, String> grossSalary();

        @Editor.Path("NetSalary")
        ValueProvider<GrdSalaryCreation_Mdl, String> netSalary();

        @Editor.Path("Tdays")
        ValueProvider<GrdSalaryCreation_Mdl, String> tdays();

        @Editor.Path("OffDays")
        ValueProvider<GrdSalaryCreation_Mdl, String> offDays();

        @Editor.Path("Holidays")
        ValueProvider<GrdSalaryCreation_Mdl, String> holidays();

        @Editor.Path("Wdays")
        ValueProvider<GrdSalaryCreation_Mdl, String> wdays();
        
        @Editor.Path("TotalEarn")
        ValueProvider<GrdSalaryCreation_Mdl, String> totalEarn();
        
        @Editor.Path("TotalDed")
        ValueProvider<GrdSalaryCreation_Mdl, String> totalDed();
        
        @Editor.Path("Leave")
        ValueProvider<GrdSalaryCreation_Mdl, String> leave();
    }

    final DmGrid<GrdSalaryCreation_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

     public Grid createGrid() {

        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().employee(), "Employee Name", 101, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().employeeCode(), "Employee Code", 0, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().father(), "Father Name", 0, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().basic(), "Basic", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().calcBasic(), "Calc. Basic", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().grossSalary(), "Gross Salary", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().netSalary(), "Net Salary", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().tdays(), "T. Days", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().offDays(), "Off Days", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().holidays(), "Holidays", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().wdays(), "Wr. Days", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().totalEarn(), "T. Earning", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().totalDed(), "T. Deduction", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().leave(), "Leaves", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1270,290, 2);

       return dmGrd.getGrid();
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSalaryMethod = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSalaryMethod() {
       txtHSalaryMethod.getDmGrid().addTextColumn(txtHSalaryMethod.getDmGrid().getPropertyAccess().name(), "Salary Method", 180, "name");
       txtHSalaryMethod.getDmGrid().initializeGrid(txtHSalaryMethod.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
       txtHSalaryMethod.getHelper().addSort("name", DmSortType.ASC);
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHDepartmentName() {
       txtHDepartmentName.getDmGrid().addTextColumn(txtHDepartmentName.getDmGrid().getPropertyAccess().name(), "Department Name", 180, "name");
       txtHDepartmentName.getDmGrid().initializeGrid(txtHDepartmentName.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
       txtHDepartmentName.getHelper().addSort("name", DmSortType.ASC);
    }
    


    
    public SalaryCreation_Dsg() {
        ini();
        iniTxtHDepartmentName();
        iniTxtHSalaryMethod();
        
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true); // It sets the 
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%"); 
        flwForm.add(vrtInnerForm);
    }
    
    private FlowPanel workingArea(){
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "428px", null, null, DmPosition.CENTER, true);
        
        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        
        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "128px", "left", null, DmPosition.NONE, false);
        flwTD.add(leftSection());
        flwTR.add(flwTD);
        
        //==============Right Section==================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "128px", "left", null, DmPosition.NONE, false);
        flwTD.add(rightSection());
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        
        //===============2nd row=======================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "98%", "300px", "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(8,Style.Unit.PX);
        flwTD.add(grdSection());
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        
        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "128px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(5,Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Salary Method");
        lbl.getElement().getStyle().setMarginLeft(25,Style.Unit.PCT);
        
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSalaryMethod));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
    
        /*====================================< 2nd Row >====================================*/
        
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(1,Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department Name");
        lbl.getElement().getStyle().setMarginLeft(25,Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        
        
        flwTD.add(vl.add(txtHDepartmentName));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);


        return flwTable;
    }
  
      private FlowPanel rightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "128px", null, null, DmPosition.CENTER, true);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(6,Style.Unit.PCT);
        btnFillDetails.setWidth("60%");
        btnFillDetails.addStyleName("dm-button-grad");
        btnFillDetails.setHeight("20px");
        btnFillDetails.getElement().setAttribute("style", btnFillDetails.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;margin-left: 50px;margin-top: 13px;");
        btnFillDetails.setTitle("Fill Details");
        flwButtonFillDetails.add(vl.add(btnFillDetails));
        flwTd.add(flwButtonFillDetails);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
      }
     
      private FlowPanel grdSection() {
        FlowPanel flwTable, flwTr, flwTd;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "300px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(createGrid()));
        
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
      }
      
    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

}
