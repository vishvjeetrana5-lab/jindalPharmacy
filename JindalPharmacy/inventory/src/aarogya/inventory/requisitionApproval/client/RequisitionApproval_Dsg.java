package aarogya.inventory.requisitionApproval.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.requisitionApproval.shared.GrdRequisitionApproval_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class RequisitionApproval_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().REQUISITION_APPROVAL.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();
    final Button btnSetting = new Button();

    public RequisitionApproval_Dsg() {
        fIni();
        createGrid();
    }

    private void fIni() {
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("576px");
        flwForm.add(toolContainer);
        flwForm.getElement().getStyle().setBackgroundColor("white");
        createToolControl();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.add(fCreateWorkingGrid());
        vrtInnerForm.add(flwWorkingArea);

        flwForm.add(vrtInnerForm);

//        txtFromDate.setEnabled(false);
        flwTableDisplaySection.setVisible(true);
        /////////////////////

    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Ok");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

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
        DmClient.setStyle(flwTable, "980px", "529px", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "529px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(dmGrd));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisitionApproval_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdRequisitionApproval_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdRequisitionApproval_Mdl, String> isView();

        @Editor.Path("IndNo")
        ValueProvider<GrdRequisitionApproval_Mdl, String> indNo();

        @Editor.Path("IndDate")
        ValueProvider<GrdRequisitionApproval_Mdl, String> indDate();

        @Editor.Path("Properties")
        ValueProvider<GrdRequisitionApproval_Mdl, String> properties();

        @Editor.Path("ItemName")
        ValueProvider<GrdRequisitionApproval_Mdl, String> itemName();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdRequisitionApproval_Mdl, String> departmentName();

        @Editor.Path("ReqDate")
        ValueProvider<GrdRequisitionApproval_Mdl, String> reqDate();

        @Editor.Path("SKUQty")
        ValueProvider<GrdRequisitionApproval_Mdl, String> skuQty();

        @Editor.Path("SKUName")
        ValueProvider<GrdRequisitionApproval_Mdl, String> skuName();

        @Editor.Path("SiteName")
        ValueProvider<GrdRequisitionApproval_Mdl, String> siteName();

        @Editor.Path("Approval")
        ValueProvider<GrdRequisitionApproval_Mdl, String> approvedBy();

        @Editor.Path("VoucherType")
        ValueProvider<GrdRequisitionApproval_Mdl, String> vType();

        @Editor.Path("ItemCurrentPosition")
        ValueProvider<GrdRequisitionApproval_Mdl, String> itemCurrentPosition();
    }

    final DmGridPaging<GrdRequisitionApproval_Mdl, Grd_PrpAcs, String> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, true);

    public void createGrid() {
        ColumnConfig<GrdRequisitionApproval_Mdl, String> selectCheckBox, selectDisplay, itemName;
        selectCheckBox = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        selectDisplay = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().isView(), "", 25, "");
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().skuQty(), "Req. Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "");
//        itemName = dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemName(), "Item Name", 200, "");
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().properties(), "Properties", 80, "");
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().reqDate(), "Required Date", 80, "");
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemGroupName(), "Item Group", 100, "");
//        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().itemCurrentPosition(), "Stk Status", 250, "");

        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().name(), "Requisition No.", 150, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().indDate(), "Req. Date", 80, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().vType(), "Vr. Type", 80, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 250, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().departmentName(), "Department", 250, "");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().siteName(), "Site", 400, "");
        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 0, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectDisplay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDisplay.setCellPadding(false);
        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdRequisitionApproval_Mdl>() {

            @Override
            public String getColStyle(GrdRequisitionApproval_Mdl model, ValueProvider<? super GrdRequisitionApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 1) {
                    if (DmShared.vNull(model.getIsChecked()).doubleValue() > 1) {
                        styleClass += "dm-FontBold ";
                    }
                }
                if (DmShared.xNull(model.getIsStatus()).equals("1") && !DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2") && !DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDairyConfirm";
                } else if (!DmShared.xNull(model.getApprovedBy()).equals("")) {
                    styleClass += "dm-GridCellDisable";
                } else if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRequisitionApproval_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });
//        itemName.setCell(new AbstractCell<String>() {
//
//            @Override
//            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
//                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word; '>" + DmShared.xNull(value) + "</div>");
//            }
//        });

        dmGrd.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC, "v_Date");
        dmGrd.getHelper().addSort("recId", DmEnum.DmSortType.DESC, "recId");

    }
//</editor-fold>
}
