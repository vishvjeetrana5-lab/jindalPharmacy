package aarogya.pharmacy.medicineModification.client;

import aarogya.pharmacy.medicineModification.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineModification_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmPharmacy().MEDICINE_MODIFICATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, (DmUserControl) null);
    final DmTextBox txtName = new DmTextBox(250, false);
    final DmTextBox txtPackSize = new DmTextBox(15, true);
    final DmSuggestBox txtDisplayName = new DmSuggestBox(100, 10, true, "Name", DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="TxtHMedGroup">
    interface TxtHMedGroup_PrpAcs extends KeyField_T_PrpAcs<TxtHMedGrp_Mdl> {

    }
    final DmTextHelp<TxtHMedGrp_Mdl, TxtHMedGroup_PrpAcs> txtHMedGroup = new DmTextHelp(GWT.create(TxtHMedGroup_PrpAcs.class), 4, true);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHMedGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 185, 0, 1);

        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHCompany_Mdl, String> shortName();
    }

    final DmTextHelp<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelp(GWT.create(TxtHCompany_PrpAcs.class), 7, true);

    private void iniTxtHCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Company")
        ValueProvider<Find_Mdl, String> company();

        @Editor.Path("Composition")
        ValueProvider<Find_Mdl, String> composition();

        @Editor.Path("MedGroup")
        ValueProvider<Find_Mdl, String> medGroup();

        @Editor.Path("RestrictGroup")
        ValueProvider<Find_Mdl, String> restrictGroup();

        @Editor.Path("Sku")
        ValueProvider<Find_Mdl, String> sku();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("MedicinePacking")
        ValueProvider<Find_Mdl, String> medicinePacking();

        @Editor.Path("ModifiedOn")
        ValueProvider<Find_Mdl, String> medifiedOn();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Medicine", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 80, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().company(), "Medicine Company", 100, "companyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().composition(), "Base Composition", 100, "compositionName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medGroup(), "Medicine Group", 100, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().restrictGroup(), "Res. Group", 80, "resName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sku(), "SKU", 50, "sku");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medicinePacking(), "Packing", 80, "itemPacking");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 50, "activeText");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medifiedOn(), "Modified On", 80, "modifiedDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public MedicineModification_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHCompany();
        iniTxtHCategory();
        iniTxtHMedGroup();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "80%", "427px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "105px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "320px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniCompositionDetails()));

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "88px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Display Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Company");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCompany));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Category");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Group");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedGroup));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Packing");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPackSize));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdCompositionDetails">
    interface GrdCompositionDetails_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdComposition_Mdl> {

        @Editor.Path("Power")
        ValueProvider<GrdComposition_Mdl, String> power();

        @Editor.Path("BaseText")
        ValueProvider<GrdComposition_Mdl, String> baseText();

        @Editor.Path("UnitText")
        ValueProvider<GrdComposition_Mdl, String> unitText();

        @Editor.Path("RestrictGroupName")
        ValueProvider<GrdComposition_Mdl, String> restrictGroupName();

    }

    final DmGrid<GrdComposition_Mdl, GrdCompositionDetails_Mdl_PrpAcs> grdCompositionDetails = new DmGrid(GWT.create(GrdCompositionDetails_Mdl_PrpAcs.class));

    private Grid iniCompositionDetails() {
        grdCompositionDetails.addRowNumberColumn();
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().name(), "Composition", 1, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().restrictGroupName(), "Restricted Group", 120, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().power(), "Power", 120, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().unitText(), "Unit", 120, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().baseText(), "Base Composition", 150, "");
        grdCompositionDetails.initializeGrid(grdCompositionDetails.getPropertyAccess().fieldHashCode(), 1020, 320, 2);

        return grdCompositionDetails.getGrid();
    }
    //</editor-fold>

}
