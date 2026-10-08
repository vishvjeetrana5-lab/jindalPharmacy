package aarogya.pharmacy.materialCategoryMast.client;

import aarogya.pharmacy.materialCategoryMast.client.*;
import aarogya.pharmacy.materialCategoryMast.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class MaterialCategoryMast_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MATERIAL_CATEGORY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtCategoryName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);

    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("IsCommon")
        ValueProvider<Find_Mdl, String> isCommon();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 8);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Category Name", 350, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isCommon(), "Common", 100, "isCommon");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="TxtHHSN">
//    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {
//
//        @Editor.Path("Hsn")
//        ValueProvider<TxtHHSN_Mdl, String> hsn();
//    }
//    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);
//
//    private void initxtHHSN() {
//        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
//        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
//        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
//        txtHHSN.getHelper().addSort("name", DmSortType.ASC);
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHFilterType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsCommon = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHFilterType() {
        txtHIsCommon.getDmGrid().addTextColumn(txtHIsCommon.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsCommon.getDmGrid().initializeGrid(txtHIsCommon.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsCommon.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    public MaterialCategoryMast_Dsg() {
        ini();
        iniTxtHFilterType();
//        iniMenu();
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
        DmClient.setStyle(flwTable, "60%", "20px", null, null, DmPosition.CENTER, true);

        //Category Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCategoryName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Used In");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsCommon));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

//    private Menu iniMenu() {
//        menu.add(mnuExportCard);
//        return menu;
//    }
}
