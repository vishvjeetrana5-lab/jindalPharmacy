package aarogya.pharmacy.grnSearch.client;

import aarogya.pharmacy.grnSearch.shared.GrdGrnDetails_Mdl;
import aarogya.pharmacy.grnSearch.shared.SearchValue_Mdl;
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

abstract class GRNSearch_Dsg {

    public final static String widgetName = "GRN Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    int colItemName = -1, colUnit = -1, colGrnNo = -1, colGrnDate = -1, colGrnQty = -1, colPendingQty = -1,
            colRemark = -1, colPORecId = -1, colPODate = -1, colIndex = 0;

    public GRNSearch_Dsg() {
        ini();
        iniGridItemDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("900px");
        popupPanel.setHeight("265px");

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
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "265px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "245px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridGrnDetail));
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

    //<editor-fold defaultstate="collapsed" desc="gridGrnDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGrnDetails_Mdl> {

        @Editor.Path("PcV_SNo")
        ValueProvider<GrdGrnDetails_Mdl, String> pcV_SNo();

        @Editor.Path("PcV_Date")
        ValueProvider<GrdGrnDetails_Mdl, String> pcV_Date();

        @Editor.Path("PcQty")
        ValueProvider<GrdGrnDetails_Mdl, String> pcQty();

        @Editor.Path("FreeQty")
        ValueProvider<GrdGrnDetails_Mdl, String> freeQty();

        @Editor.Path("PendingFreeQty")
        ValueProvider<GrdGrnDetails_Mdl, String> pendingFreeQty();

        @Editor.Path("PendingQty")
        ValueProvider<GrdGrnDetails_Mdl, String> pendingQty();

        @Editor.Path("MedicineName")
        ValueProvider<GrdGrnDetails_Mdl, String> medicineName();

        @Editor.Path("UnitName")
        ValueProvider<GrdGrnDetails_Mdl, String> unitName();

        @Editor.Path("Remark")
        ValueProvider<GrdGrnDetails_Mdl, String> remark();

        @Editor.Path("IsChecked")
        ValueProvider<GrdGrnDetails_Mdl, String> isChecked();

        @Editor.Path("PoDocID")
        ValueProvider<GrdGrnDetails_Mdl, String> poDocID();

        @Editor.Path("PoNo")
        ValueProvider<GrdGrnDetails_Mdl, String> poNo();

        @Editor.Path("PoV_Date")
        ValueProvider<GrdGrnDetails_Mdl, String> poVDate();

    }

    final DmGridPaging<GrdGrnDetails_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridGrnDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 10, false);

    private void iniGridItemDetail() {
        ColumnConfig<GrdGrnDetails_Mdl, String> selectCheckBox;

        selectCheckBox = gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        colItemName = ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().medicineName(), "Medicine Name", 140, "itemName");
        colUnit= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().unitName(), "Unit", 50, "unitName");
        colGrnQty= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().pendingQty(), "Accept Qty.", 80, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colPendingQty= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().pendingFreeQty(), "Free Qty.", 80, "pendingFreeQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colGrnNo= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().name(), "GRN No.", 110, "pcRecId");
        colGrnDate= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().pcV_Date(), "GRN Date", 75, "pcV_Date", DmDataType.DATE);
        colRemark= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().remark(), "Remark", 150, "remark");
        colPORecId= ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().poNo(), "PO No.", 100, "poNo");
        colPODate=  ++colIndex;
        gridGrnDetail.getDmGrid().addTextColumn(gridGrnDetail.getDmGrid().getPropertyAccess().poVDate(), "PO Date", 100, "poVDate", DmDataType.DATE);
        gridGrnDetail.getDmGrid().initializeGrid(gridGrnDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 880, 310, 2);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridGrnDetail.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        gridGrnDetail.getHelper().addSort("pcV_Date", DmEnum.DmSortType.DESC);
        gridGrnDetail.getHelper().addSort("pcRecId", DmEnum.DmSortType.ASC);
        gridGrnDetail.getHelper().addSort("itemName", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

}
