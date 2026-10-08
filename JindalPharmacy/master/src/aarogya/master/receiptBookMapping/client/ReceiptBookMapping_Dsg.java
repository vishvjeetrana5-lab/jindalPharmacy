package aarogya.master.receiptBookMapping.client;

import aarogya.master.receiptBookMapping.shared.GrdReceiptMapping_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ReceiptBookMapping_Dsg extends IsWidgetExtended {

    final static String widgetName = new pmMaster().RECEIPT_BOOK_MAPPING.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final PopupPanel ppForm = new PopupPanel();
    final Menu menu = new Menu();
    final MenuItem mnuExportCard = new MenuItem("Export Mapping");

    //<editor-fold defaultstate="collapsed" desc="txtHEntryPoint">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEntryPoint = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniTxtHEntryPoint() {
        txtHEntryPoint.getDmGrid().addTextColumn(txtHEntryPoint.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHEntryPoint.getDmGrid().initializeGrid(txtHEntryPoint.getDmGrid().getPropertyAccess().code(), 240, 0, 1);
        txtHEntryPoint.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
//    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {
//
//        @Editor.Path("EntryVoucherName")
//        ValueProvider<Find_Mdl, String> entryVoucherName();
//
//        @Editor.Path("ReceiptVoucherName")
//        ValueProvider<Find_Mdl, String> receiptVoucherName();
//
//        @Editor.Path("IsDefault")
//        ValueProvider<Find_Mdl, String> isDefault();
//
//    }
    final DmFind<KeyFieldExtended, KeyField_PrpAcs> dmFind = new DmFind(GWT.create(KeyField_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry Point", 250, "entryPoint");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryVoucherName(), "Entry Voucher ", 380, "entryVoucherName");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().receiptVoucherName(), "Receipt Voucher ", 360, "receiptVoucherName");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isDefault(), "Is Default ", 360, "isDefaultValue");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 300);

        dmFind.getHelper().addSort("entryPoint", DmSortType.ASC);

    }
    //</editor-fold>

    public ReceiptBookMapping_Dsg() {
        ini();
        createMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHEntryPoint();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "547px", null, null, DmPosition.CENTER, true);

        //Short Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "80%", null, "right", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Point Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEntryPoint));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Short Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(iniGrdReceiptMapping()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu createMenu() {
        menu.add(mnuExportCard);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="grdReceiptMappingDetail">
    interface GrdReceiptMapping_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdReceiptMapping_Mdl> {

        @Editor.Path("receiptVoucherName")
        ValueProvider<GrdReceiptMapping_Mdl, String> receiptVoucherName();

        @Editor.Path("refundVoucherName")
        ValueProvider<GrdReceiptMapping_Mdl, String> refundVoucherName();

        @Editor.Path("isDefaultName")
        ValueProvider<GrdReceiptMapping_Mdl, String> isDefaultName();

    }

    final DmGrid<GrdReceiptMapping_Mdl, GrdReceiptMapping_PrpAcs> grdReceiptMappingDetail = new DmGrid(GWT.create(GrdReceiptMapping_PrpAcs.class));

    private Grid iniGrdReceiptMapping() {
        grdReceiptMappingDetail.addRowNumberColumn();
        grdReceiptMappingDetail.addTextColumn(grdReceiptMappingDetail.getPropertyAccess().name(), "Entry Voucher Name", 250, null);
        grdReceiptMappingDetail.addTextColumn(grdReceiptMappingDetail.getPropertyAccess().receiptVoucherName(), "Receipt Voucher Name", 250, null);
        grdReceiptMappingDetail.addTextColumn(grdReceiptMappingDetail.getPropertyAccess().refundVoucherName(), "Refund Voucher Name", 250, null);
        grdReceiptMappingDetail.addTextColumn(grdReceiptMappingDetail.getPropertyAccess().isDefaultName(), "Default", 100, null);
        grdReceiptMappingDetail.initializeGrid(grdReceiptMappingDetail.getPropertyAccess().fieldHashCode(), 900, 500, 2);
        return grdReceiptMappingDetail.getGrid();
    }
    //</editor-fold>
}
