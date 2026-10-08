package aarogya.master.environmentSettingCompany.client;

import aarogya.master.environmentSettingCompany.shared.GrdPatientClass_Mdl;
import aarogya.master.environmentSettingCompany.shared.GrdPriority_Mdl;
import aarogya.master.environmentSettingCompany.shared.GrdPatientInfo_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;

abstract class EnvironmentSettingCompany_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().ENVIRONMENT_SETTING_COMPANY.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSave = new Button();
    final Button btnCopy = new Button();
    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final Button btnExport = new Button();

    Style stl;
    //<editor-fold defaultstate="collapsed" desc="txtHACGroupConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupOrganization = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACGroupOrgCode() {
        txtHACGroupOrganization.getDmGrid().addTextColumn(txtHACGroupOrganization.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupOrganization.getDmGrid().initializeGrid(txtHACGroupOrganization.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupOrganization.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACGroup() {
        txtHACGroupConsultant.getDmGrid().addTextColumn(txtHACGroupConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupConsultant.getDmGrid().initializeGrid(txtHACGroupConsultant.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupConsultant.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACDepartment() {
        txtHACGroupDepartment.getDmGrid().addTextColumn(txtHACGroupDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupDepartment.getDmGrid().initializeGrid(txtHACGroupDepartment.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupReferred">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupReferred = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACReferred() {
        txtHACGroupReferred.getDmGrid().addTextColumn(txtHACGroupReferred.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupReferred.getDmGrid().initializeGrid(txtHACGroupReferred.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupReferred.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupEmployee">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupEmployee = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACEmployee() {
        txtHACGroupEmployee.getDmGrid().addTextColumn(txtHACGroupEmployee.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupEmployee.getDmGrid().initializeGrid(txtHACGroupEmployee.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupEmployee.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupPharmacySupplier">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupPharmacySupplier = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACPharmacySupplier() {
        txtHACGroupPharmacySupplier.getDmGrid().addTextColumn(txtHACGroupPharmacySupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupPharmacySupplier.getDmGrid().initializeGrid(txtHACGroupPharmacySupplier.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupPharmacySupplier.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupInventorySupplier">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupInventorySupplier = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACInventorySupplier() {
        txtHACGroupInventorySupplier.getDmGrid().addTextColumn(txtHACGroupInventorySupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupInventorySupplier.getDmGrid().initializeGrid(txtHACGroupInventorySupplier.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupInventorySupplier.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupManufacturer">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupManufacturer = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACManufacturer() {
        txtHACGroupManufacturer.getDmGrid().addTextColumn(txtHACGroupManufacturer.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupManufacturer.getDmGrid().initializeGrid(txtHACGroupManufacturer.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupManufacturer.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupDivision">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupDivision = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACDivision() {
        txtHACGroupDivision.getDmGrid().addTextColumn(txtHACGroupDivision.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupDivision.getDmGrid().initializeGrid(txtHACGroupDivision.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupDivision.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACGroupCustomer">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACGroupCustomer = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHACCustomer() {
        txtHACGroupCustomer.getDmGrid().addTextColumn(txtHACGroupCustomer.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHACGroupCustomer.getDmGrid().initializeGrid(txtHACGroupCustomer.getDmGrid().getPropertyAccess().code(), 265, 0, 1);

        txtHACGroupCustomer.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHInventoryAccountingActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInventoryAccountingActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHInventoryAccountingActive() {
        txtHInventoryAccountingActive.getDmGrid().addTextColumn(txtHInventoryAccountingActive.getDmGrid().getPropertyAccess().name(), "Active", 0, "");
        txtHInventoryAccountingActive.getDmGrid().initializeGrid(txtHInventoryAccountingActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>

    public EnvironmentSettingCompany_Dsg() {

        iniTxtHACGroup();
        iniTxtHACDepartment();
        iniTxtHACGroupOrgCode();
        iniTxtHACReferred();
        iniTxtHACEmployee();
        iniTxtHACPharmacySupplier();
        iniTxtHACInventorySupplier();
        iniTxtHACManufacturer();
        iniTxtHACDivision();
        iniTxtHACCustomer();
        iniTxtHInventoryAccountingActive();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("500px");
        vrtInnerForm.add(toolContainer);
        createToolControl();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "550px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "552px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.setTitle("Copy");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Export");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        // aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExport.addStyleName("dm-top-control-btnStyle");
        btnExport.setTitle("Export");
        btnExport.getElement().appendChild(aBtn.getElement());
        btnExport.getElement().appendChild(label.getElement());
        btnExport.setWidth("70px");

    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "460px", null, null, DmPosition.NONE, true);
        tabPanel.add(vl.add(tabGeneral()), "General");
        tabPanel.add(vl.add(patientInfoPopup()), "Patient Info");
        tabPanel.add(vl.add(priorityPopup()), " Priority");
        tabPanel.add(vl.add(patientClass()), " Patient Class");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "321px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "321px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "300px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftGeneral());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "130px", "right", null, DmPosition.NONE, true);
        //flwTd.add(rightGeneral());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel leftGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "300px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===========================4th row=====================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Organization");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupOrganization));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //============================5th row======================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Referred");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupReferred));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================6th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Employee");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupEmployee));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================7th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Pharmacy Supplier");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupPharmacySupplier));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================8th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Inventory Supplier");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupInventorySupplier));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================9th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Manufacturer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupManufacturer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================10th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Division");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupDivision));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================11th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Customer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACGroupCustomer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //===================12th row================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Inventory Accounting Active");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInventoryAccountingActive));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel patientInfoPopup() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "420px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "420px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "230px", "left", null, DmPosition.NONE, true);
        flwTd.add((iniGrdPatientInfo()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "230px", "left", null, DmPosition.NONE, true);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel priorityPopup() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "420px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "420px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "230px", "left", null, DmPosition.NONE, true);
        flwTd.add((iniGrdPriority()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "230px", "left", null, DmPosition.NONE, true);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel patientClass() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "420px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "420px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "230px", "left", null, DmPosition.NONE, true);
        flwTd.add((iniGrdPatientClass()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "230px", "left", null, DmPosition.NONE, true);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd PatientInfo">
    interface GridPatientInfo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPatientInfo_Mdl> {

        @Editor.Path("SrNo")
        ValueProvider<GrdPatientInfo_Mdl, String> sNo();

        @Editor.Path("IsVisibleText")
        ValueProvider<GrdPatientInfo_Mdl, String> isVisible();

        @Editor.Path("IsMandatoryText")
        ValueProvider<GrdPatientInfo_Mdl, String> isMandatory();

    }
    final DmGrid<GrdPatientInfo_Mdl, GridPatientInfo_PrpAcs> grdPatientInfo = new DmGrid(GWT.create(GridPatientInfo_PrpAcs.class));

    private Grid iniGrdPatientInfo() {
        grdPatientInfo.addRowNumberColumn();
        grdPatientInfo.addTextColumn(grdPatientInfo.getPropertyAccess().name(), "Name", 210, "");
        grdPatientInfo.addTextColumn(grdPatientInfo.getPropertyAccess().sNo(), "Sr. No.", 55, "");
        grdPatientInfo.addTextColumn(grdPatientInfo.getPropertyAccess().isVisible(), "Is Visible", 100, "");
        grdPatientInfo.addTextColumn(grdPatientInfo.getPropertyAccess().isMandatory(), "Is Mandatory", 100, "");
        grdPatientInfo.initializeGrid(grdPatientInfo.getPropertyAccess().fieldHashCode(), 500, 220);

        return grdPatientInfo.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Priority">
    interface GridPriority_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPriority_Mdl> {

        @Editor.Path("Description")
        ValueProvider<GrdPriority_Mdl, String> description();

        @Editor.Path("Sorting")
        ValueProvider<GrdPriority_Mdl, String> sorting();

        @Editor.Path("Color")
        ValueProvider<GrdPriority_Mdl, String> color();

    }
    final DmGrid<GrdPriority_Mdl, GridPriority_PrpAcs> grdPriority = new DmGrid(GWT.create(GridPriority_PrpAcs.class));

    private Grid iniGrdPriority() {
        ColumnConfig<GrdPriority_Mdl, String> addColor;
        grdPriority.addRowNumberColumn();
        grdPriority.addTextColumn(grdPriority.getPropertyAccess().name(), "Priority", 55, "shortName");
        grdPriority.addTextColumn(grdPriority.getPropertyAccess().description(), "Description", 210, "name");
        grdPriority.addTextColumn(grdPriority.getPropertyAccess().sorting(), "Priority Index", 100, "priorityIndex");
        addColor = grdPriority.addTextColumn(grdPriority.getPropertyAccess().color(), "Color", 100, "colorName");
        grdPriority.initializeGrid(grdPriority.getPropertyAccess().fieldHashCode(), 500, 220);

        addColor.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String cellValue, color = "";
                cellValue = "";

                color = DmShared.xNull(grdPriority.getGrid().getStore().get(context.getIndex()).getColor());
                if (!DmShared.xNull(color).equalsIgnoreCase("")) {
                    cellValue += "<div style='background-color: " + color + "; height:13px; width:10px;float:left;'></div>"
                            + "<div style='float:left;margin-left:5px;'>" + color + "</div>";
                } else {
                    cellValue += "<div style='height:13px; width:10px;float:left;'></div>"
                            + "<div style='float:left;margin-left:5px;'>" + color + "</div>";
                }
                sb.appendHtmlConstant(cellValue);
            }
        });

        return grdPriority.getGrid();
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Grd PatientClass">
    interface GridPatientClass_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPatientClass_Mdl> {

        @Editor.Path("PatientClassName")
        ValueProvider<GrdPatientClass_Mdl, String> patientClassName();

        @Editor.Path("RateListName")
        ValueProvider<GrdPatientClass_Mdl, String> rateListName();
    }
    final DmGrid<GrdPatientClass_Mdl, GridPatientClass_PrpAcs> grdPatientClass = new DmGrid(GWT.create(GridPatientClass_PrpAcs.class));

    private Grid iniGrdPatientClass() {

        grdPatientClass.addRowNumberColumn();
        grdPatientClass.addTextColumn(grdPatientClass.getPropertyAccess().patientClassName(), "Class", 195, "");
        grdPatientClass.addTextColumn(grdPatientClass.getPropertyAccess().rateListName(), "Rate List", 180, "");
        grdPatientClass.initializeGrid(grdPatientClass.getPropertyAccess().fieldHashCode(), 400, 220);
        return grdPatientClass.getGrid();
    }
    //</editor-fold>

}
