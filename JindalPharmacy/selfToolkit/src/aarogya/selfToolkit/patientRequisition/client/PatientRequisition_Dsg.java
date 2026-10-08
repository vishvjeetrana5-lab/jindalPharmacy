package aarogya.selfToolkit.patientRequisition.client;

import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.selfToolkit.patientRequisition.shared.*;
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
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

public class PatientRequisition_Dsg {

    public static final String widgetName = "Requisition";
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

    //<editor-fold defaultstate="collapsed" desc="txtHRequisition">
    interface Requisition_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> department();

    }
    final DmTextHelp<KeyFieldExtended, Requisition_PrpAcs> txtHRequisition = new DmTextHelp(GWT.create(Requisition_PrpAcs.class), 7, true);

    private void iniTxtHRequisition() {
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().department(), "Department", 100, "departmentName");
        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHRequisition.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHByDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDepartment() {
        txtHByDepartment.getDmGrid().addTextColumn(txtHByDepartment.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHByDepartment.getDmGrid().initializeGrid(txtHByDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHByDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentTo">
    interface DepartmentTo_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

    }
    final DmTextHelp<KeyFieldExtended, DepartmentTo_PrpAcs> txtHToDepartment = new DmTextHelp(GWT.create(DepartmentTo_PrpAcs.class), 7, true);

    private void iniTxtHDepartmentTo() {
        txtHToDepartment.getDmGrid().addTextColumn(txtHToDepartment.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHToDepartment.getDmGrid().addTextColumn(txtHToDepartment.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Site To", 150, "siteName");
        txtHToDepartment.getDmGrid().initializeGrid(txtHToDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHToDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHSite.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GrdMedicine">
    interface Medicine_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisitionMedicine_Mdl> {

        @Editor.Path("Qty")
        ValueProvider<GrdRequisitionMedicine_Mdl, String> qty();

        @Editor.Path("UnitName")
        ValueProvider<GrdRequisitionMedicine_Mdl, String> unit();

        @Editor.Path("ReqDate")
        ValueProvider<GrdRequisitionMedicine_Mdl, String> reqDate();

        @Editor.Path("ReqTime")
        ValueProvider<GrdRequisitionMedicine_Mdl, String> reqTime();
    }
    final DmGrid<GrdRequisitionMedicine_Mdl, Medicine_PrpAcs> grdMedicine = new DmGrid(GWT.create(Medicine_PrpAcs.class));

    private Grid iniGrdMedicin() {
        grdMedicine.addTextColumn(grdMedicine.getPropertyAccess().name(), "Medicine", 150, "");
        grdMedicine.addTextColumn(grdMedicine.getPropertyAccess().qty(), "Req. Qty.", 75, "");
        grdMedicine.addTextColumn(grdMedicine.getPropertyAccess().unit(), "Unit", 50, "");
        grdMedicine.addTextColumn(grdMedicine.getPropertyAccess().reqDate(), "Req. Date", 90, "");
        grdMedicine.addTextColumn(grdMedicine.getPropertyAccess().reqTime(), "Req. Time", 60, "");
        grdMedicine.initializeGrid(grdMedicine.getPropertyAccess().fieldHashCode(), 598, 220, 1);
        return grdMedicine.getGrid();
    }
    //</editor-fold>

    public PatientRequisition_Dsg() {
        iniTxtHRequisition();
        iniTxtHDepartment();
        iniTxtHAddPriority();
        ini();
        iniSite();
        iniTxtHDepartmentTo();
    }

    private void ini() {
        FlowPanel flw;
        final VerticalPanel vrtInnerForm = new VerticalPanel();
        pnlForm.setGlassEnabled(true);
        pnlForm.setHeight("320px");
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
        flwCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "285px", null, null, DmPosition.CENTER, true);

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
        flwTd.add(vl.add(txtHRequisition));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department By");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHByDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department To");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHToDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site To");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
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

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "220px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdMedicin()));
        flwTable.add(flwTr);

        return flwTable;
    }
}
