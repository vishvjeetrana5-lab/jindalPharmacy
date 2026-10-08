package aarogya.pharmacy.medicineDepartmentInfo.client;

import aarogya.pharmacy.medicineDepartmentInfo.shared.Find_Mdl;
import aarogya.pharmacy.medicineDepartmentInfo.shared.GrdMedicineDepartmentInfo_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineDepartmentInfo_Dsg extends IsWidgetExtended {

    final String widgetName = "Medicine Department Info";

    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 100, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSiteName">
    interface GridSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }

    final DmTextHelp<KeyFieldExtended, GridSite_PrpAcs> txtHSiteName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(GridSite_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Site Name", 0, "name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().fieldHashCode(), 245, 0, 1);
        txtHSiteName.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("SiteName")
        ValueProvider<Find_Mdl, String> siteName();

        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().siteName(), "Site Name", 180, "siteName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department", 200, "departmentName");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 2);
        dmFind.getHelper().addSort("siteName", DmSortType.ASC);
    }

//</editor-fold>
    public MedicineDepartmentInfo_Dsg() {
        ini();
        iniTxtHDepartment();
        iniTxtHSite();
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

        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "460px", null, null, DmPosition.CENTER, true);

        //======================= 1st Row (MEDICINE GROUP) ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSiteName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.getElement().getStyle().setMarginLeft(101, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);

        flwTable.add(flwTr);
        //============================== END =========================================

        //======================= 3rd Row (IS COMMON) ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniMedDetails()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //============================== END =========================================
        return flwTable;
    }

    interface GrdMedDetails_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDepartmentInfo_Mdl> {

        @Editor.Path("MinStock")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> minStock();

        @Editor.Path("MaxStock")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> maxStock();

        @Editor.Path("ReOrder")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> reOrder();

        @Editor.Path("BinName")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> binName();

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> skuName();

    }

    final DmGrid<GrdMedicineDepartmentInfo_Mdl, GrdMedDetails_Mdl_PrpAcs> grdMedDetails = new DmGrid(GWT.create(GrdMedDetails_Mdl_PrpAcs.class));

    private Grid iniMedDetails() {
        grdMedDetails.addRowNumberColumn();
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().name(), "Medicine", 220, "");
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().skuName(), "SKU", 100, "");
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().minStock(), "Min. Level", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().maxStock(), "Max. Level", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().reOrder(), "Re-Order Level", 118, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().binName(), "Bin", 150, "");

        grdMedDetails.initializeGrid(grdMedDetails.getPropertyAccess().fieldHashCode(), 900, 400, 2);

        return grdMedDetails.getGrid();
    }
    //</editor-fold>

}
