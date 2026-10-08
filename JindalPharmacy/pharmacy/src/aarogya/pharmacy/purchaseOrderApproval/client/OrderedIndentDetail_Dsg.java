package aarogya.pharmacy.purchaseOrderApproval.client;

import aarogya.pharmacy.purchaseOrderApproval.shared.GrdPurchaseOrderDetail_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;

abstract class OrderedIndentDetail_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmClient.ViaList vl = new DmClient.ViaList();

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtItem = new DmTextBox();
    final DmTextBox txtReceivedQty = new DmTextBox();
     final DmUserControl userControl = new DmUserControl();
     final DmTopControl topControl = new DmTopControl(vl, userControl);

    //<editor-fold defaultstate="collapsed" desc="grdPurchaseDetGridEdt">
    interface GrdPurchaseOrdDet_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPurchaseOrderDetail_Mdl> {

        @Editor.Path("IndentName")
        ValueProvider<GrdPurchaseOrderDetail_Mdl, String> indentName();
        
        @Editor.Path("ApprovedQty")
        ValueProvider<GrdPurchaseOrderDetail_Mdl, String> approvedQty();

        @Editor.Path("OrderInPurchaseOrder")
        ValueProvider<GrdPurchaseOrderDetail_Mdl, String> orderInPurchaseOrder();

    }
    final DmGrid<GrdPurchaseOrderDetail_Mdl, GrdPurchaseOrdDet_PrpAcs> grdPurchaseDet = new DmGrid(GWT.create(GrdPurchaseOrdDet_PrpAcs.class));

    private Grid iniGrdPurchaseDetGridEdt() {
        grdPurchaseDet.addRowNumberColumn();

        grdPurchaseDet.addTextColumn(grdPurchaseDet.getPropertyAccess().indentName(), "Indent", 114, "");
        grdPurchaseDet.addTextColumn(grdPurchaseDet.getPropertyAccess().approvedQty(), "Approved Qty.", 114, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPurchaseDet.addTextColumn(grdPurchaseDet.getPropertyAccess().orderInPurchaseOrder(), "Ordered Qty (PO)", 114, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPurchaseDet.initializeGrid(grdPurchaseDet.getPropertyAccess().fieldHashCode(), 356, 288, 3);//288==>150

        grdPurchaseDet.getGrid().getView().setViewConfig(new GridViewConfig<GrdPurchaseOrderDetail_Mdl>() {
            @Override
            public String getColStyle(GrdPurchaseOrderDetail_Mdl model, ValueProvider<? super GrdPurchaseOrderDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
//                if (!model.isDiscountAllow() && (DmShared.iNull(model.getSharedKeyFieldCode()).longValue() > 0)) {
//                    return "NDSColor";
//                }
                return "";
            }

            @Override
            public String getRowStyle(GrdPurchaseOrderDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdPurchaseDet.getGrid();
    }
    //</editor-fold>

    public OrderedIndentDetail_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
   
    }


    

  

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "307px", null, null, DmPosition.CENTER, true);

        //Item & ReceivedQty////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "38%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtItem));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(21, Style.Unit.PX);
        lbl = new DmLabel("Received Qty");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "left", null, DmPosition.NONE, false);
        txtReceivedQty.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTD.add(vl.add(txtReceivedQty));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        ///////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "287px", "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(iniGrdPurchaseDetGridEdt()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTable.add(flwTR);
        return flwTable;
    }

}
