package aarogya.pharmacy.requisitionSearch.client;

import aarogya.pharmacy.requisitionSearch.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
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
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class RequisitionSearch_Dsg {

    public final static String widgetName = "Requisition Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
//    final FlowPanel flwSetting = new FlowPanel();
//    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwPriority = new FlowPanel();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();
    int grdColIndexCancel = 0, grdColIndexSelectChkBox = 0;
    ColumnConfig<GrdRequisitionDetailsSearch_Mdl, String> selectCheckBox, cancelCheckBox;
    int headerValue = 0;
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    Boolean requisitionWithoutUHID = false;

    public RequisitionSearch_Dsg() {
        this.requisitionWithoutUHID = requisitionWithoutUHID;
        ini();
        iniTxtHAddPriority();
    }

    public RequisitionSearch_Dsg(Boolean requisitionWithoutUHID) {
        this.requisitionWithoutUHID = requisitionWithoutUHID;
        ini();

        iniTxtHAddPriority();
        iniTxtHDepartment();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("975px");
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
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "600px" , "50px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "600px", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "120px", "15px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Priority");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "120px", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPriority));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "120px", "15px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "240px", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        
        flwPriority.add(vl.add(flwTr));
        
//        label = new InlineLabel("Priority");
//        label.setWidth("25px");
//        label.setText("Priority");
//        flwPriority.add(label);
//        flwPriority.getElement().setAttribute("style", "min-width:95px;padding:7px");
//        txtHPriority.setWidth("70px");
//        txtHPriority.setHeight("20px");
//        flwPriority.add(vl.add(txtHPriority));

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
        btnCancel.setTitle("Close");
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
        toolContainer.addWidget(flwPriority);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
//        flwTr.add(vl.add(topArea()));
//
//        flwTable.add(flwTr);

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
        txtSearchValue.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHAddPriority">
    interface TxtHAddPriority_PrpAcs extends KeyField_T_PrpAcs<TxtHPriority_Mdl> {

        @Editor.Path("PriorityFullName")
        ValueProvider<TxtHPriority_Mdl, String> priorityFullName();

    }
    final DmTextHelp<TxtHPriority_Mdl, TxtHAddPriority_PrpAcs> txtHPriority = new DmTextHelp(com.google.gwt.core.client.GWT.create(TxtHAddPriority_PrpAcs.class), 7, false);

    private void iniTxtHAddPriority() {
        txtHPriority.getDmGrid().addTextColumn(txtHPriority.getDmGrid().getPropertyAccess().name(), "Short Name", 0, "shortName");
        txtHPriority.getDmGrid().addTextColumn(txtHPriority.getDmGrid().getPropertyAccess().priorityFullName(), "Name", 300, "NAME");
        txtHPriority.getDmGrid().initializeGrid(txtHPriority.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHPriority.getHelper().addSort("shortName", DmEnum.DmSortType.ASC);
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    interface KeyField_PrpAcs1 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHDepartment = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs1.class), 6, false);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Depaertment", 200, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHDepartment.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="gridRequisitionDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisitionDetailsSearch_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> quantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> sku();

        @Editor.Path("MediName")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> itemName();

        @Editor.Path("Remark")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> remark();

        @Editor.Path("DepartmentText")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> departmentText();

        @Editor.Path("RequisitionDate")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> requisitionDate();

        @Editor.Path("RequisitionBy")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> requisitionBy();

        @Editor.Path("IsChecked")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> isChecked();

        @Editor.Path("IsCancelled")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> isCancelled();

        @Editor.Path("PatientName")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> patientName();

        @Editor.Path("Priority")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> priority();

        @Editor.Path("EntryDate")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> date();

        @Editor.Path("PatientUHID")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> patientUHID();

        @Editor.Path("PatientRegID")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> patientRegID();

        @Editor.Path("ReqTime")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> v_Time();

        @Editor.Path("DepartmentFromName")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> departmentFromName();

        @Editor.Path("DateOfAdmission")
        ValueProvider<GrdRequisitionDetailsSearch_Mdl, String> dateOfAdmission();

    }

    final DmGrid<GrdRequisitionDetailsSearch_Mdl, GrdPaging_PrpAcs> gridRequisitionDetail = new DmGrid(GWT.create(GrdPaging_PrpAcs.class));

    private Grid iniGridIndentDetail() {
        int i = 0;
        grdColIndexSelectChkBox = i;
        selectCheckBox = gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().isChecked(), "", 30, "");
        ++i;
        if (!requisitionWithoutUHID) {
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().date(), "Admission Dt", 75, "dateOfAdmission", DmDataType.DATE);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().quantity(), "Qty.", 40, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().sku(), "SKU", 50, "skuName");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().itemName(), "Medicine Name", 1, "mediName");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().name(), "Req. No.", 80, "recid");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().requisitionBy(), "Req. By", 100, "requestedByName");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().departmentText(), "Department", 100, "departmentName");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().requisitionDate(), "Req. Date", 75, "reqDate", DmDataType.DATE);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().patientName(), "Patient Name", 100, "patientName");
            grdColIndexCancel = ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().priority(), "P", 20, "priority");
            grdColIndexCancel = ++i;
            cancelCheckBox = gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().isCancelled(), "Cancel", 50, "");

            gridRequisitionDetail.initializeGrid(gridRequisitionDetail.getPropertyAccess().fieldHashCode(), 900, 310, 4);
        } else {
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().date(), "Admission Dt", 75, "dateOfAdmission", DmDataType.DATE);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().name(), "Req. No.", 110, "recid");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().date(), "Req. Date", 75, "date", DmDataType.DATE);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().v_Time(), "Time", 40, "v_Time", DmDataType.TIMESTAMP);
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().patientUHID(), "UHID", 110, "patientUHID");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().patientRegID(), "Reg.No.", 110, "patientRegNo");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().patientName(), "Patient Name", 200, "patientName");
            grdColIndexCancel = ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().requisitionBy(), "Req. By", 110, "requestedByName");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().departmentFromName(), "Department", 110, "departmentFrom");
            ++i;
            gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().priority(), "P", 20, "priority");
            grdColIndexCancel = ++i;
//            cancelCheckBox = gridRequisitionDetail.addTextColumn(gridRequisitionDetail.getPropertyAccess().isCancelled(), "Cancel", 50, "");

            gridRequisitionDetail.initializeGrid(gridRequisitionDetail.getPropertyAccess().fieldHashCode(), 975, 310, 7);
        }

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        if (!requisitionWithoutUHID) {
            cancelCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            cancelCheckBox.setCellPadding(false);
        }

//        selectCheckBox.setHeader(new SafeHtml() {
//            @Override
//            public String asString() {
//                headerValue = 0;
//                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
//                return headerStyle;
//            }
//        });
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        if (!requisitionWithoutUHID) {
            cancelCheckBox.setCell(new AbstractCell<String>() {
                @Override
                public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                    int val = DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(context.getIndex()).getIsCancelled()).intValue();
                    if (val == 1) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                    } else if (val == 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                    }
                }
            });
        }
        gridRequisitionDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdRequisitionDetailsSearch_Mdl>() {
            @Override
            public String getColStyle(GrdRequisitionDetailsSearch_Mdl model, ValueProvider<? super GrdRequisitionDetailsSearch_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getIsCancelled()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                } else if (DmShared.xNull(model.getIsCancelled()).equals("0")) {
                    styleClass += "dm-GridCellFormat ";
                }

                if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#f8cbad")) {
                    styleClass += "dm-GridCellDairyCancel"; //"dm-colorRed ";
                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#9bc2e6")) {
                    styleClass += "dm-GridCellSelectedLightRed ";
                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#ff5b5b")) {
                    styleClass += " dm-GridCellDairyPostpone";
                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#92d050")) {
                    styleClass += "dm-GridCellDairyConfirm";
                }

                if (DmShared.xNull(model.getPartialPending()).equals("100")) {
                    styleClass += "dm-GridCellDairyCancel";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRequisitionDetailsSearch_Mdl model, int rowIndex) {
                return "";
            }
        });

        return gridRequisitionDetail.getGrid();
    }
    //</editor-fold>

}
