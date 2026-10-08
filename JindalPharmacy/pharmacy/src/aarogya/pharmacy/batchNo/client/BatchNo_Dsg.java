package aarogya.pharmacy.batchNo.client;

import aarogya.pharmacy.batchNo.shared.BatchNoSearchValue_Mdl;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class BatchNo_Dsg {

    public final static String widgetName = "Batch No. Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmCheckBox chkAllowNegStck = new DmCheckBox();
    final DmLabel lblAllwNegStk = new DmLabel("Show Negative Stock");
    final DmLabel lblShowExpiry = new DmLabel("Show Expiry");
    final FlowPanel flwExpiryParentLbl = new FlowPanel();
    final FlowPanel flwExpiryLbl = new FlowPanel();
    final FlowPanel flwExpiryParentCb = new FlowPanel();
    final FlowPanel flwExpiryCb = new FlowPanel();
    final DmLabel lblItemName = new DmLabel("");

    final DmCheckBox cbShowExpiry = new DmCheckBox();

    public BatchNo_Dsg() {
        ini();
        iniGridBatchNoDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("720px");
        popupPanel.setHeight("330px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");

        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnCancel);
        toolContainer.addWidget(lblItemName);
        lblItemName.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lblItemName.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblItemName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblItemName.getElement().getStyle().setHeight(15, Style.Unit.PX);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "312px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridBatchNoDetail));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Batch No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(14, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(chkAllowNegStck));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(lblAllwNegStk);
        lblAllwNegStk.setTitle("Negative Stock:(Alt + N)");
        flwTr.add(flwTd);

        DmClient.setStyle(flwExpiryCb, "5%", null, "left", null, DmPosition.NONE, false);
        flwExpiryCb.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwExpiryCb.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwExpiryCb.add(vl.add(cbShowExpiry));
        flwExpiryParentCb.add(flwExpiryCb);
        flwTr.add(flwExpiryParentCb);

        DmClient.setStyle(flwExpiryLbl, "14%", null, "left", null, DmPosition.NONE, false);
        flwExpiryLbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwExpiryLbl.add(lblShowExpiry);
        lblShowExpiry.setTitle("Show Expiry:(Alt + E)");
        flwExpiryParentLbl.add(flwExpiryLbl);
        flwTr.add(flwExpiryParentLbl);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridBatchNoDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<BatchNo_Mdl> {

        @Editor.Path("BatchNo")
        ValueProvider<BatchNo_Mdl, String> batchNo();

        @Editor.Path("BalanceQty")
        ValueProvider<BatchNo_Mdl, String> balanceQty();

        @Editor.Path("ExpiryDate")
        ValueProvider<BatchNo_Mdl, String> expiryDate();

        @Editor.Path("BinName")
        ValueProvider<BatchNo_Mdl, String> binName();

        @Editor.Path("Mrp")
        ValueProvider<BatchNo_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<BatchNo_Mdl, String> saleRate();

        @Editor.Path("SkuName")
        ValueProvider<BatchNo_Mdl, String> skuName();

        @Editor.Path("BarCode")
        ValueProvider<BatchNo_Mdl, String> barCode();

        @Editor.Path("ReceiveRecId")
        ValueProvider<BatchNo_Mdl, String> receiveRecId();
    }

    final DmGridPaging<BatchNo_Mdl, GrdPaging_PrpAcs, BatchNoSearchValue_Mdl> gridBatchNoDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridBatchNoDetail() {

        gridBatchNoDetail.getDmGrid().addRowNumberColumn();
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().batchNo(), "Batch No.", 80, "batchNo");
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().expiryDate(), "Exp. Date", 90, "expiryDate", DmDataType.DATE);
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().mrp(), "MRP", 80, "mrp").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "saleRate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().balanceQty(), "Current Stock", 100, "balanceQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().binName(), "Bin", 90, "binName");
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().barCode(), "barcode", 120, "barcode");
        gridBatchNoDetail.getDmGrid().addTextColumn(gridBatchNoDetail.getDmGrid().getPropertyAccess().receiveRecId(), "Receive Vr. No.", 120, "recId");
        gridBatchNoDetail.getDmGrid().initializeGrid(gridBatchNoDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 840, 430, 2);
        gridBatchNoDetail.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<BatchNo_Mdl>() {
            @Override
            public String getColStyle(BatchNo_Mdl model, ValueProvider<? super BatchNo_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isExpiredIbCode()) {
                    styleClass += "dm-colorRed ";
                } else if (DmShared.vNull(model.getMinStock()).doubleValue() != 0) {
                    if (DmShared.vNull(model.getBalanceQty()).doubleValue() <= DmShared.vNull(model.getMinStock()).doubleValue()) {
                        styleClass += "dm-GridCellDairyCancel ";
                    } else if (DmShared.vNull(model.getBalanceQty()).doubleValue() <= DmShared.vNull(model.getReOrderLevel()).doubleValue()) {
                        styleClass += "dm-GridCellDairyConfirm ";
                    }
                } else {
                    if (DmShared.vNull(model.getBalanceQty()).doubleValue() <= 0) {
                        styleClass += "dm-fontWeightBold ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(BatchNo_Mdl model, int rowIndex) {
                return "";
            }
        });

        gridBatchNoDetail.getHelper().addSort("expiryDate", DmSortType.ASC);
        gridBatchNoDetail.getHelper().addSort("balanceQty", DmSortType.DESC);
    }
    //</editor-fold>

}
