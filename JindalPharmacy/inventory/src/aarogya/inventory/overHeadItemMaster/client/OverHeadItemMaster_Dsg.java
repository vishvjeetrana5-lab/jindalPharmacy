package aarogya.inventory.overHeadItemMaster.client;

import aarogya.inventory.overHeadItemMaster.shared.*;
import aarogya.inventory.overHeadItemMaster.shared.TxtHHSN_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

public class OverHeadItemMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().OVERHEAD_ITEM_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final Menu menu = new Menu();
    final MenuItem mnuManageSite = new MenuItem("Manage Site");

    //<editor-fold defaultstate="collapsed" desc="Panels">
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Text Boxes">
    final DmSuggestBox txtItemName = new DmSuggestBox(50, 10, true, "Name", DmEnum.DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(20, false);
    final DmTextBox txtItemGSTNature = new DmTextBox(250, false);
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Text Helps">
    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">

    @Override
    public Widget asWidget() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void searchRecord(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHSN_Mdl, String> hsn();

        @Editor.Path("ItemGSTNatureText")
        ValueProvider<TxtHHSN_Mdl, String> gstNature();
    }
    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);

    private void iniTxtHHSN() {
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().gstNature(), "GST Nature", 80, "");
        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHHSN.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSKU">
    interface SKU_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {
    }
    final DmTextHelp<Sku_Mdl, SKU_PrpAcs> txtHSKU = new DmTextHelp(GWT.create(SKU_PrpAcs.class), 4, true);

    private void iniSKU() {
        txtHSKU.getDmGrid().addTextColumn(txtHSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSKU.getDmGrid().initializeGrid(txtHSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    interface TxtHCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHCategory_Mdl> {

    }
    final DmTextHelp<TxtHCategory_Mdl, TxtHCategory_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(TxtHCategory_PrpAcs.class), 6, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 185, 0, 1);

        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHMedGroup() {
        txtHGroup.getDmGrid().addTextColumn(txtHGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGroup.getDmGrid().initializeGrid(txtHGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHGroup.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Group")
        ValueProvider<Find_Mdl, String> group();

        @Editor.Path("SkuName")
        ValueProvider<Find_Mdl, String> sku();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("ItemTypeText")
        ValueProvider<Find_Mdl, String> itemTypeText();

        @Editor.Path("HsnName")
        ValueProvider<Find_Mdl, String> hsnName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 9);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Item Name", 250, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().hsnName(), "HSN", 120, "hsnName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 100, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().group(), "Group", 120, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().itemTypeText(), "Type", 130, "typename");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sku(), "SKU", 70, "sku");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 80, "activeText");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    public OverHeadItemMaster_Dsg() {
        ini();
        iniTxtHHSN();
        iniSKU();
        iniTxtHCategory();
        iniTxtHMedGroup();
        iniTxtHActive();
        iniOtherMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

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
        DmClient.setStyle(flwTable, "60%", "175px", null, null, DmEnum.DmPosition.CENTER, true);
//-------------------------------------------------1st Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//-------------------------------------------------2nd Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//-------------------------------------------------3rd Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("HSN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHSN));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//-------------------------------------------------4th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GST Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemGSTNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//-------------------------------------------------5th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------6th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------7th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSKU));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        return menu;
    }

}
