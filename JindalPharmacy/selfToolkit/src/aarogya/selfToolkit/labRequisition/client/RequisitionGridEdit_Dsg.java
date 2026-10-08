package aarogya.selfToolkit.labRequisition.client;

import aarogya.selfToolkit.labRequisition.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RequisitionGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtReqDate = new DmTextBox(11, true);
    final DmTextBoxTime txtReqTime = new DmTextBoxTime(true);
    final DmTextBox txtRemark = new DmTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="txtHTest">
    interface TxtHTest_PrpAcs extends KeyField_T_PrpAcs<TxtHTest_Mdl> {

        @Editor.Path("categoryText")
        ValueProvider<TxtHTest_Mdl, String> categoryText();

    }
    final DmTextHelp<TxtHTest_Mdl, TxtHTest_PrpAcs> txtHTest = new DmTextHelp(GWT.create(TxtHTest_PrpAcs.class), 7, true);

    private void iniTxtHTest() {
        txtHTest.getDmGrid().addTextColumn(txtHTest.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTest.getDmGrid().addTextColumn(txtHTest.getDmGrid().getPropertyAccess().categoryText(), "Category", 200, "categoryName");
        txtHTest.getDmGrid().initializeGrid(txtHTest.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHTest.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHToDepartment = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDepartmentTo() {

        txtHToDepartment.getDmGrid().addTextColumn(txtHToDepartment.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHToDepartment.getDmGrid().initializeGrid(txtHToDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHToDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHSite.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public RequisitionGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHTest();
        iniTxtHDepartmentTo();
        iniSite();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "146px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Investigation");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTest));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department To");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site To");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHToDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTd.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Date & Time");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtReqDate));
        hrtPnl.setCellWidth(txtReqDate, "20%");
        hrtPnl.add(vl.add(txtReqTime));
        hrtPnl.setCellWidth(txtReqTime, "10%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
