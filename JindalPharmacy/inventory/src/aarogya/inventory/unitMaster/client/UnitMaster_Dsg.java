package aarogya.inventory.unitMaster.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.unitMaster.shared.Find_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class UnitMaster_Dsg extends IsWidgetExtended {

    public final String widgetName = new pmInventory().UNIT_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtUnitName = new DmSuggestBox(10, 5, true, "Name", DmDataType.STRING);
    final DmTextBoxNumeric txtDecimalPlace = new DmTextBoxNumeric(0, false, 1, false);

    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("DecimalPlaces")
        ValueProvider<Find_Mdl, String> decimalPlaces();

        @Editor.Path("GstUnitName")
        ValueProvider<Find_Mdl, String> gstUnitName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Unit Name", 450, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().decimalPlaces(), "Decimal Places", 250, "decimalPlaces");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gstUnitName(), "GST Unit", 300, "gstUnitName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGSTUnit">
    interface TxtHGSTUnit_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();
    }

    final DmTextHelp<KeyFieldExtended, TxtHGSTUnit_PrpAcs> txtHGSTUnit = new DmTextHelp(GWT.create(TxtHGSTUnit_PrpAcs.class), 4, true);

    private void iniTxtHGSTUnit() {
        txtHGSTUnit.getDmGrid().addTextColumn(txtHGSTUnit.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGSTUnit.getDmGrid().addTextColumn(txtHGSTUnit.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Code", 100, "manualCode");
        txtHGSTUnit.getDmGrid().initializeGrid(txtHGSTUnit.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHGSTUnit.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public UnitMaster_Dsg() {
        ini();
        iniTxtHGSTUnit();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "40px", null, null, DmPosition.CENTER, true);

        //Category Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnitName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //VAT
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Decimal Places");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDecimalPlace));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Unit");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGSTUnit));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
