package aarogya.master.hsnMaster.client;

import aarogya.master.hsnMaster.shared.GrdITS_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RateGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtRateFrom = new DmTextBoxNumeric(2, false, 8, true);
    final DmTextBoxNumeric txtRateUpto = new DmTextBoxNumeric(2, false, 8, true);
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnPurchase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHITSOnPurchase() {
        txtHITSOnPurchase.getDmGrid().addTextColumn(txtHITSOnPurchase.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnPurchase.getDmGrid().initializeGrid(txtHITSOnPurchase.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnPurchase.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnPurchaseReturn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnPurchaseReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHITSOnPurchaseReturn() {
        txtHITSOnPurchaseReturn.getDmGrid().addTextColumn(txtHITSOnPurchaseReturn.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnPurchaseReturn.getDmGrid().initializeGrid(txtHITSOnPurchaseReturn.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnPurchaseReturn.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSale">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSale = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHITSOnSale() {
        txtHITSOnSale.getDmGrid().addTextColumn(txtHITSOnSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSale.getDmGrid().initializeGrid(txtHITSOnSale.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSale.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSaleReturn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSaleReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHITSOnSaleReturn() {
        txtHITSOnSaleReturn.getDmGrid().addTextColumn(txtHITSOnSaleReturn.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSaleReturn.getDmGrid().initializeGrid(txtHITSOnSaleReturn.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSaleReturn.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSaleAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaleAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSaleAC() {
        txtHSaleAC.getDmGrid().addTextColumn(txtHSaleAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSaleAC.getDmGrid().initializeGrid(txtHSaleAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHSaleAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSaleReturnAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaleReturnAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSaleReturnAC() {
        txtHSaleReturnAC.getDmGrid().addTextColumn(txtHSaleReturnAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSaleReturnAC.getDmGrid().initializeGrid(txtHSaleReturnAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHSaleReturnAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPurchaseAC() {
        txtHPurchaseAC.getDmGrid().addTextColumn(txtHPurchaseAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPurchaseAC.getDmGrid().initializeGrid(txtHPurchaseAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHPurchaseAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseReturnAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseReturnAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPurchaseReturnAC() {
        txtHPurchaseReturnAC.getDmGrid().addTextColumn(txtHPurchaseReturnAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPurchaseReturnAC.getDmGrid().initializeGrid(txtHPurchaseReturnAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHPurchaseReturnAC.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    public RateGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHSaleAC();
        iniTxtHSaleReturnAC();
        iniTxtHPurchaseAC();
        iniTxtHPurchaseReturnAC();
        iniTxtHITSOnSale();
        iniTxtHITSOnSaleReturn();
        iniTxtHITSOnPurchase();
        iniTxtHITSOnPurchaseReturn();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "190px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////////////////////////1st///////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "22px", "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate From");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRateFrom));
        flwTR.add(flwTD);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate Upto");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRateUpto));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //============================================ 3rd Row ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Purchase");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnPurchase));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END ================================================

        //======================================== 4th Row ==================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Sale");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnSale));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Purchase Return");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnPurchaseReturn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END ================================================

        //======================================== 4th Row ==================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Sale Return");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnSaleReturn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END =======================================

        //======================= 5th Row (ITS ON SALE) ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales A/c");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSaleAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //======================= 5th Row (ITS ON SALE) ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Return A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseReturnAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales Return A/c");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSaleReturnAC));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        return flwTable;
    }

}
