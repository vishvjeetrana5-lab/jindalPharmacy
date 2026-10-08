package aarogya.pharmacy.indent.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.pharmacy.indent.shared.TxtHMedicineName_Mdl;
import com.google.gwt.dom.client.Style;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class IndentGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtReqDate = new DmTextBox(11, false);
    final DmTextBox txtRemarks = new DmTextBox(100, false);
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, true, 12, true);
    final DmTextBox txtUnit = new DmTextBox(20, false);
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmLabel lblReqisitionNoValue = new DmLabel("");
    DmLabel lblReqisitionNo = new DmLabel("");
    FlowPanel flwLblReqisitionNo = new FlowPanel();
    FlowPanel flwLblReqisitionNoOuter = new FlowPanel();
    final DmTextBoxNumeric txtMrp = new DmTextBoxNumeric(2, false, 12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    interface TxtHItemName_PrpAcs extends KeyField_T_PrpAcs<TxtHMedicineName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicineName_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHMedicineName_Mdl, String> skuName();

        @Editor.Path("Packing")
        ValueProvider<TxtHMedicineName_Mdl, String> packing();
    }

    final DmTextHelp<TxtHMedicineName_Mdl, TxtHItemName_PrpAcs> txtHMedicineName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    public void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Medicine Name", 330, "name");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "manualCode");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().skuName(), "SKU", 80, "skuName");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().packing(), "Packing", 80, "itemPacking");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().code(), 640, 0, 1);

        txtHMedicineName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public IndentGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHMedicineName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwLblReqisitionNo = new FlowPanel();
        DmClient.setStyle(flwLblReqisitionNo, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblReqisitionNo = new DmLabel("Requisition No.");
        lblReqisitionNo.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblReqisitionNo);
        flwLblReqisitionNo.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblReqisitionNoValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblReqisitionNoValue);
        flwLblReqisitionNo.add(flwTd);
        flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
        flwTable.add(flwLblReqisitionNoOuter);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Properties");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtProperties));
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnit));
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Required Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReqDate));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("MRP");
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtMrp));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemarks));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
