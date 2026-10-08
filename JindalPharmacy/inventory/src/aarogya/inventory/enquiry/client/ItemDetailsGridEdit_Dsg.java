package aarogya.inventory.enquiry.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.enquiry.shared.GrdIndentDetails_Mdl;
import aarogya.inventory.enquiry.shared.TxtHItem_Mdl;

public abstract class ItemDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtProperties = new DmTextBox();
    final DmTextBox txtDtReq = new DmTextBox(11, false);
    final DmTextBox txtUnit = new DmTextBox(11, false);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final Button btnAddIndent = new Button("Add Indent");
    final FlowPanel flwPopup = new FlowPanel();
    final FlowPanel flwIndentBtn = new FlowPanel();
    DmLabel lblProperties = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface GridItem_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();

        @Editor.Path("DisplayUnit")
        ValueProvider<TxtHItem_Mdl, String> displayUnit();

    }
    final DmTextHelp<TxtHItem_Mdl, GridItem_PrpAcs> txtHItemName = new DmTextHelp(GWT.create(GridItem_PrpAcs.class), 6, true);

    private void iniTxtHItem() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 200, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().displayUnit(), "Unit", 50, "purchaseUnitName");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 350, 0, 1);
        txtHItemName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ItemDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHItem();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "320px", null, null, DmPosition.CENTER, true);

        //============================== Upper Area ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "105px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "100px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(upperArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        //============================== Bottom Area =================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "220px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(bottomArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        return flwTable;
    }

    private FlowPanel upperArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "105px", null, null, DmPosition.CENTER, true);

        //================================ 1st Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        HorizontalPanel hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtHItemName));
        hrtPnl.setCellWidth(txtHItemName, "100%");
        DmClient.setStyle(flwPopup, "100%", "20px", "left", null, DmPosition.NONE, true);
        hrtPnl.add(flwPopup);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblProperties = new DmLabel("Properties");
        lblProperties.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblProperties.getElement().getStyle().setColor("Blue");
        flwTd.add(lblProperties);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        //============================== END =========================================
        //================================ 2nd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Qty");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl = new DmLabel("Unit");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================
        //================================ 3rd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Required Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDtReq));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================

        //================================ 4th Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        //============================== 1st Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", "18px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Detail :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "20px", "right", null, DmPosition.NONE, false);
        btnAddIndent.addStyleName("dm-button-grad");
        btnAddIndent.setWidth("75px");
        btnAddIndent.setHeight("20px");
        btnAddIndent.getElement().setAttribute("style", btnAddIndent.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddIndent.setTitle("Add Indent");
        flwIndentBtn.add(btnAddIndent);
        flwTd.add(flwIndentBtn);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        //============================== 2nd Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(iniIndent());
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdIndent">
    interface GrdRIndent_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdIndentDetails_Mdl> {

        @Editor.Path("IndentSkuText")
        ValueProvider<GrdIndentDetails_Mdl, String> indentSkuText();

        @Editor.Path("IndentQty")
        ValueProvider<GrdIndentDetails_Mdl, String> indentQty();

        @Editor.Path("ReqDate")
        ValueProvider<GrdIndentDetails_Mdl, String> reqDate();

        @Editor.Path("IndentDate")
        ValueProvider<GrdIndentDetails_Mdl, String> indentDate();

        @Editor.Path("Image")
        ValueProvider<GrdIndentDetails_Mdl, String> image();

    }

    final DmGrid<GrdIndentDetails_Mdl, GrdRIndent_Mdl_PrpAcs> grdIndent = new DmGrid(GWT.create(GrdRIndent_Mdl_PrpAcs.class));

    private Grid iniIndent() {
        ColumnConfig<GrdIndentDetails_Mdl, String> vacantImage;
        vacantImage = grdIndent.addTextColumn(grdIndent.getPropertyAccess().image(), "", 30, "");
        vacantImage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdIndent.addTextColumn(grdIndent.getPropertyAccess().indentQty(), "Indent SKU Qty", 115, "reqQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdIndent.addTextColumn(grdIndent.getPropertyAccess().indentSkuText(), "Sku", 50, "unit");
        grdIndent.addTextColumn(grdIndent.getPropertyAccess().reqDate(), "Req. Date", 90, "reqDate");
        grdIndent.addTextColumn(grdIndent.getPropertyAccess().name(), "Indent No", 140, "indentNo");
        grdIndent.addTextColumn(grdIndent.getPropertyAccess().indentDate(), "Indent Date", 90, "indentDate");

        vacantImage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdIndent.initializeGrid(grdIndent.getPropertyAccess().fieldHashCode(), 500, 198, 5);

        vacantImage.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:12px; color:red;'></span>");
            }
        });

        return grdIndent.getGrid();
    }
//</editor-fold>

}
