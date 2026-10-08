package aarogya.master.additionalUserPermission.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class PaymentModeGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    public PaymentModeGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
        initxtHPayment();
        initxtHVoucherType();
        iniTxtHIsDefault();
    }

    //<editor-fold defaultstate="collapsed" desc="txtHPayment">
    interface txtHPaymentGrid_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, txtHPaymentGrid_PrpAcs> txtHPaymentGrid = new DmTextHelp(GWT.create(txtHPaymentGrid_PrpAcs.class), 6, true);

    private void initxtHPayment() {

        txtHPaymentGrid.getDmGrid().addTextColumn(txtHPaymentGrid.getDmGrid().getPropertyAccess().name(), "Payment Type", 130, "name");
        txtHPaymentGrid.getDmGrid().initializeGrid(txtHPaymentGrid.getDmGrid().getPropertyAccess().code(), 150, 0);

        txtHPaymentGrid.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, txtHPaymentGrid_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(txtHPaymentGrid_PrpAcs.class), 9, true);

    private void initxtHVoucherType() {

        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Voucher Type", 160, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 170, 0);

        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHIsDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniTxtHIsDefault() {
        txtHIsDefault.getDmGrid().addTextColumn(txtHIsDefault.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsDefault.getDmGrid().initializeGrid(txtHIsDefault.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    private FlowPanel section1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "85%", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Payment Mode");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPaymentGrid));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Is Default");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsDefault));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

}
