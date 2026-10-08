package aarogya.pharmacy.orderSearch.client;

import aarogya.pharmacy.orderSearch.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
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

abstract class OrderSearch_Dsg {

    public final static String widgetName = "Order Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwSetting = new FlowPanel();
    final Button btnSetting = new Button();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public OrderSearch_Dsg() {
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
        flwSetting.add(btnSetting);
        
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

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);
        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
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
        flwTr.add(vl.add(gridOrderDetail));
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

    //<editor-fold defaultstate="collapsed" desc="gridOrderDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdOrdertDetails_Mdl> {

        @Editor.Path("ItemName")
        ValueProvider<GrdOrdertDetails_Mdl, String> itemName();

        @Editor.Path("UnitName")
        ValueProvider<GrdOrdertDetails_Mdl, String> unitName();

        @Editor.Path("OrderQty")
        ValueProvider<GrdOrdertDetails_Mdl, String> quantity();

        @Editor.Path("PendingQty")
        ValueProvider<GrdOrdertDetails_Mdl, String> pendingQty();

        @Editor.Path("RequiredDate")
        ValueProvider<GrdOrdertDetails_Mdl, String> requiredDate();

        @Editor.Path("Remark")
        ValueProvider<GrdOrdertDetails_Mdl, String> remark();

        @Editor.Path("RefNo")
        ValueProvider<GrdOrdertDetails_Mdl, String> refNo();

        @Editor.Path("OrderDate")
        ValueProvider<GrdOrdertDetails_Mdl, String> orderDate();

        @Editor.Path("IsChecked")
        ValueProvider<GrdOrdertDetails_Mdl, String> isChecked();

    }

    final DmGridPaging<GrdOrdertDetails_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridOrderDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridItemDetail() {
        ColumnConfig<GrdOrdertDetails_Mdl, String> selectCheckBox;

        selectCheckBox = gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().itemName(), "Medicine Name", 340, "itemName");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().unitName(), "Unit", 50, "");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().quantity(), "Order Qty.", 80, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().pendingQty(), "Pend. Qty.", 80, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().requiredDate(), "Req. Date", 75, "requiredDate", DmDataType.DATE);
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().remark(), "Remark", 100, "remark");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 80, "refNo");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().name(), "Order No.", 80, "orderNo");
        gridOrderDetail.getDmGrid().addTextColumn(gridOrderDetail.getDmGrid().getPropertyAccess().orderDate(), "Order Date", 75, "orderDate", DmDataType.DATE);
        gridOrderDetail.getDmGrid().initializeGrid(gridOrderDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 1030, 310, 4);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridOrderDetail.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        gridOrderDetail.getHelper().addSort("orderDate", DmEnum.DmSortType.ASC);
        gridOrderDetail.getHelper().addSort("orderNo", DmEnum.DmSortType.ASC);
        gridOrderDetail.getHelper().addSort("itemName", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

}
