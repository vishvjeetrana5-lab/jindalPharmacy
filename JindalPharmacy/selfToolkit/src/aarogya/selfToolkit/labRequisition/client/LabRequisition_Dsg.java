package aarogya.selfToolkit.labRequisition.client;

import aarogya.selfToolkit.labRequisition.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.DmEnum.*;

public class LabRequisition_Dsg {

    public static final String widgetName = "Lab Requisition";
    final PopupPanel pnlForm = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();

    final Button btnSave = new Button();
    final Button btnCancel = new Button();
    final FlowPanel flwSave = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionBy">
    interface Requisition_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> department();

    }
    final DmTextHelp<KeyFieldExtended, Requisition_PrpAcs> txtHRequisitionBy = new DmTextHelp(GWT.create(Requisition_PrpAcs.class), 7, true);

    private void iniTxtHRequisition() {
        txtHRequisitionBy.getDmGrid().addTextColumn(txtHRequisitionBy.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHRequisitionBy.getDmGrid().addTextColumn(txtHRequisitionBy.getDmGrid().getPropertyAccess().department(), "Department", 100, "departmentName");
        txtHRequisitionBy.getDmGrid().initializeGrid(txtHRequisitionBy.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHRequisitionBy.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDepartment() {
        txtHDepartmentBy.getDmGrid().addTextColumn(txtHDepartmentBy.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHDepartmentBy.getDmGrid().initializeGrid(txtHDepartmentBy.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDepartmentBy.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Investigation">
    interface Medicine_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabRequisition_Mdl> {

        @Editor.Path("ReqDate")
        ValueProvider<GrdLabRequisition_Mdl, String> reqDate();

        @Editor.Path("ReqTime")
        ValueProvider<GrdLabRequisition_Mdl, String> reqTime();

        @Editor.Path("Remark")
        ValueProvider<GrdLabRequisition_Mdl, String> remark();
    }
    final DmGrid<GrdLabRequisition_Mdl, Medicine_PrpAcs> grdTest = new DmGrid(GWT.create(Medicine_PrpAcs.class));

    private Grid iniGrdTest() {
        grdTest.addTextColumn(grdTest.getPropertyAccess().name(), "Investigation", 175, "");
        grdTest.addTextColumn(grdTest.getPropertyAccess().reqDate(), "Req. Date", 80, "");
        grdTest.addTextColumn(grdTest.getPropertyAccess().reqTime(), "Req. Time", 60, "");
        grdTest.addTextColumn(grdTest.getPropertyAccess().remark(), "Remark", 175, "");
        grdTest.initializeGrid(grdTest.getPropertyAccess().fieldHashCode(), 598, 220, 1);

        return grdTest.getGrid();
    }
    //</editor-fold>

    public LabRequisition_Dsg() {
        iniTxtHRequisition();
        iniTxtHDepartment();
        iniTxtHAddPriority();
        ini();
    }

    private void ini() {
        FlowPanel flw;
        final VerticalPanel vrtInnerForm = new VerticalPanel();
        pnlForm.setGlassEnabled(true);
        pnlForm.setHeight("250px");
        pnlForm.setWidth("600px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        pnlForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");
        btnSave.setTitle("Save");
        flwSave.add(vl.add(btnSave));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "260px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition By");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisitionBy));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department By");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentBy));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Priority");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPriority));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "220px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdTest()));

        flwTable.add(flwTr);

        return flwTable;
    }
}
