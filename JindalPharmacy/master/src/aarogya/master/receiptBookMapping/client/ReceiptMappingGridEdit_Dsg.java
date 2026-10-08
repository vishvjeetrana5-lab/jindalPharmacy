package aarogya.master.receiptBookMapping.client;

import aarogya.master.receiptBookMapping.shared.TxtHVoucherType_Mdl;
import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ReceiptMappingGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtEntryVoucher = new DmTextBox(50, true);

    //<editor-fold defaultstate="collapsed" desc="txtHReceiptVoucher">
    interface TxtHReceiptType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHVoucherType_Mdl, String> shortName();
    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHReceiptType_PrpAcs> txtHReceiptVoucher = new DmTextHelp(GWT.create(TxtHReceiptType_PrpAcs.class), 3, false);

    private void iniTxtHReceiptVoucher() {
        txtHReceiptVoucher.getDmGrid().addTextColumn(txtHReceiptVoucher.getDmGrid().getPropertyAccess().name(), "Type", 200, "Description");
        txtHReceiptVoucher.getDmGrid().addTextColumn(txtHReceiptVoucher.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "short_Name");
        txtHReceiptVoucher.getDmGrid().initializeGrid(txtHReceiptVoucher.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHReceiptVoucher.getHelper().addSort("description", DmSortType.ASC);
    }
    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="txtHRefundVoucher">
    interface TxtHRefundType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHVoucherType_Mdl, String> shortName();
    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHRefundType_PrpAcs> txtHRefundVoucher = new DmTextHelp(GWT.create(TxtHRefundType_PrpAcs.class), 3, false);

    private void iniTxtHRefundVoucher() {
        txtHRefundVoucher.getDmGrid().addTextColumn(txtHRefundVoucher.getDmGrid().getPropertyAccess().name(), "Type", 200, "Description");
        txtHRefundVoucher.getDmGrid().addTextColumn(txtHRefundVoucher.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "short_Name");
        txtHRefundVoucher.getDmGrid().initializeGrid(txtHRefundVoucher.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHRefundVoucher.getHelper().addSort("description", DmSortType.ASC);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="txtHIsDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHIsDefault() {
        txtHIsDefault.getDmGrid().addTextColumn(txtHIsDefault.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHIsDefault.getDmGrid().initializeGrid(txtHIsDefault.getDmGrid().getPropertyAccess().code(), 80, 0, 1);

    }
    //</editor-fold> 

    public ReceiptMappingGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHReceiptVoucher();
        iniTxtHRefundVoucher();
        iniTxtHIsDefault();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "90px", null, null, DmPosition.CENTER, true);

        //Entry Voucher
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Voucher");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryVoucher));
        txtEntryVoucher.setReadOnly(true);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Receipt Voucher
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt Voucher");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReceiptVoucher));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        //Refund Voucher
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Refund Voucher");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRefundVoucher));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Default
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsDefault));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
