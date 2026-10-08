package aarogya.pharmacy.duplicateBarcodeGeneration.client;

import aarogya.pharmacy.duplicateBarcodeGeneration.shared.GrdDuplicateBarcodeGeneration_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class DuplicateBarcodeGeneration_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().DUPLICATE_BARCODE_GENERATION.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnSetting = new Button();
    final Button btnClose = new Button();
    final Button btnRefresh = new Button();
    final Button btnPrint = new Button();
    final DmLabel lblBalCompanyBook = new DmLabel("0");
    final DmLabel lblAmtCr = new DmLabel("0");
    final DmLabel lblAmtDr = new DmLabel("0");
    final DmLabel lblAmtNotClg_Dr = new DmLabel("0");
    final DmLabel lblAmtNotClg_Cr = new DmLabel("0");

    final DmLabel lblOpnAsPerBank = new DmLabel("0");
    final DmLabel lblAmtClg_Dr = new DmLabel("0");
    final DmLabel lblAmtClg_Cr = new DmLabel("0");
    final DmLabel lblClgAmt = new DmLabel("0");

    final DmTextBox txtSearchText = new DmTextBox(50, false);

    ColumnConfig<GrdDuplicateBarcodeGeneration_Mdl, String> printColumn;
    ColumnConfig<GrdDuplicateBarcodeGeneration_Mdl, String> noStickerPrintColumn;
    int count = 0, printBarCodeColIndex = 0, noOfStickersColIndex = 0, headerValue = 1;
    Style stl;

    public DuplicateBarcodeGeneration_Dsg() {
        ini();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "550px", null, null, DmPosition.CENTER, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("550px");

        vrtInnerForm.add(toolContainer);
        createToolControl();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

        flwTableDisplaySection.setVisible(true);
    }

    private void createToolControl() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "525px", "left", null, DmPosition.CENTER, true);
        /*====================================< 1st Row (Main Form) >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTd.add(topArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row (First Grid)>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "450px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "450px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdAcountDetail()));

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTd, flwTr;
        DmLabel lblTemp;
        HorizontalPanel hrtPanel;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);
        /*====================================< Left Section >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchText));
        txtSearchText.setHeight("18px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("lightgray");
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        hrtPanel.getElement().getStyle().setHeight(22, Style.Unit.PX);
        flwTd.add(hrtPanel);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdBarCode">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDuplicateBarcodeGeneration_Mdl> {

        @Editor.Path("Barcode")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> barcode();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> expiryDate();

        @Editor.Path("Size")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> size();

        @Editor.Path("Color")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> color();

        @Editor.Path("BatchNo")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> batchNo();

        @Editor.Path("Department")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> department();

        @Editor.Path("ItemName")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> itemName();

        @Editor.Path("IsBarcode")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> isBarcode();

        @Editor.Path("ItemMRP")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> itemMRP();

        @Editor.Path("NoOFStricker")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> noOFStricker();

        @Editor.Path("SkuQty")
        ValueProvider<GrdDuplicateBarcodeGeneration_Mdl, String> skuQty();
    }

    final DmGrid<GrdDuplicateBarcodeGeneration_Mdl, Grd_PrpAcs> grdBarCode = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdAcountDetail() {

        grdBarCode.addRowNumberColumn().setResizable(true);
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().itemName(), "Item", 260, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().department(), "Department", 180, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().batchNo(), "Batch No.", 120, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().color(), "Color", 100, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().size(), "Size", 70, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().expiryDate(), " Expiry Date", 90, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().itemMRP(), "MRP", 90, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().barcode(), "Barcode", 130, "");
        count++;
        grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().skuQty(), "Sku Qty.", 70, "");
        count++;
        noStickerPrintColumn = grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().noOFStricker(), "Sticker", 50, "");
        noOfStickersColIndex = count;
        count++;
        printColumn = grdBarCode.addTextColumn(grdBarCode.getPropertyAccess().isBarcode(), "", 50, "");
        printBarCodeColIndex = count;
        printColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        grdBarCode.initializeGrid(grdBarCode.getPropertyAccess().fieldHashCode(), 1260, 450);

        printColumn.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                String headerStyle = "<span class = 'fa fa-check-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                return headerStyle;
            }
        });

        printColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                double val = DmShared.vNull(grdBarCode.getGrid().getStore().get(context.getIndex()).getIsBarcode()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdBarCode.getGrid();
    }
    //</editor-fold>

}
