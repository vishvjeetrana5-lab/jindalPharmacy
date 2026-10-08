package dataman.utility.itemTaxStructure.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.itemTaxStructure.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ItemTaxStructure_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().ITEM_TAX_STRUCTURE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTextBox txtITSName = new DmTextBox(50, true);
    final DmTextBoxNumeric txtTotalTaxPercent = new DmTextBoxNumeric(0, false, 2, true);
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmTextBox txtApplicableDate = new DmTextBox(11, true);
    final FlowPanel flwApplicableFrom = new FlowPanel();

    public final static String applicableByBlog = "Approved By";
    final Button btnShowAppPupup = new Button("Applicable From");
    final MenuItem mnuExport = new MenuItem("Export ITS");
    final Menu menu = new Menu();

    Style stl;

    //<editor-fold defaultstate="collapsed" desc="grdITS">
    interface GrdItemTaxStructure_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemTaxStructure_Mdl> {

        @Editor.Path("LocationText")
        ValueProvider<GrdItemTaxStructure_Mdl, String> locationText();

        @Editor.Path("accntAffectedText")
        ValueProvider<GrdItemTaxStructure_Mdl, String> accntAffectedText();

        @Editor.Path("atRate")
        ValueProvider<GrdItemTaxStructure_Mdl, String> atRate();

        @Editor.Path("siteText")
        ValueProvider<GrdItemTaxStructure_Mdl, String> siteText();

        @Editor.Path("amount")
        ValueProvider<GrdItemTaxStructure_Mdl, String> amount();

    }
    final DmGrid<GrdItemTaxStructure_Mdl, GrdItemTaxStructure_PrpAcs> grdITS = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdItemTaxStructure_PrpAcs.class));

    private Grid iniGrdReceiptVoucherConfig() {
        grdITS.addRowNumberColumn();
        grdITS.addTextColumn(grdITS.getPropertyAccess().locationText(), "Location", 90, "");
        grdITS.addTextColumn(grdITS.getPropertyAccess().name(), "Overhead", 170, "");
        grdITS.addTextColumn(grdITS.getPropertyAccess().atRate(), "Tax %", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdITS.addTextColumn(grdITS.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdITS.addTextColumn(grdITS.getPropertyAccess().accntAffectedText(), "Account Affected", 160, "");
        grdITS.addTextColumn(grdITS.getPropertyAccess().siteText(), "Site", 170, "");
        grdITS.initializeGrid(grdITS.getPropertyAccess().fieldHashCode(), 1280, 452, 2);
        return grdITS.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "ITS Name", 0, "NAME");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 495, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "nature");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public ItemTaxStructure_Dsg() {
        ini();
        iniFind();
        iniMenu();
        iniNature();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "500px", null, null, DmPosition.CENTER, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        DmClient.setStyle(flwApplicableFrom, "100%", "100%", null, null, DmPosition.NONE, true);
        flwApplicableFrom.add(btnShowAppPupup);

        userControl.addGeneralBlog(flwApplicableFrom);
        btnShowAppPupup.setWidth("100px");
        btnShowAppPupup.addStyleName("dm-button-grad");
        userControl.addGap(50);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "475px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(top());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "455px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grid()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel top() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "20px", null, null, DmPosition.CENTER, true);//75

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtITSName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total Tax Percent");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalTaxPercent));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel grid() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "455px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "455px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "455px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdReceiptVoucherConfig()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExport);
        return menu;
    }
}
