package dataman.accounts.budgetDisbursement.client;

import dataman.accounts.budgetDisbursement.client.*;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.google.gwt.dom.client.Style;
import dataman.accounts.budgetDisbursement.shared.TxtHBudgetHead_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class BudgetDisbursementGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtRemarks = new DmTextBox(100, false);
    final DmTextBox txtPlanDocID = new DmTextBox(25, false);
    final DmTextBoxNumeric txtBudget = new DmTextBoxNumeric(4, true, 12, true);
    final DmTextBox txtFromDate = new DmTextBox(11, true);
    final DmTextBox txtToDate = new DmTextBox(11, true);
    final DmTextBox txtCostCenter = new DmTextBox(100, false);

    public BudgetDisbursementGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHPlusMinus();
        iniTxtHBudgetHeadAdjustment();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHPlusMinus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPlusMinus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHPlusMinus() {
        txtHPlusMinus.getDmGrid().addTextColumn(txtHPlusMinus.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPlusMinus.getDmGrid().initializeGrid(txtHPlusMinus.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHPlusMinus.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBudgetHeadAdjustment">
    interface DmBudgetHeadAdjustment_PrpAcs extends KeyField_T_PrpAcs<TxtHBudgetHead_Mdl> {

        @Editor.Path("BudgetHeadName")
        ValueProvider<TxtHBudgetHead_Mdl, String> budgetHeadName();

        @Editor.Path("UnderHeadName")
        ValueProvider<TxtHBudgetHead_Mdl, String> underHeadName();

        @Editor.Path("CostCenterText")
        ValueProvider<TxtHBudgetHead_Mdl, String> costCenterText();
    }
    final DmTextHelp<TxtHBudgetHead_Mdl, DmBudgetHeadAdjustment_PrpAcs> txtHBudgetHeadAdjustment = new DmTextHelp(GWT.create(DmBudgetHeadAdjustment_PrpAcs.class), 4, true);

    private void iniTxtHBudgetHeadAdjustment() {
        txtHBudgetHeadAdjustment.getDmGrid().addTextColumn(txtHBudgetHeadAdjustment.getDmGrid().getPropertyAccess().budgetHeadName(), "Budget Head", 200, "budgetHeadName");
        txtHBudgetHeadAdjustment.getDmGrid().addTextColumn(txtHBudgetHeadAdjustment.getDmGrid().getPropertyAccess().underHeadName(), "Under Head", 200, "underHeadName");
        txtHBudgetHeadAdjustment.getDmGrid().addTextColumn(txtHBudgetHeadAdjustment.getDmGrid().getPropertyAccess().costCenterText(), "Costcenter", 200, "costCenterName");
        txtHBudgetHeadAdjustment.getDmGrid().initializeGrid(txtHBudgetHeadAdjustment.getDmGrid().getPropertyAccess().code(), 620, 0, 1);

        txtHBudgetHeadAdjustment.getHelper().addSort("budgetHeadName", DmSortType.ASC);
    }
    //</editor-fold>

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Head Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBudgetHeadAdjustment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Costcenter");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("+/-");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPlusMinus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Budget");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBudget));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

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
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemarks));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
