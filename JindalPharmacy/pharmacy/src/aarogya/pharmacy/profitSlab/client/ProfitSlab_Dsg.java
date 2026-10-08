package aarogya.pharmacy.profitSlab.client;

import aarogya.pharmacy.profitSlab.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.stStatic.shared.STShared;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ProfitSlab_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().OPERATION_MEDICINE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    protected DmColumnConfig<Find_Mdl, String> colFindMarginPerFrom;
    protected DmColumnConfig<Find_Mdl, String> colFindMarginPerTo;
    protected DmColumnConfig<GrdProfitSlab_Mdl, String> colGrdMarginPer;

    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBoxNumeric txtMarginFrom = new DmTextBoxNumeric(4, false, 13, false);
    final DmTextBoxNumeric txtMarginTo = new DmTextBoxNumeric(4, false, 13, false);
    final DmTextBoxNumeric txtProfitPercent = new DmTextBoxNumeric(2, false, 5, false);

    final DmLabel lblMarginPer = new DmLabel("Margin %");

    //<editor-fold defaultstate="collapsed" desc="dmFind">interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ProfitPercent")
        ValueProvider<Find_Mdl, String> ProfitPercent();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        colFindMarginPerFrom = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "From Margin", 0, "marginFrom");
        colFindMarginPerTo = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "To Margin", 0, "marginTo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().ProfitPercent(), "Profit %", 150, "profitToBeTaken").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 350, 1);

        dmFind.getHelper().addSort("marginFrom", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProfitSlab_Mdl> {

        @Editor.Path("ProfitPercent")
        ValueProvider<GrdProfitSlab_Mdl, String> profitPercent();

    }
    final DmGrid<GrdProfitSlab_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        dmGrd.addRowNumberColumn();
        colGrdMarginPer = dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Margin %", 0, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().profitPercent(), "Profit % (To be taken)", 150, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 415, 260, 2);
        return dmGrd.getGrid();
    }
    //</editor-fold>

    public ProfitSlab_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "300px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "260px", "left", null, DmPosition.NONE, false);
//        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTr.add(createGrid());
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "43px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblMarginPer);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMarginFrom));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To");
        lblTemp.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMarginTo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Profit %");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProfitPercent));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
