package aarogya.inventory.indentSearch.client;

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
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import aarogya.inventory.indentSearch.shared.GrdIndentDetailsSearch_Mdl;
import aarogya.inventory.indentSearch.shared.IndentSearchValue_Mdl;

abstract class IndentSearch_Dsg {

    public final static String widgetName = "Indent Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public IndentSearch_Dsg() {
        ini();
        iniGridIndentDetail();
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
        flwTr.add(vl.add(gridIndentDetail));
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
        txtSearchValue.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridIndentDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdIndentDetailsSearch_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> quantity();

        @Editor.Path("Sku")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> sku();

        @Editor.Path("ItemName")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> itemName();
        
        @Editor.Path("ItemMakeName")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> itemMakeName();

        @Editor.Path("Remark")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> remark();

        @Editor.Path("DepartmentText")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> departmentText();

        @Editor.Path("RequiredDate")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> requiredDate();

        @Editor.Path("IndentDate")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> indentDate();

        @Editor.Path("IsChecked")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> isChecked();

        @Editor.Path("PropertyValueCsv")
        ValueProvider<GrdIndentDetailsSearch_Mdl, String> propertyValueCsv();
    }

    final DmGridPaging<GrdIndentDetailsSearch_Mdl, GrdPaging_PrpAcs, IndentSearchValue_Mdl> gridIndentDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridIndentDetail() {
        ColumnConfig<GrdIndentDetailsSearch_Mdl, String> selectCheckBox;

        selectCheckBox = gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");

        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().quantity(), "Qty.", 60, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().sku(), "SKU", 50, "unit");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().itemName(), "Item Name", 150, "itemName");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().itemMakeName(), "Item Brand/Make", 100, "itemMakeName");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().propertyValueCsv(), "Properties", 170, "");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().departmentText(), "Department", 130, "departmentName");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().remark(), "Remark", 180, "remark");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().requiredDate(), "Req. Date", 75, "reqDate", DmDataType.DATE);
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().name(), "Indent No.", 100, "indentNo");
        gridIndentDetail.getDmGrid().addTextColumn(gridIndentDetail.getDmGrid().getPropertyAccess().indentDate(), "Indent Date", 75, "indentDate", DmDataType.DATE);
        gridIndentDetail.getDmGrid().initializeGrid(gridIndentDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 1030, 310, 0);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridIndentDetail.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        gridIndentDetail.getHelper().addSort("indentDate", DmEnum.DmSortType.ASC);
        gridIndentDetail.getHelper().addSort("indentNo", DmEnum.DmSortType.ASC);
        gridIndentDetail.getHelper().addSort("departmentName", DmEnum.DmSortType.ASC);
        gridIndentDetail.getHelper().addSort("itemName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

}
