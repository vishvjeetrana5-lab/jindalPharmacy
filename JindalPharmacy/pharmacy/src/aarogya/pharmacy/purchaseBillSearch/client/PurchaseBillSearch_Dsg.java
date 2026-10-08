package aarogya.pharmacy.purchaseBillSearch.client;

import aarogya.pharmacy.purchaseBillSearch.shared.GrdBillDetails_Mdl;
import aarogya.pharmacy.purchaseBillSearch.shared.SearchValue_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;

abstract class PurchaseBillSearch_Dsg {

    public final static String widgetName = "Purchase Bill Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwReset = new FlowPanel();
//    final FlowPanel flwSetting = new FlowPanel();
    final Button btnSetting = new Button();
    final Button btnCancel = new Button();
    final Button btnReset = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PurchaseBillSearch_Dsg() {
        ini();
        iniGridItemDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1050px");
        popupPanel.setHeight("360px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    DmLabel lblPreviousMedicineCompany = new DmLabel("");

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("OK");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOK.setTitle("Ok");
        btnOK.addStyleName("dm-top-control-btnStyle");
        btnOK.getElement().appendChild(aBtn.getElement());
        btnOK.getElement().appendChild(label.getElement());
        btnOK.setWidth("70px");
        flwOK.add(btnOK);

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");
        //   flwSetting.add(btnSetting);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh ");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        label = new InlineLabel("Reset");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-repeat");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnReset.setTitle("Reset");
        btnReset.addStyleName("dm-top-control-btnStyle");
        btnReset.getElement().appendChild(aBtn.getElement());
        btnReset.getElement().appendChild(label.getElement());
        btnReset.setWidth("70px");
        flwReset.add(btnReset);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);
        //  toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);

        toolContainer.addWidget(flwReset);
        toolContainer.addWidget(lblPreviousMedicineCompany);
        lblPreviousMedicineCompany.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "312px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridPurchaseBillDetail));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Medicine");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridPurchaseBillDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillDetails_Mdl> {

        @Editor.Path("ItemVSno")
        ValueProvider<GrdBillDetails_Mdl, String> itemVSno();

        @Editor.Path("IsChecked")
        ValueProvider<GrdBillDetails_Mdl, String> isChecked();

        @Editor.Path("BillDate")
        ValueProvider<GrdBillDetails_Mdl, String> billDate();

        @Editor.Path("RefNo")
        ValueProvider<GrdBillDetails_Mdl, String> refNo();

        @Editor.Path("ItemName")
        ValueProvider<GrdBillDetails_Mdl, String> itemName();

        @Editor.Path("ItemMake")
        ValueProvider<GrdBillDetails_Mdl, String> itemMake();

        @Editor.Path("UnitName")
        ValueProvider<GrdBillDetails_Mdl, String> unitName();

        @Editor.Path("PendingQty")
        ValueProvider<GrdBillDetails_Mdl, String> pendingQty();

        @Editor.Path("PendingFreeQty")
        ValueProvider<GrdBillDetails_Mdl, String> pendingFreeQty();

        @Editor.Path("Rate")
        ValueProvider<GrdBillDetails_Mdl, String> rate();

        @Editor.Path("Mrp")
        ValueProvider<GrdBillDetails_Mdl, String> mrp();

        @Editor.Path("SkuMrp")
        ValueProvider<GrdBillDetails_Mdl, String> skuMrp();

        @Editor.Path("BatchNo")
        ValueProvider<GrdBillDetails_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdBillDetails_Mdl, String> expiryDate();

        @Editor.Path("Remark")
        ValueProvider<GrdBillDetails_Mdl, String> remark();

    }

    final DmGridPaging<GrdBillDetails_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridPurchaseBillDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridItemDetail() {
        ColumnConfig<GrdBillDetails_Mdl, String> selectCheckBox;

        selectCheckBox = gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().billDate(), "Bill Date", 75, "replace(convert(varchar, billDate, 106), ' ','-')", DmDataType.DATE);
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().name(), "Bill No.", 90, "billRecId");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 80, "refNo");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().itemMake(), "Medicine Make", 150, "itemMake");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().itemName(), "Medicine Name", 340, "itemName");
//        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().itemVSno(), "VSno", 50, "itemVSno");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().unitName(), "Unit", 40, "");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().pendingQty(), "Pend. Qty.", 90, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().pendingFreeQty(), "Pen.Free.Qty.", 110, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().rate(), "Rate", 50, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().mrp(), "MRP", 50, "mrp").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().skuMrp(), "SKU MRP", 70, "skuMrp").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().batchNo(), "Batch No", 70, "batchNo");
        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 80, "expiryDate");

//        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 80, "refNo");
//        gridPurchaseBillDetail.getDmGrid().addTextColumn(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().remark(), "Remark", 100, "remark");
        gridPurchaseBillDetail.getDmGrid().initializeGrid(gridPurchaseBillDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 1230, 310, 6);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridPurchaseBillDetail.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        gridPurchaseBillDetail.getHelper().addSort("billDate", DmEnum.DmSortType.ASC);
        gridPurchaseBillDetail.getHelper().addSort("billDocId", DmEnum.DmSortType.ASC);
        gridPurchaseBillDetail.getHelper().addSort("itemMake", DmEnum.DmSortType.ASC);
        gridPurchaseBillDetail.getHelper().addSort("itemName", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

}
