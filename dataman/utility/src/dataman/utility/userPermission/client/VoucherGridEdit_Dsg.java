package dataman.utility.userPermission.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.userPermission.shared.TxtHVoucher_Mdl;

public abstract class VoucherGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtVoucherType = new DmTextBox();
    final DmTextBox txtVoucherCategory = new DmTextBox();
    final DmTextBox txtVoucherDescription = new DmTextBox();
    final DmTextBoxNumeric txtEntryForDays = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 3, false);
    final DmTextBoxNumeric txtSaveEntryForDays = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 3, false);
    final DmTextBoxNumeric txtDeleteEntryForDays = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 3, false);
    final DmTextBoxNumeric txtAllowValuesForDays = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHVoucher">
    interface txtHVoucherGrid_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucher_Mdl> {

        @Editor.Path("voucherType")
        ValueProvider<TxtHVoucher_Mdl, String> voucherType();

        @Editor.Path("VoucherDescription")
        ValueProvider<TxtHVoucher_Mdl, String> description();

        @Editor.Path("voucherCategory")
        ValueProvider<TxtHVoucher_Mdl, String> voucherCategory();
    }

    final DmTextHelp<TxtHVoucher_Mdl, txtHVoucherGrid_PrpAcs> txtHVoucherGrid = new DmTextHelp(GWT.create(txtHVoucherGrid_PrpAcs.class), 10, true);

    private void initxtHVoucher() {

        txtHVoucherGrid.getDmGrid().addTextColumn(txtHVoucherGrid.getDmGrid().getPropertyAccess().voucherType(), "Short Name", 150, "Short_Name");
        txtHVoucherGrid.getDmGrid().addTextColumn(txtHVoucherGrid.getDmGrid().getPropertyAccess().description(), "Description", 300, "Description");
        txtHVoucherGrid.getDmGrid().addTextColumn(txtHVoucherGrid.getDmGrid().getPropertyAccess().voucherCategory(), "Category", 150, "Category");
        txtHVoucherGrid.getDmGrid().initializeGrid(txtHVoucherGrid.getDmGrid().getPropertyAccess().code(), 650, 310);

        txtHVoucherGrid.getHelper().addSort("Short_Name", DmSortType.ASC);
        txtHVoucherGrid.getHelper().addSort("Description", DmSortType.ASC);
        txtHVoucherGrid.getHelper().addSort("Category", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActiveYN">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAuditYN = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    public void initxtHActiveYN() {
        txtHAuditYN.getDmGrid().addTextColumn(txtHAuditYN.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHAuditYN.getDmGrid().initializeGrid(txtHAuditYN.getDmGrid().getPropertyAccess().code(), 150, 0, 1); //244
    }
    //</editor-fold>

    public VoucherGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
        initxtHVoucher();
        initxtHActiveYN();
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "125px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherGrid));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("View Entry For Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryForDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Save Entry For Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaveEntryForDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Delete Entry For Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDeleteEntryForDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Value Upto");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllowValuesForDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Audit Allow");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAuditYN));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
