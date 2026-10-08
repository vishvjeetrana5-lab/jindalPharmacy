package aarogya.pharmacy.saleBillItemSearch.client;

import aarogya.pharmacy.saleBillItemSearch.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;

abstract class SaleBillItemSearch_Dsg {

    public final static String widgetName = "Sale Bill Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
//    final FlowPanel flwSetting = new FlowPanel();
//    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();
    int grdColIndexSelectChkBox = 0;
    ColumnConfig<GrdSaleBillItemSearch_Mdl, String> selectCheckBox;
    int headerValue = 0;
    final DmTextBox txtSearchText = new DmTextBox(50, false);

    public SaleBillItemSearch_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1080px");
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

//        label = new InlineLabel("Setting");
//        label.setWidth("40px");
//        aBtn = new Anchor();
//        aBtn.addStyleName("fa fa-gears");
//        aBtn.getElement().getStyle().setColor("orange");
//        btnSetting.getElement().appendChild(aBtn.getElement());
//        btnSetting.getElement().appendChild(label.getElement());
//        btnSetting.setWidth("70px");
//        flwSetting.add(btnSetting);
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
        btnCancel.setTitle("Ok");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);
//        toolContainer.addWidget(flwSetting);
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
        DmClient.setStyle(flwTr, "100%", "332px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGridIndentDetail()));
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
        txtSearchText.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchText));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridSaleBillDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSaleBillItemSearch_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> quantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> sku();

        @Editor.Path("ItemName")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> itemName();

        @Editor.Path("BillDate")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> billDate();

        @Editor.Path("IsChecked")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> isChecked();

        @Editor.Path("NetAmount")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> netAmount();

        @Editor.Path("BatchNo")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> batchNo();

        @Editor.Path("ExpDate")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> expDate();

        @Editor.Path("PatientName")
        ValueProvider<GrdSaleBillItemSearch_Mdl, String> patientName();

    }

    final DmGrid<GrdSaleBillItemSearch_Mdl, GrdPaging_PrpAcs> gridSaleBillDetail = new DmGrid(GWT.create(GrdPaging_PrpAcs.class));

    private Grid iniGridIndentDetail() {
        int i = 0;
        grdColIndexSelectChkBox = i;
        selectCheckBox = gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().isChecked(), "", 30, "");
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().itemName(), "Medicine Name", 1, "medicine");
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().quantity(), "Qty.", 60, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().sku(), "SKU", 50, "skuName");
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().netAmount(), "Net Amount", 80, "oh_Amt_Net").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().batchNo(), "Batch No", 75, "batchNo").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().expDate(), "Expiry Date", 75, "expiryDate", DmDataType.DATE);
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().name(), "Bill No.", 100, "billNo");
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().billDate(), "Bill Date", 75, "billDate", DmDataType.DATE);
        ++i;
        gridSaleBillDetail.addTextColumn(gridSaleBillDetail.getPropertyAccess().patientName(), "Patient Name", 150, "patientName");

        gridSaleBillDetail.initializeGrid(gridSaleBillDetail.getPropertyAccess().fieldHashCode(), 1080, 310, 2);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                headerValue = 0;
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                return headerStyle;
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridSaleBillDetail.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return gridSaleBillDetail.getGrid();
    }
    //</editor-fold>

}
