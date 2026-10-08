package dataman.accounts.budgetAllocationEntry.client;

import dataman.accounts.budgetAllocationEntry.shared.Find_Mdl;
import dataman.accounts.budgetAllocationEntry.shared.GrdBudgetAllocationDetail_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class BudgetAllocationEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BUDGET_ALLOCATION_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";
    
    final DmLabel lblTot = new DmLabel("Total : ");
    final DmLabel lblTotalValue = new DmLabel("0.00");
    
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtAllocationNo = new DmTextBox(20, false);
    final DmTextBox txtWTACode = new DmTextBox(15, true);
    final Button btnApprovedBy = new Button();
    final DmTextBox txtAllocationDate = new DmTextBox(11, true);
    public DmTextBox txtFromDate = new DmTextBox(11, true);
    public DmTextBox txtToDate = new DmTextBox(11, true);
    int rowIndexGrid;

    //<editor-fold defaultstate="collapsed" desc="TxtHAllocationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllocationType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHAllocationType() {
        txtHAllocationType.getDmGrid().addTextColumn(txtHAllocationType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHAllocationType.getDmGrid().initializeGrid(txtHAllocationType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHAllocationType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBudgetName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBudgetName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHBudgetName() {
        txtHBudgetName.setAppendable(true);
        txtHBudgetName.getDmGrid().addTextColumn(txtHBudgetName.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHBudgetName.getDmGrid().initializeGrid(txtHBudgetName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHBudgetName.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    
    

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("BudgetName")
        ValueProvider<Find_Mdl, String> budgetName();

        @Editor.Path("AllcationDate")
        ValueProvider<Find_Mdl, String> allcationDate();

        @Editor.Path("FromDate")
        ValueProvider<Find_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<Find_Mdl, String> toDate();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 19);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry No.", 100, "recID");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().budgetName(), "Budget Description", 200, "budgetName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().allcationDate(), "Date", 80, "allcationDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromDate(), "From Dt.", 80, "fromDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toDate(), "To Dt.", 80, "toDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "prepared by", 80, "preparedBY");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved by", 80, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1000, 0, 2);

        dmFind.getHelper().addSort("allcationDate", DmSortType.DESC);
        dmFind.getHelper().addSort("recid", DmSortType.ASC);
    }
    //</editor-fold>

    public BudgetAllocationEntry_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHAllocationType();
        iniTxtHBudgetName();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        
        userControl.addGap(50);
        userControl.addGeneralBlog(lblTot);
        lblTot.setHeight("15Px");
        lblTot.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTot.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblTot.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblTot.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addGeneralBlog(lblTotalValue);
        lblTotalValue.setHeight("15Px");
        lblTotalValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTotalValue.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblTotalValue.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblTotalValue.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        
        userControl.addGap(100);
        userControl.addTextBlog(approvedByBlog, 100, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "410px", null, null, DmPosition.CENTER, true);

        //entryArea
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "79px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        //Grid Area
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "300px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTr.add(vl.add(iniGrdMedicineDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "76px", null, null, DmPosition.CENTER, true);

        //entryArea
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "76px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "96px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea1());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel entryArea1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "76px", null, null, DmPosition.CENTER, true);

        /****************************1st Row*******************************************/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllocationType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /****************************2nd Row*******************************************/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllocationNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);

        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllocationDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /****************************3rd Row*******************************************/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Budget Description");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBudgetName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        /****************************4th Row*******************************************/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);

        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdBudgetHeadDetail">
    interface GrdBudgetAllocationDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBudgetAllocationDetail_Mdl> {

        @Editor.Path("BudgetHeadText")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> budgetHeadText();

        @Editor.Path("UnderHeadText")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> underHeadText();

        @Editor.Path("CostcenterText")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> costcenterText();
        
        @Editor.Path("FromDate")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> fromDate();
        
        @Editor.Path("ToDate")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> toDate();
        
        @Editor.Path("PlusMinusText")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> plusMinusText();
        
        @Editor.Path("Budget")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> budget();

        @Editor.Path("Remarks")
        ValueProvider<GrdBudgetAllocationDetail_Mdl, String> remarks();

    }
    final DmGrid<GrdBudgetAllocationDetail_Mdl, GrdBudgetAllocationDetail_PrpAcs> grdBudgetAllocationDetail = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdBudgetAllocationDetail_PrpAcs.class));

    public Grid iniGrdMedicineDetail() {
        grdBudgetAllocationDetail.addRowNumberColumn();
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().budgetHeadText(), "Head Name", 200, "budgetHeadName");
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().underHeadText(), "Under Head", 200, "underHeadName");
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().costcenterText(), "Costcenter", 200, "costCenterName");
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().fromDate(), "From Dt", 80, "fromDate");
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().toDate(), "To Dt", 80, "toDate");
        rowIndexGrid = 1;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().plusMinusText(), "+/-", 20, "plusMinusText");
        ++rowIndexGrid;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().budget(), "Budget", 100, "budget").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++rowIndexGrid;
        grdBudgetAllocationDetail.addTextColumn(grdBudgetAllocationDetail.getPropertyAccess().remarks(), "Remark", 300, "");
        grdBudgetAllocationDetail.initializeGrid(grdBudgetAllocationDetail.getPropertyAccess().fieldHashCode(), 1280, 322, 2);

        return grdBudgetAllocationDetail.getGrid();
    }
//</editor-fold>

}
