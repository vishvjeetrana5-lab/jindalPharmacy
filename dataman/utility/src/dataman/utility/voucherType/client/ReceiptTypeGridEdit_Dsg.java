package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.TxtHPaymentMode_Mdl;

public abstract class ReceiptTypeGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHPaymentMode">
    interface DmtxtHPaymentMode_PrpAcs extends KeyField_T_PrpAcs<TxtHPaymentMode_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHPaymentMode_Mdl, String> shortName();

    }

    final DmTextHelp<TxtHPaymentMode_Mdl, DmtxtHPaymentMode_PrpAcs> txtHPaymentMode = new DmTextHelp(GWT.create(DmtxtHPaymentMode_PrpAcs.class), 7, true);

    public Grid iniPaymentMode() {
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().name(), "Payment Mode", 1, "name");
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHPaymentMode.getDmGrid().initializeGrid(txtHPaymentMode.getDmGrid().getPropertyAccess().code(), 350, 200, 1);
        txtHPaymentMode.getHelper().addSort("name", DmSortType.ASC);
        return txtHPaymentMode.getDmGrid().getGrid();

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHIsDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniTxtHIsDefault() {
        txtHIsDefault.getDmGrid().addTextColumn(txtHIsDefault.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsDefault.getDmGrid().initializeGrid(txtHIsDefault.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public ReceiptTypeGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniPaymentMode();
        iniTxtHIsDefault();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "30px", null, null, DmPosition.CENTER, true);

        //Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Payment Mode");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPaymentMode));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is Default");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsDefault));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }
}
