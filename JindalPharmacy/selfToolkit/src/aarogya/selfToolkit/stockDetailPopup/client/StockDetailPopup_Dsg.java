package aarogya.selfToolkit.stockDetailPopup.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import aarogya.selfToolkit.stockDetailPopup.shared.StockDetailGrdEdit_Mdl;

abstract class StockDetailPopup_Dsg {

    public final static String widgetName = "Stock Details";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtItem = new DmTextBox(100, true);
    final DmTextBox txtProperties = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="grdStockDetail">
    interface StockDetailGrdEdit_PrpAcs extends KeyFieldHashCode_T_PrpAcs<StockDetailGrdEdit_Mdl> {

        @Editor.Path("Site")
        ValueProvider<StockDetailGrdEdit_Mdl, String> site();

        @Editor.Path("Godown")
        ValueProvider<StockDetailGrdEdit_Mdl, String> godown();

        @Editor.Path("Sku")
        ValueProvider<StockDetailGrdEdit_Mdl, String> sku();

        @Editor.Path("MinLevel")
        ValueProvider<StockDetailGrdEdit_Mdl, String> minLevel();

        @Editor.Path("MaxLevel")
        ValueProvider<StockDetailGrdEdit_Mdl, String> maxLevel();

        @Editor.Path("ReOrderLevel")
        ValueProvider<StockDetailGrdEdit_Mdl, String> reOrderLevel();

        @Editor.Path("CurrentStock")
        ValueProvider<StockDetailGrdEdit_Mdl, String> currentStock();

    }
    final DmGrid<StockDetailGrdEdit_Mdl, StockDetailGrdEdit_PrpAcs> grdStockDetail = new DmGrid(com.google.gwt.core.client.GWT.create(StockDetailGrdEdit_PrpAcs.class));

    public Grid iniStockDetail() {
        grdStockDetail.addRowNumberColumn();
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().site(), "Site", 120, "siteName");
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().godown(), "Godown", 120, "godown");
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().sku(), "SKU", 70, "unit");
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().minLevel(), "Min.", 80, "minStock").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().maxLevel(), "Max.", 80, "maxStock").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().reOrderLevel(), "Re-Order", 100, "reOrder").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdStockDetail.addTextColumn(grdStockDetail.getPropertyAccess().currentStock(), "Current Stock", 100, "currentStock").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdStockDetail.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 3, new HeaderGroupConfig("Stock Level", 1, 4));

            }
        });
        grdStockDetail.initializeGrid(grdStockDetail.getPropertyAccess().fieldHashCode(), 700, 170);
        return grdStockDetail.getGrid();
    }
    //</editor-fold>

    public StockDetailPopup_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("250px");
        popupPanel.setHeight("220px");

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
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "218px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setPadding(6, Style.Unit.PX);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Properties");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginBottom(6, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtItem));
        txtItem.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "56%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProperties));
        txtProperties.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "180px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(iniStockDetail()));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }
}
