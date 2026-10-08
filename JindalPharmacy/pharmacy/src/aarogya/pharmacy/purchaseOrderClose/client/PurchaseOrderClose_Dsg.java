package aarogya.pharmacy.purchaseOrderClose.client;


import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.pharmacy.purchaseOrderClose.shared.GrdPurchaseOrderClose_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class PurchaseOrderClose_Dsg {

    final String widgetName = new pmPharmacy().PURCHASE_ORDER_CLOSE.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();
    final Button btnSetting = new Button();

    public PurchaseOrderClose_Dsg() {
        fIni();
        createGrid();
    }

    private void fIni() {
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("576px");
        flwForm.add(toolContainer);
        flwForm.getElement().getStyle().setBackgroundColor("white");
        createToolControl();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.add(fCreateWorkingGrid());
        vrtInnerForm.add(flwWorkingArea);

        flwForm.add(vrtInnerForm);
        flwTableDisplaySection.setVisible(true);

    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Ok");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

    }

    private FlowPanel fCreateWorkingGrid() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "980px", "529px", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "529px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(dmGrd));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPurchaseOrderClose_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> isView();

        @Editor.Path("IsStatus")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> isStatus();

        @Editor.Path("PurchaseDate")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> purchaseDate();

        @Editor.Path("PurchaseNo")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> purchaseNo();

        @Editor.Path("RefNo")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> refNo();

        @Editor.Path("Remark")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> remark();

        @Editor.Path("Approval")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> approval();

        @Editor.Path("OrderQty")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> orderQty();

        @Editor.Path("Sku")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> sku();

        @Editor.Path("ItemName")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> itemName();

        @Editor.Path("ReqDate")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> reqDate();

        @Editor.Path("Rate")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> amount();

        @Editor.Path("Supplier")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> supplier();

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdPurchaseOrderClose_Mdl, String> propertiesCsv();

    }

    final DmGridPaging<GrdPurchaseOrderClose_Mdl, Grd_PrpAcs, String> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, true);

    public void createGrid() {
        ColumnConfig<GrdPurchaseOrderClose_Mdl, String> selectCheckBox;
        ColumnConfig<GrdPurchaseOrderClose_Mdl, String> selectDisplay;
        selectCheckBox = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        selectDisplay = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isView(), "", 25, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().orderQty(), "Ordered Qty.", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().sku(), "Unit", 50, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().rate(), "Rate", 50, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemName(), "Item Name", 170, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().propertiesCsv(), "Properties", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().reqDate(), "Req. Date", 80, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().name(), "Order No.", 80, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().purchaseDate(), "Order Date", 80, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().supplier(), "Supplier", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().remark(), "Remarks", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().approval(), "Approved By", 130, "");

        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdPurchaseOrderClose_Mdl>() {

            @Override
            public String getColStyle(GrdPurchaseOrderClose_Mdl model, ValueProvider<? super GrdPurchaseOrderClose_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 1) {
                    if (DmShared.vNull(model.getIsChecked()).doubleValue() > 1) {
                        styleClass += "dm-FontBold ";
                    }
                }
                if (DmShared.xNull(model.getIsStatus()).equals("1") && !DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2") && !DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDairyConfirm";
                } else if (!DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDisable";
                } else if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdPurchaseOrderClose_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
//                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.3em; color:green;'></span>");
            }
        });
        
        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
//                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });

        dmGrd.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC, "v_Date");
        dmGrd.getHelper().addSort("recId", DmEnum.DmSortType.DESC, "recId");

    }
    //</editor-fold>
}
