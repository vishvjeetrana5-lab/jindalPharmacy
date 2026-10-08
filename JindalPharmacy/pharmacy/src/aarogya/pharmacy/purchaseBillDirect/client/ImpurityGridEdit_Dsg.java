package aarogya.pharmacy.purchaseBillDirect.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class ImpurityGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBoxNumeric txtFoundPer = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtAcceptPer = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtNetPer = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtNetQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBoxNumeric txtSupplierQty = new DmTextBoxNumeric(4, false, 12, false);

    //<editor-fold defaultstate="collapsed" desc="txtHimpurity">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHImpurity = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHImpurity() {
        txtHImpurity.getDmGrid().addTextColumn(txtHImpurity.getDmGrid().getPropertyAccess().name(), "Impurity", 100, "name");
        txtHImpurity.getDmGrid().initializeGrid(txtHImpurity.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHImpurity.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>    

    public ImpurityGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHImpurity();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        container.getElement().setAttribute("Style", "width:100%; float:left;");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("style", "width: 96%; float: left; padding: 5px;");
//        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Impurity");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(txtHImpurity));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Found %");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtFoundPer.setWidth("40%");
        flwTd.add(vl.add(txtFoundPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Accept %");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtAcceptPer.setWidth("40%");
        flwTd.add(vl.add(txtAcceptPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Net %");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtNetPer.setWidth("40%");
        flwTd.add(vl.add(txtNetPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Net Qty.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtNetQty.setWidth("40%");
        flwTd.add(vl.add(txtNetQty));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Supplier Qty.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtSupplierQty.setWidth("40%");
        flwTd.add(vl.add(txtSupplierQty));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
