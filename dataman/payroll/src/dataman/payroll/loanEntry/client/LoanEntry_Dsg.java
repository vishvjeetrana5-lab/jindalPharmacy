package dataman.payroll.loanEntry.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.payroll.loanEntry.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;

abstract class LoanEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().LOAN_ENTRY.caption;
    final lmPayroll lmPayRoll=new lmPayroll();
    final String moduleName=lmPayRoll.moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtV_Date = new DmTextBox(11, false);
    final DmTextBox txtV_Type = new DmTextBox(11, false);
    final DmTextBox txtLoanStartDate = new DmTextBox(11, false);
    final DmTextBox txtEntryNo = new DmTextBox(100, false);
    final DmTextBoxNumeric txtLoanAmount = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBoxNumeric txtInterestRate = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtForMonth = new DmTextBoxNumeric(0, false, 3, false);

    
    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("RecId")
        ValueProvider<Find_Mdl, String> recId();
        
        @Editor.Path("V_Date")
        ValueProvider<Find_Mdl, String> v_Date();
        
        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("LoanAmount")
        ValueProvider<Find_Mdl, String> loanAmount();
        
        @Editor.Path("InterestRate")
        ValueProvider<Find_Mdl, String> interestRate();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().recId(), "Entry No.", 100, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().v_Date(), "Date", 100, "v_Date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 150, "employeeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().loanAmount(), "Loan Amt.", 80, "loanAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().interestRate(), "ROI", 80, "interestRate");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 520, 3);

        dmFind.getHelper().addSort("v_Date", DmSortType.ASC);
    }
    //</editor-fold>
    // Constructor

    //<editor-fold defaultstate="collapsed" desc="txtHReceiptVoucher">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLoanEntry_Mdl> {

        @Editor.Path("EmiDate")
        ValueProvider<GrdLoanEntry_Mdl, String> emiDate();

        @Editor.Path("EmiAmount")
        ValueProvider<GrdLoanEntry_Mdl, String> emiAmount();

        @Editor.Path("EmiPrincipal")
        ValueProvider<GrdLoanEntry_Mdl, String> emiPrincipal();

        @Editor.Path("Interest")
        ValueProvider<GrdLoanEntry_Mdl, String> interest();

    }

    final DmGrid<GrdLoanEntry_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {

        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().emiDate(), "Date", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().emiPrincipal(), "Principal", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().interest(), "Int.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().emiAmount(), "EMI", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 390, 290, 2);

        dmGrd.getGrid().getView().setViewConfig(new GridViewConfig<GrdLoanEntry_Mdl>() {
            @Override
            public String getColStyle(GrdLoanEntry_Mdl model, ValueProvider<? super GrdLoanEntry_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (!DmShared.xNull(model.getAdjustedDocID()).trim().equalsIgnoreCase("")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdLoanEntry_Mdl model, int rowIndex) {
                return "";
            }
        });
        return dmGrd.getGrid();
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmployee = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHEmployee() {
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().name(), "Department Name", 180, "name");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHEmployee.getHelper().addSort("name", DmSortType.ASC);
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEntryType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHEntryType() {
        txtHEntryType.getDmGrid().addTextColumn(txtHEntryType.getDmGrid().getPropertyAccess().name(), "Entry Type", 150, "name");
        txtHEntryType.getDmGrid().initializeGrid(txtHEntryType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHEntryType.getHelper().addSort("name", DmSortType.ASC);
    }
    
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStop = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHStop() {
        txtHStop.getDmGrid().addTextColumn(txtHStop.getDmGrid().getPropertyAccess().name(), "Status", 100, "name");
        txtHStop.getDmGrid().initializeGrid(txtHStop.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHStop.getHelper().addSort("name", DmSortType.ASC);
    } 
    
    public LoanEntry_Dsg() {
        ini();
        iniTxtHEmployee();
        iniTxtHEntryType();
        iniTxtHStop();
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

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "428px", null, null, DmPosition.CENTER, true);

        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "128px", "left", null, DmPosition.NONE, false);
        flwTD.add(entrySection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //===============2nd row=======================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "98%", "350px", "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        flwTD.add(grdSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel entrySection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "128px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry no.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEntryNo));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Date");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtV_Date));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Type");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEntryType));
        flwTR.add(flwTD);
        
        

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEmployee));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        
/*====================================< 3rd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Loan Amount");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtLoanAmount));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(16, Style.Unit.PX);
        lbl = new DmLabel("ROI");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtInterestRate));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(16, Style.Unit.PX);
        lbl = new DmLabel("For Month");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        flwTD.add(vl.add(txtForMonth));
        flwTR.add(flwTD);

        flwTable.add(flwTR);        
        
        /*====================================< 4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Loan Start Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtLoanStartDate));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(16, Style.Unit.PX);
        lbl = new DmLabel("Stop");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHStop));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);        

        return flwTable;
    }

    private FlowPanel grdSection() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "400px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(80, Style.Unit.PX);
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
