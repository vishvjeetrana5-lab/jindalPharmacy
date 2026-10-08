package dataman.utility.docIdMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.utility.docIdMaster.shared.*;

public class DocIdMaster_Dsg {

    final String widgetName = new lmUtility().DOCID_MASTER.caption;
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtVoucherPrefix = new DmTextBox(35, true);
    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniHsite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHSite.getHelper().addSort("Name", DmSortType.ASC);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("serachCode")
        ValueProvider<Find_Mdl, String> serachCode();

        @Editor.Path("voucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("voucherDescription")
        ValueProvider<Find_Mdl, String> voucherDescription();

        @Editor.Path("prefix")
        ValueProvider<Find_Mdl, String> prefix();

        @Editor.Path("voucherSerise")
        ValueProvider<Find_Mdl, String> voucherSerise();

        @Editor.Path("siteName")
        ValueProvider<Find_Mdl, String> siteName();

        @Editor.Path("siteCode")
        ValueProvider<Find_Mdl, String> siteCode();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serachCode(), "Search Code", 120, "SearchCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Voucher Type", 100, "V_Type");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDescription(), "Description", 150, "Description");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().prefix(), "Voucher Prefix", 100, "V_Prefix");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherSerise(), "Voucher No.", 100, "serialNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().siteName(), "Site Name", 150, "Site_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().siteCode(), "Site Code", 100, "Code");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("SearchCode", DmSortType.ASC);
        dmFind.getHelper().addSort("V_Type", DmSortType.ASC);
        dmFind.getHelper().addSort("Description", DmSortType.ASC);
        dmFind.getHelper().addSort("V_Prefix", DmSortType.ASC);
        dmFind.getHelper().addSort("serialNo", DmSortType.ASC);
        dmFind.getHelper().addSort("Site_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("Code", DmSortType.ASC);
    }
//</editor-fold>

    public DocIdMaster_Dsg() {
        fIni();
    }

    private void fIni() {
        iniFind();
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "101%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(fCreateVoucherGrid());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

        iniHsite();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "950px", "20px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        txtHSite.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Voucher Prefix");
        lblTemp.getElement().getStyle().setMarginLeft(150, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherPrefix));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    interface Grd_PrpAcs extends KeyField_T_PrpAcs<GrdDocIdMaster_Mdl> {

        @Editor.Path("voucherDescription")
        ValueProvider<GrdDocIdMaster_Mdl, String> voucherDescription();

        @Editor.Path("voucherSerise")
        ValueProvider<GrdDocIdMaster_Mdl, String> voucherSerise();

    }

    final DmGrid<GrdDocIdMaster_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    private FlowPanel fCreateVoucherGrid() {
        FlowPanel flwTable = new FlowPanel();
        FlowPanel flwTr;
        FlowPanel flwTd;

        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Voucher Type", 150, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().voucherDescription(), "Voucher Description", 740, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().voucherSerise(), "Series", 50, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().code(), 1280, 350);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(dmGrd.getGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
