package aarogya.inventory.purchaseOrderApproval.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.purchaseOrderApproval.shared.GrdPurchaseOrderApproval_Mdl;
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

class PurchaseOrderApproval_Dsg {

    final String widgetName = new pmInventory().PURCHASE_ORDER_APPROVAL.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();
    final Button btnSetting = new Button();
    public static final String approvedBy = "Approved By";
    public static final String unApprovedBy = "UnApproved By";
    public final static String unApprovedByBlog = "Un-Approved";

    public PurchaseOrderApproval_Dsg() {
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
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPurchaseOrderApproval_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> isView();

        @Editor.Path("IsStatus")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> isStatus();

        @Editor.Path("PurchaseDate")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> purchaseDate();

        @Editor.Path("PurchaseNo")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> purchaseNo();

        @Editor.Path("RefNo")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> refDate();

        @Editor.Path("Remark")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> remark();

        @Editor.Path("Approval")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> approval();

        @Editor.Path("ApprovedBy")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> approvedBy();

        @Editor.Path("OrderQty")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> orderQty();

        @Editor.Path("Sku")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> sku();

        @Editor.Path("ItemName")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> itemName();

        @Editor.Path("ReqDate")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> reqDate();

        @Editor.Path("Rate")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> amount();

        @Editor.Path("Supplier")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> supplier();

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> propertiesCsv();

        @Editor.Path("PurchaseOrderValue")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> purchaseOrderValue();

        @Editor.Path("PreparedBy")
        ValueProvider<GrdPurchaseOrderApproval_Mdl, String> preparedBy();

    }

    final DmGridPaging<GrdPurchaseOrderApproval_Mdl, Grd_PrpAcs, String> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, true);

    public void createGrid() {
        ColumnConfig<GrdPurchaseOrderApproval_Mdl, String> selectCheckBox;
        ColumnConfig<GrdPurchaseOrderApproval_Mdl, String> selectDisplay;
        selectCheckBox = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        selectDisplay = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isView(), "", 25, "");

        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().sku(), "Unit", 0, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().rate(), "Rate", 0, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemName(), "Item Name", 0, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().propertiesCsv(), "Properties", 0, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().reqDate(), "Req. Date", 0, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().name(), "Order No.", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().purchaseDate(), "Order Date", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().supplier(), "Supplier", 200, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().remark(), "Remarks", 0, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().purchaseOrderValue(), "Order Value", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().refDate(), "Ref. Date", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 100, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().approval(), "Approved By", 150, "");

        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdPurchaseOrderApproval_Mdl>() {

            @Override
            public String getColStyle(GrdPurchaseOrderApproval_Mdl model, ValueProvider<? super GrdPurchaseOrderApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
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
            public String getRowStyle(GrdPurchaseOrderApproval_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });

        dmGrd.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC, "v_Date");
        dmGrd.getHelper().addSort("recId", DmEnum.DmSortType.DESC, "recId");

    }
    //</editor-fold>
}
