package aarogya.dashBoard.requisitionPharmacy.client;

import aarogya.dashBoard.requisitionPharmacy.shared.*;
import aarogya.dashBoard.self.client.DashBoard_Client;
import aarogya.projectMenu.self.shared.pmDashBoard;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.MenuLink;
import java.util.ArrayList;
import java.util.List;

abstract class RequisitionPharmacy_Dsg extends IsWidgetExtended implements ClickHandler {

    final String widgetName = new pmDashBoard().MEDICINE_REQUISITION.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmTextBox txtDate = new DmTextBox(11, false);
    final DmTextBox txtDepartment = new DmTextBox(50, false);
    final DmTextBox txtRequistionBy = new DmTextBox(50, false);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final List<MenuLink> menuLinkListMedicine = new ArrayList<>();
    final FlowPanel flwDepartment = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDepartmentBy() {
        txtHDepartmentBy.getDmGrid().addTextColumn(txtHDepartmentBy.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartmentBy.getDmGrid().initializeGrid(txtHDepartmentBy.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDepartmentBy.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    public RequisitionPharmacy_Dsg() {
        iniMenuListMedicine();
        ini();
        iniTxtHDepartmentBy();
        iniGrdRequisition();

    }

    private void iniMenuListMedicine() {
        menuLinkListMedicine.clear();
        DashBoard_Client.addToList(new pmPharmacy().SALE_BILL, menuLinkListMedicine);
    }

    private void ini() {

        FlowPanel flw;
        flwForm.setStyleName("dm-FlwForm");
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setWidth("100%");
        flw.getElement().getStyle().setBackgroundColor("#eff");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Department To");
        label.setWidth("50px");
        flwDepartment.add(label);
        flwDepartment.getElement().setAttribute("style", "min-width:95px;padding:7px");
        txtHDepartmentBy.setWidth("200px");
        txtHDepartmentBy.setHeight("23px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("50px");
        btnRefresh.setTitle("Refresh");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("50px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);

        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("350px");
        toolContainer.addWidget(flwDepartment);
        toolContainer.addWidget(txtHDepartmentBy);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        return toolContainer;
    }

    private HorizontalPanel workingArea() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        FlowPanel flowPanel;

        DmClient.setStyle(horizontalPanel, "100%", "100%", null, null, DmPosition.NONE, true);

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(leftArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellWidth(flowPanel, "100%");
        horizontalPanel.setCellHeight(flowPanel, "100%");

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(rightArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellHeight(flowPanel, "100%");

        return horizontalPanel;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "570px", "left", null, DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-x:auto;background-color:white;");
        flwTable.add(gridRequisition);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "300px", "100%", "left", null, DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-y:auto;background-color:white;");

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "400px", "left", null, DmPosition.NONE, true);
        flwTr.add(rightSectionTop());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmPosition.NONE, true);
        flwTr.add(rightSectionBottom());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightSectionTop() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        //===============================Right Section Top=================================
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "390px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "37%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequisitionNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition By");

        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequistionBy));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===========================================================================
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "300px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(iniGrdRequisitionDetail());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightSectionBottom() {
        FlowPanel flMain = new FlowPanel();
        InlineLabel menuName;
        FlowPanel flwBlock;

        flMain.getElement().setAttribute("style", "width:95%;height:90%;margin:auto;overflow-y:auto;");
        for (MenuLink menuLink : menuLinkListMedicine) {
            if (menuLink != null) {

                flwBlock = new FlowPanel();

                menuName = new InlineLabel();
                menuName.setTitle(menuLink.moduleName + " | " + menuLink.menuName);
                menuName.setStyleName("ellipsis");
                menuName.setStyleName("dashboard-menu-link");
                menuName.setText(DmShared.xNull(menuLink.caption));
                flwBlock.getElement().setAttribute("style", ""
                        + ";font-family: arial; font-size:12px; float:left; padding:1px 2px; ");
                flwBlock.add(menuName);
                menuName.addClickHandler(this);
                flMain.add(flwBlock);
            }
        }
        return flMain;
    }

    //<editor-fold defaultstate="collapsed" desc="gridRequisition">
    interface GridRequisition_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisitionPharmacy_Mdl> {

        @Editor.Path("Date")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> date();

        @Editor.Path("ReqDateTime")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> reqDateTime();

        @Editor.Path("PatientName")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> patientName();

        @Editor.Path("RegNo")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> regNo();

        @Editor.Path("Uhid")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> uhid();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> departmentName();

        @Editor.Path("DepartmentToName")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> departmentToName();

        @Editor.Path("RequisitionByName")
        ValueProvider<GrdRequisitionPharmacy_Mdl, String> requisitionByName();

    }
    final DmGridPaging<GrdRequisitionPharmacy_Mdl, GridRequisition_PrpAcs, SearchValue_Mdl> gridRequisition = new DmGridPaging(GWT.create(GridRequisition_PrpAcs.class), 24, true);

    private void iniGrdRequisition() {
        ColumnConfig<GrdRequisitionPharmacy_Mdl, String> addColor;

        addColor = gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().requisitionByName(), "Requisition By", 100, "requestedBy");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().departmentName(), "Department By", 130, "departmentBy");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_date");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().reqDateTime(), "Required Date", 80, "isNull(reqDT,'31-Dec-2999')");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().name(), "Requisition No.", 120, "recId");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().departmentToName(), "Department To", 150, "departmentToName");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 130, "patientName");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().regNo(), "Reg. No.", 80, "regNo");
        gridRequisition.getDmGrid().addTextColumn(gridRequisition.getDmGrid().getPropertyAccess().uhid(), "UHID No.", 80, "uhid");
        gridRequisition.getDmGrid().initializeGrid(gridRequisition.getDmGrid().getPropertyAccess().fieldHashCode(), 972, 0);

        addColor.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String cellValue, requisition, color;
                cellValue = "";
                requisition = DmShared.xNull(gridRequisition.getDmGrid().getGrid().getStore().get(context.getIndex()).getRequisitionByName());
                color = DmShared.xNull(gridRequisition.getDmGrid().getGrid().getStore().get(context.getIndex()).getPriorityColor());
                if (!DmShared.xNull(color).equalsIgnoreCase("")) {
                    cellValue += "<div style='background-color: " + color + "; height:13px; width:10px;float:left;'></div>"
                            + "<div style='margin-left:15px;'>" + requisition + "</div>";
                } else {
                    cellValue += "<div style='height:13px; width:10px;float:left;float:left;'></div>"
                            + "<div style='margin-left:15px;'>" + requisition + "</div>";
                }

                sb.appendHtmlConstant(cellValue);
            }
        });
        gridRequisition.getHelper().addSort("patientName", DmSortType.ASC);
        gridRequisition.getHelper().addSort("priorityIndex", DmSortType.DESC);
        gridRequisition.getHelper().addSort("isNull(reqDateTime,'31-Dec-2999')", DmSortType.ASC, "reqDateTime");
        gridRequisition.getHelper().addSort("v_date", DmSortType.ASC);
        gridRequisition.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridRequisitionDetail">
    interface GridRequisitionDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisitionDetailPharmacy_Mdl> {

        @Editor.Path("ItemQty")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> itemQty();

        @Editor.Path("UnitName")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> unitName();

        @Editor.Path("ReqDate")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> reqDate();

        @Editor.Path("Remark")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> remark();

        @Editor.Path("Action")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> action();

        @Editor.Path("Check")
        ValueProvider<GrdRequisitionDetailPharmacy_Mdl, String> check();

    }

    final DmGrid<GrdRequisitionDetailPharmacy_Mdl, GridRequisitionDetail_PrpAcs> gridRequisitionDetail = new DmGrid(GWT.create(GridRequisitionDetail_PrpAcs.class));

    public Grid iniGrdRequisitionDetail() {
        ColumnConfig<GrdRequisitionDetailPharmacy_Mdl, String> particular, detail;

        particular = gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().name(), "Particular", 120, "");
        detail = gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().unitName(), "Detail", 80, "");
        detail.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        gridRequisitionDetail.initializeGrid(gridRequisitionDetail.getPropertyAccess().fieldHashCode(), 292, 300, 1);

        gridRequisitionDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdRequisitionDetailPharmacy_Mdl>() {
            @Override
            public String getColStyle(GrdRequisitionDetailPharmacy_Mdl model, ValueProvider<? super GrdRequisitionDetailPharmacy_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRequisitionDetailPharmacy_Mdl model, int rowIndex) {
                return "";
            }
        });

        particular.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                GrdRequisitionDetailPharmacy_Mdl grdRequisitionDetailToStore = gridRequisitionDetail.getGrid().getStore().get(context.getIndex());
                String cellValue;
                cellValue = "<div>" + DmShared.xNull(grdRequisitionDetailToStore.getSharedKeyFieldName()) + "</div>";
                if (!DmShared.xNull(grdRequisitionDetailToStore.getRemark()).trim().equalsIgnoreCase("")) {
                    cellValue += "<div>" + DmShared.xNull(grdRequisitionDetailToStore.getRemark()) + "</div>";
                }

                sb.appendHtmlConstant(cellValue);
            }
        });

        detail.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                GrdRequisitionDetailPharmacy_Mdl grdRequisitionDetailToStore = gridRequisitionDetail.getGrid().getStore().get(context.getIndex());
                String cellValue;
                cellValue = "<div>" + DmShared.xNull(grdRequisitionDetailToStore.getItemQty()) + " " + DmShared.xNull(grdRequisitionDetailToStore.getUnitName()) + "</div>";

                if ((!DmShared.xNull(grdRequisitionDetailToStore.getReqDate()).trim().equalsIgnoreCase("")) || (!DmShared.xNull(grdRequisitionDetailToStore.getReqTime()).trim().equalsIgnoreCase(""))) {
                    cellValue += "<div>" + DmShared.xNull(grdRequisitionDetailToStore.getReqDate()) + " " + DmShared.xNull(grdRequisitionDetailToStore.getReqTime()) + "</div>";
                    cellValue += "</div>";
                }
                sb.appendHtmlConstant(cellValue);
            }
        });

        return gridRequisitionDetail.getGrid();
    }
    //</editor-fold>
}
