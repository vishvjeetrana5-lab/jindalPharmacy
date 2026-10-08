package aarogya.inventory.purchaseBillApproval.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.purchaseBillApproval.shared.GrdPurchaseBillApproval_Mdl;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class PurchaseBillApproval_Dsg {

    final String widgetName = new pmInventory().PURCHASE_BILL_APPROVAL.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnFillGrid = new Button("Fill Grid");
    final Button btnClose = new Button();
    final Button btnSetting = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PurchaseBillApproval_Dsg() {
        fIni();
        createGrid();
    }

    private void fIni() {
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("576px");
        flwForm.add(toolContainer);
        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("600px");
        flwForm.getElement().getStyle().setBackgroundColor("white");
        createToolControl();

        btnFillGrid.addStyleName("dm-button-grad");
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
        btnSetting.setTitle("Setting");
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
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPurchaseBillApproval_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> isView();

        @Editor.Path("DocQty")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> docQty();

        @Editor.Path("AccQty")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> accQty();

        @Editor.Path("BillDate")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> billDate();

        @Editor.Path("UnitText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> unitText();

        @Editor.Path("ItemNameText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> itemName();

        @Editor.Path("SupplierText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> supplierText();

        @Editor.Path("GrnNoText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> grnNoText();

        @Editor.Path("OrderNoText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> orderNoText();

        @Editor.Path("SiteName")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> siteName();

        @Editor.Path("ApprovedBy")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> approvedBy();

        @Editor.Path("Approval")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> approval();

        @Editor.Path("VtypeText")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> vTypeText();

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> propertiesCsv();
        
        @Editor.Path("PartyBillNo")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> partyBillNo();

        @Editor.Path("PartyBillDate")
        ValueProvider<GrdPurchaseBillApproval_Mdl, String> partyBillDate();
    }

    final DmGridPaging<GrdPurchaseBillApproval_Mdl, Grd_PrpAcs, String> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, true);

    public void createGrid() {
        ColumnConfig<GrdPurchaseBillApproval_Mdl, String> selectCheckBox;
        ColumnConfig<GrdPurchaseBillApproval_Mdl, String> selectDisplay;
        selectCheckBox = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        selectDisplay = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isView(), "", 25, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().docQty(), "Doc. Qty.", 83, "docQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().accQty(), "Acc. Qty.", 83, "acceptQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().unitText(), "Unit", 50, "unitName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemName(), "Item Name", 150, "itemName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().name(), "V. No.", 80, "billNo");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().billDate(), "V. Date", 80, "billDate");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().vTypeText(), "Vr. Type", 80, "short_Name");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().grnNoText(), "GRN No.", 100, "grnNo");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().orderNoText(), "Order No.", 80, "poRecId");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().supplierText(), "Supplier", 180, "suppName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().partyBillNo(), "P.Bill No.", 80, "partyBillNo");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().partyBillDate(), "P.Bill Dt.", 80, "PartyBillDate");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().approval(), "Approved By", 130, "approvedBy");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().siteName(), "Site", 120, "siteName");
        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdPurchaseBillApproval_Mdl>() {

            @Override
            public String getColStyle(GrdPurchaseBillApproval_Mdl model, ValueProvider<? super GrdPurchaseBillApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
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
            public String getRowStyle(GrdPurchaseBillApproval_Mdl model, int rowIndex) {
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

        dmGrd.getHelper().addSort("billDate", DmEnum.DmSortType.DESC, "billDate");
        dmGrd.getHelper().addSort("billNo", DmEnum.DmSortType.DESC, "billNo");

    }
//</editor-fold>
}
