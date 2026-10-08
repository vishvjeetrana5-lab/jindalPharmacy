package aarogya.inventory.purchaseOrderDisplay.client;

import aarogya.inventory.purchaseOrderDisplay.shared.ApprovedVendors_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHSupplier_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class ItemDetail_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    public final static String widgetName = "Medicine History/Approved Vendors";
    final DmClient.ViaList vl = new DmClient.ViaList();

    public ItemDetail_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniSupplierPT();
        iniGrdPrevGrn();
        container.setStyleName("dm-FlwWorkingArea");
    }

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplier_PrpAcs> txtHSupplier = new DmTextHelp(com.google.gwt.core.client.GWT.create(TxtHSupplier_PrpAcs.class), 7, true);

    private void iniSupplierPT() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City Name", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "500px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "float: left; margin-top: 6px;");
        lbl = new DmLabel("Approved Vendors :");
        lbl.setStyleName("dm-HLabel");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(iniGridApprovedVendors()));
        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "float: left; margin-top: 6px;");
        lbl = new DmLabel("Purchase History :");
        lbl.setStyleName("dm-HLabel");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "250px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(grdPrevGrnPurchase);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdPrevGrnPurchase">
    interface GrdPrevGrnPurchase_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGrnPurchaseHistory_Mdl> {

        @Editor.Path("RefDate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> refDate();

        @Editor.Path("RefNo")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> refNo();

        @Editor.Path("SupplierName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> supplierName();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> purchaseUnitName();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> acceptQty();

        @Editor.Path("FreeQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> freeQty();

        @Editor.Path("Rate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> rate();

        @Editor.Path("DiscountPer")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> discountPer();

        @Editor.Path("SaleRate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> saleRate();

        @Editor.Path("Mrp")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> mrp();

        @Editor.Path("ProfitPer")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> profitPer();

        @Editor.Path("BatchNo")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> expiryDate();

        @Editor.Path("SkuName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuQty();

        @Editor.Path("SkuRate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuRate();

        @Editor.Path("LandedSkuValue")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> landedSkuValue();

    }

    final DmGridPaging<GrdGrnPurchaseHistory_Mdl, GrdPrevGrnPurchase_PrpAcs, String> grdPrevGrnPurchase = new DmGridPaging(GWT.create(GrdPrevGrnPurchase_PrpAcs.class), 8, true);

    private void iniGrdPrevGrn() {
        grdPrevGrnPurchase.getDmGrid().addRowNumberColumn();
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refDate(), "Ref. Date", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 110, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 185, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 50, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().acceptQty(), "Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().freeQty(), "Free Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().rate(), "Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().discountPer(), "Dis. %", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().profitPer(), "%", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().batchNo(), "Batch No.", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().expiryDate(), "Exp. Dt.", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuName(), "SKU", 40, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuQty(), "SKU Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().landedSkuValue(), "SKU Land. Val.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().initializeGrid(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().fieldHashCode(), 750, 0);
        grdPrevGrnPurchase.getHelper().addSort("refDate", DmEnum.DmSortType.DESC);
        grdPrevGrnPurchase.getHelper().addSort("v_SNo", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridApprovedVendors">
    interface GridApprovedVendors_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ApprovedVendors_Mdl> {

        @Editor.Path("V_SNo")
        ValueProvider<ApprovedVendors_Mdl, String> v_SNo();

        @Editor.Path("Rate")
        ValueProvider<ApprovedVendors_Mdl, String> rate();
    }

    final DmGrid<ApprovedVendors_Mdl, GridApprovedVendors_PrpAcs> gridApprovedVendors = new DmGrid(com.google.gwt.core.client.GWT.create(GridApprovedVendors_PrpAcs.class));

    private Grid iniGridApprovedVendors() {
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().v_SNo(), "S.No.", 30, "sNo");
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().name(), "Approved Suppliers", 360, "approvedVendorName");
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().rate(), "lst.Rate", 80, "lastRate").setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_RIGHT);
        gridApprovedVendors.initializeGrid(gridApprovedVendors.getPropertyAccess().fieldHashCode(), 550, 170, 1);
        return gridApprovedVendors.getGrid();
    }
}
