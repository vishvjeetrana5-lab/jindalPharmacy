package aarogya.inventory.purchaseOrderApproval.client;

import aarogya.inventory.purchaseOrderApproval.shared.GrdItemDetail_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class PurchaseOrderApprovalGridEdit_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtApproved = new DmTextBox();
    final DmTextBox txtPurchaseNo = new DmTextBox();
    final DmTextBox txtPurchaseDate = new DmTextBox();
    final DmTextBox txtStatus = new DmTextBox();
    final DmTextBox txtItemGroup = new DmTextBox();
    final DmTextBox txtItemName = new DmTextBox();
    final DmTextBox txtReqDate = new DmTextBox();
    final DmTextBox txtOrdQty = new DmTextBox();
    final DmTextBox txtQuotationNo = new DmTextBox();
    final DmTextBox txtRate = new DmTextBox();
    final DmTextBox txtSKU = new DmTextBox();
    final DmTextBox txtCurrency = new DmTextBox();
    final DmTextBox txtWTACode = new DmTextBox();
    final DmTextBox txtSupplier = new DmTextBox();
    final DmTextBox txtCurrentUser = new DmTextBox();
    final DmTextBox txtRemarks = new DmTextBox();
    final DmTextBox txtApprovedBy = new DmTextBox();
    final DmTextBox txtApprovedDateTime = new DmTextBox();
    final DmLabel isCancelled = new DmLabel("");
    final Button btnApprovedBy = new Button();

    public final static String approvedByBlog = "Approved By";
    public final static String approvedDateTimeBlog = "Approved Date & Time";

    Style style;

    //<editor-fold defaultstate="collapsed" desc="grdItemlDetails">
    interface GrdItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("IsView")
        ValueProvider<GrdItemDetail_Mdl, String> isView();

        @Editor.Path("ItemName")
        ValueProvider<GrdItemDetail_Mdl, String> itemName();

        @Editor.Path("OrderDate")
        ValueProvider<GrdItemDetail_Mdl, String> reqDate();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

        @Editor.Path("OrderQty")
        ValueProvider<GrdItemDetail_Mdl, String> orderQty();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("Unit")
        ValueProvider<GrdItemDetail_Mdl, String> unit();

        @Editor.Path("Image")
        ValueProvider<GrdItemDetail_Mdl, String> image();
        @Editor.Path("ClosedPurchaseOrderApproval")
        ValueProvider<GrdItemDetail_Mdl, String> closedPurchaseOrderApproval();

    }

    final DmGrid<GrdItemDetail_Mdl, GrdItemDetail_PrpAcs> grdItemlDetails = new DmGrid(GWT.create(GrdItemDetail_PrpAcs.class));

    private Grid iniGrdItemDetails() {
        ColumnConfig<GrdItemDetail_Mdl, String> cancelRecord;
        ColumnConfig<GrdItemDetail_Mdl, String> selectDisplay;
        grdItemlDetails.addRowNumberColumn();
        cancelRecord = grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().image(), "C/O", 30, "");
        selectDisplay = grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().isView(), "", 25, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().name(), "Item Name", 1, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().orderQty(), "Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().unit(), "Unit", 50, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().rate(), "Rate", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().amount(), "Amount", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().reqDate(), "Req. Date", 80, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().remark(), "Remark", 120, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().closedPurchaseOrderApproval(), "Status Remark", 120, "");

        cancelRecord.setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        grdItemlDetails.initializeGrid(grdItemlDetails.getPropertyAccess().fieldHashCode(), 790, 301, 4);

        grdItemlDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {

            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        cancelRecord.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(context.getIndex()).getIsChecked()).doubleValue();
                if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em;color:black'></span>");
                } else if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em; color:black;'></span>");
                }

            }
        });
        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });
        return grdItemlDetails.getGrid();
    }
    //</editor-fold>

    public PurchaseOrderApprovalGridEdit_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("790px");
        popupPanel.setHeight("405px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(btnClose);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "375px", null, null, DmPosition.CENTER, true);

        //Rate////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPurchaseNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order Date");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPurchaseDate));
        flwTR.add(flwTD);

        ///////////////////////////////////////////////
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26.5%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtQuotationNo));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "46%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSupplier));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Currency");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26.5%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCurrency));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(isCancelled);
        isCancelled.getElement().getStyle().setColor("red");
        isCancelled.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        isCancelled.getElement().getStyle().setMarginLeft(66, Style.Unit.PCT);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "287px", "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(iniGrdItemDetails()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTR.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        userControl.addTextBlog(approvedByBlog, 150, false, true);
        userControl.addGap(53);
        userControl.addTextBlog(approvedDateTimeBlog, 150, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.addStyleName("dm-button-grad");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        flwTD.add(vl.add(userControl));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    }

}
