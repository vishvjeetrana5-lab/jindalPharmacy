package aarogya.selfToolkit.labApproval.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import aarogya.projectMenu.self.shared.pmLab;

import aarogya.selfToolkit.labApproval.shared.GrdLabApproval_Mdl;

class LabApproval_Dsg {

    final String widgetName = new pmLab().LAB_APPROVAL.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();
    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    int gridHeight;

    public LabApproval_Dsg(boolean isOpenFromMenu) {
        if (isOpenFromMenu) {
            int gridRows = 23;
            dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), gridRows, false);
            gridHeight = 533;
        } else {
            int gridRows = (((Window.getClientHeight() - 60) / 22) - 3);
            dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), gridRows, false);
            gridHeight = (gridRows * 22) + 44;
        }
        fIni();
        createGrid();
    }

    private void fIni() {
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm dm-approval-container");
        flwForm.setHeight("560px");
        flwForm.add(toolContainer);
        flwForm.getElement().getStyle().setBackgroundColor("white");
        createToolControl();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.add(fCreateWorkingGrid());
        vrtInnerForm.add(flwWorkingArea);

        //==========  Add InnerForm in Temp Container ==========//
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.addStyleName("dm-FlwTempHeight");
        flwTemp.getElement().setAttribute("style", "width: 100%;");
        flwTemp.add(vrtInnerForm);
        //=======================================================//

        flwForm.add(flwTemp);
        flwTableDisplaySection.setVisible(true);

    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("80px");

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

    }

    private FlowPanel fCreateWorkingGrid() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "980px", "533px", "left", null, DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + "width: calc(100% - 1px); height: " + gridHeight + "px; background-color: white; border-left: 1px solid; overflow-x: auto;overflow-y:hidden;");
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "533px", "left", null, DmPosition.NONE, false);
        dmGrd.asWidget().addStyleName("dashboard-grid");
        flwTd.add(vl.add(dmGrd));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabApproval_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdLabApproval_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdLabApproval_Mdl, String> isView();

        @Editor.Path("LabType")
        ValueProvider<GrdLabApproval_Mdl, String> labType();

        @Editor.Path("LabDateTime")
        ValueProvider<GrdLabApproval_Mdl, String> labDateTime();

        @Editor.Path("Technician")
        ValueProvider<GrdLabApproval_Mdl, String> technician();

        @Editor.Path("TestTemplate")
        ValueProvider<GrdLabApproval_Mdl, String> testTemplate();

        @Editor.Path("SampleNo")
        ValueProvider<GrdLabApproval_Mdl, String> sampleNo();

        @Editor.Path("ItemName")
        ValueProvider<GrdLabApproval_Mdl, String> itemName();

        @Editor.Path("SampleConsumeQty")
        ValueProvider<GrdLabApproval_Mdl, String> sampleConsumeQty();

        @Editor.Path("Unit")
        ValueProvider<GrdLabApproval_Mdl, String> unit();

        @Editor.Path("ApprovedBy")
        ValueProvider<GrdLabApproval_Mdl, String> approvedBy();
    }
    final DmGridPaging<GrdLabApproval_Mdl, Grd_PrpAcs, String> dmGrd ;

    public void createGrid() {
        ColumnConfig<GrdLabApproval_Mdl, String> selectCheckBox;
        ColumnConfig<GrdLabApproval_Mdl, String> selectDisplay;
        selectCheckBox = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        selectDisplay = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isView(), "", 25, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().name(), "Lab No.", 120, "recId");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().labType(), "Type", 45, "short_Name");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().labDateTime(), "Date/Time", 110, "v_Date");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemName(), "Item Name", 300, "itemName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().testTemplate(), "Test Template", 80, "testTemplateName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().technician(), "Technician", 250, "technicianName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().sampleNo(), "Sample No.", 120, "sampleRecId");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().sampleConsumeQty(), "Sample Consume", 110, "sampleConsumedQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().unit(), "Unit", 50, "unit");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 140, "approvedBy");
        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, gridHeight, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdLabApproval_Mdl>() {

            @Override
            public String getColStyle(GrdLabApproval_Mdl model, ValueProvider<? super GrdLabApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 1) {
                    if (DmShared.vNull(model.getIsChecked()).doubleValue() > 1) {
                        styleClass += "dm-FontBold ";
                    }
                }
                if (!DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDisable";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdLabApproval_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em; margin-top:3px;'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em; margin-top:3px;'></span>");
                }
            }
        });
        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green; margin-top:3px;'></span>");
            }
        });

        dmGrd.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC, "v_Date");
        dmGrd.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC, "short_Name");
        dmGrd.getHelper().addSort("v_No", DmEnum.DmSortType.DESC, "v_No");
        dmGrd.getHelper().addSort("recId", DmEnum.DmSortType.ASC, "recId");

    }
    //</editor-fold>
}
