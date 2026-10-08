package aarogya.inventory.itemTransferApproval.client;

import aarogya.inventory.itemTransferApproval.shared.GrdItemDetail_Mdl;
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
import dataman.dmBase.self.shared.*;

abstract class ItemTransferApprovalGridEdit_Dsg {

    public final static String approvedByBlog = "Approved By";
    public final static String approvedDateTimeBlog = "Approved Date & Time";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtTrfNo = new DmTextBox();
    final DmTextBox txtTrfDate = new DmTextBox();
    final DmTextBox txtSite = new DmTextBox();
    final DmTextBox txtDepartment = new DmTextBox();
    final DmTextBox txtTrfCat = new DmTextBox();
    final DmLabel isCancelled = new DmLabel("");
    final Button btnApprovedBy = new Button();

    public ItemTransferApprovalGridEdit_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("690px");
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

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ind. No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTrfNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ind. Date");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTrfDate));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ind. Type");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "29%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTrfCat));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "46%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDepartment));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "29%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSite));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(isCancelled);
        isCancelled.getElement().getStyle().setColor("red");
        isCancelled.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        isCancelled.getElement().getStyle().setMarginLeft(80, Style.Unit.PCT);
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

    //<editor-fold defaultstate="collapsed" desc="grdMedicinelDetails">
    interface GrdMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("ReqDate")
        ValueProvider<GrdItemDetail_Mdl, String> reqDate();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

        @Editor.Path("IndQty")
        ValueProvider<GrdItemDetail_Mdl, String> indDisplayQty();

        @Editor.Path("ApprovedQty")
        ValueProvider<GrdItemDetail_Mdl, String> approvedDisplayQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemDetail_Mdl, String> unitName();

        @Editor.Path("Image")
        ValueProvider<GrdItemDetail_Mdl, String> image();

        @Editor.Path("DisplayProperties")
        ValueProvider<GrdItemDetail_Mdl, String> displayProperties();

    }

    final DmGrid<GrdItemDetail_Mdl, GrdMedicineDetail_PrpAcs> grdItemDetails = new DmGrid(GWT.create(GrdMedicineDetail_PrpAcs.class));

    private Grid iniGrdItemDetails() {
        ColumnConfig<GrdItemDetail_Mdl, String> cancelRecord;
        grdItemDetails.addRowNumberColumn();
        cancelRecord = grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().image(), "C/O", 30, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().name(), "Item Name", 1, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().displayProperties(), "Properties", 50, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().approvedDisplayQty(), "Appr. Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().indDisplayQty(), "Ind. Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().unitName(), "Unit", 80, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().reqDate(), "Req. Date", 80, "");
        cancelRecord.setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_CENTER);
        grdItemDetails.initializeGrid(grdItemDetails.getPropertyAccess().fieldHashCode(), 690, 301, 3);

        grdItemDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {

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
                double val = DmShared.vNull(grdItemDetails.getGrid().getStore().get(context.getIndex()).getIsChecked()).doubleValue();
                if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em;color:black'></span>");
                } else if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em; color:black;'></span>");
                }

            }
        });
        return grdItemDetails.getGrid();
    }
//</editor-fold>

}
